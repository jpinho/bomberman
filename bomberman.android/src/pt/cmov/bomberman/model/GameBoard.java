package pt.cmov.bomberman.model;

import java.util.List;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.os.Build;
import android.view.SurfaceView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameBoard {

	private final int nColumns;
	private final int nRows;
	private final float horizontalExcess;
	private final float verticalExcess;
	private final Paviment paviment;
	private final SurfaceView view;

	private IGameObject[][] board;

	public GameBoard(SurfaceView v) {
		paviment = new Paviment(v, 0, 0);
		float windowW = v.getWidth();
		float windowH = v.getHeight();
		float columns = windowW / GameObject.DEFAULT_WIDTH;
		float rows = windowH / GameObject.DEFAULT_HEIGHT;
		
		nRows = (int) Math.floor(rows);
		nColumns = (int) Math.floor(columns);
		horizontalExcess = rows - nRows;
		verticalExcess = columns - nColumns;
		this.view = v;
		
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
		for (GameObject go : gameObjects) {
			board[go.getY() % nRows][go.getX() % nColumns] = go;
		}
	}

	public float getOffsetTop() {
		return (float) (getVerticalExcess() / 2.0) * GameObject.DEFAULT_HEIGHT;
	}

	public float getOffsetLeft() {
		return (float) (getHorizontalExcess() / 2.0) * GameObject.DEFAULT_WIDTH;
	}

	public void draw(Canvas canvas) {		
		// loops all columns
		for (int i = 0; i < nRows; i++) {
			// loops all lines of 'i'
			for (int j = 0; j < nColumns; j++) {
				paviment.draw(canvas, this, j, i);

				if (board[i][j] != null)
					board[i][j].draw(canvas, this);
			}
		}
	}
	
	/**
	 * @return the view
	 */
	public SurfaceView getView() {
		return view;
	}
}