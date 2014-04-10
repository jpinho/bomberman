package pt.cmov.bomberman;

import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.util.Bitmaps;
import pt.cmov.bomberman.util.LevelFileParser;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = MainGamePanel.class.getSimpleName();

	private MainThread thread;
	private GameLevel currentGameLevel;
	
	public MainGamePanel(Context context) {
		super(context);
		
		getHolder().addCallback(this);
		Bitmaps.init(getResources());
		thread = new MainThread(getHolder(), this);
		setFocusable(true);
		currentGameLevel = new GameLevel();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		LevelFileParser.loadLevel(getResources(), "level3", getWidth(), getHeight(), currentGameLevel);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			}
			catch (InterruptedException e) {
				// Just keep trying.
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
		currentGameLevel.draw(canvas);
	}
}
