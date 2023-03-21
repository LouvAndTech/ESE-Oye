package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.SFTPFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Abstract class is used as a base for every action that need to fetch a single post from the database
 */
public abstract class AbstractOnePost {

    protected final DatabaseCredentials dbCred;

    public AbstractOnePost(DatabaseCredentials dbCred) {
        this.dbCred = dbCred;
    }

    /**
     * Fill the request with the post
     *
     * @param request an {@link HttpServletRequest} object
     * @param postId  the id of the post to fetch
     * @param format  the format of the description wanted
     * @throws Exception if the post id is null or empty
     */
    protected void fillPost(HttpServletRequest request, String postId, Format format) throws Exception {
        if (postId == null) {
            throw new Exception("No post id given");
        } else if (postId.isEmpty()) {
            throw new Exception("No valid post id given");
        }
        PostComplete p = fetchPost(postId, format);
        request.setAttribute("postId", p.getSecureId());
        request.setAttribute("post", p);
    }

    /**
     * Fill the request with the post if the posr ID is given in the request
     *
     * @param request an {@link HttpServletRequest} object
     * @param format  the format of the description wanted
     * @throws Exception if the post id is null or empty
     */
    protected void fillPost(HttpServletRequest request, Format format) throws Exception {
        String postId = request.getParameter("postId");
        fillPost(request, postId, format);
    }

    /**
     * Fetch the post from the database
     *
     * @param postId the id of the post to fetch
     * @return the post as a {@link PostComplete}
     */
    private PostComplete fetchPost(String postId, Format format) {
        PostComplete p = DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).fetchEntirePost(postId);
        System.out.println("postcomplete author secure ID : " + p.getAuthor().getSecureID());
        if (format == Format.HTML)
            p.setContent(p.getContent().replace("\n", "<br>"));
        else if (format == Format.TXT)
            p.setContent(p.getContent().replace("<br>", "\n"));
        return p;
    }

    /**
     * Execute the action given in the request
     *
     * @param request  an {@link HttpServletRequest} object
     * @param response an {@link HttpServletResponse} object
     * @throws Exception if the action is not found
     */
    protected void executeAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String action = request.getParameter("action");
        System.out.println("Action : " + action);
        if (action.equals("valid") || action.equals("delete")) {
            String postId = request.getParameter("postId");
            System.out.println("Post ID : " + postId);
            if (action.equals("valid"))
                DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).validatePost(postId);
            else
                DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).deletePost(SFTPFactory.getInstance().createNewConnection(), postId);
        } else {
            throw new Exception("Action not found");
        }
    }

    /**
     * Enum used to define the format of the description
     */
    protected enum Format {
        HTML, TXT
    }
}
