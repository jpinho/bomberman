package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class BombFire extends GameObject {
	
	private Bomb source;
	
	public BombFire(Bomb from) {
		super(R.drawable.fire);
		source = from;
	}
	
	public Bomb getSource() {
		return source;
	}
}
