package pt.cmov.bomberman.presenter.view;

import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.util.Bitmaps;
import pt.cmov.bomberman.util.LevelFileParser;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This is the main surface that handles the ontouch events and draws the image
 * to the screen.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
	//private static final String TAG = MainGamePanel.class.getSimpleName();

	private final MainThread thread;
	private final GameLevel currentGameLevel;

	public MainGamePanel(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		getHolder().addCallback(this);
		Bitmaps.init(getResources());
		thread = new MainThread(getHolder(), this);
		setFocusable(true);
		currentGameLevel = GameLevel.getInstance();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

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
		LevelFileParser.loadLevel(getResources(), "level5", getWidth(), getHeight(), currentGameLevel);

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
	protected void onDraw(Canvas canvas) {
		currentGameLevel.draw(canvas);
	}
	
	public GameLevel getCurrentGameLevel() {
		return currentGameLevel;
	}

	/**
	 * @return the thread
	 */
	public MainThread getThread() {
		return thread;
	}
}
