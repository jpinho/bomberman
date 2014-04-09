package pt.cmov.bomberman;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class GameArenaActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SurfaceView view = new MainGamePanel(this);
        setContentView(view);
    }
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
}