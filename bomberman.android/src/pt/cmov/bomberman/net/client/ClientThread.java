package pt.cmov.bomberman.net.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class ClientThread implements Runnable {
	@Override
	public void run() {

		try {
    		Log.d("ClientHost", "Going to connect");
			Socket socket = new Socket("10.0.2.2", 6000);
			Log.d("ClientHost", "Connected to server.");
			socket.close();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
}
