package pt.cmov.bomberman.model;

import java.util.ArrayList;
import java.util.List;

import android.view.SurfaceView;

public class GameGenerator {

	private static final double ROCKS_DENSITY_FACTOR = 0.15;
	private static final double WALL_DENSITY_FACTOR = 0.8;
	
	private final SurfaceView view;
	private GameBoard board;
	private List<Player> players;
	
	public GameGenerator(SurfaceView view) {
		this.view = view;
	}
	

	/**
	 * Public Methods
	 */
	
	public SurfaceView getView() {
		return view;
	}

	public GameBoard getBoard() {
		return board;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public void setBoard(GameBoard board) {
		this.board = board;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public List<GameObject> generateGame(GameBoard board, List<Player> players) {
		
		this.setBoard(board);
		this.setPlayers(players);
		
		List<GameObject> gameObjects = new ArrayList<GameObject>();
		
		//computes the numbers of elements to draw
		int nPositions = (int)(getBoard().getRowsCount() * getBoard().getColumnsCount());
		int nWallsToDraw = (int)Math.floor(nPositions * WALL_DENSITY_FACTOR);
		int nRocksToDraw = (int)Math.floor(nPositions * ROCKS_DENSITY_FACTOR);
		int nPlayersToDraw = getPlayers().size();
		
		//coordinates for all elements, used as auxiliar to avoid 
		//repeated coordinates generation, remember Set contais non repetead and ordered elements.
		ArrayList<int[]> allCoords = new ArrayList<int[]>();

		//coordinates for walls
		ArrayList<int[]> wallsCoords = new ArrayList<int[]>();

		//coordinates for rocks		
		ArrayList<int[]> rocksCoords = new ArrayList<int[]>();
		
		//coordinates for players
		ArrayList<int[]> playersCoords = new ArrayList<int[]>();

		//coordinates generation based on random numbers
		generateUniqueRndPositions(nWallsToDraw, allCoords, wallsCoords);
		generateUniqueRndPositions(nRocksToDraw, allCoords, rocksCoords);
		generateUniqueRndPositions(nPlayersToDraw, allCoords, playersCoords);
		
		//generation of objects based on their news coordinates
		
		generateWalls(gameObjects, nWallsToDraw, wallsCoords);
		generateRocks(gameObjects, nRocksToDraw,  rocksCoords);
		generatePlayers(gameObjects, nPlayersToDraw, playersCoords);
		
		return gameObjects;
	}
	
	
	/**
	 * Private Methods 
	 */
	
	private void generatePlayers(List<GameObject> gameObjects, int nPlayersToDraw, ArrayList<int[]> playersCoords) {
		/*
		if(playersCoords.size() < nPlayersToDraw)
			throw new InvalidParameterException("The number os coordinates must match the number of elements");
		
		for(int i=0; i < nPlayersToDraw; i++){
			Player p = new Player(playersCoords.get(i)[0], playersCoords.get(i)[1]);
			gameObjects.add(p);
		}
		*/		
	}

	private void generateRocks(List<GameObject> gameObjects, int nRocksToDraw, ArrayList<int[]> rocksCoords) {
		/*
		if(rocksCoords.size() < nRocksToDraw)
			throw new InvalidParameterException("The number os coordinates must match the number of elements");
		
		for(int i=0; i < nRocksToDraw; i++){
			Rock r = new Rock(rocksCoords.get(i)[0], rocksCoords.get(i)[1]);
			gameObjects.add(r);
		}
		*/
	}

	private void generateWalls(List<GameObject> gameObjects, int nWallsToDraw, ArrayList<int[]> wallsCoords) {
		/*
		if(wallsCoords.size() < nWallsToDraw)
			throw new InvalidParameterException("The number os coordinates must match the number of elements");
		
		for(int i=0; i < nWallsToDraw; i++){
			Wall w = new Wall(wallsCoords.get(i)[0], wallsCoords.get(i)[1]);
			gameObjects.add(w);
		}
		*/
	}
	
	private void generateUniqueRndPositions(int nPositions, ArrayList<int[]> globalPositions, List<int[]> outputPositions) {
		
		for(int i=0; i<nPositions; i++)
		{
			int x=0, y=0;
			boolean unique=false;
			
			while(!unique){
				x = (int)Math.floor(Math.random() * (getBoard().getColumnsCount()));
				y = (int)Math.floor(Math.random() * (getBoard().getRowsCount()));
				unique = true;
				
				for(int j=0; j<globalPositions.size(); j++){
					if(globalPositions.get(j)[0] == x && globalPositions.get(j)[1]==y){
						unique =false;
						break;
					}
				}
			}
			
			outputPositions.add(new int[]{ x, y });
		}
	}
}
