package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import android.view.SurfaceView;

public class Robot extends GameObject implements IMoveableObject{
	
	public Robot(SurfaceView view, int x, int y) {
		//TODO: create icon for the robot
		super(view, R.drawable.bman_down, x, y);
	}

	@Override
	public void handleActionUp(int eventX, int eventY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleActionLeft(int eventX, int eventY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleActionRight(int eventX, int eventY) {
		// TODO Auto-generated method stub
		
	}
}
