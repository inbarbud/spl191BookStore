package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.InventoryEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{

	//Inventory inventory;

	public InventoryService(int serviceNumber) {
		super("InventoryService" + serviceNumber);
		//this.inventory=inv;
	}

	@Override
	protected void initialize() {

		subscribeEvent(InventoryEvent.class, ev -> {
			int price= Inventory.getInstance().checkAvailabiltyAndGetPrice(ev.getBookName());
			boolean taken=false;
			if(price!=-1){			//book in stock
				if(ev.getAvailableAmount()>=price) {		//want to take the book
					if(Inventory.getInstance().take(ev.getBookName())== OrderResult.SUCCESSFULLY_TAKEN){
						complete(ev,price);
						taken=true;
					}
				}
			}
			if(!taken)
				complete(ev,-1);
			terminate();
		});
		
	}

}
