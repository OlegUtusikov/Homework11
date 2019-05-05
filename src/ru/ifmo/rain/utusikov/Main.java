package ru.ifmo.rain.utusikov;

public class Main {
    public static void main(String[] args) {
        System.out.println("Building...");
        Server server = new Server();
        Client client = new Client();
        System.out.println("Finished!");
    }
}
