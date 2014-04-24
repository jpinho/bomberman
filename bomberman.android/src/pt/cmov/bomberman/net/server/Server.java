package pt.cmov.bomberman.net.server;

import java.util.HashSet;

import pt.cmov.bomberman.model.Bomb;
import pt.cmov.bomberman.model.Player;
import android.util.Log;

public class Server {
	// The list of clients
	private HashSet<RemotePlayer> players = new HashSet<RemotePlayer>();
	
	private static Server server = new Server();

	private Server() { }
	
	public static Server getInstance() { return server; }
	
	public void addNewClient(RemotePlayer p) {
		players.add(p);
	}
	
	public void delClient(RemotePlayer p) {
		players.remove(p);
	}
	
	public String parse_msg(String[] tokens) {
		// TODO Implement
		return null;
	}
	
	public void broadcastPlayerMoved(Player player) {
		String message = "MOVE " + player.getPlayer_number() + " " + player.getX() + " " + player.getY();
		broadcastMsg(player, message);
	}
	
	public void broadcastPlayerPlantedBomb(Player player, Bomb bomb) {
		String message = "BOMB " + player.getPlayer_number() + " " + bomb.getX() + " " + bomb.getY();
		broadcastMsg(player, message);
	}
	
	private void broadcastMsg(Player from, String msg) {
		Log.d("ServerHost", "Sending new message:");
		Log.d("ServerHost", msg);
		synchronized (players) {
			for (RemotePlayer p : players)
				if (p.getPlayer_id() != from.getPlayer_number())
					p.sendMsg(msg);
		}
	}
}