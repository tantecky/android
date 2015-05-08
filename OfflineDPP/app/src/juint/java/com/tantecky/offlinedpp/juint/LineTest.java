package com.tantecky.offlinedpp.juint;
import com.tantecky.offlinedpp.model.Bus;
import com.tantecky.offlinedpp.model.Line;
import com.tantecky.offlinedpp.model.Metro;
import com.tantecky.offlinedpp.model.Tram;

import junit.framework.TestCase;

public class LineTest extends TestCase {
    private final Line mBus = new Bus("Bus 42", "Vltavská", "Holešovice");
    private final Line mMetro = new Metro("Metro C", "Vltavská", "Holešovice");
    private final Line mTram = new Metro("Tram 54", "Vltavská", "Holešovice");

    public void testEqual()
    {
        Line line2 = new Bus("Bus 42", "Vltavská", "Holešovice");
        assertTrue(mBus.equals(mBus));
        assertTrue(mBus.equals(line2));
    }

    public void testNotEqual()
    {
        assertFalse(mBus.equals(new Bus("Bus 1", "Vltavská", "Holešovice")));
        assertFalse(mBus.equals(new Bus("Bus 42", "vVltavská", "Holešovice")));
        assertFalse(mBus.equals(new Bus("Bus 43", "Vltavská", "Holešovice")));
        assertFalse(mBus.equals(new Bus("Bus 42", "Vltavská", "hHolešovice")));
        assertFalse(mBus.equals(new Tram("Tram 42", "Vltavská", "Holešovice")));
    }

    public void testResolveNumber()
    {
        assertEquals(mBus.getNumber(), 42);
        assertEquals(mMetro.getNumber(), 3);
        assertEquals(mTram.getNumber(), 54);
    }
}
