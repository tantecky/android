package com.tantecky.offlinedpp.model;

import com.tantecky.offlinedpp.Utils;

public abstract class Line {

    //region enum
    public enum Type {
        BUS(0),
        METRO(1),
        TRAM(2);

        private int mValue;

        Type(int value)
        {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }
    //endregion

    protected int mNumber;
    private String mName;
    private String mFrom;
    private String mTo;

    public Line(int number, String from, String to) {
        if (number < 1) {
            throw new IllegalArgumentException("Line argument is invalid");
        }

        if (Utils.IsNullOrEmpty(from)) {
            throw new IllegalArgumentException("From argument is invalid");
        }

        if (Utils.IsNullOrEmpty(to)) {
            throw new IllegalArgumentException("To argument is invalid");
        }

        mNumber = number;
        mFrom = from;
        mTo = to;
    }

    public String getName() {
        return mName;
    }

    public int getNumber() {
        return mNumber;
    }

    // for overloading
    public String getNumberAsString() {
        return Integer.toString(mNumber);
    }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }

    public abstract Type getType();

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Line))
            return false;

        Line line = (Line) o;

        if(line.getType() != getType())
            return false;

        return mNumber == line.getNumber()
                && mFrom.equals(line.getFrom())
                && mTo.equals(line.getTo());
    }

    @Override
    public int hashCode() {
        return mNumber ^ mFrom.hashCode() ^ mTo.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Line: %s From: %s To: %s", getNumberAsString(), mFrom, mTo);
    }
}
