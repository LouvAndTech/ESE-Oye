package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.*;
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
        //Check the Filters the user has selected
        Tuple<Integer,Integer> filters = checkFilters(request.getParameter("cat"),request.getParameter("state"));
        Integer idCategory = filters.getValueA();
        Integer idState = filters.getValueB();
        request.setAttribute("cat", idCategory);
        request.setAttribute("state", idState);
        System.out.println("Cat : "+idCategory+" State : "+idState);
        try{
            if(request.getParameter("postPage") != null){
                //Change page
                int page = Integer.parseInt(request.getParameter("postPage"));
                if(page < 1) page = 1; //make sure it's not negative
                if(fetchPost(POST_PER_PAGE, page, idCategory,idState).getValueB() < page  && page > 1){
                    page--;
                }
                fillRequest(request, POST_PER_PAGE, page , idCategory, idState);
            }else if(request.getParameter("order") != null){
                //Change order
                throw new Exception("Not implemented yet");
            }else if(request.getParameter("cat") != null || request.getParameter("state") != null) {
                //Change category
                fillRequest(request, POST_PER_PAGE, 1, idCategory, idState);
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
     * @param idCategory the id of the category
     * @param idState the id of the state
     */
    private void fillRequest (HttpServletRequest request,int nbPost, int page, int idCategory, int idState) throws Exception{
        Tuple<List<Post>,Integer> fromDB = fetchPost(nbPost, page , idCategory, idState);
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("categories", fetchCategories());
        request.setAttribute("states", fetchStates());
        request.setAttribute("postPage", page);
    }

    /**
     * Fill the request with the posts and the number of page
     * @param request  an {@link HttpServletRequest} object
     * @param nbPost   the number of post to fetch
     * @param page     the page numbers
     */
    private void fillRequest (HttpServletRequest request,int nbPost, int page) throws Exception{
        this.fillRequest(request, nbPost, page, -1, -1);
    }

    /**
     * Get all the categories from the database
     * @return a list of {@link Category}
     * @throws Exception
     */
    private List<Category> fetchCategories() throws Exception{
        List<Category> categories = new ArrayList<>();
        for (int i = 0;i<10;i++){
            categories.add(new Category(i,"Cat"+i));
        }
        return categories;
    }

    /**
     * Get all the states from the database
     * @return a list of {@link PostState}
     * @throws Exception
     */
    private List<PostState> fetchStates() throws Exception{
        List<PostState> states = new ArrayList<>();
        for (int i = 0;i<10;i++){
            states.add(new PostState(i,"State"+i));
        }
        return states;
    }

    /**
     * [WIP] Fetch the posts from the database
     * @param nbPost    the number of post to fetch
     * @param page      the page number
     * @return          a list of {@link Post}
     */
    private Tuple<List<Post>,Integer> fetchPost(int nbPost, int page, int idCategory, int idState){
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

    /**
     * Check if the filters are valid
     * @param cat   the category id
     * @param state the state id
     * @return      a {@link Tuple} containing the category id and the state id
     */
    private Tuple<Integer,Integer> checkFilters (String cat, String state){
        int idCategory = -1;
        int idState = -1;
        if(cat != null){
            try {
                idCategory = Integer.parseInt(cat);
            }catch (Exception ignored){
            }
        }
        if(state != null){
            try {
                idState = Integer.parseInt(state);
            }catch (Exception ignored){
            }
        }
        return new Tuple<>(idCategory, idState);
    }
}
