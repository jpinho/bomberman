package pt.cmov.bomberman.net.server;

import java.util.ArrayList;
import java.util.HashSet;

import pt.cmov.bomberman.model.GameLevel;
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
	
	public void parse_msg(String[] tokens) {
		if (tokens[0].equalsIgnoreCase("move"))
			GameLevel.getInstance().getBoard().actionMovePlayer(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
		else if (tokens[0].equalsIgnoreCase("bomb"))
			GameLevel.getInstance().getBoard().actionPlaceBomb(Integer.parseInt(tokens[1]));
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
	
	public void broadcastPlayersKilled(String killMsg) {
		broadcastMsg(killMsg);
	}
	
	public void broadcastBombExploded(int bx, int by, ArrayList<Tuple<Integer, Integer>> firePositions) {
		StringBuilder builder = new StringBuilder();
		builder.append("explode ").append(bx).append(" ").append(by);
		for (Tuple<Integer, Integer> pos : firePositions)
			builder.append(" ").append(pos.x).append(" ").append(pos.y);
		builder.append("\n");
		broadcastMsg(builder.toString());
	}
	
	public void updatePlayerScore(int player, int increment) {
		RemotePlayer playerObj = null;
		
		for (RemotePlayer p : players)
			if (p.getPlayer_id() == player)
				playerObj = p;
		
		if (playerObj != null)
			playerObj.sendMsg("score " + increment + "\n");
	}
	
	public void broadcastClearExplosion(String positions) {
		broadcastMsg(positions);
	}
	
	public void broadcastPlayerJoined(int newPlayer, int x, int y) {
		broadcastMsg("join " + newPlayer + " " + x + " " + y + "\n");
	}
	
	public void broadcastMsg(String msg) {
		synchronized (players) {
			for (RemotePlayer p : players)
				p.sendMsg(msg);
		}
	}
}
