package com.tantecky.offlinedpp.juint;
import com.tantecky.offlinedpp.model.Bus;
import com.tantecky.offlinedpp.model.Line;
import com.tantecky.offlinedpp.model.Tram;

import junit.framework.TestCase;

public class LineTest extends TestCase {
    private final Line mLine1 = new Bus(42, "Vltavská", "Holešovice");

    public void testEqual()
    {
        Line line2 = new Bus(42, "Vltavská", "Holešovice");
        assertTrue(mLine1.equals(mLine1));
        assertTrue(mLine1.equals(line2));
    }

    public void testNotEqual()
    {
        Line line2 = new Bus(1, "Vltavská", "Holešovice");
        assertFalse(mLine1.equals(line2));

        line2 = new Bus(42, "vVltavská", "Holešovice");
        assertFalse(mLine1.equals(line2));

        line2 = new Bus(42, "Vltavská", "hHolešovice");
        assertFalse(mLine1.equals(line2));

        Tram line3 = new Tram(42, "Vltavská", "Holešovice");
        assertFalse(mLine1.equals(line3));
    }
}
