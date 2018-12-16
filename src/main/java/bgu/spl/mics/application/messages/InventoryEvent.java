package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import javafx.util.Pair;

public class InventoryEvent implements Event<Integer> {


    int availableAmount;
    String bookTitle;
    Customer customer;

    public InventoryEvent(String bookTitle, int AvailableCreditAmount, Customer customer){
        this.bookTitle=bookTitle;
        this.availableAmount=AvailableCreditAmount;
        this.customer=customer;
    }

    public String getBookName(){
        return bookTitle;
    }

    public int getAvailableAmount(){
        return availableAmount;
    }

    public Customer getCustomer() {
        return customer;
    }
}