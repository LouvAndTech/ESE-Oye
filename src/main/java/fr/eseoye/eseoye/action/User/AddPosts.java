package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddPosts implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo : Has no use for now but mey never as any ... ?
        System.out.println("AddPosts : execute");
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("AddPosts : forward");
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }
}
