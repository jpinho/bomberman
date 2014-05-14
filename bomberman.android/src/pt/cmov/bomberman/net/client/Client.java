package pt.cmov.bomberman.net.client;

import java.io.PrintWriter;

import pt.cmov.bomberman.model.GameBoardClient;
import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.model.Player;
import pt.cmov.bomberman.presenter.activity.GameArenaActivity;
import pt.cmov.bomberman.util.LevelFileParser;


public class Client {
	private static Client client = new Client();
	
	private PrintWriter out;

	private Client() { }
	
	public static Client getInstance() { return client; }
	
	public void setClient(PrintWriter out) {
		this.out = out;
	}
	
	public void parse_msg(String[] tokens) {
		if (tokens.length < 1)
			return;
		if (tokens[0].equalsIgnoreCase("board")) {
			LevelFileParser.loadLevelFromString(tokens[1].replace('N', '\n'));
		} else if (tokens[0].equalsIgnoreCase("id")) {
			GameLevel.getInstance().getBoard().setPlayerId(Integer.parseInt(tokens[1]));
		} else if (tokens[0].equalsIgnoreCase("enemy")) {
			((GameBoardClient) GameLevel.getInstance().getBoard()).updateEnemiesPos(tokens);
		} else if (tokens[0].equalsIgnoreCase("move")) {
			GameLevel.getInstance().getBoard().actionMovePlayer(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
		} else if (tokens[0].equalsIgnoreCase("bomb")) {
			((GameBoardClient) GameLevel.getInstance().getBoard()).plantBomb(Integer.parseInt(tokens[1]), 
					                                                         Integer.parseInt(tokens[2]),
					                                                         Integer.parseInt(tokens[3]));
		} else if (tokens[0].equalsIgnoreCase("explode")) {
			((GameBoardClient) GameLevel.getInstance().getBoard()).bombExplosion(tokens);
		} else if (tokens[0].equalsIgnoreCase("clear")) {
			((GameBoardClient) GameLevel.getInstance().getBoard()).bombExplosionEnd(tokens);
		} else if (tokens[0].equalsIgnoreCase("die")) {
			if (tokens[2].equalsIgnoreCase("enemy"))
				killEnemies(tokens);
			else
				killPlayers(tokens);
		} else if (tokens[0].equalsIgnoreCase("score")) {
			Player p = GameLevel.getInstance().getBoard().getPlayer();
			if (p != null)
				p.incrementScore(Integer.parseInt(tokens[1]));
			GameArenaActivity.getInstance().getGameView().getGameStateChangeListener().onStateChange(GameLevel.getInstance());
		} else if (tokens[0].equalsIgnoreCase("join")) {
			GameLevel.getInstance().getBoard().addPlayer(new Player(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
		}
	}
	
	public void sendMoveRequest(int pid, int dir) {
		String msg = "move " + pid + " " + dir + "\n";
		sendMsgToServer(msg);
	}
	
	public void sendBombRequest(int pid) {
		String msg = "bomb " + pid + "\n";
		sendMsgToServer(msg);
	}
	
	private void sendMsgToServer(String msg) {
		synchronized (out) {
			out.print(msg);
			out.flush();
		}
	}
	
	private void killPlayers(String[] tokens) {
		for (int i = 2; i < tokens.length; i++) {
			int pid = Integer.parseInt(tokens[i]);
			GameLevel.getInstance().getBoard().kill(pid, tokens[1]);
		}
	}
	
	private void killEnemies(String[] tokens) {
		for (int i = 3; i < tokens.length; i += 2) {
			((GameBoardClient) GameLevel.getInstance().getBoard()).killEnemy(Integer.parseInt(tokens[i]), Integer.parseInt(tokens[i+1]));
		}
	}
}

