package fr.eseoye.eseoye.io.objects;

import java.util.HashSet;
import java.util.Set;

public class FetchPostFilter {

	private int categoryID, stateID;
	private FetchOrder order;
	private float maxPrice;
	private Set<String> keyWords;
	private boolean mustBeValidated;
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param order the ordering of the post
	 * @param maxPrice the maximum price for a post
	 * @param keyWords a list of key words used to filter the post
	 * @param mustBeValidated if the post must be validated by an administrator to be fetch
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, FetchOrder order, float maxPrice, Set<String> keyWords, boolean mustBeValidated) {
		this.categoryID = categoryID;
		this.stateID = stateID;
		this.order = order;
		this.maxPrice = maxPrice;
		this.keyWords = keyWords;
		this.mustBeValidated = true;
	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class<br>
	 * <i>The parameter mustBeValidated is true by default</i>
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param order the ordering of the post
	 * @param maxPrice the maximum price for a post
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, FetchOrder order, float maxPrice, Set<String> keysWords) {
		this(categoryID, stateID, order, maxPrice, keysWords, true);
	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param order the ordering of the post
	 * @param maxPrice the maximum price for a post
	 * @param mustBeValidated if the post must be validated by an administrator to be fetch
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, FetchOrder order, float maxPrice, boolean mustBeValidated) {
		this(categoryID, stateID, order, maxPrice, new HashSet<>(), mustBeValidated);
	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class <br>
	 * <i>The parameter mustBeValidated is true by default</i>
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param order the ordering of the post
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, FetchOrder order) {
		this(categoryID, stateID, order, -1, new HashSet<>(), true);
	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param order the ordering of the post
	 * @param mustBeValidated if the post must be validated by an administrator to be fetch
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, FetchOrder order, boolean mustBeValidated) {
		this(categoryID, stateID, order, -1, new HashSet<>(), mustBeValidated);

	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class<br>
	 * <i>The parameter mustBeValidated is true by default</i>
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID) {
		this(categoryID, stateID, FetchOrder.DATE_DESCENDING, -1, new HashSet<>(), true);
	}
	
	/**
	 * Create a new parameter for fetching the post list<br><br>
	 * When you doesn't want to use a filter, you can set it to <strong>-1</strong> for an integer parameter<br>
	 * or use another constructor of the class
	 * @param categoryID the id of the category you want to filter
	 * @param stateID the id of the state you want to filter
	 * @param mustBeValidated if the post must be validated by an administrator to be fetch
	 * @see fr.eseoye.eseoye.io.objects.FetchPostParameter.FetchOrder
	 */
	public FetchPostFilter(int categoryID, int stateID, boolean mustBeValidated) {
		this(categoryID, stateID, FetchOrder.DATE_DESCENDING, -1, new HashSet<>(), mustBeValidated);
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
	
	public boolean mustBeValidated() {
		return mustBeValidated;
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
