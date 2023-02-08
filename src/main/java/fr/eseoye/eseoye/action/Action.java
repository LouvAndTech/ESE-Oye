package fr.eseoye.eseoye.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Action {
    /** Execute the script link to a particular action.
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
    String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /** Execute the script link to a particular action.
     *
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @param firstLoad boolean to know it's the first load of the page
     *
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    String execute(HttpServletRequest request, HttpServletResponse response,boolean firstLoad) throws ServletException, IOException;

}
