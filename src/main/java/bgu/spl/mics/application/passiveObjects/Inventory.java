package bgu.spl.mics.application.passiveObjects;



/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {


	private static Inventory inv=null;
	private BookInventoryInfo[ ] collection;

	private Inventory(){

	}

	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		if (inv == null)
			inv = new Inventory();
		return inv;
	}

	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (BookInventoryInfo[ ] inventory ) {
		collection=inventory;
	}
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) {
		BookInventoryInfo b=null;
		for (BookInventoryInfo element: collection) {
			if (element.getBookTitle() == book)
				b = element;
		}
		if(b!=null) {
			synchronized (b) {
				if (b.getAmountInInventory() > 0) {
					b.minusAmountInInventory();
					return OrderResult.SUCCESSFULLY_TAKEN;
				}
			}
		}
		return OrderResult.NOT_IN_STOCK;
	}
	
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabiltyAndGetPrice(String book) {
		BookInventoryInfo b=null;
		for (BookInventoryInfo element: collection) {
			if (element.getBookTitle() == book)
				b = element;
		}
		if(b!=null) {
			if (b.getAmountInInventory() > 0) {
				return b.getPrice();
			}
		}
		return -1;
	}
	
	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a 
     * Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename){
		//TODO: Implement this
	}
}
