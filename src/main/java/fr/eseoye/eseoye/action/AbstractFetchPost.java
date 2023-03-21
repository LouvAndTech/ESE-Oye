package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.*;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostCategoryTable;
import fr.eseoye.eseoye.io.databases.tables.PostStateTable;
import fr.eseoye.eseoye.io.databases.tables.PostTable;
import fr.eseoye.eseoye.io.objects.FetchPostFilter;
import fr.eseoye.eseoye.utils.Tuple;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFetchPost {

    private final DatabaseCredentials dbCred;

    public AbstractFetchPost(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    protected static final int POST_PER_PAGE = 10;

    /**
     * Method called by the servlet to process a post request on the PersonalPosts page.
     * @param request  an {@link HttpServletRequest} object that
     * @param response an {@link HttpServletResponse} object that
     * @param userId  the id of the user
     * @throws Exception an {@link Exception}
     */
    protected void handlePage(HttpServletRequest request, HttpServletResponse response, String userId) throws Exception {
        request.setAttribute("userId", userId);
        handlePage(request, response, TypePost.PRIVATE);
    }
        /**
         * Fetch the posts from the database in case of a change of page
         * @param request  an {@link HttpServletRequest} object that
         * @param response an {@link HttpServletResponse} object that
         * @throws Exception an {@link Exception}
         */
    protected void handlePage(HttpServletRequest request, HttpServletResponse response, TypePost type) throws Exception {
        FetchPostFilter filters = null;
        //Print out every parameters:
        System.out.println("Parameters:");
        for(String s : request.getParameterMap().keySet()){
            System.out.println(s + " : " + request.getParameter(s));
        }
        if(type == TypePost.CLASSIC){
            //If it's a classic post we check the filters
            filters = fillFilters(request);
            request.setAttribute("cat",     filters.getCategoryID());
            request.setAttribute("state",   filters.getStateID());
            request.setAttribute("price",   filters.getMaxPrice());
            request.setAttribute("order",   filters.getOrder());
        }
        if(request.getParameter("newPage") != null && request.getParameter("newPage").matches("1")){
            System.out.println("Changing page");
            //Change page
            int page = Integer.parseInt(request.getParameter("postPage"));
            if(page < 0) page = 0; //make sure it's not negative
            if((fetchPost(POST_PER_PAGE, page,filters).getValueB()-1) < page  && page > 0){
                page--;
            }
            fillRequest(request, POST_PER_PAGE, page , filters, type);
        }else if(oneFilterExist(request)){
            //Change filters
            System.out.println("Changing filters");
            fillRequest(request, POST_PER_PAGE, 0 , filters, type);
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
     * @param type     a {@link TypePost} object
     * @throws Exception an {@link Exception} of the error if any
     */
    protected void fillRequest (HttpServletRequest request,int nbPost, int page, FetchPostFilter filters, TypePost type) throws Exception{
        Tuple<List<Post>,Integer> fromDB = fetchPost(nbPost, page , filters);
        System.out.println("size of batch : " + fromDB.getValueA().size());
        System.out.println("nb of page : " + fromDB.getValueB());
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("categories", fetchCategories());
        request.setAttribute("states", fetchStates());
        request.setAttribute("orders", fetchOrders());
        request.setAttribute("postPage", page);
    }

    /**
     * Check the filters from the request
     * @param request an {@link HttpServletRequest} object
     * @param nbPost the number of post to fetch
     * @param page the page numbers
     * @throws Exception an {@link Exception} of the error if any
     */
    protected void fillRequest (HttpServletRequest request,int nbPost, int page) throws Exception{
        Tuple<List<Post>,Integer> fromDB = fetchPost(nbPost, page , FetchPostFilter.builder().build());
        System.out.println("size of batch : " + fromDB.getValueA().size());
        System.out.println("nb of page : " + fromDB.getValueB());
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        request.setAttribute("categories", fetchCategories());
        request.setAttribute("states", fetchStates());
        request.setAttribute("orders", fetchOrders());
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("postPage", page);
    }

    /**
     * Fill a request with the posts and the number of page for a specific user
     * @param request an {@link HttpServletRequest} object
     * @param nbPost the number of post to fetch
     * @param page the page numbers
     * @param userId the id of the user
     * @throws Exception an {@link Exception} of the error if any
     */
    protected void fillRequest (HttpServletRequest request,int nbPost, int page, String userId) throws Exception {
        //todo add user id
        Tuple<List<Post>, Integer> fromDB = fetchPost(nbPost, page, FetchPostFilter.builder().user(userId).build());
        System.out.println("size of batch : " + fromDB.getValueA().size());
        System.out.println("nb of page : " + fromDB.getValueB());
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("postPage", page);
    }

    /**
     * Fetch the posts from the database
     * @return  a {@link List} of {@link FetchOrder} object
     */
    protected List<FetchOrder> fetchOrders(){
        List<FetchOrder> orders = new ArrayList<>();
        for (FetchPostFilter.FetchOrderEnum order : FetchPostFilter.FetchOrderEnum.values()){
            orders.add(order.getObject());
        }
        return orders;
    }

    /**
     * Fetch the posts from the database
     * @param nbPost    the number of post to fetch
     * @param page      the page number
     * @return          a list of {@link Post}
     */
    protected Tuple<List<Post>,Integer> fetchPost(int nbPost, int page, FetchPostFilter filters){
        return DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).fetchShortPost(nbPost, page,filters);
    }

    /**
     * Get all the categories from the database
     * @return a list of {@link Category}
     * @throws Exception
     */
    protected List<Category> fetchCategories() throws Exception{
        return DatabaseFactory.getInstance().getTable(PostCategoryTable.class, dbCred).fetchAllCategory();
    }

    /**
     * Get all the states from the database
     * @return a list of {@link PostState}
     * @throws Exception
     */
    protected List<PostState> fetchStates() throws Exception{
        return DatabaseFactory.getInstance().getTable(PostStateTable.class, dbCred).fetchAllState();
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

    /**
     * Check the filters from the request
     * @param request an {@link HttpServletRequest} object
     * @return a {@link FetchPostFilter} object filled with the request parameters
     */
    protected FetchPostFilter fillFilters(HttpServletRequest request){
        int idCategory, idState , price ;
        idCategory = idState = price = -1;
        String userId = null;
        FetchPostFilter.FetchOrderEnum order = FetchPostFilter.FetchOrderEnum.DATE_DESCENDING;
        try {
            idCategory = Integer.parseInt(request.getParameter("cat"));
            idState = Integer.parseInt(request.getParameter("state"));
            price = Integer.parseInt(request.getParameter("price"));
            order = FetchPostFilter.FetchOrderEnum.valueOf(request.getParameter("order"));
            userId = request.getParameter("userId");
        }catch (Exception ignored){}
        return FetchPostFilter.builder().category(idCategory).state(idState).maxPrice(price).order(order).user(userId).build();
    }

    protected boolean oneFilterExist(HttpServletRequest request){
        List<String> filtersName = new ArrayList<>(){{
            add("cat");
            add("state");
            add("price");
            add("order");
        }};
        for (String filter : filtersName){
            if(request.getParameter(filter) != null){
                return true;
            }
        }
        return false;
    }


    /**
     * The type of post to fetch
     *  - CLASSIC : fetch all the post (with the filters)
     *  - PRIVATE : fetch only the post of the current user (without the filters)
     */
    protected enum TypePost{
        CLASSIC, PRIVATE
    }
}
