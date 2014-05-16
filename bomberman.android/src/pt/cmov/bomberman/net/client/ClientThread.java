package pt.cmov.bomberman.net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import pt.cmov.bomberman.model.GameLevel;
import android.util.Log;

public class ClientThread implements Runnable {

	private String server_ip;
	private int port;

	public ClientThread(String server_ip, int port) {
		super();
		this.server_ip = server_ip;
		this.port = port;
	}

	@Override
	public void run() {
		Log.d("thread", "socket running");
		try {
			Socket socket = new Socket(server_ip, port);
			Log.d("ClientHost", "Connected to server.");
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			Client.getInstance().setClient(out);
			// The first message coming from the server is always the current
			// state of the board
			String line;
			while ((line = in.readLine()) != null) {
				Client.getInstance().parse_msg(line.split(" "));
				
				if(GameLevel.getInstance().isGameOver())
					break;
			}

			socket.close();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
