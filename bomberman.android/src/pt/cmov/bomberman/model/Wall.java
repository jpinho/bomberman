package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import android.view.SurfaceView;

public class Wall extends GameObject implements IObstacleObject{
	
	public Wall(SurfaceView view, int x, int y) {
		super(view, R.drawable.box_wall, x, y);
	}

	@Override
	public int getResistance() {
		return 1;
	}

}
