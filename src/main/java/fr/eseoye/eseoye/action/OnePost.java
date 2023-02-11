package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.PostComplete;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

public class OnePost implements Action{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

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

    private PostComplete fetchPost(int postId){
        //todo : Fetch the post from the database
        return new PostComplete(1, "Chair", "Jean",1672, new Date(2020, 12, 12), "Description");
    }
}
