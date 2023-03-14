package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.databases.DAOFactory;
import fr.eseoye.eseoye.databases.DatabaseType;
import fr.eseoye.eseoye.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Ternary;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class Inscription implements Action{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getParameter("name");
        request.getParameter("surname");
        request.getParameter("phone");
        request.getParameter("mail");
        request.getParameter("password");
        request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);

        /*Ternary resultMail = DAOFactory.getInstance().getUserTable().isMailAlreadyUsed(request.getParameter("mail"));
        if(resultMail == Ternary.TRUE) {
            request.setAttribute("error", "Mail already used");
            request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);

        }else if(resultMail == Ternary.FALSE) {
            /*DAOFactory.getInstance().getPostTable(DatabaseType.MARIADB, "eseoye").createPost(request.getParameter("name");
                  request.getParameter("surname");
                  request.getParameter("phone");
                  request.getParameter("mail");
                  request.getParameter("password"););

        }else if(resultMail == Ternary.UNDEFINED) {

        }*/
    }




}
