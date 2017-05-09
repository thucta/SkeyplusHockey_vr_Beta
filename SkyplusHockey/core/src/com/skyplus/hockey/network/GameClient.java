package com.skyplus.hockey.network;

import com.badlogic.gdx.Gdx;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.config.Messsage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Created by TruongNN on 5/5/2017.
 */
abstract public class GameClient implements GameClientInterface {

    protected static Socket socket;
    protected static DatagramSocket socketUDP;
    protected String localAddress;
    protected InetAddress inetOtherPlayer;
    protected GameListener gameListener;
    protected String nameRoom;

    public GameClient(GameListener gameListener, String localAddress, String nameRoom) {

        this.localAddress = localAddress;
        this.gameListener = gameListener;
        this.nameRoom = nameRoom;
    }

    @Override
    abstract public void run();

    @Override
    public void setListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    @Override
    abstract public void cancel();

    public String getLocalSubnet() {
        String[] bytes = localAddress.split("\\.");
        return bytes[0] + "." + bytes[1] + "." + bytes[2] + ".";
    }

    @Override
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    @Override
    public void onConnected() {
        gameListener.onConnected();
    }



    public void createSocketUDP(InetAddress inetAddress) {
        try {
            SocketAddress socketAddress = new InetSocketAddress(localAddress, portUDP);
            socketUDP = new DatagramSocket(socketAddress);
            inetOtherPlayer = inetAddress;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    protected class ReceiveThread implements Runnable {
        public void run() {
            Gdx.app.log("Receiving", "Message " + socket.getPort() + " " + socket.getInetAddress());
            while (isConnected()) {
                try {
                    String message = receiveStringMessage(socketUDP);
                    gameListener.onMessageReceived(message);
                } catch (Exception io) {

                }
            }
            Gdx.app.error("Closed", "Khong co nghe");
            disconnect();
        }
    }


    protected class ReceiveTCP implements Runnable {
        public void run() {
            while (isConnected()) {
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    String message = in.readUTF();
                    handleMessageTCP(message);

                } catch (Exception io) {

                    gameListener.onDisconnected();

                    Gdx.app.log("Hockey", "Disconnected");
                    disconnect();
                }
            }
            disconnect();
        }
    }

    private void handleMessageTCP(String message){
        JSONObject object = new JSONObject(message);
        String op = object.getString(Messsage.OP_MESSAGE);
        if(Messsage.PAUSE.equals(op)){
            Hockey.deviceAPI.showProgressDialog(gameListener,"Pause...");
        }else if(Messsage.RESUME.equals(op)){
            Hockey.deviceAPI.closeProgressDialog();
        }


    }

    @Override
    public void sendMessageTCP(String message) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeUTF(message);
            oos.flush();
        } catch (IOException io) {

        }
    }
    @Override
    public void sendMessageTCP(Socket socket, String message) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeUTF(message);
            oos.flush();
        } catch (IOException io) {

        }
    }


    @Override
    public synchronized void sendMessageUDP(String message) {
        try {

            byte[] m = new byte[256];
            m = message.getBytes();
            DatagramPacket packet = new DatagramPacket(m, m.length, inetOtherPlayer, portUDP);
            socketUDP.send(packet);

        } catch (Exception e) {

        }

    }

    public DatagramPacket receiveMessage(DatagramSocket socket) {     // -----------------------------------readPacketDatagram
        byte[] m = new byte[256];
        DatagramPacket p = new DatagramPacket(m, m.length);
        try {
            socket.receive(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public String receiveStringMessage(DatagramSocket socket) {     // -----------------------------------readPacketDatagram
        byte[] m = new byte[256];
        DatagramPacket p = new DatagramPacket(m, m.length);
        try {
            socket.receive(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(p.getData()).trim();
    }


}