package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.PostState;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.SFTPFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostCategoryTable;
import fr.eseoye.eseoye.io.databases.tables.PostStateTable;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddPosts implements Action {

    private final DatabaseCredentials dbCred;

    public AddPosts(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    /**
     * Method called by the servlet to process a post request on the AddPost page.
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("AddPosts : execute (start)");
        String idUser = request.getSession().getAttribute("idUser").toString();
        List<InputStream> images = request.getParts().stream().filter(part -> part.getName().equals("image_drop")).map(part -> {
            try {
                return part.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();

        try{
            PostToAdd p = new PostToAdd(request, images);
            p.isComplete(); // throws exception if not complete

            DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).createNewPost(
                    SFTPFactory.getInstance().createNewConnection(),
                    idUser,
                    p.title,
                    p.description,
                    p.price,
                    p.category,
                    p.state,
                    p.images);
        }catch (Exception e){
            request.setAttribute("error", e.getMessage());
            e.printStackTrace();
            forward(request, response, "/jsp/UserPanel.jsp");
            return;
        }
        request.setAttribute("contentPage","Annonce");
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }

    /**
     * Method called by the servlet to process a get request on the AddPost page.
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @param target    a string to define the view to forward
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        request.setAttribute("categories", DatabaseFactory.getInstance().getTable(PostCategoryTable.class, dbCred ).fetchAllCategory());
        request.setAttribute("states",DatabaseFactory.getInstance().getTable(PostStateTable.class, dbCred ).fetchAllState());
        System.out.println("AddPosts : forward");
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }


    /**
     * Class used to store the data of a post to add
     */
    private class PostToAdd {
        public String title;
        public String description;
        public Float price;
        public Integer category;
        public Integer state;
        public List<InputStream> images;

        /**
         * Constructor of the class
         * @param request an {@link HttpServletRequest} object that contains the request the client has made on the AddPost page
         * @param images a list of {@link InputStream} containing the images of the new post
         */
        public PostToAdd(HttpServletRequest request, List<InputStream> images){
            this.title = request.getParameter("title");
            this.description = request.getParameter("description");
            this.price = Float.parseFloat(request.getParameter("price"));
            this.category = Integer.parseInt(request.getParameter("category"));
            this.state = Integer.parseInt(request.getParameter("state"));
            this.images = images;
        }
        /**
         * Method to check if all the fields are filled
         * @throws IllegalArgumentException if a field is not filled
         */
        public void isComplete() throws IllegalArgumentException{
            if(title == null || description == null || price == null || category == null || state == null || images == null)
                throw new IllegalArgumentException("Tous les champs doivent être remplis");
        }
    }
}
