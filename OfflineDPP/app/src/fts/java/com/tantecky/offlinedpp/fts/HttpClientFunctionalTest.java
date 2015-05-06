package com.tantecky.offlinedpp.fts;

import com.tantecky.offlinedpp.net.AsyncCallback;
import com.tantecky.offlinedpp.net.HttpClient;

import junit.framework.TestCase;

public class HttpClientFunctionalTest extends TestCase {
    private final static String sSPOJENI = "http://spojeni.dpp.cz/ZjrForm.aspx";

    public void testGet()
    {
        HttpClient client = new HttpClient();
        String data = client.get(sSPOJENI);

        assertTrue(data.contains("vyhledat"));
    }

    public void testGetAsync() throws InterruptedException {
        HttpClient client = new HttpClient();

        client.getAsync(sSPOJENI, new AsyncCallback() {
            @Override
            public void onFailure() {
                fail();
            }

            @Override
            public void onDone(String data) {
                assertTrue(data.contains("vyhledat"));
            }
        });

        Thread.sleep(2000);
    }
}