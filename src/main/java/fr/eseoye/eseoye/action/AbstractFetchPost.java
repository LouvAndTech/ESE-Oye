package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.Post;
import fr.eseoye.eseoye.beans.PostState;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.tables.PostTable;
import fr.eseoye.eseoye.io.objects.FetchPostFilter;
import fr.eseoye.eseoye.utils.Tuple;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFetchPost {

    protected static final int POST_PER_PAGE = 10;

    /**
     * Fetch the posts from the database in case of a change of page
     * @param request  an {@link HttpServletRequest} object that
     * @param response an {@link HttpServletResponse} object that
     * @throws Exception an {@link Exception}
     */
    protected void handlePage(HttpServletRequest request, HttpServletResponse response, TypePost type) throws Exception {
        FetchPostFilter filters = null;
        if(type == TypePost.CLASSIC){
            //If it's a classic post we check the filters
            filters = checkFilters(request);
            request.setAttribute("cat",     filters.getCategoryID());
            request.setAttribute("state",   filters.getStateID());
            request.setAttribute("price",   filters.getMaxPrice());
            request.setAttribute("order",   filters.getOrder());
        }
        if(request.getParameter("postPage") != null){
            //Change page
            int page = Integer.parseInt(request.getParameter("postPage"));
            if(page < 1) page = 1; //make sure it's not negative
            if(fetchPost(POST_PER_PAGE, page,filters,type).getValueB() < page  && page > 1){
                page--;
            }
            fillRequest(request, POST_PER_PAGE, page , filters, type);
        }else if(request.getParameter("order") != null){
            //Change order
            throw new Exception("Not implemented yet");
        }else if(request.getParameter("cat") != null || request.getParameter("state") != null) {
            //Change category
            fillRequest(request, POST_PER_PAGE, 1, filters, type);
        }else {
            //not supposed to happen, forward to the ListPosts.jsp
            throw new Exception("Nothing to do");
        }
    }

    /**
     * Fill the request with the posts and the number of page
     * @param request  an {@link HttpServletRequest} object
     * @param nbPost   the number of post to fetch
     * @param page     the page numbers
     * @param filters  a {@link FetchPostFilter} object
     */
    protected void fillRequest (HttpServletRequest request,int nbPost, int page, FetchPostFilter filters, TypePost type) throws Exception{
        Tuple<List<Post>,Integer> fromDB = fetchPost(nbPost, page , filters, type);
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("categories", fetchCategories());
        request.setAttribute("states", fetchStates());
        AddOrders(request);
        request.setAttribute("postPage", page);
    }
    protected void fillRequest (HttpServletRequest request,int nbPost, int page, TypePost type) throws Exception{
        Tuple<List<Post>,Integer> fromDB = fetchPost(nbPost, page ,new FetchPostFilter(), type);
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        if(type == TypePost.CLASSIC){
            request.setAttribute("categories", fetchCategories());
            request.setAttribute("states", fetchStates());
            AddOrders(request);
        }
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("postPage", page);
    }

    protected void AddOrders(HttpServletRequest request){
        List<Orders> orders = new ArrayList<>();
        for (FetchPostFilter.FetchOrder order : FetchPostFilter.FetchOrder.values()){
            String s = order.toString().toLowerCase().replace("_", " ");
            orders.add(new Orders(s.substring(0,1).toUpperCase()+s.substring(1),order.toString()));
        }
        request.setAttribute("orders", orders);
    }

    /**
     * Fetch the posts from the database
     * @param nbPost    the number of post to fetch
     * @param page      the page number
     * @return          a list of {@link Post}
     */
    protected Tuple<List<Post>,Integer> fetchPost(int nbPost, int page, FetchPostFilter filters,TypePost type){
        return DatabaseFactory.getInstance().getTable(PostTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials());
    }

    /**
     * Get all the categories from the database
     * @return a list of {@link Category}
     * @throws Exception
     */
    protected static List<Category> fetchCategories() throws Exception{
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
    protected List<PostState> fetchStates() throws Exception{
        List<PostState> states = new ArrayList<>();
        for (int i = 0;i<10;i++){
            states.add(new PostState(i,"State"+i));
        }
        return states;
    }

    /**
     * Handle the display of the page number in the pagination
     * @param nbPage   the number of page total
     * @param page      the current page
     * @return        a list of {@link Integer} containing the page number to display
     */
    protected List<Integer> handleNBpage(int nbPage, int page){
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

    protected FetchPostFilter checkFilters (HttpServletRequest request){
        int idCategory, idState , price ;
        idCategory = idState = price = -1;
        FetchPostFilter.FetchOrder order = FetchPostFilter.FetchOrder.DATE_DESCENDING;
        try {
            idCategory = Integer.parseInt(request.getParameter("cat"));
            idState = Integer.parseInt(request.getParameter("state"));
            price = Integer.parseInt(request.getParameter("price"));
            order = FetchPostFilter.FetchOrder.valueOf(request.getParameter("order"));
        }catch (Exception ignored){}
        return new FetchPostFilter(idCategory, idState, order, price);
    }


    //Private Type
    protected enum TypePost{
        CLASSIC, PRIVATE
    }

    public class Orders{
        public String name;
        public String value;

        public Orders(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }
        public String getValue() {
            return value;
        }
    }
}
