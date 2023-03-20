package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.io.databases.DatabaseCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Index implements Action{

    private final DatabaseCredentials dbCred;

    public Index(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/Index.jsp").forward(request,response);
    }


}
