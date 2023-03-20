package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.AbstractFetchPost;
import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Posts extends AbstractFetchPost implements Action {
    private final DatabaseCredentials dbCred;
    public Posts(DatabaseCredentials dbCred) {
        this.dbCred = dbCred;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            handlePage(request, response, TypePost.PRIVATE);
        }catch (Exception e){
            //If there is as errpr we forward to the ListPosts.jsp with the first 10 posts
            this.forward(request, response, "/jsp/UserPanel.jsp");
        }
        //Forward to the ListPosts.jsp with the right posts and page number
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        try {
            fillRequest(request, POST_PER_PAGE, 1, TypePost.PRIVATE);
        } catch (Exception ignored) {}
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }
}
