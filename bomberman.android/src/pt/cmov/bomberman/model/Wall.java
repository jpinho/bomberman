package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Wall extends GameObject {
	
	public Wall() {
		super(R.drawable.box_wall);
	}
	
	@Override 
	public boolean notifyExplosion(Player responsible) {
		return true;
	}
	
	@Override
	public String toString() {
		return "W";
	}
}
