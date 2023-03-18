package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseType;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.beans.User;

public class OnePost implements Action{

    /**
     * Unused for now
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
        //todo : Has no use for now but mey never as any ... ?
    }

    /**
     * Load the post asked before forwarding to the page
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
        System.out.println("OnePost");
        try{
            int postId = Integer.parseInt(request.getParameter("postId"));
            System.out.println("postId : "+postId);
            request.setAttribute("post", fetchPost( postId ));
            request.getRequestDispatcher("/jsp/OnePost.jsp").forward(request, response);
        }catch (Exception e){
            System.out.println("Error : " + e.getMessage());
            this.forward(request, response, "/jsp/ListPosts.jsp");
        }
    }

    /**
     * Fetch the post from the database
     * @param postId    the id of the post to fetch
     * @return          the post as a {@link PostComplete}
     */
    private PostComplete fetchPost(int postId){
        //todo : Fetch the post from the database
        return new PostComplete("1", "Chair", new User(null, "Jean","Vend", "lol", new Date(System.currentTimeMillis()), "0606060606", "j@j.j", "TierMonde"),1672, new Date(2020, 12, 12),
                "Un appareil à raclette est un petit appareil de cuisine conçu pour faire fondre du fromage et le servir avec des accompagnements tels que des pommes de terre, des cornichons, de la charcuterie, etc. Il se compose généralement d'une plaque chauffante qui permet de faire fondre le fromage et de le faire couler dans des poêlons individuels qui sont placés sous la plaque chauffante.\n" +
                "\nLes poêlons sont généralement disposés autour de la plaque chauffante et peuvent être retirés pour que les convives puissent servir le fromage fondu sur leurs aliments. La plaque chauffante est souvent dotée d'un contrôle de température pour régler la chaleur en fonction des préférences individuelles.\n" +
                "\nLes appareils à raclette peuvent être électriques ou fonctionner avec des bougies chauffe-plat. Ils peuvent varier en taille et en capacité, avec des modèles allant de deux à douze poêlons.\n",
                new Category(1, "Chair"),"http://eseoye.elouan-lerissel.fr/blankImg.png",new ArrayList<>(){{for (int i = 0; i < 5; i++) add("http://eseoye.elouan-lerissel.fr/blankImg.png");}});
    }
}
