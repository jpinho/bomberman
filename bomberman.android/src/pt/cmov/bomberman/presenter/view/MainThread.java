package pt.cmov.bomberman.presenter.view;

import pt.cmov.bomberman.model.GameLevel;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	
	private static final String TAG = MainThread.class.getSimpleName();
	private SurfaceHolder surfaceHolder;
	private MainGamePanel gamePanel; 
	private boolean running;
	
	public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@SuppressLint("WrongCall")
	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		while (running) {
			canvas = null;
			try {
				canvas = this.surfaceHolder.lockCanvas();
			
				synchronized (surfaceHolder) {
					if (GameLevel.getInstance().isGameOver()) {
						running = false;
					} else {
						this.gamePanel.onDraw(canvas);
					}
				}
			} 
			finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
