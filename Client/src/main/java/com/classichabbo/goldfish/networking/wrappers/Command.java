package com.classichabbo.goldfish.networking.wrappers;

public class Command {
    private final int header;
    private final Object[] data;

    public Command(int tCmd, Object[] data) {
        this.header = tCmd;
        this.data = data;
    }

    public int getHeader() {
        return header;
    }

    public Object[] getData() {
        return data;
    }
}
