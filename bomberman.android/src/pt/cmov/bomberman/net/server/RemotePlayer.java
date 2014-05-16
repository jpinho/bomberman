package pt.cmov.bomberman.net.server;

import java.io.PrintWriter;

public class RemotePlayer {
	private int player_id;
	
	private PrintWriter player_comm;
	
	public RemotePlayer(int player_id, PrintWriter player_comm) {
		super();
		this.player_id = player_id;
		this.player_comm = player_comm;
	}
	
	public void sendMsg(String msg) {
		synchronized (player_comm) {
			player_comm.print(msg);
			player_comm.flush();
		}
	}

	public int getPlayer_id() {
		return player_id;
	}
	public PrintWriter getPlayer_comm() {
		return player_comm;
	}
}
