package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;

import java.time.Clock;
import java.time.Duration;


/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private static TimeService timer=null;

	private TimeService() {
		super("TimeService");
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		
	}

	public static TimeService getInstance() {
		if (timer == null)
			timer = new TimeService();
		return timer;
	}

}
