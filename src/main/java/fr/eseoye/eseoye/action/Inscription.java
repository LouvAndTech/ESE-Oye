package fr.eseoye.eseoye.action;


import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseType;
import fr.eseoye.eseoye.utils.Ternary;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class Inscription implements Action{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Ternary resultMail = DatabaseFactory.getInstance().getUserTable(DatabaseType.MARIADB,"eseoye").isMailAlreadyUsed(request.getParameter("mail"));
        if(resultMail == Ternary.TRUE) {
            request.setAttribute("error", "Mail already used");
            request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);

        }else if(resultMail == Ternary.FALSE) {
            //DatabaseFactory.getInstance().getPostTable(DatabaseType.MARIADB, "eseoye").createPost(request.getParameter("name");
                  request.getParameter("surname");
                  request.getParameter("phone");
                  request.getParameter("mail");
                  request.getParameter( BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt()));
                  request.getParameter("bday");

        }else if(resultMail == Ternary.UNDEFINED) {

        }
    }




}
