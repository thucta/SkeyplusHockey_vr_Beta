package com.skyplus.hockey;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.skyplus.hockey.network.GameListener;
import com.skyplus.hockey.objects.DeviceAPI;
import com.skyplus.hockey.objects.Room;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by TruongNN  on 3/24/2017.
 */
public class AndroidLauncher extends AndroidApplication implements DeviceAPI {

    private int minBufferSize = 0;
    private final int maxBufferSize = 4096;
    private ProgressDialog progressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        boolean s = isConnectedToLocalNetwork();
        initialize(new Hockey(this), config);
    }

    // get ip cua thiet bi
    @Override
    public String getIpAddress() {
        WifiManager wifiMan = (WifiManager) getContext().getSystemService(getContext().WIFI_SERVICE);
        int ip = wifiMan.getConnectionInfo().getIpAddress();

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ip = Integer.reverseBytes(ip);
        }

        byte[] ipByteArray = BigInteger.valueOf(ip).toByteArray();

        String ipAddress;
        try {
            ipAddress = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException e) {
            ipAddress = "Unable to get host address";
        }

        return ipAddress;
    }
    @Override
    public void closeProgressDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog.hide();
                    }
                    progressDialog = null;
                }
            }
        });

    }

    @Override
    public boolean progressDialogIsShow() {
        return progressDialog!=null;
    }

    @Override
    public void showProgressDialog(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressDialog == null){
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage(message);
                }
                progressDialog.show();
            }
        });

    }
    // kiem tra thiet bi co ket noi mang chua
    @Override
    public boolean isConnectedToLocalNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    @Override
    public void showNotification(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showAlertDialog(final GameListener listener,final List<Room> listRoom) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ArrayAdapter<Room> adapter = new ArrayAdapter<Room>(getContext(),android.R.layout.select_dialog_singlechoice,listRoom);
                builder.setTitle(Hockey.TITLE);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Room room = listRoom.get(which);
                                listener.connectServer(room);
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public void showAlertDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(Hockey.TITLE);
                builder.setMessage(message);
                builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public void showInputDialog(final GameListener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(Hockey.TITLE);

                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService((Context.LAYOUT_INFLATER_SERVICE));
                View viewInflated = layoutInflater.inflate(R.layout.input_layout, null);

                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String text = input.getText().toString();
                        if (!"".equals(text)) {
                            listener.createServer(text);
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }


    @Override
    public void startRecording() {

    }

    @Override
    public void transmit(byte[] message) {

    }

    @Override
    public int getBufferSize() {
        return Math.max(minBufferSize, maxBufferSize);
    }


}
