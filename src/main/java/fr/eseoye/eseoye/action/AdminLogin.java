package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.action.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminLogin implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo : Has no use for now but mey never as any ... ?
        System.out.println("Admin Login : execute");
        response.sendRedirect("/ese-oye?id=Account");
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin Login : forward");
        request.getRequestDispatcher("/jsp/AdminLogin.jsp").forward(request,response);
    }
}
