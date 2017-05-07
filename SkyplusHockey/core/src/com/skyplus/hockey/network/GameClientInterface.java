package com.skyplus.hockey.network;

import com.skyplus.hockey.objects.Room;

import java.math.RoundingMode;
import java.net.Socket;

/**
 * Created by TruongNN on 5/5/2017.
 */
public interface GameClientInterface extends Runnable {
    int port = 50012;
    int portTest = 50014;
    int portUDP = 50013;

    boolean isConnected();

    void onConnected();

    void disconnect();

    void sendMessage(String message);

    void sendMessageUDP(String message);

    void sendMessage(Socket socket,String message);

    void setListener(GameListener gameListener);

    void connectServer(Room room);

    Boolean isServer();

    void cancel();

    @Override
    void run();
}