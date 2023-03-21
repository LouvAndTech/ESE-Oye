package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.io.databases.DatabaseCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OnePost extends AbstractOnePost implements Action {

    public OnePost(DatabaseCredentials dbCred) {
        super(dbCred);
    }

    /**
     * Unused for now
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            executeAction(request, response);
        } catch (Exception e) {
            response.sendRedirect("/ese-oye?id=ListPosts");
        }
        response.sendRedirect("/ese-oye?id=ListPosts");
    }

    /**
     * Load the post asked before forwarding to the page
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setAttribute("adminState", session.getAttribute("admin"));

        System.out.println("OnePost");
        try {
            fillPost(request, Format.HTML);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/jsp/OnePost.jsp").forward(request, response);
    }


}
