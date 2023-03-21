package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.UserTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.IOException;

public class Account implements Action {

    public final DatabaseCredentials dbCred;

    public Account(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo : Has no use for now but mey never as any ... ?
        System.out.println("Account : execute");
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Account : forward");
        HttpSession session = request.getSession();
        User user = DatabaseFactory.getInstance().getTable(UserTable.class, IOHandler.getInstance().getConfiguration().getDatabaseCredentials()).getUser(session.getAttribute("idUser").toString());
        request.setAttribute("user", user);
        request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
    }
}
