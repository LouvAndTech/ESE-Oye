package fr.eseoye.eseoye.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserPanel implements Action{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String contentPage = request.getParameter("contentPage");
        if(contentPage == null){
            contentPage = "Account";
        }
        request.setAttribute("contentPage", contentPage);
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }
}
