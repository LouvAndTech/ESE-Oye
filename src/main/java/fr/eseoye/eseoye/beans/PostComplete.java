package fr.eseoye.eseoye.beans;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to store a complete version of a post
 */
public class PostComplete extends Post {
    private String content;

    //Contains
    protected List<String> imageList;
    
    public PostComplete(String id, String title, User author, float price, Date date, String content, Category category, String firstImage, List<String> imageList) {
        super(id, title, author,price, date, category, firstImage);
        this.imageList = imageList;
        this.content = content;
        
    }
    
    /**
     * Get the full list of image including the first image (or cover)
     * @return a list of URL
     */
    public List<String> getFullImageList() {
    	final List<String> res = new ArrayList<>(imageList);
    	res.add(getFirstImage());
		return res;
	}
    
    /**
     * Get the list of image except the first image (or cover)
     * @return a list of URL
     */
    public List<String> getImageList() {
		return imageList;
	}
    
    public String getContent() {
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
}
