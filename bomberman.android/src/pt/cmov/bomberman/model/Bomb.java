package pt.cmov.bomberman.model;

import android.content.Context;
import android.graphics.Canvas;
import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Bitmaps;

public class Bomb extends GameObject {
	
	private int x;
	private int y;
	
	private Player author;
	
	public Bomb(int x, int y, Player author) {
		super(R.drawable.bomb);
		this.x = x;
		this.y = y;
		this.author = author;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	public Player getAuthor() {
		return author;
	}
	@Override
	public void draw(Context ctx, Canvas canvas, float x, float y) {
		canvas.drawBitmap(Bitmaps.getBitmap(R.drawable.pavement), x, y, null);
		super.draw(ctx, canvas, x, y);
	}
	@Override
	public String toString() {
		return "B" + author.toString();
	}
}
