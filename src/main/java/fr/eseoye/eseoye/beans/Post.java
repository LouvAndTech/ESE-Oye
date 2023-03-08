package fr.eseoye.eseoye.beans;

import java.sql.Date;

/**
 * Used to store a simplified version of a post
 */
public class Post {
    protected String id;
    protected String title;
    
    protected User author;
    protected float price;
    
    protected Date date;

    public Post(String id, String title, User author, float price, Date date){
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.date = date;
    }
    
    public Post(String title, User author, float price, Date date){
        this.id = null; //TODO Generate new hash for the post
        this.title = title;
        this.author = author;
        this.price = price;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return author;
    }

    public float getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

}
