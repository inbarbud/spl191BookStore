package bgu.spl.mics.application.passiveObjects;
import java.util.LinkedList;


/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister {

	private static MoneyRegister money=null;
	private LinkedList<OrderReceipt> list;
	private int ID=0;

	private MoneyRegister(){

	}

	/**
     * Retrieves the single instance of this class.
     */
	public static MoneyRegister getInstance() {
		if (money == null)
			money = new MoneyRegister();
		return money;
	}
	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public void file (OrderReceipt r) {
		synchronized (list) {
			list.add(r);
		}
	}

	public int getReceiptID(){
		ID++;
		return ID;
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
		int sum=0;
		for (OrderReceipt element:list) {
			sum+=element.getPrice();
		}
		return sum;
	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public void chargeCreditCard(Customer c, int amount) {
		//c.setAvailableAmountInCreditCard(amount);
	}
	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.
     */
	public void printOrderReceipts(String filename) {
		//TODO: Implement this
	}
}
