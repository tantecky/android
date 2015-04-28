package com.tantecky.offlinedpp;

import com.tantecky.offlinedpp.model.Line;
import com.tantecky.offlinedpp.model.LinesRoster;

import junit.framework.TestCase;


public class LineRosterTest extends TestCase {
    private LinesRoster mRoster;
    private Line mLine1 = new Line(42, "Vltavská", "Holešovice");
    private Line mLine1Dup = new Line(42, "Vltavská", "Holešovice");
    private Line mLine2 = new Line(42, "Muzeum", "Holešovice");

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRoster = new LinesRoster();
    }

    public void testIsEmpty() {
        assertTrue(mRoster.isEmpty());
    }

    public void testAdd() {
        mRoster.add(mLine1);
        assertEquals(mRoster.size(), 1);
        assertEquals(mLine1, mRoster.get(0));

        mRoster.add(mLine2);
        assertEquals(mRoster.size(), 2);
    }

    public void testDuplicate() {
        mRoster.add(mLine1);
        mRoster.add(mLine1Dup);
        assertEquals(mRoster.size(), 1);
    }

    public void testIterator() {
        mRoster.add(mLine1);
        mRoster.add(mLine2);

        int i = 0;
        for (Line line : mRoster) {
            if (i == 0)
                assertEquals(mLine1, line);
            else if (i == 1)
                assertEquals(mLine2, line);

            i++;
        }

        assertEquals(i, 2);
    }
}
