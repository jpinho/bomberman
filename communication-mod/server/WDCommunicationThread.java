package pt.cmov.bomberman.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import pt.cmov.bomberman.model.GameLevel;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;

public class WDCommunicationThread implements Runnable {
	private SimWifiP2pSocket clientSocket;
	private int player_id;

	public WDCommunicationThread(SimWifiP2pSocket clientSocket, int player_id) {
		this.clientSocket = clientSocket;
		this.player_id = player_id;
	}

	public void run() {
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			RemotePlayer remotePlayer = new RemotePlayer(player_id, out);
			GameLevel.getInstance().getBoard().addNewPlayer(remotePlayer);
			Server.getInstance().sendPlayerId(remotePlayer);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				Server.getInstance().parse_msg(inputLine.split(" "));
			}
			Server.getInstance().delClient(remotePlayer);
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
