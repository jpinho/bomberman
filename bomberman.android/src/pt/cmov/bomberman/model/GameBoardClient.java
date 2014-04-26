package pt.cmov.bomberman.model;


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
		// TODO Update board state with new data arrived from server
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
