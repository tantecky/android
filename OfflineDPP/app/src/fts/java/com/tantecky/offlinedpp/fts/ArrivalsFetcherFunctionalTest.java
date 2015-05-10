package com.tantecky.offlinedpp.fts;

import com.tantecky.offlinedpp.model.Bus;
import com.tantecky.offlinedpp.model.Metro;
import com.tantecky.offlinedpp.model.Tram;
import com.tantecky.offlinedpp.net.ArrivalsFetcher;

import junit.framework.TestCase;


public class ArrivalsFetcherFunctionalTest extends TestCase {
    private final Bus mBus140 = new Bus("Bus 140", "Letňany", "Čakovice");
    private final Bus mBus510 = new Bus("NBus 510", "Terminál 1", "Na Beránku");
    private final Metro mMetroB = new Metro("Metro B", "Zličín", "Černý Most");
    private final Tram mTram3 = new Tram("Tram 3", "Levského", "Kobylisy");

    public void testBus140() {
        assertNotNull(ArrivalsFetcher.fetch(mBus140));
    }

    public void testBus510() {
        assertNotNull(ArrivalsFetcher.fetch(mBus510));
    }

    public void testMetroB() {
        assertNotNull(ArrivalsFetcher.fetch(mMetroB));
    }

    public void testTram3() {
        assertNotNull(ArrivalsFetcher.fetch(mTram3));
    }
}
