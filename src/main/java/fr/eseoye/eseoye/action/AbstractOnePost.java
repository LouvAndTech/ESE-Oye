package fr.eseoye.eseoye.action;

import fr.eseoye.eseoye.beans.PostComplete;
import fr.eseoye.eseoye.io.DatabaseFactory;
import fr.eseoye.eseoye.io.databases.DatabaseCredentials;
import fr.eseoye.eseoye.io.databases.tables.PostTable;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractOnePost {

    protected final DatabaseCredentials dbCred;

    public AbstractOnePost(DatabaseCredentials dbCred){
        this.dbCred = dbCred;
    }

    protected void fillPost(HttpServletRequest request) throws Exception{
        String postId = request.getParameter("postId");
        if(postId == null){
            throw new Exception("No post id given");
        }else if(postId.isEmpty()){
            throw new Exception("No valid post id given");
        }
        PostComplete p = fetchPost(postId);
        System.out.println(p.getSecureId());
        request.setAttribute("postId", p.getSecureId());
        request.setAttribute("post", p);
    }

    /**
     * Fetch the post from the database
     * @param postId    the id of the post to fetch
     * @return          the post as a {@link PostComplete}
     */
    private PostComplete fetchPost(String postId){
        PostComplete p = DatabaseFactory.getInstance().getTable(PostTable.class, dbCred).fetchEntirePost(postId);
        System.out.println("postcomplete author secure ID : "+p.getAuthor().getSecureID());
        p.setContent(p.getContent().replace("\n", "<br>"));
        return p;
    }
}
