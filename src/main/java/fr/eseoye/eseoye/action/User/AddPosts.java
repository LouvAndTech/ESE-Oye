package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.AbstractNewPost;
import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.SFTPFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AddPosts extends AbstractNewPost implements Action {

    public AddPosts(DatabaseCredentials dbCred){
        super(dbCred);
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

        try{
            List<InputStream> images = super.getImagesFromPart(request);
            analysePost p = new analysePost(request, images);
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
        System.out.println("AddPosts : forward");
        fillCategoriesStates(request);
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }
}
