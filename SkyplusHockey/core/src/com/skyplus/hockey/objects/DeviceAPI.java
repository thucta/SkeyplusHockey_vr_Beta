package com.skyplus.hockey.objects;

import com.skyplus.hockey.network.GameListener;

import java.util.List;

public interface DeviceAPI {

    String getIpAddress();

    boolean isConnectedToLocalNetwork();

    void showNotification(String message);

    void showAlertDialog(GameListener gameListener, List<Room> rooms);
    void showAlertDialog(String message);
    void showProgressDialog(String message);

    void closeProgressDialog();

    boolean progressDialogIsShow();

    void showInputDialog(GameListener gameListener);

    void startRecording();

    void transmit(byte[] message);

    int getBufferSize();
}