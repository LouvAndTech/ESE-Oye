package fr.eseoye.eseoye.beans;

import java.sql.Date;

public class Post {
    protected int id;
    protected String title;
    protected String author;
    protected int price;
    protected Date date;

    public Post(int id, String title,String author, int price, Date date){
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.date = date;
    }

    public int getId() {
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
