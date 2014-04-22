package pt.cmov.bombermanserver;

import java.util.HashMap;

public class Server {
	HashMap<String, Game> games;
	
	public void create_game(String name, int currentLevel, int levelRows, int levelCols, int max_players) {
		if (games.containsKey(name))
			return;
		games.put(name, new Game(name, 0, currentLevel, levelRows, levelCols, max_players));
	}
	
	public void destroy_game(String name) {
		
	}
	
	public int join_game(String name) {
		if (!games.containsKey(name))
			return -1;
		
	}
	
	public void leaveGame(String name, int player) {
		
	}
}
