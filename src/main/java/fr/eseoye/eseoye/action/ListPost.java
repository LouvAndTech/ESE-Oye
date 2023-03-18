package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.Post;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseType;
import fr.eseoye.eseoye.utils.Tuple;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ListPost implements Action{

    private static final int POST_PER_PAGE = 10;

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
            if(request.getParameter("postPage") != null){
                //Change page
                int page = Integer.parseInt(request.getParameter("postPage"));
                if(page < 1) page = 1; //make sure it's not negative
                if(fetchPost(POST_PER_PAGE, page).getValueB() < page  && page > 1){
                    page--;
                }
                fillRequest(request, POST_PER_PAGE, page);
            }else if(request.getParameter("order") != null){
                //Change order
                throw new Exception("Not implemented yet");
            }else {
                //not supposed to happen, forward to the ListPosts.jsp
                throw new Exception("Nothing to do");
            }
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
        try {
            fillRequest(request, POST_PER_PAGE, 1);
        } catch (Exception e) {

        }
        request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
    }

    /**
     * Fill the request with the posts and the number of page
     * @param request  an {@link HttpServletRequest} object
     * @param nbPost   the number of post to fetch
     * @param page     the page numbers
     */
    private void fillRequest (HttpServletRequest request,int nbPost, int page) throws Exception{
        Tuple<List<Post>,Integer> fromDB = fetchPost(nbPost, page );
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("postPage", page);
    }

    /**
     * [WIP] Fetch the posts from the database
     * @param nbPost    the number of post to fetch
     * @param page      the page number
     * @return          a list of {@link Post}
     */
    private Tuple<List<Post>,Integer> fetchPost(int nbPost, int page ){
        //todo : Fetch the post from the database
        List <Post> posts = new ArrayList<>();
        for(int i = 0; i < nbPost; i++){
            User us = new User( String.valueOf(i),"Name "+i,"Surname "+i, "pass",new Date(System.currentTimeMillis()),"0987654321","mail@mail.mail","OK");
            Post post = new Post(String.valueOf(i), "Title"+i, us,i+10, new Date(System.currentTimeMillis()),new Category(i, "Cat"+i),"http://eseoye.elouan-lerissel.fr/blankImg.png");
            posts.add(post);
        }
        int maxPageProv = 7;
        if(page > maxPageProv) posts = new ArrayList<>();
        return new Tuple<>(posts, maxPageProv);
    }

    /**
     * Handle the display of the page number in the pagination
     * @param nbPage   the number of page total
     * @param page      the current page
     * @return        a list of {@link Integer} containing the page number to display
     */
    private List<Integer> handleNBpage(int nbPage, int page){
        if(nbPage > 5){
            if(page <= 3){
                return new ArrayList<Integer>(){{
                    for (int i = 1; i <= 5; i++){
                        add(i);
                    }
                }};
            }else if(page >= nbPage - 2){
                return new ArrayList<Integer>(){{
                    for(int i = 4; i >= 0; i--){
                        add(nbPage - i);
                    }
                }};
            }else{
                return new ArrayList<Integer>(){{
                    for (int i = -2; i <= 2; i++){
                        add(page + i);
                    }
                }};
            }
        }else {
            List<Integer> list = new ArrayList<>();
            for(int i = 1; i <= nbPage; i++){
                list.add(i);
            }
            return list;
        }
    }
}
