package test;

import org.junit.Test;
import ru.ifmo.rain.utusikov.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PersonTests {
    Server server = new Server();
    @Test
    public void test1() {
        System.out.println(1);
        final Bank bank = Utils.get("//localhost/bank");
        assertNotNull(bank);
        try {
            assertNull(bank.getPerson("id", "local"));
        } catch (RemoteException ignored) {
        }
    }


    @Test
    public void test2() {
        System.out.println(2);
        final Bank bank = Utils.get("//localhost/bank");
        assertNotNull(bank);
        try {
            assertNotNull(bank.savePerson("oleg", "2", "3", "remote"));
        } catch (RemoteException ignored) {
        }
    }

    @Test
    public void test3() {
        System.out.println(3);
        final Bank bank = Utils.get("//localhost/bank");
        assertNotNull(bank);
        Person  person = null;
        try {
            person = bank.savePerson("oleg", "2", "3", "remote");
        } catch (RemoteException ignored) {
        }
        try {
            assertEquals(person, bank.getPerson("3", "remote"));
        } catch (RemoteException ignored) {
        }
    }

    @Test
    public void test4() {
        System.out.println(4);
        final Bank bank = Utils.get("//localhost/bank");
        assertNotNull(bank);
        List<Person> personList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            final String ID = Integer.toString(i);
            try {
                personList.add(bank.savePerson(ID, ID, ID, "remote"));
                personList.get(i).changeAccount(ID, 100, bank);
                assertEquals(100, personList.get(i).getAccount(ID, bank).getAmount());
                assertEquals(100, bank.getAccount(ID + ":" + ID).getAmount());
            } catch (RemoteException ignored) {
            }
        }
    }

    @Test
    public void test5() {
        System.out.println(5);
        final Bank bank = Utils.get("//localhost/bank");
        assertNotNull(bank);
        List<Person> personList = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            final String ID = Integer.toString(i);
            try {
                personList.add(bank.savePerson(ID, ID, ID, "remote"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        List<Person> localPersons = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            final String ID = Integer.toString(i);
            try {
                localPersons.add(bank.getPerson(ID, "local"));
                assertNull(bank.getAccount(ID + ":" + ID));
                personList.get(i).getAccount(ID, bank);
                localPersons.get(i).changeAccount(ID, 255, bank);
                assertEquals(255, localPersons.get(i).getAccount(ID, bank).getAmount());
                assertEquals(0, bank.getAccount(ID + ":" + ID).getAmount());
            } catch (RemoteException ignored) {
            }
        }

        for(int i = 0; i < 15; i++) {
            final String ID = Integer.toString(i);
            try {
                personList.get(i).changeAccount(ID, 100, bank);
                assertEquals(100, bank.getAccount(ID + ":" + ID).getAmount());
                assertEquals(255, localPersons.get(i).getAccount(ID, bank).getAmount());
            } catch (RemoteException ignored) {
            }
        }
    }
}