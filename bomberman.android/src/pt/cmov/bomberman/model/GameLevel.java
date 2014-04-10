package pt.cmov.bomberman.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.DONUT)
public class GameLevel {	
	private GameBoard board;
	private int max_players;
	private int duration;
	private String level_name;
	private int explosion_timeout;
	private int explosion_range;
	private int explosion_duration;
	private int enemy_speed;
	private int robot_score;
	private int opponent_score;
	
	public void loadLevel(Resources res, String levelFile, int view_width, int view_height) {		
		InputStream levelConf;
		try {
			levelConf = res.getAssets().open(levelFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(levelConf));
			boolean reading_map = false;
			int rows, cols;
			int row, col;
			row = col = 0;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (reading_map) {
					int p;
					for (int i = 0; i < line.length(); i++) {
		            	if (line.charAt(i) == '-') {
		        			board.setPosition(row, col++, null);
		            	}
		            	else if (line.charAt(i) == 'R') {
		            		board.setPosition(row, col++, new Rock());
		            	}
		            	else if (line.charAt(i) == 'W') {
		            		board.setPosition(row, col++, new Wall());
		            	}
		            	else if (1 <= (p = Character.getNumericValue(line.charAt(i))) && p <= max_players) {
		        			board.setPosition(row, col++, new Player(p));
		            	}
					}
					col = 0;
					row++;
				}
				else {
					if (line.matches("LN.*")) {
						level_name = line.substring(3, line.length());
					}
					else if (line.matches("GD.*")) {
						duration = Integer.parseInt(line.substring(3, line.length()));
					}
					else if (line.matches("ET.*")) {
						explosion_timeout = Integer.parseInt(line.substring(3, line.length()));
					}
					else if (line.matches("ER.*")) {
						explosion_range = Integer.parseInt(line.substring(3, line.length()));
					}
					else if (line.matches("ED.*")) {
						explosion_duration = Integer.parseInt(line.substring(3, line.length()));
					}
					else if (line.matches("RS.*")) {
						enemy_speed = Integer.parseInt(line.substring(3, line.length()));
					}
					else if (line.matches("PR.*")) {
						robot_score = Integer.parseInt(line.substring(3, line.length()));
					}
					else if (line.matches("PO.*")) {
						opponent_score = Integer.parseInt(line.substring(3, line.length()));
					}
					else if (line.matches("MP.*")) {
						max_players = Integer.parseInt(line.substring(3, line.length()));
					}
					else if (line.matches("MAP.*")) {
						int i;
						for (i = 4; line.charAt(i) != ','; i++);
						rows = Integer.parseInt(line.substring(4, i));
						cols = Integer.parseInt(line.substring(i+1, line.length()));
						board = new GameBoard(rows, cols);
						reading_map = true;
					}
				}
			}
			board.setScreenDimensions(view_width, view_height);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(Canvas canvas) {
		board.draw(canvas);
	}
}
