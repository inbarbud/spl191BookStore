package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import javafx.util.Pair;


public class AcquireVehicleEvent  implements Event<DeliveryVehicle> {

    Customer orderingCustomer;
//    Pair<String,Integer> book;

    public AcquireVehicleEvent(Customer customer){
        this.orderingCustomer=customer;
    }

    public Customer getCustomer(){
        return orderingCustomer;
    }
//
//    public String getBookName(){
//        return book.getKey();
//    }
//
//    public Integer getDelay(){
//        return book.getValue();
//    }


}
