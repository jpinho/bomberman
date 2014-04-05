package pt.cmov.bomberman.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceView;

public interface IGameObject {

	public int getX();

	public void setX(int x);

	public int getY();

	public void setY(int x);

	public SurfaceView getView();

	public void setView(SurfaceView view);

	public Bitmap getBitmap();

	public void draw(Canvas canvas, GameBoard board);
	
	public void draw(Canvas canvas, float screenX, float screenY);
	
	public void draw(Canvas canvas, GameBoard board, int x, int y);
}
