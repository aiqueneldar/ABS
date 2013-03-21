/**
 * 
 */
package utils;

import java.util.GregorianCalendar;

/**
 * @author Calle "Aiquen" Lejdbrandt
 *
 */
public class Transaction {

	/**
	 * Id of the transaction
	 */
	private String id;
	
	/**
	 * Which item is being used in the transaction
	 */
	private String itemid;
	
	/**
	 * Which user is using the transaction
	 */
	private String userid;
	
	/**
	 * What amount is transfered. Positive numbers are increasing stock, negative numbers decrease stock
	 */
	private int amount;
	
	/**
	 * What price was used for buying/selling
	 */
	private float price;
	
	/**
	 * When did the transaction happened
	 */
	private GregorianCalendar date;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the itemid
	 */
	public String getItemid() {
		return itemid;
	}

	/**
	 * @param itemid the itemid to set
	 */
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return the date
	 */
	public GregorianCalendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	
	
}
