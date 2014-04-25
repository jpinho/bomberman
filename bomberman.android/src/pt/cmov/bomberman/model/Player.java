package pt.cmov.bomberman.model;

import android.graphics.Canvas;
import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Bitmaps;

public class Player extends GameObject {
	
	public static final String DEFAULT_NAME = "Player";
	private int player_number;
	private int x;
	private int y;
	
	/* Indicates whether this player is participating in the game */
	private volatile boolean isActive; 
	
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
		// scoreBoard = new PlayerScore();
		player_number = player;
		this.x = x;
		this.y = y;
		
		isActive = false;
	}
	
	@Override
	public boolean isSolid() {
		return isActive;
	}
	
	@Override
	public void draw(Canvas canvas, float x, float y) {
		if (isActive)
			super.draw(canvas, x, y);
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
		
		this.setPosition(x, y, v_x, v_y);
	}
	
	public void setPosition(int x, int y, int v_x, int v_y){
		this.x = x;
		this.y = y;
		
		setDirection(v_x, v_y);
		updateAvatar();
	}
	
	public void setDirection(int v_x, int v_y){
		this.v_x = v_x;
		this.v_y = v_y;
	}

	public int getV_x() {
		return v_x;
	}

	public int getV_y() {
		return v_y;
	}
	
	private void updateAvatar() {
		if (v_x == 1 && v_y == 0) {
			setBitmapCode(R.drawable.bman_down);
		}
		else if (v_x == -1 && v_y == 0) {
			setBitmapCode(R.drawable.bman_up);
		}
		else if (v_x == 0 && v_y == 1) {
			setBitmapCode(R.drawable.bman_right);
		}
		else if (v_x == 0 && v_y == -1) {
			setBitmapCode(R.drawable.bman_left);
		}
	}
	
	public void activate() {
		isActive = true;
	}
}
