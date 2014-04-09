package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import android.view.SurfaceView;

public class Player extends GameObject implements IMovableObject{
	
	public static final String DEFAULT_NAME = "Player";
	private PlayerScore scoreBoard;
	private String name;
		
	public Player(int x, int y) {
		super(R.drawable.bman_down, x, y);
	}

	public Player(SurfaceView view, String name) {
		this(0, 0);
		this.name = name;
		scoreBoard = new PlayerScore();
	}
	
	@Override
	public void handleActionUp(int eventX, int eventY) {
		//setBitmap(BitmapFactory.decodeResource(getView().getResources(), R.drawable.bman_up));

	}

	@Override
	public void handleActionDown(int eventX, int eventY) {
		//setBitmap(BitmapFactory.decodeResource(getView().getResources(), R.drawable.bman_down));
	}

	@Override
	public void handleActionLeft(int eventX, int eventY) {
		//setBitmap(BitmapFactory.decodeResource(getView().getResources(), R.drawable.bman_left));
	}

	@Override
	public void handleActionRight(int eventX, int eventY) {
		//setBitmap(BitmapFactory.decodeResource(getView().getResources(), R.drawable.bman_right));
	}
}
