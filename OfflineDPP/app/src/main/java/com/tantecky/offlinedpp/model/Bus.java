package com.tantecky.offlinedpp.model;


public final class Bus extends Line {
    public Bus(int number, String from, String to) {
        super(number, from, to);
    }

    @Override
    public Type getType() {
        return Type.BUS;
    }

}
