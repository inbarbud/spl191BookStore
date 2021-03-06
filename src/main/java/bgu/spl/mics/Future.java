package bgu.spl.mics;

//package scheduledexecutorservice;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * <p>
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
    private T t;
    private boolean done;
    private boolean dummy;

    /**
     * This should be the the only public constructor in this class.
     */
    public Future() {
        done = false;
        dummy = false;
    }

    private boolean isDummy() {
        return dummy;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     *
     * @return return the result of type T if it is available, if not wait until it is available.
     */
    public T get() {
        synchronized (this) {
            while (!isDone()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return t;
    }

    /**
     * Resolves the result of this Future object.
     */
    public void resolve(T result) {
        synchronized (this) {
            notifyAll();
            t = result;
            done = true;
            dummy = true;
        }
    }

    /**
     * @return true if this object has been resolved, false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     *
     * @param timout the maximal amount of time units to wait for the result.
     * @param unit   the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not,
     * wait for {@code timeout} TimeUnits {@code unit}. If time has
     * elapsed, return null.
     */
    public T get(long timeout, TimeUnit unit) {
        //long startTime = System.currentTimeMillis();
		/*new Thread(()->{
			done = true;
			notifyAll();
		}).start();*/
        synchronized (this) {
            ScheduledExecutorService execService = Executors.newScheduledThreadPool(1);
            execService.scheduleAtFixedRate(() -> {
                dummy = true;
                synchronized (this) {
                    notifyAll();
                }
            }, 0, timeout, unit);

            while (!isDummy()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            if (!isDone())
                dummy = false;
            return t;
        }








		/*while ((System.currentTimeMillis()-startTime)<unit.toMillis(timeout)){
			if(isDone())
				return t;
		}
		return null;*/

    }

}
