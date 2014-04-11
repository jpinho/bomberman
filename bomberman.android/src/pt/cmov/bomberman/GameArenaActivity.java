package pt.cmov.bomberman;


import pt.cmov.bomberman.model.JoyStick;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
public class GameArenaActivity extends Activity {

    /** Called when the activity is first created. */

	private static final String TAG = GameArenaActivity.class.getSimpleName();


	RelativeLayout game;
	JoyStick js;
	SurfaceView gameView;
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