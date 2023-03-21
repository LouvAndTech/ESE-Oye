package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Ternary;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AdminAddUser implements Action {

    private final DatabaseCredentials dbCred;

    public AdminAddUser(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }



    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        Ternary resultMail = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).isAccoundCreationPossible(request.getParameter("mail"), request.getParameter("phone"));
        if(resultMail == Ternary.TRUE) {
            request.setAttribute("error", "Mail already used");
            request.getRequestDispatcher("/jsp/Inscription.jsp").forward(request,response);

        }else if(resultMail == Ternary.FALSE) {
            /*
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sf.parse(request.getParameter("bday"));
            java.sql.Date dateSql = new java.sql.Date(date.getTime());
             */
            java.sql.Date dateSql = java.sql.Date.valueOf(request.getParameter("bday"));
            DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).createUserAccount(request.getParameter("name"),
                    request.getParameter("surname"),
                    request.getParameter( BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt())),
                    "Angers",
                    dateSql,
                    request.getParameter("mail"),
                    request.getParameter("phone"));

        }else if(resultMail == Ternary.UNDEFINED) {

        }
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin add user : forward");
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }
}
