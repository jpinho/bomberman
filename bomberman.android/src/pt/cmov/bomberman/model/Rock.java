package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Rock extends GameObject {	
	public Rock() {
		super(R.drawable.rock);
	}
	
	@Override
	public String toString() {
		return "R";
	}
}
