package ru.ifmo.rain.utusikov;

import java.io.Serializable;
import java.rmi.Remote;

public interface Person extends Serializable, Remote {
    String getName();
    String getSurname();
    String getPassport();
    Account getAccount(final String id, final Bank bank);
    void changeAccount(final String id, final int delta, final Bank bank);
}
