package ru.ifmo.rain.utusikov;

import java.rmi.Remote;

public class RemotePerson implements Person, Remote {
    private String name;
    private String surname;
    private String passport;

    RemotePerson(final String name, final String surname, final String passport) {
        this.name = name;
        this.surname = surname;
        this.passport = passport;
    }

    @Override
    public synchronized String getName() {
        return name;
    }

    @Override
    public synchronized String getSurname() {
        return surname;
    }

    @Override
    public synchronized String getNumberOfPassport() {
        return passport;
    }
}
