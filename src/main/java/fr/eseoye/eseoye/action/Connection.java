package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Ternary;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;

public class Connection implements Action{
    private final DatabaseCredentials dbCred;

    public Connection(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        String isaccount = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).checkUserConnection(request.getParameter("mail"), request.getParameter("password"));
        Ternary isAccountLock = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).isUserLocked(isaccount);
        if(isaccount == null || isAccountLock == Ternary.TRUE){
            request.setAttribute("error", "Mail or password incorrect");
            request.getRequestDispatcher("/jsp/Connection.jsp").forward(request,response);
        }else{
            HttpSession session = request.getSession();
            session.setAttribute("idUser", isaccount);
            response.sendRedirect(request.getRequestURI()+"?id=ListPosts");
        }
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin add user : forward");
        request.getRequestDispatcher("/jsp/Connection.jsp").forward(request,response);
    }
}
