package pt.cmov.bomberman.net.server;

import java.util.HashSet;

import pt.cmov.bomberman.model.Bomb;
import pt.cmov.bomberman.model.Player;

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
	
	public void broadcastPlayerMoved(int pid, int dir) {
		String message = "move " + pid + " " + dir + "\n";
		broadcastMsg(message);
	}
	
	public void broadcastPlayerPlantedBomb(Player player, int x, int y) {
		String message = "bomb " + player.getPlayer_number() + " " + x + " " + y + "\n";
		broadcastMsg(message);
	}
	
	public void broadcastEnemiesPositions(String positions) {
		broadcastMsg(positions);
	}
	
	public void sendPlayerId(RemotePlayer p) {
		p.sendMsg("id " + p.getPlayer_id() + "\n");
	}
	
	private void broadcastMsg(String msg) {
		//Log.d("ServerHost", "Sending new message:");
		//Log.d("ServerHost", msg);
		synchronized (players) {
			for (RemotePlayer p : players)
				p.sendMsg(msg);
		}
	}
}
