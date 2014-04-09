package pt.cmov.bomberman.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface IGameObject {

	public int getX();

	public int getY();

	public Bitmap getBitmap();

	public void draw(Canvas canvas, GameBoard board);
	
	public void draw(Canvas canvas, float screenX, float screenY);
	
	public void draw(Canvas canvas, GameBoard board, int x, int y);
}
