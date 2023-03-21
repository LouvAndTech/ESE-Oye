package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.tables.AdminTable;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminLogin implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Admin Login : execute");
        //todo : Has no use for now but mey never as any ... ?
        String admin = DatabaseFactory.getInstance().getTable(AdminTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).checkAdminConnection(request.getParameter("pseudo"), BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt()));
        System.out.println(request.getParameter("pseudo") + " -- "+ BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt()));
        System.out.println(admin);
        //response.sendRedirect(request.getContextPath()+"/ese-oye?id=Account");
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin Login : forward");
        request.getRequestDispatcher("/jsp/AdminLogin.jsp").forward(request,response);
    }
}
