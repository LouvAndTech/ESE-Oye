package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.beans.SimplifiedEntity;
import fr.eseoye.eseoye.helpers.ConnectionHelper;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.AdminTable;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Ternary;
import fr.eseoye.eseoye.utils.Tuple;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminListUser implements Action {

    private final DatabaseCredentials dbCred;

    public AdminListUser(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    /**
     * Manage the user by delete it, make it admin or lock it
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        if(ConnectionHelper.isLockAdmin(request, response)) {
            //todo : Has no use for now but mey never as any ... ?
            System.out.println("Admin list user : execute");
            if (request.getParameter("delete") != null) {
                Ternary daoRequest = DatabaseFactory.getInstance().getTable(AdminTable.class, dbCred).isAnAdminSecureID(request.getParameter("secureID"));
                if (daoRequest == Ternary.TRUE) {
                    DatabaseFactory.getInstance().getTable(AdminTable.class, dbCred).deleteAdminAccount(request.getParameter("secureID"));
                }
                DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).deleteUserAccount(request.getParameter("secureID"));
            } else if (request.getParameter("admin") != null) {
                Ternary daoRequest = DatabaseFactory.getInstance().getTable(AdminTable.class, dbCred).isAnAdminSecureID(request.getParameter("secureID"));
                if (daoRequest == Ternary.TRUE) {
                    DatabaseFactory.getInstance().getTable(AdminTable.class, dbCred).deleteAdminAccount(request.getParameter("secureID"));
                } else if (daoRequest == Ternary.FALSE) {
                    Tuple<String, String> nameSurname = DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).getNameSurname(request.getParameter("secureID"));
                    String password = DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).getPassword(request.getParameter("secureID"));
                    DatabaseFactory.getInstance().getTable(AdminTable.class, dbCred).createAdminAccount(request.getParameter("secureID"), nameSurname.getValueA(), nameSurname.getValueB(), password);
                } else if (daoRequest == Ternary.UNDEFINED) {
                    System.out.println("Error : AdminListUser : execute : isAnAdminSecureID : UNDEFINED");
                }
            } else if (request.getParameter("lock") != null) {
                Ternary isLocked = DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).isUserLocked(request.getParameter("secureID"));
                if (isLocked == Ternary.TRUE) {
                    DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).manageLockForUser(request.getParameter("secureID"), false);
                } else if (isLocked == Ternary.FALSE) {
                    DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).manageLockForUser(request.getParameter("secureID"), true);
                } else if (isLocked == Ternary.UNDEFINED) {
                    System.out.println("Error : AdminListUser : execute : isUserLocked : UNDEFINED");
                }
            }
            loadUserList(request, response);
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
        }
    }

    /**
     * Forward the request to the view
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @param target    a string to define the view to forward
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        if(ConnectionHelper.isLockAdmin(request, response)) {
            System.out.println("Admin list user : forward");
            loadUserList(request, response);
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
        }
    }

    /**
     * Load the list of user and add it to the request
     * @param request
     * @param response
     */
    private void loadUserList(HttpServletRequest request, HttpServletResponse response){
        List<SimplifiedEntity> listUser = DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).getUserList();
        for(SimplifiedEntity user : listUser){
            user.setIsAdmin(DatabaseFactory.getInstance().getTable(AdminTable.class, dbCred).isAnAdminSecureID(user.getSecureID()) == Ternary.TRUE);
            user.setIsLocked(DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).isUserLocked(user.getSecureID()) == Ternary.TRUE);
        }
        //${isAdmin.get(i) ? 'activer' : 'desactiver'}
        request.setAttribute("listUser", listUser);
    }
}
