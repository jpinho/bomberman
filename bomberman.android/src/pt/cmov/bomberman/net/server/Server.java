package pt.cmov.bomberman.net.server;

import java.io.PrintWriter;
import java.util.HashSet;

import android.util.Log;

public class Server {

	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	
	public synchronized static void addNewClient(PrintWriter writer) {
		writers.add(writer);
	}
	
	public synchronized static void delClient(PrintWriter writer) {
		writers.remove(writer);
	}
	
	public synchronized static void notifyMovePlayer(int pid, int dir) {
		Log.d("ServerHost", "New message: " + "MOVE " + pid + " " + dir);
		for (PrintWriter w : writers) {
			w.println("MOVE " + pid + " " + dir);
			w.flush();
		}
	}
	
	public synchronized static void notifyPlaceBomb(int player) {
		Log.d("ServerHost", "New message: " + "BOMB " + player);
		for (PrintWriter w : writers) {
			w.println("BOMB " + player);
			w.flush();
		}
	}
	
	public synchronized static void sendEnemies(String new_positions) {
		/*Log.d("ServerHost", "New message:");
		Log.d("ServerHost", new_positions);*/
		for (PrintWriter w : writers) {
			w.print(new_positions);
			w.flush();
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
