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
    
    protected Category category;
    protected String firstImage;

    public Post(String id, String title, User author, float price, Date date, Category category, String firstImage){
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.date = date;
        this.category = category;
        this.firstImage = firstImage;
    }
    
    public String getFirstImage() {
		return firstImage;
	}
    
    public Category getCategory() {
		return category;
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
