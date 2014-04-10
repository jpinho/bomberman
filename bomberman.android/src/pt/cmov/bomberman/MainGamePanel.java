package pt.cmov.bomberman;

import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.util.Bitmaps;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
	//private final Bitmap bombBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb);
	private static final String TAG = MainGamePanel.class.getSimpleName();
	public static final int MAX_PLAYERS = 4;

	private MainThread thread;
	private GameLevel currentGameLevel;
	private Paint controllerPaint;
	
	public MainGamePanel(Context context) {
		super(context);
		
		getHolder().addCallback(this);
		Bitmaps.init(getResources());
		thread = new MainThread(getHolder(), this);
		setFocusable(true);
		currentGameLevel = new GameLevel();
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
		currentGameLevel.loadLevel(getResources(), "level1.xml", getWidth(), getHeight());
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
		// @author jp
		// messy code i'm just providing quick UI elements this needs to be organized!!!
		/*
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
					(Bitmaps.width()*2.0f+Bitmaps.width()/3.0f), 
					(this.getHeight() - (Bitmaps.width()*2.0f+Bitmaps.width()/3.0f)), 
					(Bitmaps.width()*2.0f), 
					controllerPaint);
			
			controllerPaint.setColor(Color.argb(127, 0, 255, 0));
			controllerPaint.setStyle(Style.STROKE);
			canvas.drawCircle(
					(Bitmaps.width()*2.0f+Bitmaps.width()/3.0f), 
					(this.getHeight() - (Bitmaps.width()*2.0f+Bitmaps.width()/3.0f)), 
					(Bitmaps.width()*2.0f), 
					controllerPaint);
		}
		*/
	}
}
