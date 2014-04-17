package pt.cmov.bomberman.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pt.cmov.bomberman.model.Enemy;
import pt.cmov.bomberman.model.GameBoard;
import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.model.Player;
import pt.cmov.bomberman.model.Rock;
import pt.cmov.bomberman.model.Wall;
import android.content.res.Resources;

public class LevelFileParser {
	public static void loadLevel(Resources res, String levelFile, int view_width, int view_height, GameLevel level) {		
		InputStream levelConf;
		try {
			levelConf = res.getAssets().open(levelFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(levelConf));
			buildLevel(level, view_width, view_height, reader);
		}
		catch (IOException e) {
			throw new InternalError("Can't read level input file: " + e.getMessage());
		}
	}
	
	private static void buildLevel(GameLevel level, int view_width, int view_height, BufferedReader reader) throws IOException {
		int rows, cols;
		int max_players = 0;
		String line;
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
		GameBoard board = process_map(level, view_width, view_height, rows, cols, reader, max_players);
		board.setScreenDimensions(view_width, view_height);
		level.setBoard(board);
		board.notifyFinishedParse();
	}
	private static GameBoard process_map(GameLevel level, int view_width, int view_height, int rows, int cols, BufferedReader reader, int max_players) throws IOException {
		GameBoard board = new GameBoard(rows, cols, max_players);
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
				board.setPosition(row, col, player);
				board.addPlayer(player);
	    	}
		}
	}
}
