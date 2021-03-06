package bgu.spl.mics.application.passiveObjects;

import java.util.List;
import javafx.util.Pair;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Comparator;


/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer {

	private int id;
	private String name;
	private String address;
	private int distance;
	private List<OrderReceipt> Receipts;
	private int creditCard;
	private int availableAmountInCreditCard;
	private PriorityBlockingQueue<Pair<String,Integer>> orderSchedule;
	//BlockingQueue b=new PriorityBlockingQueue<Pair<String,Integer>>(100, Comparator.comparingInt(Pair::getValue));    parser


	public Customer(int id, String name, String address, int distance, List<OrderReceipt> Receipts, int creditCard, int availableAmountInCreditCard, PriorityBlockingQueue<Pair<String,Integer>> orderSchedule){
		this.id=id;
		this.name=name;
		this.address=address;
		this.distance=distance;
		this.Receipts=Receipts;
		this.creditCard=creditCard;
		this.availableAmountInCreditCard=availableAmountInCreditCard;
		this.orderSchedule= orderSchedule;
	}

	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		return name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		return id;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		return address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		return distance;
	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
		return Receipts;
	}
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
		return availableAmountInCreditCard;
	}

	public void setAvailableAmountInCreditCard(int amountToCharge){
		availableAmountInCreditCard=availableAmountInCreditCard-amountToCharge;
	}
	
	/**
     * Retrieves this customers credit card serial number.    
     */
	public int getCreditNumber() {
		return creditCard;
	}

	public PriorityBlockingQueue<Pair<String,Integer>> getOrderSchedule(){
		return orderSchedule;
	}

	public void addReceipt(OrderReceipt r){
		Receipts.add(r);
	}
}
