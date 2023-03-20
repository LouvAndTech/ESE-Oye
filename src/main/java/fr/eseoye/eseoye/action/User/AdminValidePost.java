package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.beans.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class AdminValidePost implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo : Has no use for now but mey never as any ... ?
        System.out.println("Admin list user : execute");
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin list user : forward");
        try{
            // todo : get older not validate annonce
            request.setAttribute("post", fetchPost( 1 ));
            request.setAttribute("postID", 1);
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
        }catch (Exception e){
            System.out.println("Error : " + e.getMessage());
            request.setAttribute("postID", 0);
            this.forward(request, response, "/jsp/UserPanel.jsp");
        }
    }

    private PostComplete fetchPost(int postId){
        //todo : Fetch the post from the database
        PostComplete post = new PostComplete("1", "Chair", new User(null, "Jean","Vend", "lol", new Date(System.currentTimeMillis()), "0606060606", "j@j.j", "TierMonde"),1672, new Date(2020, 12, 12),
                "Un appareil à raclette est un petit appareil de cuisine conçu pour faire fondre du fromage et le servir avec des accompagnements tels que des pommes de terre, des cornichons, de la charcuterie, etc. Il se compose généralement d'une plaque chauffante qui permet de faire fondre le fromage et de le faire couler dans des poêlons individuels qui sont placés sous la plaque chauffante.\n" +
                        "\nLes poêlons sont généralement disposés autour de la plaque chauffante et peuvent être retirés pour que les convives puissent servir le fromage fondu sur leurs aliments. La plaque chauffante est souvent dotée d'un contrôle de température pour régler la chaleur en fonction des préférences individuelles.\n" +
                        "\nLes appareils à raclette peuvent être électriques ou fonctionner avec des bougies chauffe-plat. Ils peuvent varier en taille et en capacité, avec des modèles allant de deux à douze poêlons.\n",
                new Category(1, "Chair"),"http://eseoye.elouan-lerissel.fr/blankImg.png",new ArrayList<>(){{for (int i = 0; i < 5; i++) add("http://eseoye.elouan-lerissel.fr/blankImg.png");}});

        post.setContent(post.getContent().replace("\n", "<br>"));
        return post;
    }
}
