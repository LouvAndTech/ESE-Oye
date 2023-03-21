package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostCategoryTable;
import fr.eseoye.eseoye.io.databases.tables.PostStateTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AbstractNewPost {

    protected final DatabaseCredentials dbCred;

    public AbstractNewPost(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    /**
     * Method used to fill the request with the categories and states of the posts
     * @param request an {@link HttpServletRequest} object that contains the request the client has made on the AddPost page
     */
    protected void fillCategoriesStates(HttpServletRequest request) {
        request.setAttribute("categories", DatabaseFactory.getInstance().getTable(PostCategoryTable.class, dbCred ).fetchAllCategory());
        request.setAttribute("states",DatabaseFactory.getInstance().getTable(PostStateTable.class, dbCred ).fetchAllState());
    }

    /**
     * Method used to get the images from the request
     * @param request an {@link HttpServletRequest} object
     * @return  a list of {@link InputStream} containing the images of the new post
     */
    public List<InputStream> getImagesFromPart(HttpServletRequest request) throws ServletException, IOException {
        return request.getParts().stream().filter(part -> part.getName().equals("image_drop")).map(part -> {
            try {
                return part.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();
    }

    /**
     * Class used to store the data of a post to add
     */
    protected class analysePost {
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
        public analysePost(HttpServletRequest request, List<InputStream> images){
            this.title = request.getParameter("title");
            this.description = request.getParameter("description");
            this.price = Float.parseFloat(request.getParameter("price"));
            this.category = Integer.parseInt(request.getParameter("category"));
            this.state = Integer.parseInt(request.getParameter("state"));
            this.images = images;
        }

        public analysePost(HttpServletRequest request){
            new analysePost(request, null);
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
