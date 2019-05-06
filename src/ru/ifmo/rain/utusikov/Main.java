package ru.ifmo.rain.utusikov;

public class Main {
    public static void main(String[] args) {
        System.out.println("Building...");
        Server server = new Server();
        PersonClient client = new PersonClient();
        System.out.println("Finished!");
    }
}
