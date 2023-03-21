package fr.eseoye.eseoye.action;


import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.DatabaseType;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Ternary;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class Inscription implements Action{

    @Override

    /**
     * Load the post asked before forwarding to the page
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                      of the servlet
     * @param response  an {@link HttpServletResponse} object that
     *                      contains the response the servlet sends
     *                      to the client
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {

        /**
         * Check if the mail is already used
         */

        Ternary resultMail = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).isAccoundCreationPossible(request.getParameter("mail"), request.getParameter("phone"));
        if(resultMail == Ternary.FALSE) {
            request.setAttribute("error", "Mail already used");
            request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);

        }else if(resultMail == Ternary.TRUE) {
            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy;HH:mm:ss");
            java.util.Date date = sf.parse(request.getParameter("date"));
            java.sql.Date dateSql = new java.sql.Date(date.getTime());
            String userID = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).createUserAccount(request.getParameter("name"),
                  request.getParameter("surname"),
                  request.getParameter( BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt())),
                    request.getParameter("address"),
                  dateSql,
                  request.getParameter("mail"),
                  request.getParameter("phone"));
            if(userID != null) {

                HttpSession session = request.getSession();
                session.setAttribute("idUser", userID);
                //setting session to expiry in 30 mins
                session.setMaxInactiveInterval(50*60);
            }else{
                request.setAttribute("error", "une erreur est survenue, veuillez réessayer");
                request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);
            }




        }else if(resultMail == Ternary.UNDEFINED) {
            request.setAttribute("error", "une erreur est survenue, veuillez réessayer");
            request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);


        }
    }




}
