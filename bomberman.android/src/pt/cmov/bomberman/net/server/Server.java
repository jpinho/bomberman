package pt.cmov.bomberman.net.server;

import java.util.ArrayList;
import java.util.HashSet;

import pt.cmov.bomberman.util.Tuple;

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
	
	public void broadcastPlayerPlantedBomb(int player, int x, int y) {
		String message = "bomb " + player + " " + x + " " + y + "\n";
		broadcastMsg(message);
	}
	
	public void broadcastEnemiesPositions(String positions) {
		broadcastMsg(positions);
	}
	
	public void sendPlayerId(RemotePlayer p) {
		p.sendMsg("id " + p.getPlayer_id() + "\n");
	}
	
	public void broadcastBombExploded(int bx, int by, ArrayList<Tuple<Integer, Integer>> firePositions) {
		StringBuilder builder = new StringBuilder();
		builder.append("explode ").append(bx).append(" ").append(by);
		for (Tuple<Integer, Integer> pos : firePositions)
			builder.append(" ").append(pos.x).append(" ").append(pos.y);
		builder.append("\n");
		broadcastMsg(builder.toString());
	}
	
	public void broadcastClearExplosion(String positions) {
		broadcastMsg(positions);
	}
	
	private void broadcastMsg(String msg) {
		synchronized (players) {
			for (RemotePlayer p : players)
				p.sendMsg(msg);
		}
	}
}
