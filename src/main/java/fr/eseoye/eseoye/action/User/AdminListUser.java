package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.beans.SimplifiedEntity;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.UserTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AdminListUser implements Action {

    private final DatabaseCredentials dbCred;

    public AdminListUser(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo : Has no use for now but mey never as any ... ?
        System.out.println("Admin list user : execute");
        if(request.getParameter("delete") != null){
            DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).deleteUserAccount(request.getParameter("secureID"));
            System.out.println("delete");
        } else if (request.getParameter("admin") != null){
            System.out.println("admin");
        } else if (request.getParameter("lock") != null){
            System.out.println("lock");
        }
        System.out.println(request.getParameter("secureID"));
        loadUserList(request, response);
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin list user : forward");
        loadUserList(request, response);
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }

    private void loadUserList(HttpServletRequest request, HttpServletResponse response){
        List<SimplifiedEntity>  listUser= DatabaseFactory.getInstance().getTable(UserTable.class, dbCred).getUserList();
        request.setAttribute("listUser", listUser);
    }
}
