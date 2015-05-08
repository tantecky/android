package com.tantecky.offlinedpp.model;


public final class Tram extends Line {
    public Tram(String name, String from, String to) {
        super(name, from, to);
    }

    @Override
    public Type getType() {
        return Type.TRAM;
    }

}
