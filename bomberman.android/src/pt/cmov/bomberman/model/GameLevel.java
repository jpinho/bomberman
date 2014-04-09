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
		players = new ArrayList<Player>();
		players.add(createNewPlayer());
		players.add(createNewPlayer());		
		gameGenerator = new GameGenerator(view);
	}

	public void buildLevel() {		
		//instantiates a new board
		this.initializeBoard();
		
		//generates a new game
		List<GameObject> gameObjects = gameGenerator.generateGame(board, players);
		
		//adds the objects to the game matrix
		board.setupObjects(gameObjects);
	}
	
	public void draw(Canvas canvas){
		board.draw(canvas);
	}
	
	private Player createNewPlayer(){
		return new Player(view, String.format("%s %d", Player.DEFAULT_NAME, players.size()));
	}
	
	private void initializeBoard() {
		board = new GameBoard(view);
	}
}
