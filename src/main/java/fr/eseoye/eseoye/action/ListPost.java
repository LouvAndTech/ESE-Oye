package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.Post;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.databases.DAOFactory;
import fr.eseoye.eseoye.databases.DatabaseType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ListPost implements Action{

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
        //if the user click on the next button
        try{
            //Get the page number from the request and increment it
            int page = Integer.parseInt(request.getParameter("postPage"));
            if(page < 1) page = 1; //make sure it's not negative
            //Get the 10 posts corresponding from the database
            List<Post> posts = fetchPost(10, page);
            //If there is no more post to display, we decrement the page number and get the right list of posts
            if(posts.size() == 0){
                page--;
                posts = fetchPost(10, page);
            }
            System.out.println("Page : " + page);
            //Set the page number to the request
            request.setAttribute("postPage", page);
            //Forward to the ListPosts.jsp
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
        }catch (Exception e){
            //If the parse fail, we forward to the ListPosts.jsp with the first 10 posts
            //also append when there is no more post to display
            System.out.println("Error : " + e.getMessage());
            this.forward(request, response, "/jsp/ListPosts.jsp");
        }
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
        request.setAttribute("posts", fetchPost(10, 1   ));
        request.setAttribute("postPage", 1);
        request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
    }


    /**
     * [WIP] Fetch the posts from the database
     * @param nbPost    the number of post to fetch
     * @param page      the page number
     * @return          a list of {@link Post}
     */
    private List<Post> fetchPost(int nbPost, int page ){
        //todo : Fetch the post from the database
        List <Post> posts = new ArrayList<>();
        //posts.add(new Post(4, "Lit", "Pen", 100, new Date(2023, 2, 18)));
        //posts.add(new Post(3, "Commode", "Le", 256, new Date(2021, 10, 28)));
        //posts.add(new Post(2, "Table", "Marie",3, new Date(2021, 5, 3)));
        //posts.add(new Post(1, "Chair", "Jean",1672, new Date(2020, 12, 12)));
        return posts;
    }
}
