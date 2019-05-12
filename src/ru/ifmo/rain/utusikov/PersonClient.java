package ru.ifmo.rain.utusikov;

import java.rmi.RemoteException;
import java.util.Objects;

public class PersonClient {
    public static void main(String[] args) {
        if (args.length < 5 || args.length > 6) {
            System.err.println("Incorrect count of arguments!");
            return;
        }
        for (String arg : args) {
            if (Objects.isNull(arg)) {
                System.err.println("Arguments mustn't a null!");
                return;
            }
        }
        final String name = args[0];
        final String surname = args[1];
        final String passport = args[2];
        final String accountId = args[3];
        final String mode = args[5];
        final int moneyDelta;
        try {
            moneyDelta = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            System.err.println("Incorrect format of money!");
            return;
        }
        System.out.println(name + " " + surname + " " + passport + " " + accountId + " " + moneyDelta + " " + mode);
        Bank bank;
        try {
            bank = Utils.get("//localhost/bank");
        } catch (RemoteException e) {
            System.err.println("Can't get a bank!");
            return;
        }
        if (bank == null) {
            System.err.println("Error with connecting bank!");
            return;
        }

        Person curPerson;
        try {
            curPerson = bank.getPerson(passport, mode);
        } catch (RemoteException e) {
            System.err.println("Can't remote person!");
            return;
        }

        if (curPerson == null) {
            try {
                curPerson = bank.savePerson(name, surname, passport, mode);
            } catch (RemoteException e) {
                System.err.println("Can't save a new person with passport " + passport);
            }
        }

        if (curPerson == null) {
            System.err.println("Mir tlen!");
            return;
        }

        try {
            if (!(curPerson.getName().equals(name) && curPerson.getSurname().equals(surname) && curPerson.getPassport().equals(passport))) {
                System.out.println("Incorrect information about person with passport " + passport + " inside bank");
                return;
            }
        } catch (RemoteException e) {
            System.err.println("Error");
        }

        try {
            System.out.println("Account value: " + curPerson.getAccount(accountId, bank).getAmount());
            System.out.println("Changing...");
            curPerson.changeAccount(accountId, moneyDelta, bank);
            System.out.println("Value in bank: " + bank.getAccount( curPerson.getPassport() + ":" + accountId).getAmount());
            System.out.println("Value in person: " + curPerson.getAccount(accountId, bank).getAmount());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
