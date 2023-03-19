package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.action.User.Account;
import fr.eseoye.eseoye.action.User.AddPosts;
import fr.eseoye.eseoye.action.User.Posts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserPanel implements Action{
    private static UserPanel instance = null;
    private Map<String, Action> actionMap = new HashMap<>();

    private UserPanel(){
        actionMap.put("Account", new Account());
        actionMap.put("Annonce", new Posts());
        actionMap.put("AddAnnonce", new AddPosts());
    }

    public static UserPanel getInstance(){
        if(instance == null){
            instance = new UserPanel();
        }
        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Temporary code for session variable
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") == null) {
            session.setAttribute("admin", false);
        }
        // End of temporary code


        String id = request.getParameter("contentPage");
        System.out.println("Execute : "+id);
        if(id == null || !actionMap.containsKey(id)) {
            id="Account";
        }
        request.setAttribute("contentPage", id);
        request.setAttribute("adminState", session.getAttribute("admin"));
        actionMap.get(id).execute(request,response);
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        // Temporary code for session variable
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") == null) {
            session.setAttribute("admin", false);
        }
        // End of temporary code


        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("contentPage");
        //System.out.println("Forward : "+id);
        if(id == null || !actionMap.containsKey(id)) {
            id="Account";
        }
        request.setAttribute("contentPage", id);
        request.setAttribute("adminState", session.getAttribute("admin"));
        actionMap.get(id).forward(request,response,"/jsp/UserPanel.jsp");


    }
}
