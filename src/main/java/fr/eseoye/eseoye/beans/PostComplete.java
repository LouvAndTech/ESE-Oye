package fr.eseoye.eseoye.beans;

import java.sql.Date;

/**
 * Used to store a complete version of a post
 */
public class PostComplete extends Post{
    private String content;

    public PostComplete(int id, String title, String author, int price, Date date, String content) {
        super(id, title, author,price, date);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
