package ru.ifmo.rain.utusikov;

import java.rmi.RemoteException;

public class RemoteAccount implements Account {
    private static final long serialVersionUID = 1L;
    private final String id;
    private int amount;

    RemoteAccount(final String id) {
        this.id = id;
        amount = 0;
    }

    public RemoteAccount(final String id, final int amount) {
        this.id = id;
        this.amount = amount;
    }

    RemoteAccount(final Account account) throws RemoteException {
        this(account.getId(), account.getAmount());
    }

    public String getId() {
        return id;
    }

    public synchronized int getAmount() {
        return amount;
    }

    public synchronized void setAmount(final int amount) {
        this.amount = amount;
    }
}
