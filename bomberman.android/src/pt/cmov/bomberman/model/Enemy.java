package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Enemy extends GameObject {
	private int x;
	private int y;
	public Enemy(int x, int y) {
		super(R.drawable.enemy);
		this.x = x;
		this.y = y;
	}
	public void setPosition(int x, int y) {
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
