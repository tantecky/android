package com.tantecky.offlinedpp.model;

import com.tantecky.offlinedpp.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Line {

    //region enum
    public enum Type {
        BUS(0),
        METRO(1),
        TRAM(2);

        private int mValue;

        Type(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }
    //endregion

    //region static members
    private final static Pattern sNUMBER = Pattern.compile("\\d+");
    private final static Pattern sLETTER = Pattern.compile("[ABC]");
    //endregion

    protected int mNumber;
    private String mName;
    private String mFrom;
    private String mTo;

    public Line(String name, String from, String to) {
        if (Utils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Name argument is invalid");
        }

        if (Utils.isNullOrEmpty(from)) {
            throw new IllegalArgumentException("From argument is invalid");
        }

        if (Utils.isNullOrEmpty(to)) {
            throw new IllegalArgumentException("To argument is invalid");
        }

        mName = name;
        mFrom = from;
        mTo = to;

        resolveNumber();
    }

    /**
     * Obtain line number from name
     */
    private void resolveNumber() {
        String name = getName();
        Matcher lineNumber = sNUMBER.matcher(name);

        if (lineNumber.find()) {
            mNumber = Integer.parseInt(lineNumber.group());
        } else {
            Matcher metroLetter = sLETTER.matcher(name);

            if (metroLetter.find()) {
                mNumber = Metro.letterToLineNumber(metroLetter.group());
            } else {
                throw new IllegalArgumentException("Unknown metro letter");
            }

        }

        if (mNumber < 1) {
            throw new IllegalArgumentException("Line argument is invalid");
        }
    }

    public String getName() {
        return mName;
    }

    public int getNumber() {
        return mNumber;
    }

    /**
     * for overloading
     *
     * @return line number as String, metro returns letter
     */
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

        if (line.getType() != getType())
            return false;

        return mName.equals(line.getName())
                && mFrom.equals(line.getFrom())
                && mTo.equals(line.getTo());
    }

    @Override
    public int hashCode() {
        return mName.hashCode() ^ mFrom.hashCode() ^ mTo.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Line: %s Name: %s From: %s To: %s", getNumberAsString(),
                mName, mFrom, mTo);
    }
}
