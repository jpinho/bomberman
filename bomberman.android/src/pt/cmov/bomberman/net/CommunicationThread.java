package pt.cmov.bomberman.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread implements Runnable {
        private Socket clientSocket;
        private int player_id;
        public CommunicationThread(Socket clientSocket, int player_id) {
            this.clientSocket = clientSocket;
            this.player_id = player_id;
        }
        public void run() {
    		try {
    			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    			BufferedReader in = new BufferedReader(new InputStreamReader(
    					clientSocket.getInputStream()));
    			Server.addNewClient(out);
    			String inputLine;
    			while ((inputLine = in.readLine()) != null) {
    				String reply;
    				if ((reply = Server.parse_msg(inputLine.split(" "))) != null) {
    					out.println(reply);
    					out.flush();
    				}
    			}
    			Server.delClient(out);
    			clientSocket.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
    }
