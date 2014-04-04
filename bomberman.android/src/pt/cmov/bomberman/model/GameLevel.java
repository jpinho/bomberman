package pt.cmov.bomberman.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.view.SurfaceView;

public class GameLevel {
	
	private final SurfaceView view;
	private final Paviment paviment;
	private final GameGenerator gameGenerator;
	private boolean preventDraw=true;
	
	private GameBoard board;
	private List<Player> players;
	
	public GameLevel(SurfaceView view) {
		this.view = view;
		setPlayers(new ArrayList<Player>());
		paviment = new Paviment(view, 0, 0);
		
		players.add(createNewPlayer());
		players.add(createNewPlayer());		
		gameGenerator = new GameGenerator(view, null, null);
	}

	
	/**
	 * Public Methods
	 */
	
	public void setupGame() {
		preventDraw = true;
		
		//instanciates a new board
		this.initializeBoard();
		
		//generates a new game
		gameGenerator.setBoard(board);
		gameGenerator.setPlayers(players);
		List<GameObject> gameObjects = gameGenerator.generateGame();
		
		//adds the objects to the game matrix
		getBoard().setupObjects(gameObjects);
		
		//allows the onDraw event to call draw of the game level
		preventDraw = false;
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
		if(preventDraw) return;
		
		int rows = board.getRowsCount();
		int cols = board.getColumnsCount();
		IGameObject[][] gameBoard = board.getBoard();
		
		//loops all columns
		for(int i=0; i<rows; i++){
			//loops all lines of 'i'
			for(int j=0; j<cols; j++){
				drawGameObject(canvas, paviment, j, i);
				
				if(gameBoard[i][j] != null)
					drawGameObject(canvas, gameBoard[i][j], j, i);
			}
		}
		
		drawArena(canvas);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
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

		board = new GameBoard(nRows, nColumns);
	}
	
	private void drawGameObject(Canvas canvas, IGameObject go, int screenX, int screenY){
		float offsetTop = board.getOffsetTop(),
			  offsetLeft = board.getOffsetLeft();
		
		canvas.drawBitmap(go.getBitmap(),
				offsetLeft + screenX*GameObject.DEFAULT_WIDTH, 
				offsetTop + screenY*GameObject.DEFAULT_HEIGHT, 
				null);
	}
	
	private void drawArena(Canvas canvas){
		//TODO
	}
}
