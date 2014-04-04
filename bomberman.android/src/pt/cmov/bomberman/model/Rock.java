package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import android.view.SurfaceView;

public class Rock extends GameObject implements IObstacleObject{

	private static final int INDESTRUCTIBLE_RESISTENCE = -1;
	
	public Rock(SurfaceView view, int x, int y) {
		super(view, R.drawable.rock, x, y);
	}

	@Override
	public int getResistance() {
		return INDESTRUCTIBLE_RESISTENCE;
	}
}
