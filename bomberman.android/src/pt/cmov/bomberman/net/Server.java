package pt.cmov.bomberman.net;

import java.io.PrintWriter;
import java.util.HashSet;

public class Server {

	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	
	public synchronized static void addNewClient(PrintWriter writer) {
		writers.add(writer);
	}
	
	public synchronized static void delClient(PrintWriter writer) {
		writers.remove(writer);
	}
	
	public synchronized static void notifyMovePlayer(int pid, int dir) {
		for (PrintWriter w : writers) {
			w.println("MOVE " + pid + " " + dir);
		}
	}
	
	public synchronized static void notifyPlaceBomb(int player) {
		for (PrintWriter w : writers) {
			w.println("BOMB " + player);
		}
	}
	
	public static String parse_msg(String[] msg) {
		if (msg.length < 1)
			return null;
		if (msg[0].equalsIgnoreCase("server"))
			return parse_msg_to_server(msg);
		else if (msg[0].equalsIgnoreCase("game"))
			return parse_msg_to_game(msg);
		return null;
	}

	private static String parse_msg_to_server(String[] msg) {
		return "";
	}

	private static String parse_msg_to_game(String[] msg) {
		return "";
	}
}
