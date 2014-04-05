package pt.cmov.bomberman.model;

import java.util.List;

import android.graphics.Canvas;
import android.view.SurfaceView;

public class GameBoard {

	private final int nColumns;
	private final int nRows;
	private final float horizontalExcess;
	private final float verticalExcess;
	private final Paviment paviment;
	private final SurfaceView view;

	private IGameObject[][] board;

	public GameBoard(SurfaceView view, float rows, float cols) {
		nRows = (int) Math.floor(rows);
		nColumns = (int) Math.floor(cols);

		horizontalExcess = rows - nRows;
		verticalExcess = cols - nColumns;

		this.view = view;
		board = new IGameObject[nRows][nColumns];
		paviment = new Paviment(view, 0, 0);
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
		drawBorders(canvas, new Rock(getView(), 0, 0));

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

	private void drawBorders(Canvas canvas, IGameObject border) {

		float w = border.getBitmap().getWidth();	
		float h = border.getBitmap().getHeight();
		
		float xLeft = getOffsetLeft() - w;
		float xRight = getView().getWidth() - getOffsetLeft();
		
		float yTop = getOffsetTop() - h;
		float yBottom = getView().getHeight() - getOffsetTop();

		int nCorners = 2;
		
		//draws top-bottom borders
		for (int i = 0; i < (nColumns + nCorners); i++){
			//border top
			border.draw(canvas, xLeft + i * w, yTop);
			
			//border bottom
			border.draw(canvas, xLeft + i * w, yBottom);
		}
		
		int marginRight = -5, marginLeft   = +5;
		int marginTop   = +2, marginBottom = +2;
		
		//draws left-right borders, without the corners => the first and last
		//to prevent the horizontal borders from overlapping the vertical ones.
		for (int i = 1; i < (nRows + nCorners - 1); i++){
			//border left
			border.draw(canvas, xLeft + marginRight, yTop + marginTop + i*h);
			
			//border right
			border.draw(canvas, xRight + marginLeft, yTop + marginBottom + i*h);
		}
	}

	/**
	 * @return the view
	 */
	public SurfaceView getView() {
		return view;
	}
}