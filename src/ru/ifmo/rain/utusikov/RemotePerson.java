package ru.ifmo.rain.utusikov;


import java.rmi.RemoteException;

public class RemotePerson extends NormalPerson {

    RemotePerson(String name, String surname, String passport) {
        super(name, surname, passport);
    }

    public Account getAccount(final String id, final Bank bank) {
        final String ID = this.getPassport() + ":" + id;
        try {
            Account account = bank.getAccount(ID);
            if (account == null) {
                System.out.println("Saving account!");
                account = saveAccount(id, bank);
            }
            return account;
        } catch (RemoteException e) {
            System.err.println("Can't remote account!");
            return null;
        }
    }

    private Account saveAccount(final String id, final Bank bank) {
        final String ID = this.getPassport() + ":" + id;
        try {
            bank.createAccount(ID);
            return bank.getAccount(ID);
        } catch (RemoteException e) {
            System.err.println("Can't save account with id " + ID + "!");
            return null;
        }
    }

    public void changeAccount(final String id, final int delta, final Bank bank) {
        Account account = getAccount(id, bank);
        try {
            account.setAmount(account.getAmount() + delta);
        } catch (RemoteException e) {
            System.err.println("Couldn't change amount!");
        }
    }
}
