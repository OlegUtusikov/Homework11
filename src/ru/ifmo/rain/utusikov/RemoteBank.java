package ru.ifmo.rain.utusikov;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemoteBank implements Bank {
    private final int port;
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();

    public RemoteBank(final int port) {
        this.port = port;
    }

    public Account createAccount(final String id) throws RemoteException {
        System.out.println("Creating account " + id);
        final Account account = new RemoteAccount(id);
        if (accounts.putIfAbsent(id, account) == null) {
            UnicastRemoteObject.exportObject(account, port);
            return account;
        } else {
            return getAccount(id);
        }
    }

    public Person savePerson(final Person person) throws RemoteException {
        System.out.println("Saving new user " + person.getNumberOfPassport());
        final RemotePerson curPerson = new RemotePerson(person.getName(), person.getSurname(), person.getNumberOfPassport());
        if (persons.putIfAbsent(curPerson.getNumberOfPassport(), curPerson) == null) {
            UnicastRemoteObject.exportObject(curPerson, port);
            return curPerson;
        } else {
            return getPerson(curPerson.getNumberOfPassport());
        }
    }

    public Person getPerson(final String passport) {
        System.out.println("Retrieving person " + passport);
        return persons.get(passport);
    }

    public Person getLocalPersone() {
        return null;
    }

    public Account getAccount(final String id) {
        System.out.println("Retrieving account " + id);
        return accounts.get(id);
    }
}
