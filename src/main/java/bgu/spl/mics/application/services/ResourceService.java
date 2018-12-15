package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{



	public ResourceService(int serviceNumber) {
		super("ResourceService" + serviceNumber);
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		// TODO Implement this

		subscribeEvent(AcquireVehicleEvent.class, ev -> {
			Future<DeliveryVehicle> take= ResourcesHolder.getInstance().acquireVehicle();
			DeliveryVehicle car = take.get();								//???
			car.deliver(ev.getCustomer().getAddress(),ev.getCustomer().getDistance());
			ResourcesHolder.getInstance().releaseVehicle(car);
			terminate();
		});

}
}