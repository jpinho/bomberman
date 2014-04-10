package pt.cmov.bomberman.model;

import pt.cmov.bomberman.util.Bitmaps;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class GameObject implements IGameObject {
	private int bitmapCode;
	public GameObject(int bitmapCode) {
		this.bitmapCode = bitmapCode;
	}

	@Override
	public Bitmap getBitmap() {
		return Bitmaps.getBitmap(bitmapCode);
	}
	
	@Override
	// Screen coordinates
	public void draw(Canvas canvas, float x, float y) {
		canvas.drawBitmap(getBitmap(), x, y, null);
	}
}
