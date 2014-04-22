package pt.cmov.bombermanserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {

	private Socket socket;
	private Server server;

	public ClientThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				out.println(parse_msg(inputLine.split(" ")));
				out.flush();
			}
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String parse_msg(String[] msg) {
		if (msg.length < 1)
			return "";
		if (msg[0].equalsIgnoreCase("server"))
			return parse_msg_to_server(msg);
		else if (msg[0].equalsIgnoreCase("game"))
			return parse_msg_to_game(msg);
		else
			return "";
	}

	private String parse_msg_to_server(String[] msg) {
		if (msg.length < 2)
			return "";
		if (msg[1].equalsIgnoreCase("create") && msg.length >= 7) {
			/* name level rows cols max_players */
			server.create_game(msg[2], Integer.parseInt(msg[3]),
					Integer.parseInt(msg[4]), Integer.parseInt(msg[5]),
					Integer.parseInt(msg[6]));
		} else if (msg[1].equalsIgnoreCase("join") && msg.length >= 3) {

		} else if (msg[1].equalsIgnoreCase("destroy") && msg.length >= 3) {

		} else if (msg[1].equalsIgnoreCase("leave") && msg.length >= 4) {
			
		}
		return "";
	}

	private String parse_msg_to_game(String[] msg) {

	}
}