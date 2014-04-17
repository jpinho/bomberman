package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Bomb extends GameObject {
	
	private int x;
	private int y;
	
	public Bomb(int x, int y) {
		super(R.drawable.bomb);
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
