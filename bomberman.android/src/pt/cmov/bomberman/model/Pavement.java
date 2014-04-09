package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import android.view.SurfaceView;

public class Pavement extends GameObject {
	
	public Pavement(SurfaceView view, int x, int y) {
		super(view, R.drawable.pavement, x, y);
	}
}
