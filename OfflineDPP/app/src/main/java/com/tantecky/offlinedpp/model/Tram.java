package com.tantecky.offlinedpp.model;


public final class Tram extends Line {
    public Tram(int number, String from, String to) {
        super(number, from, to);
    }

    @Override
    public Type getType() {
        return Type.TRAM;
    }

}
