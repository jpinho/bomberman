package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Wall extends GameObject implements IObstacleObject{
	
	public Wall(int x, int y) {
		super(R.drawable.box_wall, x, y);
	}

	@Override
	public int getResistance() {
		return 1;
	}

}
