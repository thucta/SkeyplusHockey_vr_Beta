package com.skyplus.hockey.network;

import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.objects.Room;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by TruongNN on 5/5/2017.
 */
public class Client extends GameClient {

    ExecutorService pool;
    List<Room> listRoom;

    public Client(GameListener gameListener, String localAddress, String nameRomm) {
        super(gameListener, localAddress, nameRomm);
        listRoom = new ArrayList<Room>();
    }


    @Override
    public void run() {
        try {
            socket.close();
//            socketUDP.close();

        } catch (Exception e) {
        }

        int bitCount = 254;

        pool = Executors.newFixedThreadPool(bitCount); // tao ra 1 pool voi 254 Thread thuc thi nhiem vu

        String subnet = getLocalSubnet();

        Hockey.deviceAPI.showProgressDialog(gameListener,"Finding player...");
        for (int i = 0; i <= bitCount; i++) {
            Runnable task = new ConnectThread(subnet + i);
            pool.submit(task);
        }

        pool.shutdown();
        while (!pool.isTerminated()) {

        }
        // kiem tra co nguoi choi nao dang tao phong khong
        if (listRoom.size() > 0) {
            Hockey.deviceAPI.showAlertDialog(gameListener, listRoom);
        } else {
            Hockey.deviceAPI.showAlertDialog("Not found player!");
        }
            Hockey.deviceAPI.closeProgressDialog();

    }

    @Override
    public void connectServer(Room room) {
        Thread thread = new Thread(new ConnectServer(room));
        thread.start();

    }
    @Override
    public void disconnect() {

        try {
            socket.close();
        } catch (Exception e) {

        }
        try {
            socketUDP.close();
        } catch (Exception e) {

        }
    }
    @Override
    public void cancel() {
        if (isConnected()) {
            try {
                socket.close();
//                socketUDP.close();
            } catch (IOException io) {

            }
        }

        if (pool != null && pool.isShutdown() && !pool.isTerminated()) pool.shutdownNow();
    }

    private class ConnectThread implements Runnable {

        private String address;

        public ConnectThread(String address) {

            this.address = address;
        }

        public void run() {
            try {
                Socket s = new Socket();
                s.connect(new InetSocketAddress(address, portTest), 2000); // tao mot ket noi timeout la 2s
                DataInputStream in = new DataInputStream(s.getInputStream());
                String nameRoom = in.readUTF();
                listRoom.add(new Room(s.getInetAddress().getHostAddress(), nameRoom));
                in.close();
                s.close();

            } catch (IOException io) {
//                Gdx.app.error("loi o day",io+"");
            }
        }
    }

    private class ConnectServer implements Runnable {
        private Room room;

        public ConnectServer(Room room) {
            this.room = room;
        }

        @Override
        public void run() {
            try {
                Hockey.deviceAPI.showProgressDialog(gameListener,"Loading...");
                try {
                    socket.close();
                } catch (Exception e) {

                }
                // tao ket noi voi server
                socket = new Socket();
                socket.connect(new InetSocketAddress(room.getSocket(),port),3000);

                try {
                    if (!socket.getReuseAddress()) {
                        socket.setReuseAddress(true);
                    }
                    if (!socket.getTcpNoDelay()) {
                        socket.setTcpNoDelay(true); // mac dinh voi cac goi tin nho thi TCP se cho cac goi tin de gop lai, set NoDelay de dam bao goi tin gui di voi toc do nhanh nhat, khong cho
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                createSocketUDP(socket.getInetAddress());
                Thread receiveThread = new Thread(new ReceiveThread());
                receiveThread.start();

                Thread thread = new Thread(new ReceiveTCP());
                thread.start();

                gameListener.onConnected();


            } catch (Exception e) {
//                gameListener.onConnectionFailed();
                Hockey.deviceAPI.showAlertDialog("Join game faild,Game was started!");
            } finally {
                try {
                    Hockey.deviceAPI.closeProgressDialog();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public Boolean isServer() {
        return false;
    }
}
