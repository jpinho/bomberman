package pt.cmov.bomberman.net.client;

import java.io.PrintWriter;

import pt.cmov.bomberman.model.GameBoardClient;
import pt.cmov.bomberman.model.GameLevel;
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
		}
		// TODO add other messages
	}
}
