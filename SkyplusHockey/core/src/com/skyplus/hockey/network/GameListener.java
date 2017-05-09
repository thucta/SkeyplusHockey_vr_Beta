package com.skyplus.hockey.network;

import com.skyplus.hockey.objects.Room;

/**
 * Created by TruongNN on 5/5/2017.
 */
public interface GameListener {

    void createServer(String nameRoom);

    void connectServer(Room room);
    void backProgesDialog();
    void onConnected();

    void onDisconnected();

    void onConnectionFailed();

    void onMessageReceived(String message);

}
