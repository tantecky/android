package com.tantecky.offlinedpp.model;

import com.tantecky.offlinedpp.Utils;

public class Line {
    private int mNumber;
    private String mFrom;
    private String mTo;

    public int getNumber() {
        return mNumber;
    }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Line))
            return false;

        Line line = (Line) o;

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
        return String.format("Line: %d From: %s To: %s", mNumber, mFrom, mTo);
    }
}
