package fr.eseoye.eseoye.servlet;

//Actions
import fr.eseoye.eseoye.action.*;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.IOHandler;
import fr.eseoye.eseoye.io.SFTPFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

//Libraries
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "ese-oye", value = "/ese-oye")
public class ESEOyeServlet extends HttpServlet {

    private Map<String, Action> actionMap = new HashMap<>();

    /**
     * Function executed when a server is instanced
     */
    @Override
    public void init(){
        try {
            SFTPFactory.createInstance(IOHandler.getInstance().getConfiguration().getSFTPCredentials());
            DatabaseCredentials dbCred = IOHandler.getInstance().getConfiguration().getDatabaseCredentials();
            actionMap.put("Index", new Index(dbCred));
            actionMap.put("ListPosts", new ListPost(dbCred));
            actionMap.put("OnePost", new OnePost(dbCred));
            actionMap.put("UserPanel", UserPanel.getInstance(dbCred));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("INIT");

    }

    /** Function executed when a client execute a Get request
     *
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") == null) {
            session.setAttribute("admin", false);
            session.setAttribute("idUser", "1");
        }

        String id = request.getParameter("id");
        System.out.println("doGet : "+id);
        if(id == null || !actionMap.containsKey(id)) {
            id="Index";
        }
        actionMap.get(id).forward(request,response,"/jsp/"+id+".jsp");
    }


    /** Function executed when a client execute a Post request
     *
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.getAttribute("admin") == null) {
            session.setAttribute("admin", false);
            session.setAttribute("idUser", "1");
        }

        String id = request.getParameter("id");
        System.out.println("doPost : "+id);
        if(id == null || !actionMap.containsKey(id)) {
            id="Index";
        }
        actionMap.get(id).execute(request,response);
    }
}