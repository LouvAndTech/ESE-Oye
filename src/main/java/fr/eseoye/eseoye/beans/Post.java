package fr.eseoye.eseoye.beans;

import java.sql.Date;

/**
 * Used to store a simplified version of a post
 */
public class Post {
    protected String secureId;
    protected String title;    
    protected float price;
    protected String firstImage;
    
    protected Date date;
    protected SimplifiedEntity author;
    protected PostState state;
    protected Category category;

    public Post(String secureId, String title, SimplifiedEntity author, float price, Date date, Category category, PostState postState, String firstImage){
        this.secureId = secureId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.date = date;
        this.category = category;
        this.state = postState;
        this.firstImage = firstImage;
    }

    public String getFirstImage() {
		return firstImage;
	}
    
    public Category getCategory() {
		return category;
	}
    
    public PostState getState() {
		return state;
	}

    public String getSecureId() {
        return secureId;
    }

    public String getTitle() {
        return title;
    }

    public SimplifiedEntity getAuthor() {
        return author;
    }

    public float getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

}
