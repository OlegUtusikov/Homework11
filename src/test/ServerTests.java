package test;

import org.junit.Test;
import ru.ifmo.rain.utusikov.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import static org.junit.Assert.*;

public class ServerTests {
    @Test
    public void test1() {
        Server server = new Server();
        try {
            assertNotNull((Bank) Utils.get("//localhost/bank"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}