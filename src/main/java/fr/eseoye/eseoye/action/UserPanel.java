package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.action.User.*;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class UserPanel implements Action{
    private static UserPanel instance = null;
    private Map<String, Action> actionMap = new HashMap<>();

    private final DatabaseCredentials dbCred;

    private UserPanel(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
        //User Part
        actionMap.put("Account", new Account(this.dbCred));
        actionMap.put("Annonce", new Posts(this.dbCred));
        actionMap.put("AddAnnonce", new AddPosts(this.dbCred));

        //Admin Part
        actionMap.put("AdminListUser", new AdminListUser(this.dbCred));
        actionMap.put("AdminValidePost", new AdminValidePost(this.dbCred));
        actionMap.put("AdminAddUser", new AdminAddUser(this.dbCred));
        actionMap.put("AdminEditPost", new AdminEditPost(this.dbCred));
    }

    public static UserPanel getInstance(DatabaseCredentials dbCred){
        if(instance == null){
            instance = new UserPanel(dbCred);
        }
        return instance;
    }

    /**
     * Execute the action asked
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     * @throws SQLException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException, SQLException {
        HttpSession session = request.getSession();
        request.setAttribute("adminState", session.getAttribute("admin"));

        String id = request.getParameter("contentPage");
        System.out.println("Execute : "+id);
        if(id == null || !actionMap.containsKey(id)) {
            id="Account";
        }
        request.setAttribute("contentPage", id);
        actionMap.get(id).execute(request,response);
    }

    /**
     * Forward to the page asked
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @param target    a string to define the view to forward
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setAttribute("adminState", session.getAttribute("admin"));

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("contentPage");
        //System.out.println("Forward : "+id);
        if(id == null || !actionMap.containsKey(id)) {
            id="Account";
        }
        request.setAttribute("contentPage", id);
        actionMap.get(id).forward(request,response,"/jsp/UserPanel.jsp");


    }
}
