package com.tantecky.offlinedpp.net;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public final class HttpClient {
    private final static OkHttpClient sClient = new OkHttpClient();

    private Request.Builder mBuilder;
    private FormEncodingBuilder mFormData;

    public HttpClient() {
    }

    private Request buildRequest(String url) {
        if (mBuilder == null)
            mBuilder = new Request.Builder();

        if (mFormData == null)
            return mBuilder.url(url).build();
        else
            return mBuilder.url(url).post(mFormData.build()).build();
    }

    public void addHeader(String name, String value) {
        mBuilder = mBuilder.addHeader(name, value);
    }

    public void addFormData(String name, String value) {
        if (mFormData == null)
            mFormData = new FormEncodingBuilder();

        mFormData = mFormData.add(name, value);
    }

    /**
     * Dispatch GET/POST requests, type depends if there are any FormData
     * @param url
     * @return HTTP body response, null in case of error
     */
    public String fetch(String url) {
        Request request = buildRequest(url);

        try {
            Response response = sClient.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            mBuilder = null;
            mFormData = null;
        }
    }
}
