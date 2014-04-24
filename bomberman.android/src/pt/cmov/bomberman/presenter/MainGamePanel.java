package pt.cmov.bomberman.presenter;

import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.util.Bitmaps;
import pt.cmov.bomberman.util.LevelFileParser;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This is the main surface that handles the ontouch events and draws the image
 * to the screen.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
	//private static final String TAG = MainGamePanel.class.getSimpleName();

	private MainThread thread;
	
	private String ip;
	private int port;
	private boolean isServer;
	
	/* Used for players that host a game */
	public MainGamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		Bitmaps.init(getResources());
		thread = new MainThread(getHolder(), this);
		setFocusable(true);
		isServer = true;
	}
	
	/* Used for players joining on a game hosted by someone else */
	public MainGamePanel(Context context, String ip, int port) {
		super(context);
		this.ip = ip;
		this.port = port;
		isServer = false;
		// TODO Implement this
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		/* Upon surface creation, we must call loadLevel() to load the bootstrap level.
		 * The parser will read the level file and build the level accordingly; level attributes
		 * are stored in currentGameLevel, and the map layout is retrieved and turned into a board
		 * that is hold by currentGameLevel.
		 *
		 * It is crucial to call loadLevel() before enabling the rendering thread and picking bitmaps, because we need
		 * to view the map dimensions to decide the scaling factor and the borders size.
		 */
		LevelFileParser.loadLevel(getResources(), "level5", getWidth(), getHeight(), isServer);
		if (isServer) {
			GameLevel.getInstance().getBoard().startLevel();			
		} else {
			// TODO Implement client - must grab current game state from server
			//GameLevel.getInstance().getBoard().startLevel(ip, port);
			//Log.d("ClientHost", "Entered game as a client.");
		}
		/* Now that the screen arrangement has been decided, it is safe to start drawing. */
		getThread().setRunning(true);
		getThread().start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry) {
			try {
				getThread().join();
				retry = false;
			}
			catch (InterruptedException e) {
				// Just keep trying.
			}
		}
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
		GameLevel.getInstance().draw(canvas);
	}

	public MainThread getThread() {
		return thread;
	}
}