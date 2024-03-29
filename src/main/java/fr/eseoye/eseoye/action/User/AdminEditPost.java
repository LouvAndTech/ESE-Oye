package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.AbstractOnePost;
import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class AdminEditPost extends AbstractOnePost implements Action {

    public AdminEditPost(DatabaseCredentials dbCred) {
        super(dbCred);
    }

    /**
     * Method called by the servlet to process a post request on the AddPost page.
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
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        System.out.println("AdminEditPost : execute");
        try {
            if (request.getParameter("description") == null || request.getParameter("description").equals(""))
                throw new Exception("Remplir le champ description");
            if (request.getParameter("title") == null || request.getParameter("title").equals(""))
                throw new Exception("Remplir le champ titre");
            if (request.getParameter("postId") == null || request.getParameter("postId").equals(""))
                throw new Exception("Pas d'id de post");
            DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).modifyPost(request.getParameter("postId"), request.getParameter("title"), request.getParameter("description"));
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            e.printStackTrace();
            this.forward(request, response, "");
            return;
        }
        response.sendRedirect(request.getRequestURI() + "?id=UserPanel&contentPage=AdminValidePost");
    }

    /**
     * Method called by the servlet to process a get request on the AddPost page.
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
        System.out.println("AdminEditPost : forward");
        try {
            fillPost(request, Format.TXT);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
    }
}
