package com.tantecky.offlinedpp.net;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public final class HttpClient {
    private final static OkHttpClient sClient = new OkHttpClient();

    private Request.Builder mBuilder;

    public HttpClient() {
        mBuilder = new Request.Builder();
    }

    public void addHeader(String name, String value) {
        mBuilder = mBuilder.addHeader(name, value);
    }

    public String get(String url) {
        Request request = mBuilder.url(url).build();

        try {
            Response response = sClient.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getAsync(String url, final AsyncCallback callback) {
        Request request = mBuilder.url(url).build();

        sClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();

                callback.onFailure();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                callback.onDone(response.body().string());
            }
        });
    }
}
