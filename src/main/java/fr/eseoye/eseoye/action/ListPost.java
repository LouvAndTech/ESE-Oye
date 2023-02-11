package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.Post;
import fr.eseoye.eseoye.beans.PostComplete;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ListPost implements Action{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //if the user click on the next button
        try{
            //Get the page number, and put it back in the request
            int page = Integer.parseInt(request.getParameter("postPage"));
            if(page < 1) page = 1; //make sure it's not negative
            request.setAttribute("postPage", page);
            //Get the 10 posts corresponding from the database and forward to the ListPosts.jsp
            request.setAttribute("posts", fetchPost(10, page));
            request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
        }catch (Exception e){
            //If the parse fail, we forward to the ListPosts.jsp with the first 10 posts
            //also append when there is no more post to display
            System.out.println("Error : " + e.getMessage());
            this.forward(request, response, "/jsp/ListPosts.jsp");
        }
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        request.setAttribute("posts", fetchPost(10, 1   ));
        request.setAttribute("postPage", 1);
        request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
    }

    private List<Post> fetchPost(int nbPost, int page ){
        //todo : Fetch the post from the database
        List <Post> posts = new ArrayList<>();
        posts.add(new Post(4, "Lit", "Pen", 100, new Date(2023, 2, 18)));
        posts.add(new Post(3, "Commode", "Le", 256, new Date(2021, 10, 28)));
        posts.add(new Post(2, "Table", "Marie",3, new Date(2021, 5, 3)));
        posts.add(new Post(1, "Chair", "Jean",1672, new Date(2020, 12, 12)));
        return posts;
    }
}
