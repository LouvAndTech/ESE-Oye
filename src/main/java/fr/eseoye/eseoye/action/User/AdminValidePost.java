package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.AbstractOnePost;
import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminValidePost extends AbstractOnePost implements Action {

    public AdminValidePost(DatabaseCredentials dbCred) {
        super(dbCred);
    }

    /**
     * Unused for now
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            executeAction(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
        }
        this.forward(request, response, "/jsp/UserPanel.jsp");
    }

    /**
     * Load the latest post not validate and posibility to validate, delete it or edit it
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     * @param target   a string to define the view to forward
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin list user : forward");

        try {
            String secureId = DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).fetchLastLockedPostID();
            System.out.println("Secure ID de la plus ancienne non validé : " + secureId);
            if (secureId.equals("")) {
                request.setAttribute("postId", "");
            } else {
                fillPost(request, secureId, Format.HTML);
            }
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("postId", "");
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request, response);
        }
    }
}
