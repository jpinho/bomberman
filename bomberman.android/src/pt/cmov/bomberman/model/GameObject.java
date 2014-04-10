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
	public void draw(Canvas canvas, float x, float y) {
		canvas.drawBitmap(getBitmap(), x, y, null);
	}

	/*
	@Override
	public void draw(Canvas canvas, GameBoard board) {
		float offsetTop = board.getOffsetTop();
		float offsetLeft = board.getOffsetLeft();

		drawGameObject(canvas, offsetLeft, offsetTop, x, y);
	}
	
	@Override
	public void draw(Canvas canvas, GameBoard board, int x, int y){
		float offsetTop = board.getOffsetTop();
		float offsetLeft = board.getOffsetLeft();
		
		drawGameObject(canvas, offsetLeft, offsetTop, x, y);
	}
	
	@Override
	public void draw(Canvas canvas, float screenX, float screenY){
		canvas.drawBitmap(this.getBitmap(), screenX, screenY,null);
	}
	
	private void drawGameObject(Canvas canvas,float offsetLeft, float offsetTop, int x, int y) {
		canvas.drawBitmap(this.getBitmap(), 
				offsetLeft + x * getBitmap().getWidth(), 
				offsetTop + y * getBitmap().getHeight(), null);
	}
	*/
}
