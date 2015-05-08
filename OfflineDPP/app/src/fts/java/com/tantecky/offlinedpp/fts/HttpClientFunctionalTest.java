package com.tantecky.offlinedpp.fts;

import com.tantecky.offlinedpp.net.AsyncCallback;
import com.tantecky.offlinedpp.net.HttpClient;

import junit.framework.TestCase;

public class HttpClientFunctionalTest extends TestCase {
    private final static String sTEST_URL = "http://myhttp.info/";
    private final static String sEXPECTED_STRING = "okhttp";

    public void testGet()
    {
        HttpClient client = new HttpClient();
        String data = client.get(sTEST_URL);

        assertTrue(data.contains(sEXPECTED_STRING));
    }

    public void testGetAsync() throws InterruptedException {
        HttpClient client = new HttpClient();

        client.getAsync(sTEST_URL, new AsyncCallback() {
            @Override
            public void onFailure() {
                fail();
            }

            @Override
            public void onDone(String data) {
                assertTrue(data.contains(sEXPECTED_STRING));
            }
        });

        Thread.sleep(2000);
    }
}