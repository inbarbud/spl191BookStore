package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.services.ResourceService;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {
	private static ResourcesHolder res=null;
	private LinkedBlockingQueue<DeliveryVehicle> vehicleQueue;
	private LinkedBlockingQueue<Future> futureQueue;


	public ResourcesHolder(){

	}
	/**
     * Retrieves the single instance of this class.
     */
	public static ResourcesHolder getInstance() {
		if (res == null)
			res = new ResourcesHolder();
		return res;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> futureCar= new Future<>();
		if(!vehicleQueue.isEmpty()){
			try{
				DeliveryVehicle car =vehicleQueue.take();
				futureCar.resolve(car);
			}
			catch(InterruptedException ex) {}
		}
		else{
			try{
				futureQueue.put(futureCar);
			}
			catch(InterruptedException ex) {}
		}
		return futureCar;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		if(futureQueue.isEmpty()){
			try{
				vehicleQueue.put(vehicle);
			}
			catch(InterruptedException ex) {}
		}
		else {
			try{
				Future<DeliveryVehicle> futureCar= futureQueue.take();
				futureCar.resolve(vehicle);
			}
			catch(InterruptedException ex) {}
		}
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		for(int i=0;i<vehicles.length;i++){
			try{
				vehicleQueue.put(vehicles[i]);
			}
			catch(InterruptedException ex) {}
		}
	}

}
