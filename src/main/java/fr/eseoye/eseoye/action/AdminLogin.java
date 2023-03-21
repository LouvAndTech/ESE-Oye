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
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminLogin implements Action {

    /**
     * Try if the admin exist and if the password is correct before set the session for the admin
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
        System.out.println("Admin Login : execute");
        String admin = DatabaseFactory.getInstance().getTable(AdminTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).checkAdminConnection(request.getParameter("pseudo"), request.getParameter("password"));
        if(admin != null) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", true);
            session.setAttribute("idUser", admin);
        }
        response.sendRedirect(request.getContextPath()+"/ese-oye?id=UserPanel&contentPage=AdminListUser");
        //request.getRequestDispatcher("/jsp/AdminLogin.jsp").forward(request,response);
    }

    /**
     * Forward to the admin login page
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
        System.out.println("Admin Login : forward");
        request.getRequestDispatcher("/jsp/AdminLogin.jsp").forward(request,response);
    }
}
