package com.tantecky.offlinedpp.model;


public final class Metro extends Line {
    public Metro(int number, String from, String to) {
        super(number, from, to);
    }

    @Override
    public Type getType() {
        return Type.METRO;
    }

    @Override
    public String getNumberAsString() {
        switch (mNumber) {
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "C";
            default:
                throw new IllegalArgumentException("Unknown metro line");

        }
    }
}
