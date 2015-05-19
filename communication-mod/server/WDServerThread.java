package pt.cmov.bomberman.net.server;

import java.io.IOException;

import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.model.Player;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketServer;
import android.util.Log;

public class WDServerThread implements Runnable {
	public static final int SERVER_PORT = 10001;
	private SimWifiP2pSocketServer serverSocket;

	@Override
	public void run() {
		try {
			serverSocket = new SimWifiP2pSocketServer(SERVER_PORT);
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		Log.d("ServerHost", "Initialized new server on port " + SERVER_PORT);
		while (!Thread.currentThread().isInterrupted()) {
			try {
				SimWifiP2pSocket clientSocket = serverSocket.accept();

				int new_player_id;
				if ((new_player_id = GameLevel.getInstance().isJoinable()) != -1) 
				{
					Log.d("ServerHost", "Accepted new player, id = " + new_player_id);
					WDCommunicationThread commThread = new WDCommunicationThread(clientSocket, new_player_id);
					new Thread(commThread).start();
					
					Player newPlayer = GameLevel.getInstance().getBoard().findPlayer(new_player_id);
					Server.getInstance().broadcastPlayerJoined(new_player_id, newPlayer.getX(), newPlayer.getY());
				}
				else {
					/* Level is full */
					Log.d("ServerHost", "Declined new request - no room for more players!");
					clientSocket.close();
				}
			}
			catch (IOException e) {
				// Ignore this client.
				throw new RuntimeException(e.getMessage());
			}
		}
		
		try {
			serverSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
