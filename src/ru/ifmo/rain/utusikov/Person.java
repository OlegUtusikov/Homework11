package ru.ifmo.rain.utusikov;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Person extends Serializable, Remote {
    String getName() throws RemoteException;
    String getSurname() throws RemoteException;
    String getPassport() throws RemoteException;
    Account getAccount(final String id, final Bank bank) throws RemoteException;
    void changeAccount(final String id, final int delta, final Bank bank) throws RemoteException;
}
