package com.tantecky.offlinedpp.juint;

import com.tantecky.offlinedpp.model.Arrival;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;

public class ArrivalTest extends TestCase {
    private final Arrival mArrival1 = new Arrival(14, 5, Arrival.DayType.SATURDAY);
    private final Arrival mArrival2 = new Arrival(14, 0, Arrival.DayType.SATURDAY);
    private final Arrival mArrival3 = new Arrival(15, 5, Arrival.DayType.SATURDAY);
    private final Arrival mArrival4 = new Arrival(20, 10, Arrival.DayType.SUNDAY);
    private final Arrival mArrival5 = new Arrival(14, 5, Arrival.DayType.WORK_DAY);

    public void testEqual() {

        assertEquals(mArrival1, mArrival1);
        assertEquals(mArrival1, mArrival5);
    }

    public void testNotEqual() {
        assertFalse(mArrival1.equals(mArrival2));
        assertFalse(mArrival1.equals(mArrival3));
        assertFalse(mArrival1.equals(mArrival4));
    }

    public void testDelta() {
        assertEquals(Arrival.delta(mArrival1, mArrival1), 0);
        assertEquals(Arrival.delta(mArrival2, mArrival1), -5);
        assertEquals(Arrival.delta(mArrival4, mArrival1), 6 * 60 + 5);
    }


    public void testSort() {
        ArrayList<Arrival> list = new ArrayList<>();
        list.add(mArrival3);
        list.add(mArrival4);
        list.add(mArrival2);

        Collections.sort(list);

        assertSame(list.get(0), mArrival2);
        assertSame(list.get(1), mArrival3);
        assertSame(list.get(2), mArrival4);
    }
}
