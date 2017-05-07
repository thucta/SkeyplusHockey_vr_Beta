package com.skyplus.hockey.network;

import com.badlogic.gdx.Gdx;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.objects.Room;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by TruongNN on 5/5/2017.
 */
public class Server extends GameClient {

    private static ServerSocket serverSocket, socketTest;

    public Server(GameListener gameListener, String localAddress, String nameRoom) {
        super(gameListener, localAddress, nameRoom);

    }

    @Override
    public void run() {
        try {
            try {
                serverSocket.close();
            } catch (Exception e) {

            }
            try {
                socketTest.close();
            } catch (Exception e) {

            }
            serverSocket = new ServerSocket(port);
            socketTest = new ServerSocket(portTest);

            Gdx.app.error("ip", serverSocket + "" + serverSocket.getInetAddress().getHostName());
            Hockey.deviceAPI.showNotification("Created game at " + localAddress + " name: " + nameRoom);


            Thread t1 = new Thread(new NormalServerThread());
            t1.start();

            Thread t2 = new Thread(new NormalServerTest());
            t2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void cancel() {

    }

    private class NormalServerTest implements Runnable {
        @Override
        public void run() {
            try {
                while (!isConnected()){
                    Socket socket = socketTest.accept();
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(nameRoom);
                    out.close();
                    socket.close();

                }
            } catch (IOException io) {
                Hockey.deviceAPI.showNotification("Failed to create game");
//                MainMenuScreen.debugText = "Failed to create a server:\n " + io.getMessage();
//                callback.onConnectionFailed();
            }finally {
                try {
                    socketTest.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // lang nghe client ket noi toi
    private class NormalServerThread implements Runnable {
        @Override
        public void run() {
            try {
                Hockey.deviceAPI.showProgressDialog("Waiting...");
                socket = serverSocket.accept();
                Hockey.deviceAPI.closeProgressDialog();

                Hockey.deviceAPI.showProgressDialog("Loading...");
                if (!socket.getReuseAddress()) {
                    socket.setReuseAddress(true);
                }
                if (!socket.getTcpNoDelay()) {
                    socket.setTcpNoDelay(true);
                }
                createSocketUDP(socket.getInetAddress());
                Thread t = new Thread(new ReceiveThread());
                t.start();
                gameListener.onConnected();
            } catch (IOException io) {
                Hockey.deviceAPI.showNotification("Failed to create game");
//                MainMenuScreen.debugText = "Failed to create a server:\n " + io.getMessage();
//                callback.onConnectionFailed();
            }finally {
                try {
                    Thread.sleep(2000);
                    Hockey.deviceAPI.closeProgressDialog();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void connectServer(Room room) {

    }

    @Override
    public Boolean isServer() {
        return true;
    }


}