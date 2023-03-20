package fr.eseoye.eseoye.io.objects;

import java.util.HashSet;
import java.util.Set;

public class FetchPostFilter {

	private int categoryID, stateID;
	private FetchOrder order;
	private float maxPrice;
	private Set<String> keyWords;
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param order the ordering of the post
	 * @param maxPrice the maximum price for a post
	 * @param keyWords a list of key words used to filter the post
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, FetchOrder order, float maxPrice, Set<String> keyWords) {
		this.categoryID = categoryID;
		this.stateID = stateID;
		this.order = order;
		this.maxPrice = maxPrice;
		this.keyWords = keyWords;
	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param order the ordering of the post
	 * @param maxPrice the maximum price for a post
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, FetchOrder order, float maxPrice) {
		this(categoryID, stateID, order, maxPrice, new HashSet<>());
	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param order the ordering of the post
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, FetchOrder order) {
		this(categoryID, stateID, order, -1, new HashSet<>());

	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID) {
		this(categoryID, stateID, FetchOrder.DATE_DESCENDING, -1, new HashSet<>());
	}

	public FetchPostFilter() {
		this(-1, -1, FetchOrder.DATE_DESCENDING, -1, new HashSet<>());
	}
	
	public int getCategoryID() {
		return categoryID;
	}
	
	public int getStateID() {
		return stateID;
	}
	
	public Set<String> getKeyWords() {
		return keyWords;
	}
	
	public float getMaxPrice() {
		return maxPrice;
	}
	
	public FetchOrder getOrder() {
		return order;
	}
	
	public boolean isCategoryPresent() {
		return categoryID != -1;
	}
	
	public boolean isStatePresent() {
		return stateID != -1;
	}
	
	public boolean isKeyWordsListEmpty() {
		return keyWords.isEmpty();
	}
	
	public boolean anyKeyWordsPresent() {
		return !keyWords.isEmpty();
	}
	
	public boolean isMaxPricePresent() {
		return maxPrice != -1;
	}
	
	public enum FetchOrder {
		PRICE_ASCENDING,
		PRICE_DESCENDING,
		DATE_ASCENDING,
		DATE_DESCENDING;
	}

}
