package com.seyone22.catfish.asr;

public interface IWhisperListener {
    void onUpdateReceived(String message);
    void onResultReceived(String result);
}
