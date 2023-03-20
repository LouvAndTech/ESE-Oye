package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Ternary;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Connexion  implements Action{
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

         String isaccount = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).checkUserConnection(request.getParameter("mail"), request.getParameter("password"));
         if(isaccount== null){
                request.setAttribute("error", "Mail or password incorrect");
                request.getRequestDispatcher("/jsp/Connexion.jsp").forward(request,response);



         }else{
             HttpSession session = request.getSession();
             session.setAttribute("login", isaccount);
             session.setAttribute("password", request.getParameter("password"));
             response.sendRedirect("/ESEOye/accueil");

         }
    }


}
