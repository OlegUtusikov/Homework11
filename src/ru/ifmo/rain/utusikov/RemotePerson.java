package ru.ifmo.rain.utusikov;


import java.rmi.RemoteException;

public class RemotePerson extends AbstractPerson {

    RemotePerson(String name, String surname, String passport) {
        super(name, surname, passport);
    }

    public synchronized Account getAccount(final String id, final Bank bank) throws RemoteException {
        try {
            Account account = bank.getAccount(Utils.makeId(id, this));
            if (account == null) {
                account = saveAccount(id, bank);
            }
            return account;
        } catch (RemoteException e) {
            throw new RemoteException("Can't remote account!");
        }
    }

    private synchronized Account saveAccount(final String id, final Bank bank) throws RemoteException {
        try {
            return bank.createAccount(Utils.makeId(id, this));
        } catch (RemoteException e) {
            throw new RemoteException("Can't save account with id " + Utils.makeId(id, this) + "!");
        }
    }

    public synchronized void changeAccount(final String id, final int delta, final Bank bank) throws RemoteException {
        Account account = getAccount(id, bank);
        try {
            account.setAmount(account.getAmount() + delta);
        } catch (RemoteException e) {
            throw new RemoteException("Couldn't change amount!");
        }
    }
}
