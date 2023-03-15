package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddPosts implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uploadPath = request.getServletContext().getRealPath("") + File.separator + "images";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        Part part = request.getPart("image_drop");
        String fileName = part.getSubmittedFileName();
        part.write(uploadPath + File.separator + fileName);
        File folder = new File(request.getSession().getServletContext().getRealPath("/images"));
        //todo : get the author id from the session and the state
        //PostComplete post = new PostComplete(request.getParameter("title"), /* Author ID from SESSION*/ "author id", Integer.parseInt(request.getParameter("price")), null, request.getParameter("description"));
        //todo : add the annonce to the database
        System.out.println("AddPosts : execute");
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
