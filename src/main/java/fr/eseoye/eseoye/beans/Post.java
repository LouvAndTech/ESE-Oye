package fr.eseoye.eseoye.beans;

import java.sql.Date;

/**
 * Used to store a simplified version of a post
 */
public class Post {
    protected String id;
    protected String title;
    protected String author;
    protected int price;
    protected Date date;

    public Post(String id, String title,String author, int price, Date date){
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.date = date;
    }
    public Post( String title,String author, int price, Date date){
        this.id = null;
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

    public String getAuthor() {
        return author;
    }

    public int getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

}
