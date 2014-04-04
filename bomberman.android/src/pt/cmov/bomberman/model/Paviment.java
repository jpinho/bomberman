package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import android.view.SurfaceView;

public class Paviment extends GameObject {
	
	public Paviment(SurfaceView view, int x, int y) {
		super(view, R.drawable.paviment, x, y);
	}
}
