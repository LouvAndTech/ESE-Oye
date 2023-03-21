package fr.eseoye.eseoye.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ConnectionHelper {
    public static Boolean isLockAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        System.out.println("Is lock ? : "+session.getAttribute("admin") == null);
        if(session.getAttribute("admin") == null){
            response.sendRedirect(request.getRequestURI()+"?id=ListPosts");
            return false;
        }
        return true;
    }

    public static Boolean isConnected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String id = request.getParameter("id");
        if(session.getAttribute("idUser") == null && !(id.equals("Inscription") || id.equals("Connection") || id.equals("AdminLogin"))){
            response.sendRedirect(request.getRequestURI()+"?id=Connection");
            return false;
        }
        return true;
    }
}
