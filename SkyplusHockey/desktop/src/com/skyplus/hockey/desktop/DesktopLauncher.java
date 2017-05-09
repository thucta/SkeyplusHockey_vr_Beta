package com.skyplus.hockey.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.skyplus.hockey.Hockey;
import com.skyplus.hockey.network.GameListener;
import com.skyplus.hockey.objects.DeviceAPI;
import com.skyplus.hockey.objects.Room;

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

		return "192.168.43.28";
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
	public void showAlertDialogDisconnected(GameListener gameListener, String message) {

	}

	@Override
	public void showAlertDialogExitGame(GameListener gameListener, String message) {

	}

	@Override
	public void showProgressDialog(GameListener gameListener, String message) {

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
	public void vibRate(int miliSecond) {

	}


}
