package com.tantecky.offlinedpp.model;


public final class Metro extends Line {
    protected static String lineNumberToLetter(int number) {
        switch (number) {
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

    protected static int letterToLineNumber(String letter) {
        switch (letter) {
            case "A":
                return 1;
            case "B":
                return 2;
            case "C":
                return 3;
            default:
                throw new IllegalArgumentException("Unknown metro letter");

        }
    }


    public Metro(String name, String from, String to) {
        super(name, from, to);
    }

    @Override
    public Type getType() {
        return Type.METRO;
    }

    @Override
    public String getNumberAsString() {
        return Metro.lineNumberToLetter(mNumber);
    }
}
