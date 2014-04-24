package pt.cmov.bomberman.model;

import java.util.ArrayList;

import pt.cmov.bomberman.net.server.Server;
import pt.cmov.bomberman.net.server.ServerThread;
import pt.cmov.bomberman.presenter.view.JoystickView;
import pt.cmov.bomberman.util.Misc;
import pt.cmov.bomberman.util.Tuple;
import android.os.Handler;
import android.util.Log;

/** 
 * A GameBoardServer controls everything that happens in a game.
 * Anyone wanting to plant a bomb or move the player must ask a GameBoardServer, and can only do so after
 * the server acknowledges the request.
 * 
 * The gameboard server is also responsible for moving enemies around (and broadcasting their positions).
 * 
 * @author filipe
 *
 */

public class GameBoardServer extends GameBoard {
	/* Server is always player 1 */
	private int current_players;
	
	private int enemies_timer_interval;
	
	public GameBoardServer(int rows, int cols, int max_players) {
		super(rows, cols, max_players);
		current_players = 1;
	}
	
	public void startLevel() {
		Handler enemiesHandler = new Handler();
		enemiesHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				moveEnemies();
			}
		}, enemies_timer_interval = 1000/GameLevel.getInstance().getEnemy_speed());
		player = newPlayer(); // Activates player 1, the game owner
		Thread server = new Thread(new ServerThread());
		server.start();
	}
	
	@Override
	public synchronized Player newPlayer() {
		Log.d("ServerHost", "In newPlayer()");
		Player player = findPlayer(current_players);
		if (player != null) {
			synchronized (board) {
				player.activate();
			}
			current_players++;
			// TODO Notify others that new player arrived
		}
		Log.d("ServerHost", "player is null? " + (player == null ? "yes" : "no"));
		return player;
	}
	
	@Override
	/** Called when a player wants to move. */
	public synchronized boolean actionMovePlayer(Player p, int dir) {
		int v_x, v_y;
		v_x = v_y = 0;
		switch (dir) {
		case JoystickView.BOTTOM:
			v_x = 1;
			break;
		case JoystickView.UP:
			v_x = -1;
			break;
		case JoystickView.LEFT:
			v_y = -1;
			break;
		case JoystickView.RIGHT:
			v_y = 1;
			break;
		}
		if (isValidMove(p.getX(), p.getY(), v_x, v_y)) {
			int new_x = p.getX() + v_x;
			int new_y = p.getY() + v_y;
			board[p.getX()][p.getY()] = null;
			board[new_x][new_y] = p;
			p.setPosition(new_x, new_y);
			Server.getInstance().broadcastPlayerMoved(p);
			return true;
		}
		return false;
	}
	
	@Override
	/** Called when the owner of the game wants to move his player */
	public boolean actionMovePlayer(int dir) {
		return actionMovePlayer(player, dir);
	}
	
	
	
	
	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *                 BOMB PHYSICS
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                
	 */
	@Override
	public synchronized boolean actionPlaceBomb(Player p) {
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
			Server.getInstance().broadcastPlayerPlantedBomb(p, b);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean actionPlaceBomb() {
		return actionPlaceBomb(player);
	}
	
	private synchronized void bombExploded(Bomb b) {
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
	
	private synchronized void clearFire(ArrayList<Tuple<Integer, Integer>> lst) {
		for (Tuple<Integer, Integer> t : lst)
			board[t.x][t.y] = null;
	}
	
	private synchronized ArrayList<Tuple<Integer, Integer>> propagateFire(int x, int y, int range, int x_step, int y_step) {
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
	
	
	
	
	
	
	
	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *                 ENEMIES MOVEMENT
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                
	 */
	private synchronized void moveEnemies() {
		StringBuilder new_positions = new StringBuilder();
		new_positions.append("ENEMY ");
		for (Enemy e : enemies) {
			new_positions.append(e.getX()).append(" ").append(e.getY()).append(" -> ");
			Tuple<Integer, Integer> new_pos = chooseNextEnemyPosition(e.getX(), e.getY());
			if (new_pos != null) {
				board[e.getX()][e.getY()] = null;
				board[new_pos.x][new_pos.y]= e;
				e.setPosition(new_pos.x, new_pos.y);
			}
			new_positions.append(e.getX()).append(" ").append(e.getY()).append(" ");
		}
		Handler enemiesHandler = new Handler();
		enemiesHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				moveEnemies();
			}
		}, enemies_timer_interval);
		Server.getInstance().broadcastEnemiesPositions(player, new_positions.toString());
	}
	
	private synchronized Tuple<Integer, Integer> chooseNextEnemyPosition(int e_x, int e_y) {
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
}
