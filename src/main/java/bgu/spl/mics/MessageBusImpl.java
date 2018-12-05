package bgu.spl.mics;


//import javafx.util.Pair;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

    private ConcurrentHashMap<Class<? extends Event<?>>, LinkedBlockingQueue<MicroService>> eventList = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<MicroService>> broadcastList = new ConcurrentHashMap<>();
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> serviceList = new ConcurrentHashMap<>();

	private static MessageBusImpl bus = null;

	private  MessageBusImpl()
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
			LinkedBlockingQueue<MicroService> q = new LinkedBlockingQueue<>();
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
			else {
				LinkedBlockingQueue<MicroService> q = new LinkedBlockingQueue<>();
				try {
					q.put(m);
				}
				catch ( InterruptedException e){}
				broadcastList.put(type, q);
			}
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO: Future object resolve
		//<Future Object of e>.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {

    	for(int i=0;i<broadcastList.get(b).size();i++) {
			MicroService m = broadcastList.get(b).poll();           //dequeue microService
			try {
				serviceList.get(m).put(b);                            //push event to message queue
			}
			catch (InterruptedException e){}
			try {
				broadcastList.get(b).put(m);                            //enqueue microService
			}
			catch (InterruptedException e){}
		}

	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		MicroService m= eventList.get(e).poll();			//dequeue microService
		if (m==null)										//no suitable microService
			return null;
		try {
			serviceList.get(m).put(e);                      //push event to message queue
		}
		catch (InterruptedException ei){}
		try {
			eventList.get(e).put(m);                        //enqueue microService
		}
		catch (InterruptedException ei){}

		return new Future<>();							//TODO: connect future to e
	}

	@Override
	public void register(MicroService m) {
		if(!serviceList.containsKey(m)){
			LinkedBlockingQueue<Message> q = new LinkedBlockingQueue<>();
			serviceList.put(m,q);
		}

	}

	@Override
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub
		//delete microService from all data structures, delete m's message queue

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Exception
		Message message= serviceList.get(m).poll();
		//<Future Object of message>.get();
	}

	public static MessageBusImpl getInstance(){
		if (bus == null)
			bus = new MessageBusImpl();
		return bus;
	}

}
