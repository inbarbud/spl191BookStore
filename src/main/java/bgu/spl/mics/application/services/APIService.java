package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import javafx.util.Pair;
import bgu.spl.mics.application.passiveObjects.Customer;
import java.util.concurrent.TimeUnit;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService{

	private Pair <String,Integer>[] orderSchedule;			//name,Tick
	private Customer orderingCustomer;
	int currentTime;

	public APIService(int serviceNumber,Customer orderingCustomer, Pair <String,Integer>[] orderSchedule) {
		super("APIService" + serviceNumber);
		this.orderSchedule=orderSchedule;
		this.orderingCustomer=orderingCustomer;
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		subscribeBroadcast(TickBroadcast.class, br -> {
			currentTime = br.getTime();
			terminate();
		});

		for(int i=0;i<orderSchedule.length;i++) {
			BookOrderEvent ev= new BookOrderEvent(orderingCustomer, orderSchedule[i]);
			Future<OrderReceipt> result = sendEvent(ev);
			if (result != null) {
				OrderReceipt receipt = result.get(100, TimeUnit.MILLISECONDS);		//TODO: repair order by tick times
				if(receipt==null)
					complete(ev,null);
				else {
					complete(ev, receipt);
					DeliveryEvent ev2= new DeliveryEvent(orderingCustomer, orderSchedule[i]);

				}
			}
		}


	}

}
