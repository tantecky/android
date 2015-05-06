package com.tantecky.offlinedpp.net;

public interface AsyncCallback {
    void onFailure();
    void onDone(String data);
}
