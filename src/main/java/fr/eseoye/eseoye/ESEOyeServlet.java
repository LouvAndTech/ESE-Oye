package fr.eseoye.eseoye;

//Actions

import fr.eseoye.eseoye.action.*;
import fr.eseoye.eseoye.helpers.ConnectionHelper;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.SFTPFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "ese-oye", value = "/ese-oye")
public class ESEOyeServlet extends HttpServlet {

    private final Map<String, Action> actionMap = new HashMap<>();

    /**
     * Function executed when a server is instanced
     */
    @Override
    public void init() {
        try {
            SFTPFactory.createInstance(IOHandler.getInstance().getConfiguration().getSFTPCredentials());
            DatabaseCredentials dbCred = IOHandler.getInstance().getConfiguration().getDatabaseCredentials();
            actionMap.put("ListPosts", new ListPost(dbCred));
            actionMap.put("OnePost", new OnePost(dbCred));
            actionMap.put("UserProfile", new UserProfile(dbCred));
            actionMap.put("UserPanel", UserPanel.getInstance(dbCred));
            actionMap.put("Logout", new Logout());
            actionMap.put("Inscription", new Inscription(dbCred));
            actionMap.put("Connection", new Connection(dbCred));

            //Admin Part
            actionMap.put("AdminLogin", new AdminLogin());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("INIT");

    }

    /**
     * Function executed when a client execute a Get request
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (ConnectionHelper.isConnected(request, response)) {
            HttpSession session = request.getSession();

            //session.setAttribute("admin", true);
            //session.setAttribute("idUser", "1");
            request.setAttribute("adminState", session.getAttribute("admin"));

            String id = request.getParameter("id");
            System.out.println("doGet : " + id);
            if (id == null || !actionMap.containsKey(id)) {
                id = "Connection";
            }
            actionMap.get(id).forward(request, response, "/jsp/" + id + ".jsp");
        }
    }


    /**
     * Function executed when a client execute a Post request
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (ConnectionHelper.isConnected(request, response)) {
            HttpSession session = request.getSession();

            //session.setAttribute("admin", true);
            //session.setAttribute("idUser", "1");
            request.setAttribute("adminState", session.getAttribute("admin"));

            String id = request.getParameter("id");
            System.out.println("doPost : " + id);
            if (id == null || !actionMap.containsKey(id)) {
                id = "Connection";
            }
            try {
                actionMap.get(id).execute(request, response);
            } catch (ParseException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}