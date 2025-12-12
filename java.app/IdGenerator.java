package com.banking.service;

public class IdGenerator {

    private static int nextId = 1;

    public static void setStartId(int start) {
        nextId = start;
    }

    public static int generateId() {
        return nextId++;
    }
}
