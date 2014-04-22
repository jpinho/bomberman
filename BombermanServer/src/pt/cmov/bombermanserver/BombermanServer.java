package pt.cmov.bombermanserver;

import java.io.IOException;
import java.net.ServerSocket;

public class BombermanServer {
	private static final int PORT_NUMBER = 4889;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Server server = new Server();
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
		} catch (IOException e) {
			System.out.println("Unable to listen on port " + PORT_NUMBER);
			return;
		}
		while (true) {
			try {
				new Thread(new ClientThread(serverSocket.accept(), server)).start();
			} catch (IOException e) {
				System.out.println("Something went wrong when trying to accept a new client.");
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					// Who cares? We're exiting anyway...
				}
			}
		}
	}
}