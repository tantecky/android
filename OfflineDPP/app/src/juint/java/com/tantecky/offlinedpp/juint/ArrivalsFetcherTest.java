package com.tantecky.offlinedpp.juint;

import android.content.res.Resources;
import android.test.InstrumentationTestCase;

import com.tantecky.offlinedpp.R;
import com.tantecky.offlinedpp.Utils;
import com.tantecky.offlinedpp.net.ArrivalsFetcher;
import com.tantecky.offlinedpp.net.ParserException;

public class ArrivalsFetcherTest extends InstrumentationTestCase {
    private Resources mRes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRes = getInstrumentation().getTargetContext().getResources();
    }

    public void testObtainSessionInfo() throws ParserException {

        String html = Utils.readRawTextFile(mRes, R.raw.spojeni);
        assertNotNull(html);

        ArrivalsFetcher.fromTestCallObtainSessionInfo(html);
    }

    public void testBus140() throws ParserException {
        String html = Utils.readRawTextFile(mRes, R.raw.bus_140);
        assertNotNull(html);

        assertNotNull(ArrivalsFetcher.fromTestCallParseArrivalsTables(html));
    }

    public void testBus510() throws ParserException {
        String html = Utils.readRawTextFile(mRes, R.raw.bus_510);
        assertNotNull(html);

        assertNotNull(ArrivalsFetcher.fromTestCallParseArrivalsTables(html));
    }

    public void testMetroB() throws ParserException {
        String html = Utils.readRawTextFile(mRes, R.raw.metro_b);
        assertNotNull(html);

        assertNotNull(ArrivalsFetcher.fromTestCallParseArrivalsTables(html));
    }

    public void testTram3() throws ParserException {
        String html = Utils.readRawTextFile(mRes, R.raw.tram_3);
        assertNotNull(html);

        assertNotNull(ArrivalsFetcher.fromTestCallParseArrivalsTables(html));
    }
}
