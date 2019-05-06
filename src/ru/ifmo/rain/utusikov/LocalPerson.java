package ru.ifmo.rain.utusikov;

import java.io.*;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalPerson extends NormalPerson {
    private static final long serialVersionUID = 1L;
    private ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();

    LocalPerson(String name, String surname, String passport) {
        super(name, surname, passport);
    }

    private <T> T copy(T obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;

        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois;

        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account getAccount(final String id, final Bank bank) {
        final String ID = this.getPassport() + ":" + id;
        if (accounts.containsKey(ID)) {
            return accounts.get(ID);
        } else {
            try {
                Account account = bank.getAccount(ID);
                accounts.put(ID, new RemoteAccount(account));
                return accounts.get(ID);
            } catch (RemoteException e) {
                System.err.println("Can't remote account!");
                return null;
            }
        }
    }

    public void changeAccount(final String id, final int delta, final Bank bank) {
        Account account = getAccount(id, bank);
        if (account == null) {
            return;
        }
        try {
            // link ? value
            account.setAmount(account.getAmount() + delta);
        } catch (RemoteException e) {
            System.err.println("Couldn't change amount!");
        }
    }
}
