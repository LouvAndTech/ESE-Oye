package fr.eseoye.eseoye.servlet;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.action.Index;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "ese-oye", value = "/ese-oye")
public class ESEOyeServlet extends HttpServlet {

    private Map<String, Action> actionMap = new HashMap<>();

    /**
     * Function executed when a server is instanced
     */
    @Override
    public void init(){
        actionMap.put("index", new Index());
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
        String id = request.getParameter("id");
        if(id == null || !actionMap.containsKey(id)) {
            id="index";
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
        String id = request.getParameter("id");
        if(id == null || !actionMap.containsKey(id)) {
            id="index";
        }
        actionMap.get(id).execute(request,response);
    }
}