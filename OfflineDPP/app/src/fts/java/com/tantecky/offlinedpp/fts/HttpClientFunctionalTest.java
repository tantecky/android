package com.tantecky.offlinedpp.fts;

import com.tantecky.offlinedpp.net.HttpClient;

import junit.framework.TestCase;

public class HttpClientFunctionalTest extends TestCase {
    private final static String sTEST_URL = "http://myhttp.info/";
    private final static String sEXPECTED_STRING = "okhttp";

    public void testGet()
    {
        HttpClient client = new HttpClient();
        String data = client.fetch(sTEST_URL);

        assertTrue(data.contains(sEXPECTED_STRING));
    }
}