package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.SimplifiedEntity;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.UserTable;
import fr.eseoye.eseoye.utils.Tuple;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserProfile extends AbstractFetchPost implements Action{

    public UserProfile(DatabaseCredentials dbCred) {
        super(dbCred);
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Nothing to do
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        String targetUserId = request.getParameter("targetUser");
        try{
            if(targetUserId != null) {
                fillBack(request, targetUserId);
                //print all request attributes
                for (String key : request.getParameterMap().keySet()) {
                    System.out.println(key + " : " + request.getParameter(key));
                }
                request.getRequestDispatcher("/jsp/UserProfile.jsp").forward(request, response);
            }
        }catch (Exception e){
            response.sendRedirect("/ese-oye");
        }
    }

    private void fillBack(HttpServletRequest request, String targetUserId){
        try{
            System.out.println("Filling request with targetUserId = " + targetUserId);
            super.fillRequest(request, POST_PER_PAGE, 0, targetUserId);
            Tuple<String,String> rep = DatabaseFactory.getInstance().getTable(UserTable.class ,dbCred).getNameSurname(targetUserId);
            System.out.println("Name : " + rep.getValueA() + " Surname : " + rep.getValueB());
            request.setAttribute("targetName", rep.getValueA());
            request.setAttribute("targetSurname", rep.getValueB());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
