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
		// TODO Pick current board state from server
		// TODO Pick a player number from board
		// player_id = ...
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
}
