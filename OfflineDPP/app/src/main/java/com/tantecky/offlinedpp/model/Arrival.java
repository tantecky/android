package com.tantecky.offlinedpp.model;

public final class Arrival implements Comparable<Arrival> {
    //region enum
    public enum DayType {
        WORK_DAY(0),
        SATURDAY(1),
        SUNDAY(2);

        private int mValue;

        DayType(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

        @Override
        public String toString() {
            switch (mValue)
            {
                case 0:
                    return "WORK_DAY";
                case 1:
                    return "SATURDAY";
                case 2:
                    return "SUNDAY";
                default:
                    throw new IllegalArgumentException("Invalid DayType");
            }
        }
    }
    //endregion

    //region static members

    /**
     * Simple arrival subtraction does not do any time wrapping
     * @param arrival1 arrival1
     * @param arrival2 arrival2
     * @return arrival1 - arrival2 in minutes
     */
    public static int delta(Arrival arrival1, Arrival arrival2) {
        return arrival1.getHour() * 60 + arrival1.getMinute()
                - arrival2.getHour() * 60 - arrival2.getMinute();
    }
    //endregion

    private int mHour;
    private int mMinute;
    private DayType mDayType;

    public Arrival(int hour, int minute, DayType type) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Invalid hour");
        }

        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Invalid minute");
        }

        mHour = hour;
        mMinute = minute;
        mDayType = type;

    }

    public int getHour() {
        return mHour;
    }

    public int getMinute() {
        return mMinute;
    }

    public DayType getDayType() {
        return mDayType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Arrival))
            return false;

        Arrival arrival = (Arrival) o;

        return mMinute == arrival.getMinute()
                && mHour == arrival.getHour()
                && mDayType == arrival.getDayType();
    }

    @Override
    public int hashCode() {
        return mHour ^ mMinute ^ mDayType.getValue();
    }

    @Override
    public String toString() {
        return String.format("Hour: %d Minute: %d DayType: %s", mHour, mMinute, mDayType.toString());
    }

    @Override
    public int compareTo(Arrival another) {
        return delta(this, another);
    }
}
