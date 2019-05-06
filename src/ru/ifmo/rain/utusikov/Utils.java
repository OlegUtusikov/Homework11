package ru.ifmo.rain.utusikov;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

class Utils {
    static <T> T get(String address) {
        T obj;
        try {
            obj = (T) Naming.lookup(address);
        } catch (final NotBoundException e) {
            System.out.println("Object is not bound");
            return null;
        } catch (final MalformedURLException e) {
            System.out.println("Object URL is invalid");
            return null;
        } catch (RemoteException e) {
            System.err.println("Failed remote!");
            e.printStackTrace();
            return null;
        }
        return obj;
    }
}
