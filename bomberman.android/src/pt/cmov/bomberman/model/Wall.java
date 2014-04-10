package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Wall extends GameObject implements IObstacleObject {
	
	public Wall() {
		super(R.drawable.box_wall);
	}

	@Override
	public int getResistance() {
		return 1;
	}

}
