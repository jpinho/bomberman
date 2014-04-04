package pt.cmov.bomberman.model;

import java.util.List;

public class GameBoard {

	private final int nColumns;
	private final int nRows;
	private final float horizontalExcess;
	private final float verticalExcess;
	private IGameObject[][] board;
	
	public GameBoard(float rows, float cols) {
		nRows = (int)Math.floor(rows);
		nColumns = (int)Math.floor(cols);
		
		horizontalExcess = rows - nRows;
		verticalExcess = cols - nColumns;
		
		board = new IGameObject[nRows][nColumns];
	}
	
	/**
	 * @return the board
	 */
	public IGameObject[][] getBoard() {
		return board;
	}

	/**
	 * @return the horizontalExcess
	 */
	public float getHorizontalExcess() {
		return horizontalExcess;
	}

	/**
	 * @return the verticalExcess
	 */
	public float getVerticalExcess() {
		return verticalExcess;
	}

	/**
	 * @return the nColumns
	 */
	public int getColumnsCount() {
		return nColumns;
	}

	/**
	 * @return the nRows
	 */
	public int getRowsCount() {
		return nRows;
	}

	public void setupObjects(List<GameObject> gameObjects) {
		for(GameObject go : gameObjects){
			board[go.getY() % nRows][go.getX() % nColumns] = go;
		}
	}

	public float getOffsetTop() {
		return (float) (getVerticalExcess() / 2.0) * GameObject.DEFAULT_HEIGHT;
	}

	public float getOffsetLeft() {
		return (float) (getHorizontalExcess() / 2.0) * GameObject.DEFAULT_WIDTH;
	}
}