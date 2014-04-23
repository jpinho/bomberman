package pt.cmov.bomberman.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pt.cmov.bomberman.model.GameLevel;

public class ServerThread implements Runnable {
	private static final int SERVER_PORT = 4889;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			return;
		}
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Socket clientSocket = serverSocket.accept();
				int new_player_id;
				if ((new_player_id = GameLevel.getInstance().isJoinable()) != -1) {
					CommunicationThread commThread = new CommunicationThread(clientSocket, new_player_id);
					new Thread(commThread).start();
				} else {
					/* Level is full */
					clientSocket.close();
				}
			} catch (IOException e) {
				// Ignore this client.
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			// Who cares? We're exiting anyway...
		}
	}
}
