package fr.eseoye.eseoye.action;


import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.DatabaseType;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Ternary;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class Inscription implements Action{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {

        Ternary resultMail = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).isAccoundCreationPossible(request.getParameter("mail"), request.getParameter("password"));
        if(resultMail == Ternary.TRUE) {
            request.setAttribute("error", "Mail already used");
            request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);

        }else if(resultMail == Ternary.FALSE) {
            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy;HH:mm:ss");
            java.util.Date date = sf.parse(request.getParameter("date"));
            java.sql.Date dateSql = new java.sql.Date(date.getTime());
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).createUserAccount(request.getParameter("name"),
                  request.getParameter("surname"),
                  request.getParameter( BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt())),
                  dateSql,
                  request.getParameter("mail"),
                  request.getParameter("phone"));

        }else if(resultMail == Ternary.UNDEFINED) {


        }
    }




}
