package com.skyplus.hockey.objects;

import java.net.Socket;

/**
 * Created by NVTT on 5/6/2017.
 */

public class Room {

    private String socket;
    private String nameRoom;

    public Room(String socket, String nameRoom) {
        this.socket = socket;
        this.nameRoom = nameRoom;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    @Override
    public String toString() {
        return nameRoom;
    }
}
