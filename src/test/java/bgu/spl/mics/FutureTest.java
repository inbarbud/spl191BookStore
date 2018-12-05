package bgu.spl.mics;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class FutureTest {

    private Future<Integer> f;

    /**
     * Set up for a test.  Note the @Before annotation.  It indicate this method is executed before the tests of
     * this test case are executed.
     */
    @Before
    protected void setUp() throws Exception {
        this.f = createFuture();
    }

    /**
     * This creates the object under test.  Note that we must create a specific implementation (StackImpl)
     * of the interface under test. The rest of the test class only refers to the interface under test.
     *
     * @return a {@link Future} instance.
     */
    protected Future<Integer> createFuture() {
        return new Future<>();
    }



    @Test
    public void get() {
        AtomicInteger i= new AtomicInteger(0);
        new Thread(()->{
            i.getAndSet(f.get());
        }).start();
        assertEquals(0,i.get());
        new Thread(()->{
            f.resolve(5);
        }).start();
        assertEquals(5,i.get());
    }

    @Test
    public void resolve() {
        AtomicInteger i= new AtomicInteger(0);
        new Thread(()->{
            f.resolve(5);
        }).start();
        i.getAndSet(f.get());
        assertEquals(5,i.get());
    }

    @Test
    public void isDone() {
        AtomicBoolean b= new AtomicBoolean(true);
        assertFalse(b.get());
        new Thread(()->{
            f.resolve(5);
        }).start();
        b.getAndSet(f.isDone());
        assertTrue(b.get());
    }

    @Test
    public void get1() {
        AtomicInteger i= new AtomicInteger(0);
        long timeout = 1;
        new Thread(()->{
            i.getAndSet(f.get(timeout, TimeUnit.SECONDS));
        }).start();
        assertEquals(0,i.get());
        new Thread(()->{
            f.resolve(5);
        }).start();

        if(i!=null)
            assertEquals(5,i.get());
    }
}