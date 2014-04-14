package pt.cmov.bomberman.model;

import android.graphics.Canvas;
import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Bitmaps;

public class Player extends GameObject implements IMovableObject {
	
	public static final String DEFAULT_NAME = "Player";
	private int player_number;
	private int x;
	private int y;
	/*
	private PlayerScore scoreBoard;
	private String name;
	*/
	public Player(int player, int x, int y) {
		super(R.drawable.bman_down);
	//	scoreBoard = new PlayerScore();
		player_number = player;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void draw(Canvas canvas, float x, float y) {
		canvas.drawBitmap(Bitmaps.getBitmap(R.drawable.pavement), x, y, null);
		super.draw(canvas, x, y);
	}
	
	@Override
	public void handleActionUp(int eventX, int eventY) {
		//setBitmap(BitmapFactory.decodeResource(getView().getResources(), R.drawable.bman_up));

	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		//setBitmap(BitmapFactory.decodeResource(getView().getResources(), R.drawable.bman_down));
	}

	@Override
	public void handleActionLeft(int eventX, int eventY) {
		//setBitmap(BitmapFactory.decodeResource(getView().getResources(), R.drawable.bman_left));
	}

	@Override
	public void handleActionRight(int eventX, int eventY) {
		//setBitmap(BitmapFactory.decodeResource(getView().getResources(), R.drawable.bman_right));
	}

	public int getPlayer_number() {
		return player_number;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
