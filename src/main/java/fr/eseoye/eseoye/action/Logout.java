package fr.eseoye.eseoye.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Logout implements Action {

    /**
     * Not supposed to be used so retunr to main page
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @throws SQLException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException, SQLException {
        response.sendRedirect(request.getContextPath() + "/ese-oye?id=ListPosts");
    }

    /**
     * Logout the user and redirect to the home page.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @param target   a string to define the view to forward
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin list user : forward");

        HttpSession session = request.getSession();
        session.setAttribute("admin", false);
        session.setAttribute("idUser", null);

        response.sendRedirect(request.getContextPath() + "/ese-oye?id=ListPosts");
    }
}
