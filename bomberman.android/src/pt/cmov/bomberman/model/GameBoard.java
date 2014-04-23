package pt.cmov.bomberman.model;

import java.util.ArrayList;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Bitmaps;
import pt.cmov.bomberman.util.Misc;
import pt.cmov.bomberman.util.Tuple;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameBoard {
	/*
	 * horizontalExcess and verticalExcess are the top and left offsets of the
	 * map location in pixels.
	 */
	private double horizontalExcess;
	private double verticalExcess;
	/* Object width and height in pixels */
	private int object_width;
	private int object_height;

	private GameObject[][] board; // Stores game objects in world coordinates
	private ArrayList<Player> players;
	private ArrayList<Enemy> enemies;
	private int enemies_timer_interval;
	/* The board's dimensions */
	private final int nCols;
	private final int nRows;
	
	private int max_players;
	private int current_players;

	private final Pavement pavement;

	public GameBoard(int rows, int cols, int max_players) {
		board = new GameObject[rows][cols];
		players = new ArrayList<Player>(max_players);
		enemies = new ArrayList<Enemy>();
		pavement = new Pavement();
		nRows = rows;
		nCols = cols;
		this.max_players = max_players;
		current_players = 1;
	}

	public void setPosition(int x, int y, GameObject item) {
		board[x][y] = item;
	}

	public void addPlayer(Player p) {
		players.add(p);
	}
	
	public void addEnemy(Enemy e) {
		enemies.add(e);
	}
	
	public int playerJoined() {
		// TODO Implement
		return ++current_players;
	}
	
	public int getCurrent_players() { return current_players; }

	public void actionMovePlayer(int pid, int dir) {
		Player p;
		if ((p = findPlayer(pid)) == null) {
			return;
		}
		int v_x, v_y;
		v_x = v_y = 0;
		switch (dir) {
		case JoyStick.STICK_DOWN:
			v_x = 1;
			break;
		case JoyStick.STICK_UP:
			v_x = -1;
			break;
		case JoyStick.STICK_LEFT:
			v_y = -1;
			break;
		case JoyStick.STICK_RIGHT:
			v_y = 1;
			break;
		}
		if (isValidMove(p.getX(), p.getY(), v_x, v_y)) {
			int new_x = p.getX() + v_x;
			int new_y = p.getY() + v_y;
			board[p.getX()][p.getY()] = null;
			board[new_x][new_y] = p;
			p.setPosition(new_x, new_y);
		}
	}
	
	public void placeBomb(int player) {
		Player p;
		if ((p = findPlayer(player)) == null)
			return;
		final int bomb_x = p.getX() + p.getV_x();
		final int bomb_y = p.getY() + p.getV_y();
		if (validPosition(bomb_x, bomb_y)) {
			final Bomb b = new Bomb(bomb_x, bomb_y);
			board[bomb_x][bomb_y] = b;
			Handler bhandler = new Handler();
			bhandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					bombExploded(b);
				}
			}, GameLevel.getInstance().getExplosion_timeout()*1000);
		}
	}

	private void bombExploded(Bomb b) {
		board[b.getX()][b.getY()] = new BombFire();
		final ArrayList<Tuple<Integer, Integer>> pos_to_clear;
		int range = GameLevel.getInstance().getExplosion_range();
		pos_to_clear = propagateFire(b.getX(), b.getY(), range, 1, 0); // Goes down
		pos_to_clear.addAll(propagateFire(b.getX(), b.getY(), range, -1, 0)); // Goes up
		pos_to_clear.addAll(propagateFire(b.getX(), b.getY(), range, 0, 1)); // Goes to the right
		pos_to_clear.addAll(propagateFire(b.getX(), b.getY(), range, 0, -1)); //Goes to the left
		pos_to_clear.add(new Tuple<Integer, Integer>(b.getX(), b.getY()));
		Handler endExplosion = new Handler();
		endExplosion.postDelayed(new Runnable() {
			@Override
			public void run() {
				clearFire(pos_to_clear);					
			}
		}, GameLevel.getInstance().getExplosion_duration()*1000);
	}
	
	private void clearFire(ArrayList<Tuple<Integer, Integer>> lst) {
		for (Tuple<Integer, Integer> t : lst)
			board[t.x][t.y] = null;
	}
	
	private ArrayList<Tuple<Integer, Integer>> propagateFire(int x, int y, int range, int x_step, int y_step) {
		ArrayList<Tuple<Integer, Integer>> positions = new ArrayList<Tuple<Integer, Integer>>();
		boolean hit = false;
		while (!hit && inBoard(x += x_step, y += y_step) && range-- > 0) {
			if (board[x][y] == null || board[x][y].notifyExplosion()) {
				if (board[x][y] != null)
					hit = true;
				board[x][y] = new BombFire();
				positions.add(new Tuple<Integer, Integer>(x, y));
			}
			else
				hit = true;
		}
		return positions;
	}
	
	private Player findPlayer(int id) {
		int i;
		for (i = 0; i < players.size() && players.get(i).getPlayer_number() != id; i++);
		return i < players.size() ? players.get(i) : null;
	}

	private boolean isValidMove(int x_from, int y_from, int v_x, int v_y) {
		int new_x = x_from + v_x;
		int new_y = y_from + v_y;
		return validPosition(new_x, new_y);
	}

	private boolean validPosition(int x, int y) {
		return inBoard(x, y) && (board[x][y] == null || !board[x][y].isSolid());
	}
	
	private boolean inBoard(int x, int y) {
		return 0 <= x && x < nRows && 0 <= y && y < nCols;
	}
	
	private void moveEnemies() {
		Log.d("LevelFileParser", "In moveEnemies()");
		for (Enemy e : enemies) {
			Tuple<Integer, Integer> new_pos = chooseNextPosition(e.getX(), e.getY());
			if (new_pos != null) {
				Log.d("LevelFileParser", "New pos = " + new_pos.x + "," + new_pos.y);
				board[e.getX()][e.getY()] = null;
				board[new_pos.x][new_pos.y]= e;
				e.setPosition(new_pos.x, new_pos.y);
			}
			else { Log.d("LevelFileParser", "new_pos = NULL"); }
		}
		Handler enemiesHandler = new Handler();
		enemiesHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				moveEnemies();
			}
		}, enemies_timer_interval);
	}
	
	private Tuple<Integer, Integer> chooseNextPosition(int e_x, int e_y) {
		int total_attempts = 0;
		int directions[] = { -1, 0, 1, 0, 0, -1, 0, 1 };
		                  /* | Up | Down | Left | Right */ 
		boolean directions_tried[] = { false, false, false, false };
		                             /*  Up    Down   Left   Right */
		int direction;
		int x;
		int y;
		do {
			direction = Misc.randInt(0, directions_tried.length-1);
			if (directions_tried[direction] == false) {
				directions_tried[direction] = true;
				total_attempts++;
			}
			x = directions[direction*2]+e_x;
			y = directions[direction*2+1]+e_y;
		} while (total_attempts < directions_tried.length && !validPosition(x, y));
		
		return validPosition(x, y) ? new Tuple<Integer, Integer>(x, y) : null;
	}

	/*
	 * This is called by the level parser routine when it is reading a
	 * configuration file and finds the map's dimensions. This function will
	 * decide how to scale an object such that a map with nRows rows and nCols
	 * columns fits in an XxY screen. To do so, it computes the dimensions (in
	 * pixels) that each object will have, and then it calculates how much space
	 * is empty. Empty space is filled with a border image (rocks)
	 */
	public void setScreenDimensions(int width, int height) {
		int max_object_width = (int) Math.floor((width * 1.0) / nCols);
		int max_object_height = (int) Math.floor((height * 1.0) / nRows);

		/*
		 * delta is the amount (in pixels) of increment or decrement on each
		 * side of the object. If the object fits into the space defined by
		 * [max_object_width,max_object_height], then delta is positive, because
		 * we can zoom the object until one of its sides hits the limit.
		 * Otherwise, delta is negative, because we must resize the object until
		 * all of its sides fit into the space. The use of delta ensures that
		 * the aspect ratio of the image file is kept.
		 */
		int delta;
		if (fitsIn(max_object_width, max_object_height, Bitmaps.ORIGINAL_WIDTH,
				Bitmaps.ORIGINAL_HEIGHT)) {
			delta = Math.min(max_object_width - Bitmaps.ORIGINAL_WIDTH,
					max_object_height - Bitmaps.ORIGINAL_HEIGHT);
		} else {
			delta = -Math.max(Bitmaps.ORIGINAL_WIDTH - max_object_width,
					Bitmaps.ORIGINAL_HEIGHT - max_object_height);
		}
		object_width = Bitmaps.ORIGINAL_WIDTH + delta;
		object_height = Bitmaps.ORIGINAL_HEIGHT + delta;
		horizontalExcess = (width * 1.0 - object_width * nCols) / 2;
		verticalExcess = (height * 1.0 - object_height * nRows) / 2;
		// Tell Bitmaps class how to resize images
		Bitmaps.setWidth(object_width);
		Bitmaps.setHeight(object_height);
	}

	public int getColumnsCount() {
		return nCols;
	}

	public int getRowsCount() {
		return nRows;
	}

	public void draw(Canvas canvas) {
		drawBorders(canvas);
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				double x = horizontalExcess + object_width * j;
				double y = verticalExcess + object_height * i;
				if (board[i][j] != null)
					board[i][j].draw(canvas, (float) x, (float) y);
				else
					pavement.draw(canvas, (float) x, (float) y);
			}
		}
	}

	private void drawBorders(Canvas canvas) {
		Bitmap border = Bitmaps.getBitmap(R.drawable.rock);

		float y;
		// Top
		y = (float) verticalExcess;
		do {
			y -= object_height;
			drawLeftRightBorder(canvas, border, y);
			for (float x = (float) horizontalExcess; x < horizontalExcess
					+ nCols * object_width; x += object_width) {
				canvas.drawBitmap(border, x, y, null);
			}
		} while (y >= 0);

		// Left and Right
		for (y = (float) verticalExcess; y <= verticalExcess + (nRows - 1)
				* object_height; y += object_height)
			drawLeftRightBorder(canvas, border, y);

		// Bottom
		y = (float) verticalExcess + (nRows - 1) * object_height;
		float y_limit = (float) verticalExcess * 2 + nRows * object_height;
		do {
			y += object_height;
			drawLeftRightBorder(canvas, border, y);
			for (float x = (float) horizontalExcess; x < horizontalExcess
					+ nCols * object_width; x += object_width) {
				canvas.drawBitmap(border, x, y, null);
			}
		} while (y <= y_limit);
	}

	// Draws borders on left and right sides of the screen that are outside of
	// the map zone.
	private void drawLeftRightBorder(Canvas canvas, Bitmap border, float y) {
		float x = (float) horizontalExcess;
		do {
			x -= object_width;
			canvas.drawBitmap(border, x, y, null);
		} while (x >= 0);
		x = (float) horizontalExcess + (nCols - 1) * object_width;
		float x_limit = (float) horizontalExcess * 2 + nCols * object_width;
		do {
			x += object_width;
			canvas.drawBitmap(border, x, y, null);
		} while (x <= x_limit);
	}

	private boolean fitsIn(int max_width, int max_height, int object_w,
			int object_h) {
		return object_w <= max_width && object_h <= max_height;
	}
	
	public void notifyFinishedParse() {
		Handler enemiesHandler = new Handler();
		enemiesHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				moveEnemies();
			}
		}, enemies_timer_interval = 1000/GameLevel.getInstance().getEnemy_speed());
	}
	public int getMax_players() { return max_players; }
}
