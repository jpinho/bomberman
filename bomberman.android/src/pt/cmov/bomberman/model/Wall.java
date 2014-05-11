package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Wall extends GameObject {
	
	public Wall() {
		super(R.drawable.box_wall);
	}
	
	@Override 
	public int notifyExplosion(Player responsible) {
		return 0;
	}
	
	@Override
	public String toString() {
		return "W";
	}
}
