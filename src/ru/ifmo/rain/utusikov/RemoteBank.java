package ru.ifmo.rain.utusikov;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
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
        final Account account = new RemoteAccount(id);
        if (accounts.putIfAbsent(id, account) == null) {
            UnicastRemoteObject.exportObject(account, port);
            return account;
        } else {
            return getAccount(id);
        }
    }

    public Account getAccount(final String id) throws RemoteException {
        return accounts.get(id);
    }

    public Person savePerson(final String name, final String surname, final String passport, final String mode) throws RemoteException {
        if (mode.equals("remote")) {
            final Person curPerson = new RemotePerson(name, surname, passport);
            if (persons.putIfAbsent(curPerson.getPassport(), curPerson) == null) {
                UnicastRemoteObject.exportObject(curPerson, port);
                return curPerson;
            } else {
                return getPerson(passport, mode);
            }
        } else {
            return null;
        }
    }

    public Person getPerson(final String passport, final String mode) throws RemoteException {
        final Person curPerson = persons.get(passport);
        if (curPerson == null) {
            return null;
        }
        if (mode.equals("remote")) {
            return curPerson;
        } else if (mode.equals("local")) {
            Person local = new LocalPerson(curPerson.getName(), curPerson.getSurname(), curPerson.getPassport());
            for(Map.Entry<String, Account> accountEntry : accounts.entrySet()) {
                if (accountEntry.getKey().startsWith(passport)) {
                    String[] ids = accountEntry.getKey().split(":");
                    local.changeAccount(ids[1], accountEntry.getValue().getAmount(), this);
                }
            }
            return  local;
        } else {
            return null;
        }
    }
}
