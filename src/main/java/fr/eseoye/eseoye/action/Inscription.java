package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Ternary;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class Inscription implements Action{
    private final DatabaseCredentials dbCred;

    public Inscription(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        Ternary resultMail = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).isAccountCreationPossible(request.getParameter("mail"), request.getParameter("password"));
        if(resultMail == Ternary.FALSE) {
            request.setAttribute("error", "Mail already used");
            request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);

        }else if(resultMail == Ternary.TRUE) {
            java.sql.Date dateSql = java.sql.Date.valueOf(request.getParameter("bday"));
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).createUserAccount(request.getParameter("name"),
                    request.getParameter("surname"),
                    BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt()),
                    "Angers",
                    dateSql,
                    request.getParameter("mail"),
                    request.getParameter("phone"));
            response.sendRedirect(request.getRequestURI()+"?id=Connection");
        }else if(resultMail == Ternary.UNDEFINED) {
            request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);
        }

    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin add user : forward");
        request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);
    }
}
