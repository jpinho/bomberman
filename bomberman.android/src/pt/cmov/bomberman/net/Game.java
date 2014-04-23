package pt.cmov.bomberman.net;

public class Game {

	private String roomName;
	private int players;
	private int max_players;
	private int currentLevel;
	private int owner;
	private char pos_map[][];
	private int rows;
	private int cols;

	public Game(String name, int owner, int currentLevel, int levelRows,
			int levelCols, int max_players) {
		roomName = name;
		players = 0;
		this.max_players = max_players;
		this.currentLevel = currentLevel;
		this.owner = owner;
		pos_map = new char[levelRows][levelCols];
		rows = levelRows;
		cols = levelCols;
	}

}
