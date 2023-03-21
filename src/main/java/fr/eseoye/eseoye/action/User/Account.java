package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.IOException;

public class Account implements Action {

    public final DatabaseCredentials dbCred;

    public Account(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    /**
     * In case of a GET request, edit the right user information
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
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Account : execute");
        HttpSession session = request.getSession();
        if(request.getParameter("nameSurnameBtn") != null){
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).setNameSurname(session.getAttribute("idUser").toString(), request.getParameter("name"), request.getParameter("surname"));
        }else if (request.getParameter("imageBtn") != null) {

        }else if (request.getParameter("mailBtn") != null) {
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).setMail(session.getAttribute("idUser").toString(), request.getParameter("mail"));
        }else if (request.getParameter("phoneBtn") != null) {
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).setPhoneNumber(session.getAttribute("idUser").toString(), request.getParameter("phone"));
        }else if (request.getParameter("birthBtn") != null) {
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).setBirthDate(session.getAttribute("idUser").toString(), request.getParameter("birth"));
        }else if (request.getParameter("passwordBtn") != null) {
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).setPassword(session.getAttribute("idUser").toString(), BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt()));
        }else if (request.getParameter("deleteBtn") != null) {
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).deleteUserAccount(session.getAttribute("idUser").toString());
        }
        request.setAttribute("user", getUserInformation(session));
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }

    /**
     * Set the different user information in the request
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
        System.out.println("Account : forward");
        HttpSession session = request.getSession();
        request.setAttribute("user", getUserInformation(session));
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }

    /**
     * Get the user information from the database
     * @param session
     * @return
     */
    private User getUserInformation(HttpSession session){
        return DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).getUser(session.getAttribute("idUser").toString());
    }
}
