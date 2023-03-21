package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.helpers.ConnectionHelper;
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

public class AdminEditPost extends AbstractNewPost implements Action {

    public AdminEditPost(DatabaseCredentials dbCred){
        super(dbCred);
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        if(ConnectionHelper.isLockAdmin(request, response)) {
            System.out.println("Admin add user : execute");
            try {
                analysePost p = new analysePost(request);
                p.isComplete(); // throws exception if not complete

                //todo : Push the changes into the DB
            } catch (Exception e) {
                request.setAttribute("error", e.getMessage());
                e.printStackTrace();
                forward(request, response, "/jsp/UserPanel.jsp");
                return;
            }
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
        }
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        if(ConnectionHelper.isLockAdmin(request, response)) {
            System.out.println("Admin add user : forward");
            fillCategoriesStates(request);
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
        }
    }
}
