package com.tantecky.offlinedpp.model;

import java.util.ArrayList;
import java.util.Iterator;

public final class LinesRoster implements Iterable<Line> {

    //region singleton
    private static LinesRoster sRoster = null;

    public static LinesRoster getInstance() {
        if (sRoster == null) {
            sRoster = new LinesRoster();
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

    private LinesRoster() {}

    public void load() {
        sRoster.add(new Metro("Metro C", "I.P.Pavlova", "Tuchoměřice"));
        sRoster.add(new Tram("Tram 2", "Poliklinika Černý Most", "Obchodní centrum Čakovice"));
        sRoster.add(new Bus("Bus 140", "Českomoravská ", "Avia Letňany"));
        sRoster.add(new Bus("Bus 50", "Podolská vodárna", "Palmovka"));
        sRoster.add(new Bus("Bus 50", "Zemědělská univerzita", "Suchdol"));
        sRoster.add(new Bus("Bus 50", "Nádraží Holešovice", "Dejvická"));
        sRoster.add(new Metro("Metro A", "Dolnoměcholupská ", "Chaplinovo náměstí"));
        sRoster.add(new Metro("Metro C", "Sídliště Malešice", "Želivského "));
        sRoster.add(new Metro("Metro C", "Na Košíku", "Satalice"));
        sRoster.add(new Metro("Metro B", "Smíchovské nádraží", "Lehovec"));
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

    public void clear() {
        mLines.clear();
    }

    @Override
    public Iterator<Line> iterator() {
        return new LinesRosterIterator();
    }
}
