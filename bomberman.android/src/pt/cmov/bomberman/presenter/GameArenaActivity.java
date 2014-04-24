package pt.cmov.bomberman.presenter;


import pt.cmov.bomberman.R;
import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.model.JoyStick;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
public class GameArenaActivity extends Activity {

    /** Called when the activity is first created. */

	private static final String TAG = GameArenaActivity.class.getSimpleName();


	RelativeLayout game;
	JoyStick js;
	MainGamePanel gameView;
	RelativeLayout buttons;
	private Button bombButton;
	private RelativeLayout fireButtons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen
        getWindow().setFlags(
        	WindowManager.LayoutParams.FLAG_FULLSCREEN,
        	WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    Bundle extras = getIntent().getExtras();
	    boolean isServer = extras.getBoolean("isHost");
        
	    if (isServer) {
	    	gameView = new MainGamePanel(this);
	    }
	    else {
	    	String ip = extras.getString("BombermanServerIP");
	    	int port = extras.getInt("BombermanServerPort");
	    	gameView = new MainGamePanel(this, ip, port);
	    }

        //LinearLayout GameWidgets = new LinearLayout(this);
        game = new RelativeLayout(this);

        RelativeLayout.LayoutParams mainGame = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        game.setLayoutParams(mainGame);
        buttons = new RelativeLayout(this);
        fireButtons = new RelativeLayout(this);

        RelativeLayout.LayoutParams b1 = new LayoutParams(200, 200);
        b1.addRule(RelativeLayout.BELOW, gameView.getId());
        b1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        b1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        RelativeLayout.LayoutParams b2 = new LayoutParams(100, 100);
        b2.addRule(RelativeLayout.BELOW, gameView.getId());
        b2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        b2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        fireButtons.setLayoutParams(b2);

        buttons.setLayoutParams(b1);
        buttons.setBackgroundResource(R.drawable.image_button_bg);

        js = new JoyStick(this, buttons, R.drawable.image_button);
	    js.setStickSize(150, 150);
	    js.setLayoutSize(500, 500);
	    js.setLayoutAlpha(150);
	    js.setStickAlpha(100);
	    js.setOffset(90);
	    js.setMinimumDistance(50);
	    RelativeLayout.LayoutParams fireParams = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    bombButton = new Button(this);
	    bombButton.setLayoutParams(fireParams);
	    bombButton.setText("Bomb");
        game.addView(gameView);
        fireButtons.addView(bombButton);
        game.addView(fireButtons);
        game.addView(buttons);
        setContentView(game);

	    bombButton.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v)
	        {
	        	GameLevel.getInstance().getBoard().actionPlaceBomb();
	        }
	    });

	    buttons.setOnTouchListener(new OnTouchListener() {
	    	private boolean moved;
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				js.drawStick(arg1);
				if (arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_UP) {
					moved = false;
				}
				if ((arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE)
						&& !moved) {
					int direction = js.get8Direction();
					if (direction == JoyStick.STICK_DOWN
							|| direction == JoyStick.STICK_UP
							|| direction == JoyStick.STICK_LEFT
							|| direction == JoyStick.STICK_RIGHT) {
						moved = true;
						GameLevel.getInstance().getBoard().actionMovePlayer(direction);
					}
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
	
	@Override
	public void onBackPressed() {
		try {
			gameView.getThread().setRunning(false);
			gameView.getThread().join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		super.onBackPressed();
	}
}