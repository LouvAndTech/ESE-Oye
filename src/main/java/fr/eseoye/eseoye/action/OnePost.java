package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.DatabaseType;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

public class OnePost implements Action{

    private final DatabaseCredentials dbCred;

    public OnePost(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

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
     * @throws ServletException an {@link ServletException}
     * @throws IOException     an {@link IOException}
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
        HttpSession session = request.getSession();
        request.setAttribute("adminState", session.getAttribute("admin"));

        System.out.println("OnePost");
        try{
            String postId = request.getParameter("postId");
            System.out.println("postId : "+postId);
            request.setAttribute("post", fetchPost( postId ));
            request.getRequestDispatcher("/jsp/OnePost.jsp").forward(request, response);
        }catch (Exception e){
            System.out.println("Error : " + e.getMessage());
            request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
        }
    }

    /**
     * Fetch the post from the database
     * @param postId    the id of the post to fetch
     * @return          the post as a {@link PostComplete}
     */
    private PostComplete fetchPost(String postId){
        PostComplete p = DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).fetchEntirePost(postId);
        System.out.println("postcomplete author secure ID : "+p.getAuthor().getSecureId());
        p.setContent(p.getContent().replace("\n", "<br>"));
        return p;
    }
}
