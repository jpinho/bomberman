package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;

public class Player extends GameObject implements IMovableObject{
	
	public static final String DEFAULT_NAME = "Player";
	private PlayerScore scoreBoard;
	private String name;
	private int player_number;
		
	public Player(int player) {
		super(R.drawable.bman_down);
		scoreBoard = new PlayerScore();
		player_number = player;
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
