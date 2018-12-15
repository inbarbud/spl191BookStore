package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import javafx.util.Pair;

public class DeliveryEvent implements Event<String> {

    private Customer customer;
    private Pair<String,Integer> order;

    public DeliveryEvent(Customer customer, Pair<String,Integer> order){
        this.customer=customer;
        this.order=order;
    }

    public Customer getCustomer(){
        return customer;
    }

}
