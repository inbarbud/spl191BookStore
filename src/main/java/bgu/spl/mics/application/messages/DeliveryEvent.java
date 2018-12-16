package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import javafx.util.Pair;

public class DeliveryEvent implements Event<String> {

    private Customer customer;

    public DeliveryEvent(Customer customer){
        this.customer=customer;
    }

    public Customer getCustomer(){
        return customer;
    }

}
