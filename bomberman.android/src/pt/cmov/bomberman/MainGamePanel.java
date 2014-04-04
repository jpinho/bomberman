package pt.cmov.bomberman;

import pt.cmov.bomberman.model.GameLevel;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	public static final int MAX_PLAYERS = 4;
			
	private MainThread thread;
	private GameLevel currentGame;
	
	public MainGamePanel(Context context) {
		super(context);
		
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		// creates the game
		setCurrentGame(new GameLevel(this));
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
		getCurrentGame().setupGame();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// fills the canvas with black
		canvas.drawColor(Color.BLACK);
	
		// draws the current game state onto the canvas
		getCurrentGame().draw(canvas);
	}

	/**
	 * @return the currentGame
	 */
	public GameLevel getCurrentGame() {
		return currentGame;
	}

	/**
	 * @param currentGame the currentGame to set
	 */
	public void setCurrentGame(GameLevel currentGame) {
		this.currentGame = currentGame;
	}

}
