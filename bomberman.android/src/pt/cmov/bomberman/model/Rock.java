package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Rock extends GameObject implements IObstacleObject{

	private static final int INDESTRUCTIBLE_RESISTENCE = -1;
	
	public Rock(int x, int y) {
		super(R.drawable.rock, x, y);
	}

	@Override
	public int getResistance() {
		return INDESTRUCTIBLE_RESISTENCE;
	}
}
