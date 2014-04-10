package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Bitmaps;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameBoard {
	private final int nCols;
	private final int nRows;
	private double horizontalExcess;
	private double verticalExcess;
	private int object_width;
	private int object_height;
	private final Pavement pavement;
	private IGameObject[][] board;

	public GameBoard(int rows, int cols) {
		board = new IGameObject[rows][cols];
		pavement = new Pavement();
		nRows = rows;
		nCols = cols;
	}
	
	public void setScreenDimensions(int width, int height) {
		int max_object_width = (int) Math.floor((width*1.0)/nCols);
		int max_object_height = (int) Math.floor((height*1.0)/nRows);
		int delta;		
		if (fitsIn(max_object_width, max_object_height, Bitmaps.ORIGINAL_WIDTH, Bitmaps.ORIGINAL_HEIGHT)) {
			delta = Math.min(max_object_width - Bitmaps.ORIGINAL_WIDTH, max_object_height - Bitmaps.ORIGINAL_HEIGHT);
		}
		else {
			delta = -Math.max(Bitmaps.ORIGINAL_WIDTH - max_object_width, Bitmaps.ORIGINAL_HEIGHT - max_object_height);
		}
		object_width = Bitmaps.ORIGINAL_WIDTH+delta;
		object_height = Bitmaps.ORIGINAL_HEIGHT+delta;
		horizontalExcess = (width*1.0-object_width*nCols)/2;
		verticalExcess = (height*1.0-object_height*nRows)/2;
		Bitmaps.setWidth(object_width);
		Bitmaps.setHeight(object_height);
		Log.d("LevelFileParser", "width: " + width + " height: " + height);
		Log.d("LevelFileParser", "nRows: " + nRows + " nCols: " + nCols);
		Log.d("LevelFileParser", "object_width: " + object_width + " object_height: " + object_height);
		Log.d("LevelFileParser", "horizontalExcess: " + horizontalExcess + " verticalExcess: " + verticalExcess);
	}

	public void setPosition(int x, int y, IGameObject item) {
		board[x][y] = item;
	}

	public int getColumnsCount() {
		return nCols;
	}

	public int getRowsCount() {
		return nRows;
	}

	public void draw(Canvas canvas) {
		drawBorders(canvas);
		/*
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				double x = horizontalExcess+object_width*i;
				double y = verticalExcess+object_height*j;
				if (board[i][j] != null)
					board[i][j].draw(canvas, (float) x, (float) y);
				else
					pavement.draw(canvas, (float) x, (float) y);
			}
		}
		*/	
	}

	private void drawBorders(Canvas canvas) {
		Bitmap border = Bitmaps.getBitmap(R.drawable.rock);
		//border-top/bottom
		for (int j = 0; j < nCols+1; j++) {
			canvas.drawBitmap(border, (float) horizontalExcess+j*object_width, (float) verticalExcess-object_height, null);
			canvas.drawBitmap(border, (float) horizontalExcess+j*object_width, (float) verticalExcess+nRows*object_height, null);
		}
		
		//border-left/right
		for (int j = 0; j < nRows; j++) {
			canvas.drawBitmap(border, (float) horizontalExcess-object_width, (float) verticalExcess+j*object_height, null);
			canvas.drawBitmap(border, (float) horizontalExcess+nCols*object_width, (float) verticalExcess+j*object_height, null);
		}
	}
	
	private boolean fitsIn(int max_width, int max_height, int object_w, int object_h) {
		return object_w <= max_width && object_h <= max_height;
	}
}
