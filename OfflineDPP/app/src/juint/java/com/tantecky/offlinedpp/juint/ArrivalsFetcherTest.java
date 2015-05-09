package com.tantecky.offlinedpp.juint;

import android.content.res.Resources;
import android.test.ActivityTestCase;
import android.test.InstrumentationTestCase;

import com.tantecky.offlinedpp.R;
import com.tantecky.offlinedpp.Utils;
import com.tantecky.offlinedpp.net.ArrivalsFetcher;

import junit.framework.TestCase;

import java.io.InputStream;

public class ArrivalsFetcherTest extends InstrumentationTestCase {
    private Resources mRes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRes = getInstrumentation().getTargetContext().getResources();
    }

    public void testObtainSessionInfo() {

        String html = Utils.readRawTextFile(mRes, R.raw.spojeni);
        assertNotNull(html);

        ArrivalsFetcher.fromTestCallObtainSessionInfo(html);
    }
}
