package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Pavement extends GameObject {
	public Pavement() {
		super(R.drawable.pavement);
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
	
	@Override
	public String toString() {
		return "-";
	}
}
