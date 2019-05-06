package ru.ifmo.rain.utusikov;

import java.io.*;
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

    static <T> T copy(T obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;

        try {
            oos = new ObjectOutputStream(baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois;

        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
