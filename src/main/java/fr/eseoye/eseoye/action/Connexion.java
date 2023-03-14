package fr.eseoye.eseoye.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Connexion  implements Action{
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getParameter("mail");
        request.getParameter("password");
        request.getRequestDispatcher("/jsp/Connexion.jsp").forward(request,response);
        //DAOFactory.getInstance().getPostTable(DatabaseType.MARIADB, "eseoye").createPost();

    }


}
