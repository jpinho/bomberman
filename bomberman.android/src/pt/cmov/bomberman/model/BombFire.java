package pt.cmov.bomberman.model;

import android.graphics.Canvas;
import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Bitmaps;

public class BombFire extends GameObject {
	
	private Player source;
	
	public BombFire(Player from) {
		super(R.drawable.fire);
		source = from;
	}
	
	public Player getSource() {
		return source;
	}
	@Override
	public void draw(Canvas canvas, float x, float y) {
		canvas.drawBitmap(Bitmaps.getBitmap(R.drawable.pavement), x, y, null);
		super.draw(canvas, x, y);
	}
	@Override
	public String toString() {
		return "F" + source.toString();
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
	
	@Override
	public Player isLethal() {
		return source;
	}
	
}
