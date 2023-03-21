package fr.eseoye.eseoye.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Logout implements Action{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException, SQLException {
        response.sendRedirect(request.getContextPath()+"/ese-oye?id=ListPosts");
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin list user : forward");

        HttpSession session = request.getSession();
        session.setAttribute("admin", false);
        session.setAttribute("idUser", null);

        response.sendRedirect(request.getContextPath()+"/ese-oye?id=ListPosts");
    }
}
