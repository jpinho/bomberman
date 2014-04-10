package pt.cmov.bomberman.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface IGameObject {
	public Bitmap getBitmap();	
	public void draw(Canvas canvas, float x, float y);
}
