package fr.eseoye.eseoye.action.User;

import fr.eseoye.eseoye.action.Action;
import fr.eseoye.eseoye.beans.Category;
import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.beans.User;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class AdminValidePost implements Action {

    private final DatabaseCredentials dbCred;

    public AdminValidePost(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //todo : Has no use for now but mey never as any ... ?
        System.out.println("Admin list user : execute");
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, String target) throws ServletException, IOException {
        System.out.println("Admin list user : forward");
        try{
            // todo : get older not validate annonce
            request.setAttribute("post", fetchPost( "6"));
            request.setAttribute("postID", 1);
            request.getRequestDispatcher("/jsp/UserPanel.jsp").forward(request,response);
        }catch (Exception e){
            System.out.println("Error : " + e.getMessage());
            request.setAttribute("postID", 0);
            this.forward(request, response, "/jsp/UserPanel.jsp");
        }
    }

    /**
     * Fetch the post from the database
     * @param postId    the id of the post to fetch
     * @return          the post as a {@link PostComplete}
     */
    private PostComplete fetchPost(String postId){
        System.out.println("Fetch One entiere post :");
        System.out.println(DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).fetchEntirePost(postId));
        return DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).fetchEntirePost(postId);
    }
}
