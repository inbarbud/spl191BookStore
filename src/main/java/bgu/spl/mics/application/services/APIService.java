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

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
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

	private Customer orderingCustomer;
	//int currentTime;
	private PriorityBlockingQueue<Future<OrderReceipt>> futureQueue;

	public APIService(int serviceNumber,Customer orderingCustomer) {
		super("APIService" + serviceNumber);
		this.orderingCustomer=orderingCustomer;
		this.futureQueue= new PriorityBlockingQueue<>(100,(b1,b2)->{
			if(b1.isDone())
				return -1;
			if(b2.isDone())
				return 1;
			return 0;
		});
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		subscribeBroadcast(TickBroadcast.class, br -> {
			while(br.getTime()==orderingCustomer.getOrderSchedule().peek().getValue())
			{
				try{
					BookOrderEvent ev= new BookOrderEvent(orderingCustomer, orderingCustomer.getOrderSchedule().take());
					Future<OrderReceipt> result = sendEvent(ev);
					futureQueue.add(result);
				}
				catch (InterruptedException e){}
			}
			while (futureQueue.peek().isDone()) {
				try {
					Future<OrderReceipt> result = futureQueue.take();
					if (result != null) {
						OrderReceipt receipt = result.get();
						if (receipt != null)
							orderingCustomer.addReceipt(receipt);
							DeliveryEvent ev2 = new DeliveryEvent(orderingCustomer);
							sendEvent(ev2);
						}

				} catch (InterruptedException e) {}
			}

			terminate();
		});
		}


	}
