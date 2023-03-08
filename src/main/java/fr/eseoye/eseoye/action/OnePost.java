package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.databases.DAOFactory;
import fr.eseoye.eseoye.databases.DatabaseType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

public class OnePost implements Action{

    /**
     * Unused for now
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
        //todo : Has no use for now but mey never as any ... ?
    }

    /**
     * Load the post asked before forwarding to the page
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
        System.out.println("OnePost");
        try{
            int postId = Integer.parseInt(request.getParameter("postId"));
            System.out.println("postId : "+postId);
            request.setAttribute("post", fetchPost( postId ));
            request.getRequestDispatcher("/jsp/OnePost.jsp").forward(request, response);
        }catch (Exception e){
            System.out.println("Error : " + e.getMessage());
            this.forward(request, response, "/jsp/ListPosts.jsp");
        }
    }

    /**
     * Fetch the post from the database
     * @param postId    the id of the post to fetch
     * @return          the post as a {@link PostComplete}
     */
    private PostComplete fetchPost(int postId){
        //todo : Fetch the post from the database
        PostComplete post = DAOFactory.getInstance().getPostTable(DatabaseType.MARIADB,"eseoye").fetchEntirePost(postId);
        System.out.println("Post : " + post);
        return post;
    }
}
