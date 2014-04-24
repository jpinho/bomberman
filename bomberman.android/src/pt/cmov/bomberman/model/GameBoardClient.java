package pt.cmov.bomberman.model;


/** 
 * A GameBoardClient obeys a GameBoardServer's orders. It just performs actions as instructed by the server.
 * When the player wants to perform an action that would change the board configuration, the request will
 * be redirected to the server, who will decide if it can be fulfilled.
 */
public class GameBoardClient extends GameBoard {
	private int player_number; /* Client can be any player other than 1 */
	
	public GameBoardClient(int rows, int cols, int max_players) {
		super(rows, cols, max_players);
		player_number = -1; /* Will be set on startLevel() */
	}
	
	public void startLevel() {
		// TODO Pick current board state from server
		// TODO Pick a player number from board
	}
	
	@Override
	public boolean actionMovePlayer(int pid, int dir) {
		// TODO Send request to server and wait for acknowledgement
		return false;
	}
	
	@Override
	public boolean actionPlaceBomb(int player) {
		// TODO Send request to server and wait for acknowledgement
		return false;
	}
}
