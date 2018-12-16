package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
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
import javafx.util.Pair;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
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
	//int currentTime;
	Customer customer;
	private LinkedBlockingQueue<BookOrderEvent> bookEventQueue;
	private PriorityBlockingQueue<Pair<BookOrderEvent,Future>> eventAndFutureQueue;


	public SellingService(int serviceNumber) {
		super("SellingService" + serviceNumber);
		//this.money = money;
		this.bookEventQueue= new LinkedBlockingQueue<BookOrderEvent>(100);
		this.eventAndFutureQueue= new PriorityBlockingQueue<>(100,(b1,b2)->{
			if(b1.getValue().isDone())
				return -1;
			if(b2.getValue().isDone())
				return 1;
			return 0;
		});
	}

	@Override
	protected void initialize() {

		//TODO:BookOrderEvent(wait until Tick)

		subscribeBroadcast(TickBroadcast.class, br -> {

			while(!bookEventQueue.isEmpty()){
				try{
				BookOrderEvent e= bookEventQueue.take();
				e.setProcessTick(br.getTime());
				Future<Integer> futureObject = sendEvent(new InventoryEvent(e.getBookName(), e.getCustomer().getAvailableCreditAmount(),customer));
				eventAndFutureQueue.add(new Pair<>(e,futureObject));
				} catch (InterruptedException e) {}
			}
			while (eventAndFutureQueue.peek().getValue().isDone()){
				try {
					BookOrderEvent e= eventAndFutureQueue.peek().getKey();
					Future<Integer> futureObject = eventAndFutureQueue.take().getValue();
					if (futureObject != null) {
						int price = futureObject.get();
						if (price == -1)                                //book not taken
							complete(e, null);                //receipt null
						else {
							//MoneyRegister.getInstance().chargeCreditCard(customer, price);
							OrderReceipt r = new OrderReceipt(MoneyRegister.getInstance().getReceiptID(), this.getName(), e.getCustomer().getId(), e.getBookName(), price, br.getTime(), e.getDelay(), e.getProcessTick());    //create receipt
							MoneyRegister.getInstance().file(r);            //add receipt
							complete(e, r);
						}
					}
				}
				catch (InterruptedException e){}
			}

			terminate();
		});

		subscribeEvent(BookOrderEvent.class, ev -> {
			try {
				bookEventQueue.put(ev);
			} catch (InterruptedException e) {}

			terminate();
		});
	}
}