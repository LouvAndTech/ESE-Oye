package fr.eseoye.eseoye.io.objects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.eseoye.eseoye.beans.FetchOrder;

public class FetchPostFilter {

	private int categoryID, stateID;
	private FetchOrderEnum order;
	private float maxPrice;
	private Set<String> keyWords;
	private boolean mustBeValidated;
	private String userSecureID;
	
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
	private FetchPostFilter(FetchPostFilter.Builder builder) {
		this.categoryID = builder.categoryID;
		this.stateID = builder.stateID;
		this.keyWords = builder.keyWords;
		this.maxPrice = builder.maxPrice;
		this.order = builder.order;
		this.mustBeValidated = builder.mustBeValidated;
		this.userSecureID = builder.userSecureID;
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
	
	public FetchOrderEnum getOrder()
	{
		System.out.println("Order then: " + order);
		return order;
	}
	
	public String getUserSecureID() {
		return userSecureID;
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
	
	public boolean isUserIDPresent() {
		return userSecureID != null;
	}
	
	public static FetchPostFilter.Builder builder() {
		return new FetchPostFilter.Builder();
	}
	
	public static class Builder {
		
		private int categoryID = -1;
		private int stateID = -1;
		private FetchOrderEnum order = FetchOrderEnum.DATE_DESCENDING;
		private float maxPrice = -1;
		private Set<String> keyWords;
		private boolean mustBeValidated = true;
		private String userSecureID = null;
		
		private Builder() {
			this.keyWords = new HashSet<>();
		}
		
		public Builder category(int categoryID) {
			this.categoryID = categoryID;
			return this;
		}
		
		public Builder state(int stateID) {
			this.stateID = stateID;
			return this;
		}
		
		public Builder maxPrice(float maxPrice) {
			this.maxPrice = maxPrice;
			return this;
		}
		
		public Builder order(FetchOrderEnum o) {
			System.out.println("Order set to " + o.name());
			this.order = o;
			return this;
		}
		
		public Builder keyWords(Set<String> words) {
			this.keyWords = words;
			return this;
		}
		
		public Builder keyWords(String... words) {
			if(words != null) for(String v : words) this.keyWords.add(v);
			return this;
		}
		
		public Builder keyWords(String word) {
			this.keyWords.add(word);
			return this;
		}
		
		public Builder mustBeValidated(boolean value) {
			this.mustBeValidated = value;
			return this;
		}
		
		public Builder user(String userID) {
			this.userSecureID = userID;
			return this;
		}
		
		public FetchPostFilter build() {
			return new FetchPostFilter(this);
		}
		
	}
	
	public enum FetchOrderEnum {
		PRICE_ASCENDING("Prix ascendant"),
		PRICE_DESCENDING("Prix descendant"),
		DATE_ASCENDING("Date ascendante"),
		DATE_DESCENDING("Date descendante");
		
		private String name;
		private FetchOrderEnum(String name) {
			this.name = name;
		}
		
		public static FetchOrderEnum of(String objname) {
			return Arrays.asList(FetchOrderEnum.values()).stream().filter(o -> o.name().equalsIgnoreCase(objname)).findFirst().get();
		}
		
		public FetchOrder getObject() {
			return new FetchOrder(name, this);
		}
	}
}
