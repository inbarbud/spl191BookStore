package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import com.sun.java.util.jar.pack.Package;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    private Inventory i;

    /**
     * Set up for a test.  Note the @Before annotation.  It indicate this method is executed before the tests of
     * this test case are executed.
     */
    @Before
    protected void setUp() throws Exception {
        this.i = createInventory();
    }

    /**
     * This creates the object under test.  Note that we must create a specific implementation (StackImpl)
     * of the interface under test. The rest of the test class only refers to the interface under test.
     *
     * @return a {@link Future} instance.
     */
    protected Inventory createInventory() {
        return new Inventory();
    }

    @Test
    public void getInstance() {
        Inventory k= i.getInstance();
        if(k.getClass()!=null)
            assertEquals(Inventory.class, k.getClass());
    }

    @Test
    public void load() {
        BookInventoryInfo[ ] inv = new BookInventoryInfo[1];
        inv[0]= new BookInventoryInfo("a",2,30);
        i.load(inv);
        assertEquals(30, i.checkAvailabiltyAndGetPrice(inv[0].getBookTitle()));
    }

    @Test
    public void take() {
        OrderResult res= i.take("");
        assertEquals(OrderResult.NOT_IN_STOCK,res);
        BookInventoryInfo[ ] inv = new BookInventoryInfo[1];
        inv[0]= new BookInventoryInfo("a",2,30);
        i.load(inv);
        res=i.take(inv[0].getBookTitle());
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, res);
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
        int price= i.checkAvailabiltyAndGetPrice("");
        assertEquals(-1,price);
        BookInventoryInfo[ ] inv = new BookInventoryInfo[1];
        inv[0]= new BookInventoryInfo("a",2,30));
        i.load(inv);
        price=i.checkAvailabiltyAndGetPrice(inv[0].getBookTitle());
        assertEquals(price,inv[0].getPrice());
    }
}