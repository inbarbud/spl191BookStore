package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import javafx.util.Pair;

public class InventoryEvent implements Event<Integer> {


    int availableAmount;
    String bookTitle;

    public InventoryEvent(String bookTitle, int AvailableCreditAmount){
        this.bookTitle=bookTitle;
        this.availableAmount=AvailableCreditAmount;
    }

    public String getBookName(){
        return bookTitle;
    }

    public int getAvailableAmount(){
        return availableAmount;
    }

}