package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.AbstractFetchPost;
import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Posts extends AbstractFetchPost implements Action {
    public Posts(DatabaseCredentials dbCred) {
        super(dbCred);
    }

    /**
     * The Method that will be called when the user send a post request on the list of his posts
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException if the target resource throws this exception
     * @throws IOException    if the target resource throws this exception
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            handlePage(request, response, request.getSession().getAttribute("idUser").toString());
        }catch (Exception e){
            //If there is as errpr we forward to the ListPosts.jsp with the first 10 posts
            this.forward(request, response, "/jsp/UserPanel.jsp");
        }
        //Forward to the ListPosts.jsp with the right posts and page number
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
    }

    /**
     * The Method that will be called when the user want to see the list of his posts
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
     * @throws ServletException if the target resource throws this exception
     * @throws IOException if the target resource throws this exception
     */
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        try {
            fillRequest(request, POST_PER_PAGE, 1, request.getSession().getAttribute("idUser").toString());
        } catch (Exception ignored) {}
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }
}
