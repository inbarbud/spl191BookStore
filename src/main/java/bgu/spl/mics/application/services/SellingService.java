package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.InventoryEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.concurrent.TimeUnit;


/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService {

	//MoneyRegister money;
	int currentTime;
	Customer customer;

	public SellingService(int serviceNumber) {
		super("SellingService" + serviceNumber);
		//this.money = money;
	}

	@Override
	protected void initialize() {

		//TODO:BookOrderEvent(wait until Tick)

		subscribeBroadcast(TickBroadcast.class, br -> {
			currentTime = br.getTime();
			terminate();
		});
		subscribeEvent(BookOrderEvent.class, ev -> {
			customer=ev.getCustomer();


			Future<Integer> futureObject = sendEvent(new InventoryEvent(ev.getBookName(), customer.getAvailableCreditAmount()));
			if (futureObject != null) {
				int price = futureObject.get(100, TimeUnit.MILLISECONDS);
				if(price==-1)								//book not taken
					complete(ev,null);				//receipt null
				else {
					MoneyRegister.getInstance().chargeCreditCard(customer,price);
					OrderReceipt r= new OrderReceipt(MoneyRegister.getInstance().getReceiptID(),this.getName(), ev.getCustomer().getId(), ev.getBookName(), price, .........);	//create receipt
					MoneyRegister.getInstance().file(r);			//add receipt
					complete(ev,r);
				}
			}
			terminate();
		});
	}
}