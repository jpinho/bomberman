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
	
	private boolean preventDraw=true;
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
	
	public void setupGame() {
		setPreventDraw(true);
		
		//instantiates a new board
		this.initializeBoard();
		
		//generates a new game
		List<GameObject> gameObjects = gameGenerator.generateGame(board, players);
		
		//adds the objects to the game matrix
		getBoard().setupObjects(gameObjects);
		
		//allows the onDraw event to call draw of the game level
		setPreventDraw(false);
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
	
	public void draw(Canvas canvas){
		if(isPreventDraw()) return;
		
		//draws the board and borders
		board.draw(canvas);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public boolean isPreventDraw() {
		return preventDraw;
	}

	public void setPreventDraw(boolean preventDraw) {
		this.preventDraw = preventDraw;
	}
	
	
	/**
	 * Private Methods
	 */
	
	private Player createNewPlayer(){
		return new Player(getView(), String.format("%s %d", Player.DEFAULT_NAME, getPlayers().size()));
	}
	
	private void initializeBoard() {
		SurfaceView v = getView();
		float windowW = v.getWidth();
		float windowH = v.getHeight();

		float nColumns = windowW / GameObject.DEFAULT_WIDTH;
		float nRows = windowH / GameObject.DEFAULT_HEIGHT;

		board = new GameBoard(v, nRows, nColumns);
	}
}
