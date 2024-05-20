package com.seyone22.catfish.asr;

public interface IRecorderListener {
    void onUpdateReceived(String message);

    void onDataReceived(float[] samples);
}
