package ru.ifmo.rain.utusikov;

public  abstract  class NormalPerson implements Person {
    private String name;
    private String surname;
    private String passport;

    NormalPerson(final String name, final String surname, final String passport) {
        this.name = name;
        this.passport = passport;
        this.surname = surname;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPassport() {
        return passport;
    }
}
