package pt.cmov.bomberman.model;

import android.graphics.Canvas;
import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Bitmaps;

public class Player extends GameObject implements IMovableObject {
	
	public static final String DEFAULT_NAME = "Player";
	private int player_number;
	private int x;
	private int y;
	/* This pair of values stores the direction of the last movement performed.
	 * It is used to determine where to place a bomb.
	 */
	private int v_x;
	private int v_y;
	/*
	private PlayerScore scoreBoard;
	private String name;
	*/
	public Player(int player, int x, int y) {
		super(R.drawable.bman_down);
		/* bman_down -> (1, 0) */
		v_x = 1;
		v_y = 0;
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
	public void setPosition(int x, int y) {
		v_x = x - this.x;
		v_y = y - this.y;
		this.x = x;
		this.y = y;
	}

	public int getV_x() {
		return v_x;
	}

	public int getV_y() {
		return v_y;
	}
}
