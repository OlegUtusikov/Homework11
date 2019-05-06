package ru.ifmo.rain.utusikov;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemoteBank implements Bank {
    private final int port;
    private final ConcurrentMap<String, Account> accounts;
    private final ConcurrentMap<String, Person> persons;

    RemoteBank(final int port) {
        accounts = new ConcurrentHashMap<>();
        persons = new ConcurrentHashMap<>();
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

    public Account getAccount(final String id) throws RemoteException {
        System.out.println("Retrieving account " + id);
        return accounts.get(id);
    }

    public Person savePerson(final String name, final String surname, final String passport) throws RemoteException {
        System.out.println("Saving new user " + passport);
        final Person curPerson = new RemotePerson(name, surname, passport);
        if (persons.putIfAbsent(curPerson.getPassport(), curPerson) == null) {
            UnicastRemoteObject.exportObject(curPerson, port);
            System.out.println("Saved!");
            return curPerson;
        } else {
            return getPerson(passport);
        }
    }

    public Person getPerson(final String passport) throws RemoteException {
        System.out.println("Retrieving person " + passport);
        return persons.get(passport);
    }
}
