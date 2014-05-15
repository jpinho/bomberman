package pt.cmov.bomberman.net.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import android.util.Log;

public class WDClientThread implements Runnable {

	private String server_ip;
	private int port;

	public WDClientThread(String server_ip, int port) {
		super();
		this.server_ip = server_ip;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			SimWifiP2pSocket socket = new SimWifiP2pSocket(server_ip, port);
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
			}
			socket.close();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
