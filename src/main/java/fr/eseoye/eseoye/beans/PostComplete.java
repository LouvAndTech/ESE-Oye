package fr.eseoye.eseoye.beans;

import java.sql.Date;
import java.util.List;

/**
 * Used to store a complete version of a post
 */
public class PostComplete extends Post {
    private String content;

    //Contains
    protected List<String> imageList;
    
    public PostComplete(String id, String title, User author, float price, Date date, String content) {
        super(id, title, author,price, date);
        this.content = content;
    }

    public String getContent() {
        return content;
    }


}
