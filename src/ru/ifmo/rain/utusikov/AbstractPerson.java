package ru.ifmo.rain.utusikov;

import java.rmi.RemoteException;

public  abstract  class AbstractPerson implements Person {
    private String name;
    private String surname;
    private String passport;

    AbstractPerson(final String name, final String surname, final String passport) {
        this.name = name;
        this.passport = passport;
        this.surname = surname;
    }
    public String getName() throws RemoteException {
        return name;
    }
    public String getSurname() throws RemoteException {
        return surname;
    }
    public String getPassport() throws RemoteException {
        return passport;
    }
}
