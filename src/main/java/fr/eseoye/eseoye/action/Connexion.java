package fr.eseoye.eseoye.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Connexion  implements Action{
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("mail");
        String password = request.getParameter("password");
        request.getRequestDispatcher("/jsp/Connexion.jsp").forward(request,response);
        HttpSession session = request.getSession(true);
        session.setAttribute("login", login);
        session.setAttribute("password", password);
        //DAOFactory.getInstance().getPostTable(DatabaseType.MARIADB, "eseoye").createPost();

    }


}
