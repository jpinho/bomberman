package pt.cmov.bomberman;


import pt.cmov.bomberman.model.JoyStick;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
public class GameArenaActivity extends Activity {

    /** Called when the activity is first created. */

	private static final String TAG = GameArenaActivity.class.getSimpleName();


	RelativeLayout game;
	JoyStick js;
	MainGamePanel gameView;
	RelativeLayout buttons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen
        getWindow().setFlags(
        	WindowManager.LayoutParams.FLAG_FULLSCREEN,
        	WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // load images bitmaps into memory
        //Bitmaps.init(this);

        // creates the game view
        gameView = new MainGamePanel(this);
        //LinearLayout GameWidgets = new LinearLayout(this);
        game = new RelativeLayout(this);
        RelativeLayout.LayoutParams mainGame = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        game.setLayoutParams(mainGame);
        buttons = new RelativeLayout(this);
        RelativeLayout.LayoutParams b1 = new LayoutParams(
        200,
        200);


        b1.addRule(RelativeLayout.BELOW, gameView.getId());
        b1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        b1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        buttons.setLayoutParams(b1);
        buttons.setBackgroundResource(R.drawable.image_button_bg);
        js = new JoyStick(this, buttons, R.drawable.image_button);
	    js.setStickSize(150, 150);
	    js.setLayoutSize(500, 500);
	    js.setLayoutAlpha(150);
	    js.setStickAlpha(100);
	    js.setOffset(90);
	    js.setMinimumDistance(50);

        game.addView(gameView);
        game.addView(buttons);
        setContentView(game);

	    buttons.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				js.drawStick(arg1);
				if(arg1.getAction() == MotionEvent.ACTION_DOWN
						|| arg1.getAction() == MotionEvent.ACTION_MOVE) {
					Log.d(TAG, "X : " + String.valueOf(js.getX()));
					Log.d(TAG, "Y : " + String.valueOf(js.getY()));
					Log.d(TAG, "Angle : " + String.valueOf(js.getAngle()));
					Log.d(TAG, String.valueOf(js.getDistance()));
					int direction = js.get8Direction();
					if(direction == JoyStick.STICK_UP) {
						Log.d(TAG, "js direction up");
					} else if(direction == JoyStick.STICK_UPRIGHT) {
						Log.d(TAG, "Direction : Up Right");
					} else if(direction == JoyStick.STICK_RIGHT) {
						Log.d(TAG, "Direction : Right");
					} else if(direction == JoyStick.STICK_DOWNRIGHT) {
						Log.d(TAG, "Direction : Down Right");
					} else if(direction == JoyStick.STICK_DOWN) {
						Log.d(TAG, "Direction : Down");
					} else if(direction == JoyStick.STICK_DOWNLEFT) {
						Log.d(TAG, "Direction : Down left");
					} else if(direction == JoyStick.STICK_LEFT) {
						Log.d(TAG, "Direction : Left");
					} else if(direction == JoyStick.STICK_UPLEFT) {
						Log.d(TAG, "Direction : up left");
					} else if(direction == JoyStick.STICK_NONE) {
						Log.d(TAG, "Direction : Center");
					}
					gameView.getCurrentGameLevel().getBoard().actionMovePlayer(1, direction);
				}
				return true;
			}
        });
        Log.d(TAG, "View added");
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