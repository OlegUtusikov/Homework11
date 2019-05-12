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
        if (accounts.containsKey(Utils.makeId(id, this))) {
            return accounts.get(Utils.makeId(id, this));
        } else {
            try {
                Account newAccount = new RemoteAccount(Utils.makeId(id, this));
                accounts.put(Utils.makeId(id, this), newAccount);
                return newAccount;
            } catch (RemoteException e) {
                throw new RemoteException("Can't remote account!");
            }
        }
    }

    public void changeAccount(final String id, final int delta, final Bank bank) throws RemoteException {
        Account account = getAccount(id, bank);
        account.setAmount(account.getAmount() + delta);
    }
}
