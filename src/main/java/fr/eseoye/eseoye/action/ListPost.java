package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.io.databases.DatabaseCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListPost extends AbstractFetchPost implements Action{

    public ListPost(DatabaseCredentials dbCred) {
        super(dbCred);
    }

    /**
     * Handle the next and previous button
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            handlePage(request, response, TypePost.CLASSIC);
        }catch (Exception e){
            //If there is as errpr we forward to the ListPosts.jsp with the first 10 posts
            this.forward(request, response, "/jsp/ListPosts.jsp");
        }
        //Forward to the ListPosts.jsp with the right posts and page number
        request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
    }

    /**
     * Load the first 10 posts from the database before forwarding to the page
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Forwarding to " + target);
        try {
            fillRequest(request, POST_PER_PAGE, 0, TypePost.CLASSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
    }
}
