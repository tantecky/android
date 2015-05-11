package com.tantecky.offlinedpp.model;

public enum DayType {
    WORK_DAY(1),
    SATURDAY(2),
    SUNDAY(4),
    FRIDAY(8),
    FROM_MONDAY_TO_THURDAY(16),
    EVERY_DAY(32);

    //region static members
    public static DayType fromString(String name) throws IllegalArgumentException {
        switch (name) {
            case "Pracovní den":
                return DayType.WORK_DAY;
            case "Sobota":
                return DayType.SATURDAY;
            case "Neděle":
                return DayType.SUNDAY;
            case "Pátek":
                return DayType.FRIDAY;
            case "Pondělí - Čtvrtek":
                return DayType.FROM_MONDAY_TO_THURDAY;
            case "Celý týden":
                return DayType.EVERY_DAY;
            default:
                throw new IllegalArgumentException("Invalid DayType name: " + name);
        }
    }
    //endregion

    private int mValue;

    DayType(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    @Override
    public String toString() throws IllegalArgumentException {
        switch (mValue) {
            case 1:
                return "Pracovní den";
            case 2:
                return "Sobota";
            case 4:
                return "Neděle";
            case 8:
                return "Pátek";
            case 16:
                return "Pondělí - Čtvrtek";
            case 32:
                return "Celý týden";
            default:
                throw new IllegalArgumentException("Invalid DayType: " + Integer.toString(mValue));
        }
    }
}