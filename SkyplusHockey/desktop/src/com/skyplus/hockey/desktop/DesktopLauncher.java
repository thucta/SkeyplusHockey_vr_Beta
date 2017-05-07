package com.skyplus.hockey.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.network.GameListener;
import com.skyplus.hockey.objects.DeviceAPI;
import com.skyplus.hockey.objects.Room;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by TruongNN  on 3/24/2017.
 */
public class DesktopLauncher implements DeviceAPI{

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		DesktopLauncher desktopLauncher = new DesktopLauncher();
		new LwjglApplication(new Hockey(desktopLauncher), config);
	}
	public   void newGame(){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		DesktopLauncher desktopLauncher = new DesktopLauncher();
		new LwjglApplication(new Hockey(desktopLauncher), config);
	}
	@Override
	public String getIpAddress() {

		return "192.168.0.103";
	}

	@Override
	public boolean isConnectedToLocalNetwork() {
		return true;
	}

	@Override
	public void showNotification(String message) {

	}

	@Override
	public void showAlertDialog(GameListener gameListener, List<Room> rooms) {

	}

	@Override
	public void showAlertDialog(String message) {

	}


	@Override
	public void showProgressDialog(String message) {

	}

	@Override
	public void closeProgressDialog() {

	}

	@Override
	public boolean progressDialogIsShow() {
		return false;
	}


	@Override
	public void showInputDialog(GameListener gameListener) {
//		return null;
        gameListener.createServer("Sas");
	}


	@Override
	public void startRecording() {

	}

	@Override
	public void transmit(byte[] message) {

	}

	@Override
	public int getBufferSize() {
		return 0;
	}
}
