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
     * @param type     a {@link TypePost} object
     * @throws Exception an {@link Exception} of the error if any
     */
    protected void fillRequest (HttpServletRequest request,int nbPost, int page, FetchPostFilter filters, TypePost type) throws Exception{
        Tuple<List<Post>,Integer> fromDB = fetchPost(nbPost, page , filters, type);
        System.out.println(fromDB.getValueA().size());
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("categories", fetchCategories());
        request.setAttribute("states", fetchStates());
        request.setAttribute("orders", fetchOrders(request));
        request.setAttribute("postPage", page);
    }

    /**
     * Check the filters from the request
     * @param request an {@link HttpServletRequest} object
     * @param nbPost the number of post to fetch
     * @param page the page numbers
     * @param type a {@link TypePost} object
     * @throws Exception an {@link Exception} of the error if any
     */
    protected void fillRequest (HttpServletRequest request,int nbPost, int page, TypePost type) throws Exception{
        System.out.println(type);
        System.out.println(fetchPost(nbPost, page , FetchPostFilter.builder().build(), type));
        Tuple<List<Post>,Integer> fromDB = fetchPost(nbPost, page , FetchPostFilter.builder().build(), type);
        List<Integer> nbPage = handleNBpage(fromDB.getValueB(), page);
        request.setAttribute("posts", fromDB.getValueA());
        if(type == TypePost.CLASSIC){
            System.out.println("Classic");
            System.out.println(fetchCategories());
            request.setAttribute("categories", fetchCategories());
            request.setAttribute("states", fetchStates());
            request.setAttribute("orders", fetchOrders(request));
        }
        request.setAttribute("nbPage", nbPage);
        request.setAttribute("postPage", page);
    }

    /**
     * Fetch the posts from the database
     * @param request
     * @return  a {@link List} of {@link FetchOrder} object
     */
    protected List<FetchOrder> fetchOrders(HttpServletRequest request){
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
    protected Tuple<List<Post>,Integer> fetchPost(int nbPost, int page, FetchPostFilter filters,TypePost type){
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
    protected FetchPostFilter checkFilters (HttpServletRequest request){
        int idCategory, idState , price ;
        idCategory = idState = price = -1;
        FetchPostFilter.FetchOrderEnum order = FetchPostFilter.FetchOrderEnum.DATE_DESCENDING;
        try {
            idCategory = Integer.parseInt(request.getParameter("cat"));
            idState = Integer.parseInt(request.getParameter("state"));
            price = Integer.parseInt(request.getParameter("price"));
            order = FetchPostFilter.FetchOrderEnum.valueOf(request.getParameter("order"));
        }catch (Exception ignored){}
        return FetchPostFilter.builder().category(idCategory).state(idState).maxPrice(price).order(order).build();
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
