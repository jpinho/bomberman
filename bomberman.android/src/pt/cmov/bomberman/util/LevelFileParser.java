package pt.cmov.bomberman.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import pt.cmov.bomberman.model.Bomb;
import pt.cmov.bomberman.model.BombFire;
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
				level.setTimeLeft(Integer.parseInt(line.substring(3, line.length())));
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
		ArrayList<BFDescriptor> bombAndFireToShow = new ArrayList<BFDescriptor>();
		if (isServer)
			board = new GameBoardServer(rows, cols, max_players);
		else
			board = new GameBoardClient(rows, cols, max_players);
		int row = 0;
		for (String map_entry = reader.readLine(); map_entry != null; map_entry = reader.readLine(), row++) {
			bombAndFireToShow.addAll(process_map_line(map_entry, row, board));
		}
		for (BFDescriptor entity : bombAndFireToShow) {
			entity.addToBoard(board);
		}
		return board;
	}
	private static ArrayList<BFDescriptor> process_map_line(String line, int row, GameBoard board) {
		int p;
		ArrayList<BFDescriptor> bombAndFireToShow = new ArrayList<BFDescriptor>();
		for (int col = 0, y = 0; col < line.length(); col++, y++) {
			char c = line.charAt(col);
	    	if (c == '-') {
				board.setPosition(row, y, null);
	    	}
	    	else if (c == 'R') {
	    		board.setPosition(row, y, new Rock());
	    	}
	    	else if (c == 'W') {
	    		board.setPosition(row, y, new Wall());
	    	}
	    	else if (c == 'E') {
	    		Enemy e = new Enemy(row, y);
	    		board.setPosition(row, y, e);
	    		board.addEnemy(e);
	    	} else if (Character.isDigit(c)) {
	    		p = Character.getNumericValue(c);
	    		Player player = new Player(p, row, y);
				board.addPlayer(player);
				Log.d("LevelFileParser", "Added player " + p + " at (" + row + "," + y + ")");
	    	}
	    	// These are not part of the map file, but can be sent by a server to a new client
	    	else if (c == 'B') {
	    		int bombAuthor = Character.getNumericValue(line.charAt(y+1));
	    		bombAndFireToShow.add(new BombDescriptor(row, y, bombAuthor));
	    		col++;
	    	} else if (c == 'F') {
	    		int fireAuthor = Character.getNumericValue(line.charAt(y+1));
	    		bombAndFireToShow.add(new FireDescriptor(row, y, fireAuthor));
	    		col++;
	    	}
		}
		return bombAndFireToShow;
	}
}

 class BFDescriptor {
	protected int x;
	protected int y;
	protected int player;
	public BFDescriptor(int x, int y, int player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}
	public void addToBoard(GameBoard board) { }
 }
 
 class BombDescriptor extends BFDescriptor {
	 public BombDescriptor(int x, int y, int player) {
		super(x, y, player);
	}
	@Override
	 public void addToBoard(GameBoard board) {
		 board.setPosition(x, y, new Bomb(x, y, board.findPlayer(player)));
	 }
 }
 
 class FireDescriptor extends BFDescriptor {
	 public FireDescriptor(int x, int y, int player) {
		super(x, y, player);
	}
	@Override
	 public void addToBoard(GameBoard board) {
		 board.setPosition(x, y, new BombFire(board.findPlayer(player)));
	 }
 }

