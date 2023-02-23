package fr.eseoye.eseoye.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

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
     * @param target    a string to define the view to forward
     *
     * @throws ServletException an {@link ServletException}
     * @throws IOException      an {@link IOException}
     */
    default void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        request.getRequestDispatcher(target).forward(request,response);
    }
}
