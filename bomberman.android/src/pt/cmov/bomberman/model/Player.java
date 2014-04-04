package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import android.view.SurfaceView;

public class Player extends GameObject implements IMoveableObject{
	
	public static final String DEFAULT_NAME = "Player";
	private PlayerScore scoreBoard;
	private String name;
		
	public Player(SurfaceView view, int x, int y) {
		super(view, R.drawable.bman_down, x, y);
	}

	public Player(SurfaceView view, String name){
		this(view, 0, 0);
		setName(name);
		setScoreBoard(new PlayerScore());
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

	/**
	 * @return the scoreBoard
	 */
	public PlayerScore getScoreBoard() {
		return scoreBoard;
	}

	/**
	 * @param scoreBoard the scoreBoard to set
	 */
	public void setScoreBoard(PlayerScore scoreBoard) {
		this.scoreBoard = scoreBoard;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
