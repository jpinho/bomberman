package pt.cmov.bomberman.model;

import java.io.PrintWriter;
import java.util.ArrayList;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.net.server.RemotePlayer;
import pt.cmov.bomberman.net.server.Server;
import pt.cmov.bomberman.util.Bitmaps;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
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
	
	protected GameObject[][] board; // Stores game objects in world coordinates
	protected ArrayList<Player> players;
	protected ArrayList<Enemy> enemies;
	protected int max_players; /* Size of players array */
	
	protected Player player; /* This player */
	/* The board's dimensions */
	protected final int nCols;
	protected final int nRows;
	private final Pavement pavement;
	
	public GameBoard(int rows, int cols, int max_players) {
		board = new GameObject[rows][cols];
		players = new ArrayList<Player>(max_players);
		this.max_players = max_players;
		enemies = new ArrayList<Enemy>();
		pavement = new Pavement();
		nRows = rows;
		nCols = cols;
	}

	protected Player findPlayer(int id) {
		int i;
		for (i = 0; i < players.size() && players.get(i).getPlayer_number() != id; i++);
		return i < players.size() ? players.get(i) : null;
	}
	
	/* Begin: methods used by LevelFileParser to build board
	 * Do NOT call / use this outside of LevelFileParser 
	 */
	public void setPosition(int x, int y, GameObject item) {
		board[x][y] = item;
	}
	public void addPlayer(Player p) {
		players.add(p);
	}
	public void addEnemy(Enemy e) {
		enemies.add(e);
	}
	/* End: methods used by LevelFileParser */
	
	
	
	/* Overridden methods. These methods are different depending on whether this is a client
	 * or a server. 
	 */
	public boolean actionMovePlayer(Player p, int dir) {
		return false;
	}
	
	public boolean actionMovePlayer(int dir) {
		return false;
	}
	
	public boolean actionPlaceBomb(Player p) {
		return false;
	}
	
	public boolean actionPlaceBomb() {
		return false;
	}

	public void startLevel() { }
	public Player newPlayer() { return null; }
	/* End methods overridden */
	
	public boolean actionMovePlayer(int pid, int dir) {
		return actionMovePlayer(findPlayer(pid), dir);
	}
	
	public boolean actionPlaceBomb(int pid) {
		return actionPlaceBomb(findPlayer(pid));
	}

	/* Called when a new player wants to join this game. Only useful for GameBoardServer.
	 * Returns player ID of new player, or -1 if there is no space for a new player.
	 */
	public int join() {
		return -1;
	}
	
	/* Misc. */
	protected boolean isValidMove(int x_from, int y_from, int v_x, int v_y) {
		int new_x = x_from + v_x;
		int new_y = y_from + v_y;
		return validPosition(new_x, new_y);
	}

	protected boolean validPosition(int x, int y) {
		return inBoard(x, y) && (board[x][y] == null || !board[x][y].isSolid());
	}
	
	protected boolean inBoard(int x, int y) {
		return 0 <= x && x < nRows && 0 <= y && y < nCols;
	}

	public int getColumnsCount() {
		return nCols;
	}

	public int getRowsCount() {
		return nRows;
	}
	
	
	
	
	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *                   GRAPHICS STUFF
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                
	 */
	
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

	/* The final master piece */
	public void draw(Canvas canvas) {
		drawBorders(canvas);
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				double x = horizontalExcess + object_width * j;
				double y = verticalExcess + object_height * i;
				synchronized (board) {
					if (board[i][j] != null)
						board[i][j].draw(canvas, (float) x, (float) y);
					else
						pavement.draw(canvas, (float) x, (float) y);
				}
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
	
	public void addNewPlayer(RemotePlayer p) {
		StringBuilder msg = new StringBuilder();
		msg.append("board ");
		synchronized (board) {
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					if (board[i][j] == null) {
						msg.append("-");
					}
					else {
						msg.append(board[i][j].toString());
					}
				}
				msg.append("\n");
			}
			synchronized (p.getPlayer_comm()) {
				p.getPlayer_comm().print(msg);
				p.getPlayer_comm().flush();
			}
			Server.getInstance().addNewClient(p);
		}
	}
}
