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
        try{
            super.handlePage(request, response,TypePost.USER);
            fillName(request, request.getParameter("targetUserId"));
            request.getRequestDispatcher("/jsp/UserProfile.jsp").forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
            request.getRequestDispatcher("/jsp/ListPosts.jsp").forward(request, response);
        }
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

    /**
     * Fill the request with the posts of the user and his name
     * @param request an {@link HttpServletRequest} object that contains the request
     * @param targetUserId the id of the user
     */
    private void fillBack(HttpServletRequest request, String targetUserId){
        try{
            super.fillRequest(request, POST_PER_PAGE, 0, targetUserId);
            fillName(request, targetUserId);
            request.setAttribute("targetUserId", targetUserId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Fill the name and surname of the user
     * @param request an {@link HttpServletRequest} object that contains the request
     * @param targetUserId the id of the user
     */
    private void fillName(HttpServletRequest request, String targetUserId){
        Tuple<String,String> rep = DatabaseFactory.getInstance().getTable(UserTable.class ,dbCred).getNameSurname(targetUserId);
        request.setAttribute("targetName", rep.getValueA());
        request.setAttribute("targetSurname", rep.getValueB());
        request.setAttribute("targetUserId", targetUserId);
    }
}
