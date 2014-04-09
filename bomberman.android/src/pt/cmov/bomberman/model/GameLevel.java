package pt.cmov.bomberman.model;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.os.Build;
import android.view.SurfaceView;

@TargetApi(Build.VERSION_CODES.DONUT)
public class GameLevel {
	
	private final SurfaceView view;
	private final GameGenerator gameGenerator;
	
	private GameBoard board;
	private List<Player> players;
	
	
	public GameLevel(SurfaceView view) {
		this.view = view;
		setPlayers(new ArrayList<Player>());
		
		players.add(createNewPlayer());
		players.add(createNewPlayer());		
		gameGenerator = new GameGenerator(view);
	}

	
	/**
	 * Public Methods
	 */
	
	public void buildLevel() {		
		//instantiates a new board
		this.initializeBoard();
		
		//generates a new game
		List<GameObject> gameObjects = gameGenerator.generateGame(board, players);
		
		//adds the objects to the game matrix
		getBoard().setupObjects(gameObjects);
	}

	public void addPlayer(String name){
		Player newPlayer = new Player(getView(), name);
		this.getPlayers().add(newPlayer);
	}

	public GameBoard getBoard() {
		return board;
	}

	public SurfaceView getView() {
		return view;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	/**
	 * Draws the entire game onto the canvas.
	 * 
	 * @param canvas - place to draw the game into.
	 */
	public void draw(Canvas canvas){
		board.draw(canvas);
	}
	
	
	/**
	 * Private Methods
	 */
	
	private Player createNewPlayer(){
		return new Player(getView(), String.format("%s %d", Player.DEFAULT_NAME, getPlayers().size()));
	}
	
	private void initializeBoard() {
		board = new GameBoard(getView());
	}
}
