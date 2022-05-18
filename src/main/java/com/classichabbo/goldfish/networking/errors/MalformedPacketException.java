package com.classichabbo.goldfish.networking.errors;

public class MalformedPacketException extends Exception {
    public MalformedPacketException(String error) {
        super(error);
    }
}
