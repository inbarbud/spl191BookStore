package bgu.spl.mics;


//import javafx.util.Pair;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

//    private ConcurrentHashMap<Class<? extends Event<?>>, LinkedBlockingQueue<MicroService>> eventList = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class, LinkedBlockingQueue<MicroService>> eventList = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class, LinkedBlockingQueue<MicroService>> broadcastList = new ConcurrentHashMap<>();

//	private ConcurrentHashMap<Event<?>, Future<?>> futureList = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Event, Future> futureList = new ConcurrentHashMap<>();

	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> serviceList = new ConcurrentHashMap<>();
	private static MessageBusImpl bus = null;

	private MessageBusImpl()
	{

	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

		if(eventList.containsKey(type)){
			if(!(eventList.get(type)).contains(m))
			{
				try {
					(eventList.get(type)).put(m);
				}
				catch (InterruptedException e){}
			}

		}
		else {
			LinkedBlockingQueue<MicroService> q = new LinkedBlockingQueue<>(100);
			try {
				q.put(m);
			}
			catch ( InterruptedException e){}
			eventList.put(type, q);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(broadcastList.containsKey(type)) {
			if (!(broadcastList.get(type)).contains(m)) {
				try {
					(broadcastList.get(type)).put(m);
				}
				catch ( InterruptedException e){}
			}
		}
		else{
			LinkedBlockingQueue<MicroService> q = new LinkedBlockingQueue<>(100);
			try {
				q.put(m);
			}
			catch (InterruptedException e){}
			broadcastList.put(type, q);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		    Future<T> f= (Future<T>) futureList.get(e);
			f.resolve(result);
			futureList.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		int k=0;
    	for(int i=0;i<broadcastList.get(b.getClass()).size();i++) {
			try {
				MicroService m = broadcastList.get(b.getClass()).take();           //dequeue microService
				serviceList.get(m).put(b);                            //push broadcast to message queue
				broadcastList.get(b.getClass()).put(m);                            //enqueue microService
			}
			catch (InterruptedException e){}
		}

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> f= new Future<>();
		futureList.put(e,f);
		try {
			if(eventList.isEmpty()) {
			    futureList.remove(e);
                return null;//TODO: delete future
            }
			MicroService m= eventList.get(e.getClass()).take();//.poll();			//dequeue microService
			if (m==null)										//no suitable microService
            {
                futureList.remove(e);
                return null;//TODO: delete future
            }
            LinkedBlockingQueue<Message> m2=serviceList.get(m);
			//m2.add(e);
            //while(!m2.add(e));
			serviceList.get(m).put(e);//offer(e);                      //push event to message queue
			eventList.get(e.getClass()).put(m);                        //enqueue microService
		}
		catch (InterruptedException ei){}


		return f;
	}

	@Override
	public void register(MicroService m) {
		if(!serviceList.containsKey(m)){
			LinkedBlockingQueue<Message> q = new LinkedBlockingQueue<>();
			serviceList.put(m,q);
		}

	}
	//TODO: look for last case problem sender5 hand1 doesnt do shit hand2 works good
	@Override
	public void unregister(MicroService m) {
		// TODO: synchronize?

		for(Enumeration <LinkedBlockingQueue<MicroService>> e = eventList.elements(); e.hasMoreElements();)
		{
			 LinkedBlockingQueue<MicroService> q = e.nextElement();
			 if(q.contains(m)) {
				 q.remove(m);
			 }
		}

		for(Enumeration <LinkedBlockingQueue<MicroService>> e = broadcastList.elements(); e.hasMoreElements();)
		{
			LinkedBlockingQueue<MicroService> q = e.nextElement();
			if(q.contains(m)) {
				q.remove(m);
			}
		}

		serviceList.remove(m);

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Exception???
		return serviceList.get(m).take();
	}

	public static MessageBusImpl getInstance(){
		if (bus == null)
			bus = new MessageBusImpl();
		return bus;
	}

}
