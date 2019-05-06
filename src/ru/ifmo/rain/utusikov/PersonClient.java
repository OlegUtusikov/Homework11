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
        final String mod = args[5];
        final String name = args[0];
        final String surname = args[1];
        final String passport = args[2];
        final String accountId = args[3];
        final int moneyDelta;
        try {
            moneyDelta = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            System.err.println("Incorrect format of money!");
            return;
        }
        System.out.println(name + " " + surname + " " + passport + " " + accountId + " " + moneyDelta + " " + mod);
        Bank bank = Utils.get("//localhost/bank");
        if (bank == null) {
            System.err.println("Error with connecting bank!");
            return;
        }

        Person curPerson;
        try {
            curPerson = bank.getPerson(passport);
        } catch (RemoteException e) {
            System.err.println("Can't remote person!");
            return;
        }
        if (mod.equals("local") && curPerson != null) {
            curPerson = new LocalPerson(curPerson.getName(), curPerson.getSurname(), curPerson.getPassport());
            System.out.println("local space");
        } else if (mod.equals("local")) {
            System.out.println("Can't create a local user!");
            return;
        }
        if (curPerson == null) {
            try {
                curPerson = bank.savePerson(name, surname, passport);
            } catch (RemoteException e) {
                System.err.println("Can't save a new person with passport " + passport);
            }
        }
        if (!(curPerson.getName().equals(name) && curPerson.getSurname().equals(surname) && curPerson.getPassport().equals(passport))) {
            System.out.println("Incorrect information about person with passport " + passport + " inside bank");
            return;
        }
        Account account = curPerson.getAccount(accountId, bank);
        try {
            System.out.println(account.getAmount());
            curPerson.changeAccount(accountId, moneyDelta, bank);
            System.out.println(curPerson.getAccount(accountId, bank).getAmount());
            System.out.println(account.getAmount());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
