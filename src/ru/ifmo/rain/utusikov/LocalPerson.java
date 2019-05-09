package ru.ifmo.rain.utusikov;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalPerson extends AbstractPerson {
    private static final long serialVersionUID = 1L;
    private ConcurrentMap<String, Account> accounts;

    LocalPerson(String name, String surname, String passport) {
        super(name, surname, passport);
        accounts = new ConcurrentHashMap<>();
    }

    public Account getAccount(final String id, final Bank bank) throws RemoteException {
        final String ID = this.getPassport() + ":" + id;
        if (accounts.containsKey(ID)) {
            return accounts.get(ID);
        } else {
            try {
                Account account = bank.getAccount(ID);
                Account newAccount;
                if (account == null) {
                    newAccount = new RemoteAccount(ID);
                } else {
                    newAccount = new RemoteAccount(account);
                }
                accounts.put(ID, newAccount);
                return newAccount;
            } catch (RemoteException e) {
                System.err.println("Can't remote account!");
                return null;
            }
        }
    }

    public void changeAccount(final String id, final int delta, final Bank bank) throws RemoteException {
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
