package events;

import core.HelperGenerator;
import core.StateManager;
import core.StatusReport;
import core.transaction.Transaction;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class EventEmmiterTest {

    @Test
    public void addFirstEventListener() throws InvalidKeySpecException, InvalidKeyException {
        TestSubscriber subscriber = new TestSubscriber();
        EventEmmiter.addListener(subscriber);
        EventEmmiter.onTransactionReceived(new HelperGenerator().getTransaction());
        ArrayList<StatusReport> reports = EventEmmiter.onStatusReportRequest();

        assertEquals(reports.size(), 1);
        assertEquals(subscriber.counter, 2);
    }

}

class TestSubscriber extends EthosListener {

    public int counter = 0;
    public Transaction tx;

    public TestSubscriber() {}

    public void onTransaction(Transaction tx) {
        this.tx = tx;
        counter++;
    }

    public StatusReport onStatusReport() {
        this.counter++;
        return new StatusReport("test");
    }
}