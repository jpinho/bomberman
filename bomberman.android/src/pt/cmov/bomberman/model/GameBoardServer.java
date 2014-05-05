package pt.cmov.bomberman.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pt.cmov.bomberman.net.server.Server;
import pt.cmov.bomberman.net.server.ServerThread;
import pt.cmov.bomberman.presenter.activity.GameArenaActivity;
import pt.cmov.bomberman.presenter.view.JoystickView;
import pt.cmov.bomberman.util.Misc;
import pt.cmov.bomberman.util.Tuple;
import android.os.Handler;
import android.util.Log;

/**
 * A GameBoardServer controls everything that happens in a game. Anyone wanting
 * to plant a bomb or move the player must ask a GameBoardServer, and can only
 * do so after the server acknowledges the request.
 * 
 * The gameboard server is also responsible for moving enemies around (and
 * broadcasting their positions).
 * 
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
		}, enemies_timer_interval = 1000 / GameLevel.getInstance().getEnemy_speed());
		player = newPlayer(); // Activates player 1, the game owner
		Thread server = new Thread(new ServerThread());
		server.start();
	}

	@Override
	public synchronized Player newPlayer() {
		Player player = findPlayer(current_players);
		if (player != null) {
			synchronized (board) {
				board[player.getX()][player.getY()] = player;
			}
			current_players++;
			// TODO Notify others that new player arrived
		}
		return player;
	}

	@Override
	/** Called when a player wants to move. */
	public boolean actionMovePlayer(Player p, int dir) {
		int v_x, v_y;
		v_x = v_y = 0;

		switch (dir) {
		case JoystickView.BOTTOM:
			v_x = 1;
			break;
		case JoystickView.FRONT:
			v_x = -1;
			break;
		case JoystickView.RIGHT:
			v_y = 1;
			break;
		case JoystickView.LEFT:
			v_y = -1;
			break;
		}

		boolean moved = false;
		synchronized (board) {
			if ((moved = isValidMove(p.getX(), p.getY(), v_x, v_y))) {
				int new_x = p.getX() + v_x;
				int new_y = p.getY() + v_y;

				// is turned left or right, must turn it first
				if (p.getV_x() != v_x)
					new_x = p.getX();

				// is turned up or down, must turn it first
				if (p.getV_y() != v_y)
					new_y = p.getY();

				board[p.getX()][p.getY()] = null;
				board[new_x][new_y] = p;

				p.setPosition(new_x, new_y, v_x, v_y);
				Server.getInstance().broadcastPlayerMoved(p.getPlayer_number(), dir);
			}
		}
		return moved;
	}

	@Override
	/** Called when the owner of the game wants to move his player */
	public boolean actionMovePlayer(int dir) {
		if (player != null)
			return actionMovePlayer(player, dir);
		return false;
	}
	
	/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *                   BOMB PHYSICS
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                
	 */
	
	private void bombExploded(final Bomb b) {
		board[b.getX()][b.getY()] = new BombFire(b.getAuthor());
		final ArrayList<Tuple<Integer, Integer>> pos_to_clear;
		int range = GameLevel.getInstance().getExplosion_range();
		pos_to_clear = propagateFire(b, b.getX(), b.getY(), range, 1, 0); // Goes
																		// down
		pos_to_clear.addAll(propagateFire(b, b.getX(), b.getY(), range, -1, 0)); // Goes
																				// up
		pos_to_clear.addAll(propagateFire(b, b.getX(), b.getY(), range, 0, 1)); // Goes
																				// to
																				// the
																				// right
		pos_to_clear.addAll(propagateFire(b, b.getX(), b.getY(), range, 0, -1)); // Goes
																				// to
																				// the
																				// left
		pos_to_clear.add(new Tuple<Integer, Integer>(b.getX(), b.getY()));
		Server.getInstance().broadcastBombExploded(b.getX(), b.getY(), pos_to_clear);
		Handler endExplosion = new Handler();
		endExplosion.postDelayed(new Runnable() {
			@Override
			public void run() {
				synchronized (board) {
					clearFire(pos_to_clear, b.getAuthor());
				}
			}
		}, GameLevel.getInstance().getExplosion_duration() * 1000);
	}

	private ArrayList<Tuple<Integer, Integer>> propagateFire(Bomb bomb, int x, int y, int range, int x_step, int y_step) {
		ArrayList<Tuple<Integer, Integer>> positions = new ArrayList<Tuple<Integer, Integer>>();
		boolean hit = false;
		while (!hit && inBoard(x += x_step, y += y_step) && range-- > 0) {
			if (board[x][y] == null || board[x][y].notifyExplosion()) {
				if (board[x][y] != null)
					hit = true;
				board[x][y] = new BombFire(bomb.getAuthor());
				positions.add(new Tuple<Integer, Integer>(x, y));
			}
			else
				hit = true;
		}
		return positions;
	}
	
	private void clearFire(ArrayList<Tuple<Integer, Integer>> lst, Player author) {
		StringBuilder positions = new StringBuilder();
		positions.append("clear");
		for (Tuple<Integer, Integer> t : lst) {
			positions.append(" ").append(t.x).append(" ").append(t.y);
			board[t.x][t.y] = null;
		}
		positions.append("\n");
		Server.getInstance().broadcastClearExplosion(positions.toString());
		author.setPlantedBomb(false);
	}

	@Override
	public boolean actionPlaceBomb(Player p) {
		boolean res = false;
		int bx, by;
		synchronized (board) {
			final int bomb_x = p.getX() + p.getV_x();
			final int bomb_y = p.getY() + p.getV_y();
			bx = bomb_x;
			by = bomb_y;
			if ((res = validPosition(bomb_x, bomb_y)) && (GameArenaActivity.GOD_MODE || !p.plantedBomb())) {
				
				final Bomb b = new Bomb(bomb_x, bomb_y, p);
				board[bomb_x][bomb_y] = b;
				
				p.setPlantedBomb(true);
				
				GameArenaActivity.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {						
						Handler bhandler = new Handler();
						bhandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								synchronized (board) {
									bombExploded(b);
								}
							}
						}, GameLevel.getInstance().getExplosion_timeout() * 1000);
					}
				});
				
				Server.getInstance().broadcastPlayerPlantedBomb(p.getPlayer_number(), bx, by);
			}
		}
		return res;
	}

	@Override
	public boolean actionPlaceBomb() {
		if (player != null) {
			return actionPlaceBomb(player);
		}
		return false;
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
	 * ENEMIES MOVEMENT
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	private synchronized void moveEnemies() {
		StringBuilder new_positions = new StringBuilder();
		new_positions.append("ENEMY");
		ArrayList<Player> playersKilled = new ArrayList<Player>();

		synchronized (board) {
			for (Enemy e : enemies) {
				new_positions.append(" ").append(e.getX()).append(" ").append(e.getY());
				Tuple<Integer, Integer> new_pos = chooseNextEnemyPosition(e.getX(), e.getY());
				if (new_pos != null) {
					board[e.getX()][e.getY()] = null;
					board[new_pos.x][new_pos.y] = e;
					e.setPosition(new_pos.x, new_pos.y);
					
				}
				playersKilled.addAll(checkEnemyKill(e.getX(), e.getY()));
				new_positions.append(" ").append(e.getX()).append(" ").append(e.getY());
			}
			new_positions.append("\n");
			Server.getInstance().broadcastEnemiesPositions(new_positions.toString());
			
			boolean playerDied = false;
			StringBuilder killMsg = new StringBuilder();
			killMsg.append("die enemy");
			for (Player pKilled : playersKilled) {
				if (pKilled == player)
					playerDied = true;
				else
					kill(pKilled);
				killMsg.append(" " + pKilled.getPlayer_number());
			}
			killMsg.append("\n");
			if (playerDied) {
				// Player hosting the game is now dead!
				die("an enemy.");
			}
			if (playersKilled.size() > 0)
				Server.getInstance().broadcastPlayersKilled(killMsg.toString());
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				moveEnemies();
			}
		}, enemies_timer_interval);
	}
	
	private ArrayList<Player> checkEnemyKill(int enemyX, int enemyY) {
		ArrayList<Player> playersKilled = new ArrayList<Player>();
		for (Player player : players) {
			int px = player.getX();
			int py = player.getY();
			if ((enemyX == px || enemyX == px-1 || enemyX == px+1) &&
				(enemyY == py || enemyY == py-1 || enemyY == py+1))
				playersKilled.add(player);
		}
		return playersKilled;
	}

	private synchronized Tuple<Integer, Integer> chooseNextEnemyPosition(int e_x, int e_y) {
		int total_attempts = 0;
		int directions[] = { -1, 0, 1, 0, 0, -1, 0, 1 };
		                  /* | Up | Down | Left | Right */
		boolean directions_tried[] = { false, false, false, false };
		                             /* Up     Down   Left   Right */
		int direction;
		int x;
		int y;
		do {
			direction = Misc.randInt(0, directions_tried.length - 1);
			if (directions_tried[direction] == false) {
				directions_tried[direction] = true;
				total_attempts++;
			}
			x = directions[direction * 2] + e_x;
			y = directions[direction * 2 + 1] + e_y;
		} while (total_attempts < directions_tried.length && !validPosition(x, y));

		return validPosition(x, y) ? new Tuple<Integer, Integer>(x, y) : null;
	}
	
	/**
	 * Enemy AI Movement - Unfinished, is too slow! 
	 *
	
	private synchronized Tuple<Integer, Integer> chooseNextEnemyPosition(Enemy e, int e_x, int e_y) {
		int x;
		int y;

		// target reached or no more path to it, let's choose another point for to enemy to focus on.
		if(e.getTargetPath() == null || e.getTargetPath().isEmpty()){
			boolean valid_pos = false;
			
			while(!valid_pos){
				e.setTarget_x(Misc.randInt(0, this.nCols-1));
				e.setTarget_y(Misc.randInt(0, this.nRows-1));
				valid_pos = validPosition(e.getTarget_x(), e.getTarget_y());
				
				// tracing initial route to target.
				if(valid_pos) 
					e.setTargetPath(find_path(e_x, e_y, e.getTarget_x(), e.getTarget_y()));
			}
		}
		
		List<Tuple<Integer, Integer>> path = e.getTargetPath();
		
		if(path == null || path.isEmpty())
			return null;
		
		// if route still valid follow it.
		if(validPosition(path.get(0).x, path.get(0).y)){
			x = path.get(0).x;
			y = path.get(0).y;
		}
		else return null;
		
		// traveled position is removed to allow the enemy to move on down the path.
		e.setTargetPath(path.subList(1, path.size()));
		
		return new Tuple<Integer, Integer>(x, y);
	}
		 
	private List<Tuple<Integer, Integer>> find_path(int e_x, int e_y, int t_x, int t_y){
	 
		List<Tuple<Integer, Integer>> path;
		boolean visit[][] = new boolean[nRows][nCols];
		
		do{
			path = find_path(e_x, e_y, t_x, t_y, visit);
			
			if(path.isEmpty())
				continue;
		}
		while(path.get(path.size()-1).x != t_x && path.get(path.size()-1).y != t_y);
		
		return path;
	}

	private List<Tuple<Integer, Integer>> find_path(int e_x, int e_y, int t_x, int t_y, boolean[][] visit) {
		List<Tuple<Integer, Integer>> result = new LinkedList<Tuple<Integer, Integer>>();
		
		if(e_x == t_x && e_y == t_y){
			result.add(new Tuple<Integer, Integer>(t_x, t_y));
			return result;
		}
		
		int directions[] = { -1, 0, 1, 0, 0, -1, 0, 1 };

		// Up | Down | Left | Right
		boolean directions_tried[] = { false, false, false, false };
		int x=0,y=0,direction;
		int total_attempts=0;
		
		while (total_attempts < directions_tried.length) {
			direction = Misc.randInt(0, directions_tried.length - 1);
			if (directions_tried[direction] == false) {
				directions_tried[direction] = true;
				total_attempts++;
			}
			x = directions[direction * 2] + e_x;
			y = directions[direction * 2 + 1] + e_y;
			
			if(!validPosition(x,y))
				continue;
			
			if(visit[x][y])
				continue;
		} 
			
		// blocked, return empty list to append to the previous call.
		if(!validPosition(x, y) || (validPosition(x, y) && visit[x][y]))
			return result;
		
		visit[x][y] = true;
		
		// add new position to path.
		result.add(new Tuple<Integer, Integer>(x, y));
		
		// keep trying to find the target from the current (x,y) pos.
		result.addAll(find_path(x, y, t_x, t_y, visit));
		
		return result;
	}*/
}
