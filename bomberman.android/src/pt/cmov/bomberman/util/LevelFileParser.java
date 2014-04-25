package pt.cmov.bomberman.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import pt.cmov.bomberman.model.Enemy;
import pt.cmov.bomberman.model.GameBoard;
import pt.cmov.bomberman.model.GameBoardClient;
import pt.cmov.bomberman.model.GameBoardServer;
import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.model.Player;
import pt.cmov.bomberman.model.Rock;
import pt.cmov.bomberman.model.Wall;
import pt.cmov.bomberman.presenter.view.MainGamePanel;
import android.content.res.Resources;
import android.util.Log;

public class LevelFileParser {
	private static int width;
	private static int height;
	private static MainGamePanel gPanel;
	
	public static void setDimensions(int w, int h) {
		width = w;
		height = h;
	}
	
	public static void setDoneCallback(MainGamePanel panel) {
		gPanel = panel;
	}
	
	public static void loadLevelFromFile(Resources res, String levelFile) {		
		InputStream levelConf;
		try {
			levelConf = res.getAssets().open(levelFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(levelConf));
			buildLevel(reader, true);
			gPanel.levelParsedCallback();
		}
		catch (IOException e) {
			throw new InternalError("Can't read level input file: " + e.getMessage());
		}
	}
	
	public static void loadLevelFromString(String levelRep) {
		Log.d("LevelFileParser", "Level representation: " + levelRep);
		BufferedReader reader = new BufferedReader(new StringReader(levelRep));
		try {
			buildLevel(reader, false);
			gPanel.levelParsedCallback();
		} catch (IOException e) {
			// Should never happen
			throw new InternalError("Unable to read level representation string.");
		}
	}
	
	private static void buildLevel(BufferedReader reader, boolean isServer) throws IOException {
		int rows, cols;
		int max_players = 0;
		String line;
		GameLevel level = GameLevel.getInstance();
		for (line = reader.readLine(); !line.matches("MAP.*"); line = reader.readLine()) {
			if (line.matches("LN.*")) {
				level.setLevel_name(line.substring(3, line.length()));
			}
			else if (line.matches("GD.*")) {
				level.setDuration(Integer.parseInt(line.substring(3, line.length())));
			}
			else if (line.matches("ET.*")) {
				level.setExplosion_timeout(Integer.parseInt(line.substring(3, line.length())));
			}
			else if (line.matches("ER.*")) {
				level.setExplosion_range(Integer.parseInt(line.substring(3, line.length())));
			}
			else if (line.matches("ED.*")) {
				level.setExplosion_duration(Integer.parseInt(line.substring(3, line.length())));
			}
			else if (line.matches("RS.*")) {
				level.setEnemy_speed(Integer.parseInt(line.substring(3, line.length())));
			}
			else if (line.matches("PR.*")) {
				level.setRobot_score(Integer.parseInt(line.substring(3, line.length())));
			}
			else if (line.matches("PO.*")) {
				level.setOpponent_score(Integer.parseInt(line.substring(3, line.length())));
			}
			else if (line.matches("MP.*")) {
				max_players = Integer.parseInt(line.substring(3, line.length()));
			}
		}
		int i;
		for (i = 4; line.charAt(i) != ','; i++);
		rows = Integer.parseInt(line.substring(4, i));
		cols = Integer.parseInt(line.substring(i+1, line.length()));
		GameBoard board = process_map(rows, cols, reader, max_players, isServer);
		board.setScreenDimensions(width, height);
		level.setBoard(board);
	}
	private static GameBoard process_map(int rows, int cols, BufferedReader reader, int max_players, boolean isServer) throws IOException {
		GameBoard board;
		if (isServer)
			board = new GameBoardServer(rows, cols, max_players);
		else
			board = new GameBoardClient(rows, cols, max_players);
		int row = 0;
		for (String map_entry = reader.readLine(); map_entry != null; map_entry = reader.readLine(), row++) {
			process_map_line(map_entry, row, board);
		}
		return board;
	}
	private static void process_map_line(String line, int row, GameBoard board) {
		int p;
		for (int col = 0; col < line.length(); col++) {
	    	if (line.charAt(col) == '-') {
				board.setPosition(row, col, null);
	    	}
	    	else if (line.charAt(col) == 'R') {
	    		board.setPosition(row, col, new Rock());
	    	}
	    	else if (line.charAt(col) == 'W') {
	    		board.setPosition(row, col, new Wall());
	    	}
	    	else if (line.charAt(col) == 'E') {
	    		Enemy e = new Enemy(row, col);
	    		board.setPosition(row, col, e);
	    		board.addEnemy(e);
	    	}
	    	else {
	    		p = Character.getNumericValue(line.charAt(col));
	    		Player player = new Player(p, row, col);
				board.addPlayer(player);
	    	}
		}
	}
}
