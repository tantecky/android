package com.tantecky.offlinedpp.model;

import java.util.ArrayList;
import java.util.Iterator;

public final class LinesRoster implements Iterable<Line> {

    //region singleton
    private static LinesRoster sRoster = null;

    public static LinesRoster getInstance() {
        if (sRoster == null) {
            sRoster = new LinesRoster();
            sRoster.add(new Line(11, "Muzeum", "Letnany"));
            sRoster.add(new Line(12, "Muzeum", "Letnany"));
            sRoster.add(new Line(13, "Muzeum", "Letnany"));
        }

        return sRoster;
    }
    //endregion

    //region iterator
    private class LinesRosterIterator implements Iterator<Line> {
        private int mCurrentIndex = 0;

        @Override
        public boolean hasNext() {
            return mCurrentIndex < mLines.size();
        }

        @Override
        public Line next() {
            return mLines.get(mCurrentIndex++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    //endregion

    private ArrayList<Line> mLines = new ArrayList<>();

    /**
     * just for JUint!!!
      */
    public LinesRoster() {
    }

    public boolean contains(Line newLine) {
        return mLines.contains(newLine);
    }

    public void add(Line newLine) {
        if (!contains(newLine))
            mLines.add(newLine);
    }

    public int size() {
        return mLines.size();
    }

    public Line get(int pos) {
        return mLines.get(pos);
    }

    public boolean isEmpty() {
        return mLines.isEmpty();
    }

    @Override
    public Iterator iterator() {
        return new LinesRosterIterator();
    }
}
