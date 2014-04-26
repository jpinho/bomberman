package pt.cmov.bomberman.model;

import pt.cmov.bomberman.net.server.Server;
import pt.cmov.bomberman.presenter.view.JoystickView;


/** 
 * A GameBoardClient obeys a GameBoardServer's orders. It just performs actions as instructed by the server.
 * When the player wants to perform an action that would change the board configuration, the request will
 * be redirected to the server, who will decide if it can be fulfilled.
 */
public class GameBoardClient extends GameBoard {
	
	public GameBoardClient(int rows, int cols, int max_players) {
		super(rows, cols, max_players);
	}
	
	public void startLevel() {
		// TODO What to do here?
	}
	
	@Override
	public boolean actionMovePlayer(Player player, int dir) {
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
		
		synchronized (board) {
			// Assumes valid move, since data comes from server
			int new_x = player.getX() + v_x;
			int new_y = player.getY() + v_y;

			//is turned left or right, must turn it first
			if (player.getV_x() != v_x)
				new_x = player.getX();

			// is turned up or down, must turn it first
			if (player.getV_y() != v_y)
				new_y = player.getY();
			
			board[player.getX()][player.getY()] = null;
			board[new_x][new_y] = player;

			player.setPosition(new_x, new_y, v_x, v_y);
		}
		return false;
	}
	
	@Override
	public boolean actionMovePlayer(int dir) {
		// TODO Send request to server and wait for acknowledgement
		return false;
	}
	
	@Override
	public boolean actionPlaceBomb(int player) {
		// TODO Send request to server and wait for acknowledgement
		return false;
	}
	
	@Override
	public boolean actionPlaceBomb() {
		// TODO Implement
		return false;
	}
	
	@Override
	public Player newPlayer() { return null; }
	
	@Override
	public synchronized void setPlayerId(int id) { 
		player = findPlayer(id);
	}
	
	@Override
	public void addPlayer(Player p) {
		super.addPlayer(p);
		board[p.getX()][p.getY()] = p;
	}
	
	public void updateEnemiesPos(String[] serverTokens) {
		synchronized (board) {
			for (int i = 1; i < serverTokens.length; i += 4) {
				int old_x = Integer.parseInt(serverTokens[i]);
				int old_y = Integer.parseInt(serverTokens[i+1]);
				int new_x = Integer.parseInt(serverTokens[i+2]);
				int new_y = Integer.parseInt(serverTokens[i+3]);
				Enemy e = (Enemy) board[old_x][old_y];
				board[old_x][old_y] = null;
				e.setPosition(new_x, new_y);
				board[new_x][new_y] = e;
			}
		}
	}
}
