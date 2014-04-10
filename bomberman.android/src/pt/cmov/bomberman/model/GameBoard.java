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

		float y;
		// Top
		y = (float) verticalExcess;
		do {
			y -= object_height;
			drawLeftRightBorder(canvas, border, y);
			for (float x = (float) horizontalExcess; x < horizontalExcess+nCols*object_width; x += object_width) {
				canvas.drawBitmap(border, x, y, null);
			}
		} while (y >= 0);
		
		// Left and Right
		for (y = (float) verticalExcess; y <= verticalExcess+(nRows-1)*object_height; y += object_height)
			drawLeftRightBorder(canvas, border, y);
		
		// Bottom
		y = (float) verticalExcess+(nRows-1)*object_height;
		float y_limit = (float) verticalExcess*2+nRows*object_height; 
		do {
			y += object_height;
			drawLeftRightBorder(canvas, border, y);
			for (float x = (float) horizontalExcess; x < horizontalExcess+nCols*object_width; x += object_width) {
				canvas.drawBitmap(border, x, y, null);
			}
		} while (y <= y_limit);
	}
	
	private void drawLeftRightBorder(Canvas canvas, Bitmap border, float y) {
		float x = (float) horizontalExcess;
		do {
			x -= object_width;
			canvas.drawBitmap(border, x, y, null);
		} while (x >= 0);
		x = (float) horizontalExcess+(nCols-1)*object_width;
		float x_limit = (float) horizontalExcess*2+nCols*object_width;
		do {
			x += object_width;
			canvas.drawBitmap(border, x, y, null);
		} while (x <= x_limit);
	}
	
	private boolean fitsIn(int max_width, int max_height, int object_w, int object_h) {
		return object_w <= max_width && object_h <= max_height;
	}
}
