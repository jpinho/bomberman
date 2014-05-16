package pt.cmov.bomberman.model;

import java.util.ArrayList;

import pt.cmov.bomberman.net.server.Server;
import pt.cmov.bomberman.net.server.ServerThread;
import pt.cmov.bomberman.presenter.activity.GameArenaActivity;
import pt.cmov.bomberman.presenter.view.JoystickView;
import pt.cmov.bomberman.util.Misc;
import pt.cmov.bomberman.util.Tuple;
import android.os.Handler;

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
		player.setPlayerName(GameLevel.getInstance().getPlayer_name());
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
				Player killer;
				if (board[new_x][new_y] != null && (killer = board[new_x][new_y].isLethal()) != null) {
					// TODO Merge these
					if (p == player) {
						die("Player" + killer.getPlayer_number());						
					} else {
						kill(p);
					}
					if (killer != p) {
						killer.incrementScore(GameLevel.getInstance().getOpponent_score());
						if (killer != player)
							Server.getInstance().updatePlayerScore(killer.getPlayer_number(), GameLevel.getInstance().getOpponent_score());
						else
							GameArenaActivity.getInstance().getGameView().getGameStateChangeListener().onStateChange(GameLevel.getInstance());
					}
					Server.getInstance().broadcastPlayersKilled("die Player" + killer.getPlayer_number() + " " + p.getPlayer_number() + "\n");
				} else {
					board[new_x][new_y] = p;
					p.setPosition(new_x, new_y, v_x, v_y);
					Server.getInstance().broadcastPlayerMoved(p.getPlayer_number(), dir);
				}
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
		int scoreUpdate = 0;
		while (!hit && inBoard(x += x_step, y += y_step) && range-- > 0) {
			if (board[x][y] == null || (hit = ((scoreUpdate = board[x][y].notifyExplosion(bomb.getAuthor())) > -1))) {
				board[x][y] = new BombFire(bomb.getAuthor());
				positions.add(new Tuple<Integer, Integer>(x, y));
				if (hit) {
					bomb.getAuthor().incrementScore(scoreUpdate);
					if (bomb.getAuthor() != player) {
						Server.getInstance().updatePlayerScore(bomb.getAuthor().getPlayer_number(), scoreUpdate);
					} else {
						GameArenaActivity.getInstance().getGameView().getGameStateChangeListener().onStateChange(GameLevel.getInstance());
					}
				}
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
		StringBuilder killed_enemies = new StringBuilder();
		killed_enemies.append("die player enemy");
		ArrayList<Player> playersKilled = new ArrayList<Player>();
		ArrayList<Enemy> enemiesKilled = new ArrayList<Enemy>();

		synchronized (board) {
			for (Enemy e : enemies) {
				Tuple<Integer, Integer> new_pos = chooseNextEnemyPosition(e.getX(), e.getY());
				board[e.getX()][e.getY()] = null;
				Player killer;
				if (board[new_pos.x][new_pos.y] != null && (killer = board[new_pos.x][new_pos.y].isLethal()) != null) {
					enemiesKilled.add(e);
					killed_enemies.append(" ").append(e.getX()).append(" ").append(e.getY());
					killer.incrementScore(GameLevel.getInstance().getRobot_score());
					if (killer != player)
						Server.getInstance().updatePlayerScore(killer.getPlayer_number(), GameLevel.getInstance().getRobot_score());
					else
						GameArenaActivity.getInstance().getGameView().getGameStateChangeListener().onStateChange(GameLevel.getInstance());
				}
				else {
					board[new_pos.x][new_pos.y] = e;
					new_positions.append(" ").append(e.getX()).append(" ").append(e.getY());
					e.setPosition(new_pos.x, new_pos.y);
					new_positions.append(" ").append(e.getX()).append(" ").append(e.getY());
				}
				playersKilled.addAll(checkEnemyKill(e.getX(), e.getY()));
			}
			
			killed_enemies.append("\n");
			new_positions.append("\n");
			Server.getInstance().broadcastEnemiesPositions(new_positions.toString());
			if (enemiesKilled.size() > 0)
				Server.getInstance().broadcastMsg(killed_enemies.toString());
			
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
			
			for (Enemy e : enemiesKilled)
				enemies.remove(e);
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
		for (Player player : getPlayers()) {
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

		return validPosition(x, y) ? new Tuple<Integer, Integer>(x, y) : new Tuple<Integer, Integer>(e_x, e_y);
	}

	@Override
	public void setPlayerId(int id) {
		// client logic - do nothing!
	}

	@Override
	public boolean actionUpdatePlayerName(int playerID) {
		// client logic - do nothing
		return false;
	}
}
