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
    
    public PostComplete(String secureId, String title, SimplifiedEntity author, float price, Date date, String content, Category category, PostState postState, String firstImage, List<String> imageList) {
        super(secureId, title, author, price, date, category, postState, firstImage);
        this.imageList = imageList;
        this.content = content;
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
