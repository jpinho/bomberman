package pt.cmov.bomberman;

import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.model.GameObject;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This is the main surface that handles the ontouch events and draws the image
 * to the screen.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

	private final Bitmap bombBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb);
	private static final String TAG = MainGamePanel.class.getSimpleName();
	public static final int MAX_PLAYERS = 4;

	private MainThread thread;
	private GameLevel currentGame;
	private Paint controllerPaint;
	
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
		
		controllerPaint = new Paint();
		controllerPaint.setStyle(Style.STROKE);
		controllerPaint.setColor(Color.argb(127, 0, 255, 0));
		controllerPaint.setStrokeWidth(3);
		controllerPaint.setAntiAlias(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void surfaceCreated(SurfaceHolder holder) {		
        // initializes default width/height scaled sizes
        GameObject.initializeDefaults(this);
        
        // generates the map and game defaults
		getCurrentGame().setupGame();
		
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
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
			}
			catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// delegating event handling to the droid
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// the gestures
			
			
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// touch was released
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// draws the current game state onto the canvas
		getCurrentGame().draw(canvas);
		
		// @author jp
		// messy code i'm just providing quick UI elements this needs to be organized!!!
		if(controllerPaint!=null){
			
			//right controller (bomb planting)
			controllerPaint.setColor(Color.argb(80, 0, 190, 0));
			controllerPaint.setStyle(Style.FILL);
			canvas.drawCircle(getWidth() - 35, getHeight() - 35, 30, controllerPaint);
			
			controllerPaint.setColor(Color.argb(127, 0, 255, 0));
			controllerPaint.setStyle(Style.STROKE);
			canvas.drawCircle(getWidth() - 35, getHeight() - 35, 30, controllerPaint);
			
			canvas.drawBitmap(bombBitmap, getWidth()-55, getHeight()-57, null);
			
			
			//left controller (movement controller)
			controllerPaint.setColor(Color.argb(80, 0, 190, 0));
			controllerPaint.setStyle(Style.FILL);
			canvas.drawCircle(
					(float)(GameObject.DEFAULT_WIDTH*2.0 + GameObject.DEFAULT_WIDTH/3.0), 
					(float)(this.getHeight() - (GameObject.DEFAULT_WIDTH*2.0 + GameObject.DEFAULT_WIDTH/3.0)), 
					(float)(GameObject.DEFAULT_WIDTH*2.0), 
					controllerPaint);
			
			controllerPaint.setColor(Color.argb(127, 0, 255, 0));
			controllerPaint.setStyle(Style.STROKE);
			canvas.drawCircle(
					(float)(GameObject.DEFAULT_WIDTH*2.0 + GameObject.DEFAULT_WIDTH/3.0), 
					(float)(this.getHeight() - (GameObject.DEFAULT_WIDTH*2.0 + GameObject.DEFAULT_WIDTH/3.0)), 
					(float)(GameObject.DEFAULT_WIDTH*2.0), 
					controllerPaint);
		}
	}

	/**
	 * @return the currentGame
	 */
	public GameLevel getCurrentGame() {
		return currentGame;
	}

	/**
	 * @param currentGame
	 *            the currentGame to set
	 */
	public void setCurrentGame(GameLevel currentGame) {
		this.currentGame = currentGame;
	}

}
