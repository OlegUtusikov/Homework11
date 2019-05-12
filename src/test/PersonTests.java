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
    public void createBankTest() {
        Bank bank = null;
        try {
            bank = Utils.get("//localhost/bank");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assertNotNull(bank);
        try {
            assertNull(bank.getPerson("id", "local"));
        } catch (RemoteException ignored) {
        }
    }


    @Test
    public void saveRemotePersonTest() {
        Bank bank = null;
        try {
            bank = Utils.get("//localhost/bank");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assertNotNull(bank);
        try {
            assertNotNull(bank.savePerson("oleg", "2", "3", "remote"));
        } catch (RemoteException ignored) {
        }
    }

    @Test
    public void getRemotePersonTest() {
        Bank bank = null;
        try {
            bank = Utils.get("//localhost/bank");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
    public void createAndChangeAccountRemotePersonTest() {
        Bank bank = null;
        try {
            bank = Utils.get("//localhost/bank");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
    public void changeAccountLocalPersonsTest() {
        Bank bank = null;
        try {
            bank = Utils.get("//localhost/bank");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assertNotNull(bank);
        for(int i = 0; i < 15; i++) {
            final String ID = Integer.toString(i);
            try {
                bank.savePerson(ID, ID, ID, "remote");
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
                bank.getPerson(ID, "remote").getAccount(ID, bank);
                localPersons.get(i).changeAccount(ID, 255, bank);
                assertEquals(255, localPersons.get(i).getAccount(ID, bank).getAmount());
                assertEquals(0, bank.getAccount(ID + ":" + ID).getAmount());
            } catch (RemoteException ignored) {
            }
        }

        for(int i = 0; i < 15; i++) {
            final String ID = Integer.toString(i);
            try {
                bank.getPerson(ID, "remote").changeAccount(ID, 100, bank);
                assertEquals(100, bank.getAccount(ID + ":" + ID).getAmount());
                assertEquals(255, localPersons.get(i).getAccount(ID, bank).getAmount());
            } catch (RemoteException ignored) {
            }
        }
    }

    @Test
    public void serializationTest() {
        Bank bank = null;
        try {
            bank = Utils.get("//localhost/bank");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assertNotNull(bank);
        try {
            bank.savePerson("Oleg", "Utusikov", "123", "remote");
            Person person = bank.getPerson("123", "local");
            Person copyPerson = Utils.copy(person);
            assertEquals(copyPerson.getName(), person.getName());
            assertEquals(copyPerson.getSurname(), person.getSurname());
            assertEquals(copyPerson.getPassport(), person.getPassport());
        } catch (RemoteException e) {
        }
    }

    @Test
    public void  localPersonsTestBeforeAndAfter() {
        Bank bank = null;
        try {
            bank = Utils.get("//localhost/bank");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            bank.savePerson("oleg", "utusikov", "1", "remote");
            Person personRemote1 = bank.getPerson("1", "remote");
            Person personLocal1 = bank.getPerson("1", "local");
            personRemote1.changeAccount("1", 100, bank);
            Person personLocal2 = bank.getPerson("1", "local");
            assertEquals(0, personLocal1.getAccount("1", bank).getAmount());
            assertEquals(100, personLocal2.getAccount("1", bank).getAmount());

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assertNotNull(bank);
    }
}