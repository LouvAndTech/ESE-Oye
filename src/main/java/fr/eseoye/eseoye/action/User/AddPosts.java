package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddPosts implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("AddPosts : execute (start)");
        try{
            //todo : List<String> imageUrl = SFTPFactory.getInstance().createNewConnection().uploadFile(request.getPart("image_drop").getInputStream());
            //todo : push the rest of the data to the server
            throw new Exception("Not implemented yet");
        }catch (Exception e){
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
            return;
        }

        //todo : get the author id from the session and the state
        //PostComplete post = new PostComplete(request.getParameter("title"), /* Author ID from SESSION*/ "author id", Integer.parseInt(request.getParameter("price")), null, request.getParameter("description"));
        //todo : add the annonce to the database
        //System.out.println("AddPosts : execute (end)");
        //request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        //todo : import the list of category
        List<String> categories = new ArrayList<>();
        categories.add("meuble");
        categories.add("objet bizarre");
        request.setAttribute("categories", categories);
        //todo : import the list of state
        List<String> states = new ArrayList<>();
        states.add("neuf");
        states.add("occasion");
        request.setAttribute("states", states);
        System.out.println("AddPosts : forward");
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }
}
