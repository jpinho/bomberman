package pt.cmov.bomberman.model;

import java.util.List;

import pt.cmov.bomberman.util.Bitmaps;
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
	private final Pavement pavement;
	private final SurfaceView view;

	private IGameObject[][] board;

	public GameBoard(SurfaceView v) {
		pavement = new Pavement(0, 0);
		float columns = (v.getWidth()*1.0f)/Bitmaps.width();
		float rows = (v.getHeight()*1.0f)/Bitmaps.height();
		nRows = (int) Math.floor(rows);
		nColumns = (int) Math.floor(columns);
		horizontalExcess = columns - nColumns;
		verticalExcess = rows - nRows;
		this.view = v;
		board = new IGameObject[nRows][nColumns];
	}

	public int getColumnsCount() {
		return nColumns;
	}

	public int getRowsCount() {
		return nRows;
	}

	public void setupObjects(List<GameObject> gameObjects) {
		for (GameObject go : gameObjects) {
			board[go.getY() % nRows][go.getX() % nColumns] = go;
		}
	}

	public float getOffsetTop() {
		return (verticalExcess/2.0f)*Bitmaps.height();
	}

	public float getOffsetLeft() {
		return (horizontalExcess/2.0f)*Bitmaps.width();
	}

	public void draw(Canvas canvas) {
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nColumns; j++) {				
				pavement.draw(canvas, this, j, i);

				if (board[i][j] != null)
					board[i][j].draw(canvas, this);
			}
		}
		
		drawBorders(canvas);
	}

	private void drawBorders(Canvas canvas) {
		IGameObject border = new Rock(0, 0);

		//border-top/bottom
		for (int j = 0; j < nColumns+1; j++) {	
			border.draw(canvas, j*Bitmaps.width(),-Bitmaps.height()+getOffsetTop());
			border.draw(canvas, j*Bitmaps.width(), getOffsetTop()+ (nRows*Bitmaps.height()));
		}
		
		//border-left/right
		for (int j = 0; j < nRows; j++) {	
			border.draw(canvas, -Bitmaps.width()+getOffsetLeft(), getOffsetTop()+ j*Bitmaps.height());
			border.draw(canvas, +getOffsetLeft() + nColumns*Bitmaps.width(), getOffsetTop()+ j*Bitmaps.height());
		}
	}
	
	/**
	 * @return the view
	 */
	public SurfaceView getView() {
		return view;
	}
}