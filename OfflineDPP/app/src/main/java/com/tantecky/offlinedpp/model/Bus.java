package com.tantecky.offlinedpp.model;


public final class Bus extends Line {
    public Bus(String name, String from, String to) {
        super(name, from, to);
    }

    @Override
    public Type getType() {
        return Type.BUS;
    }

}
