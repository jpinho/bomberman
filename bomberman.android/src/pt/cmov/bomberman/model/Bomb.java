package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Bomb extends GameObject {
	
	private int x;
	private int y;
	
	Player author;
	
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
}
