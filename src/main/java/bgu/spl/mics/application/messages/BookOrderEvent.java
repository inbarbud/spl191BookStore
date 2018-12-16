package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import javafx.util.Pair;

public class BookOrderEvent implements Event<OrderReceipt> {

    Customer orderingCustomer;
    Pair <String,Integer> book;
    int processTick;

    public BookOrderEvent(Customer customer, Pair<String,Integer> book){
        this.book=book;
        orderingCustomer=customer;
    }

    public Customer getCustomer(){
        return orderingCustomer;
    }

    public String getBookName(){
        return book.getKey();
    }

    public Integer getDelay(){
        return book.getValue();
    }

    public void setProcessTick(int tick){
        processTick=tick;
    }
    public int getProcessTick(){
        return processTick;
    }
}
