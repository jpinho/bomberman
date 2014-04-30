package pt.cmov.bomberman.presenter.activity;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.presenter.view.JoystickView;
import pt.cmov.bomberman.presenter.view.JoystickView.OnJoystickMoveListener;
import pt.cmov.bomberman.presenter.view.MainGamePanel;
import pt.cmov.bomberman.presenter.view.proxy.GameStatusViewProxy;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GameArenaActivity extends Activity 
{
	
	private static GameArenaActivity instance;
	/* God mode: 
	 * - players can plant unlimited number of bombs at the same time
	 * - it is impossible to die
	 */
	public static final boolean GOD_MODE = false;
	
    /** Called when the activity is first created. */
	RelativeLayout game;
	JoystickView jsv;
	MediaPlayer playerGameSound;
	MainGamePanel gameView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        

        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen
        getWindow().setFlags(
        	WindowManager.LayoutParams.FLAG_FULLSCREEN,
        	WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // creates the activity based on the xml
        setContentView(R.layout.activity_game_arena);
        
        // the surface view where the game is being drawn
        gameView = (MainGamePanel)this.findViewById(R.id.gameSurface);
	    
        Bundle extras = getIntent().getExtras();
	    boolean isServer = extras.getBoolean("isHost");
        
	    if (isServer) {
	    	gameView.initAsServer();
	    }
	    else {
	    	String ip = extras.getString("BombermanServerIP");
	    	int port = extras.getInt("BombermanServerPort");
	    	gameView.initAsClient(ip, port);
	    }
	    
        setupGameStatusUpdate();        
		setupControls();
        setupSounds();
        
        instance = this;
    }
    
    public static GameArenaActivity getInstance() {
    	return instance;
    }
    
    public void notifyDied(String killer) {
    	Context context = getApplicationContext();
    	CharSequence text = "You were killed by " + killer;
    	int duration = Toast.LENGTH_LONG;
    	
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }

	private void setupGameStatusUpdate() {
		final GameStatusViewProxy gameStatusProxy = new GameStatusViewProxy(this.findViewById(R.id.gameTitleContainer));
        
        gameView.onGameStateChange(new MainGamePanel.OnGameStateChange() {	
			@Override
			public void onStateChange(GameLevel gameLevel) {
				gameStatusProxy.update(gameLevel);
			}
		});
	}
    
    @Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {    	
    	if(keyCode == KeyEvent.KEYCODE_W){
    		GameLevel.getInstance().getBoard().actionMovePlayer(1, JoystickView.FRONT);
    		return true;
    	}
    	else if(keyCode == KeyEvent.KEYCODE_S){
    		GameLevel.getInstance().getBoard().actionMovePlayer(1, JoystickView.BOTTOM);
    		return true;
    	}
    	else if(keyCode == KeyEvent.KEYCODE_A){
    		GameLevel.getInstance().getBoard().actionMovePlayer(1, JoystickView.LEFT);
    		return true;
    	}
    	else if(keyCode == KeyEvent.KEYCODE_D){
    		GameLevel.getInstance().getBoard().actionMovePlayer(1, JoystickView.RIGHT);
    		return true;
    	}
    	else if(keyCode == KeyEvent.KEYCODE_ENTER){
    		((Button)this.findViewById(R.id.btnBombPlant)).callOnClick();
    		return true;
    	}
    		
    	return false;
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(playerGameSound != null){
			playerGameSound.release();
			playerGameSound = null;
		}
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
		
		if(playerGameSound != null){
			playerGameSound.release();
			playerGameSound = null;
		}
		
		super.onBackPressed();
	}
	
	private void setupControls() {
		Button btnBombPlant = (Button)this.findViewById(R.id.btnBombPlant);
        jsv = (JoystickView)this.findViewById(R.id.joystickView);

        // event handling setup
        btnBombPlant.setOnClickListener(new OnClickListener() {
	        @Override
			public void onClick(View v){
	        	GameLevel.getInstance().getBoard().actionPlaceBomb();	        
	        }
	    });
        
        jsv.setOnJoystickMoveListener(new OnJoystickMoveListener() {
			@Override
			public void onValueChanged(int angle, int power, int direction) {
				if (direction == JoystickView.BOTTOM || direction == JoystickView.FRONT || 
					direction == JoystickView.LEFT || direction == JoystickView.RIGHT) { 
					
					GameLevel.getInstance().getBoard().actionMovePlayer(direction);
				}
			}
		});
	}

	private void setupSounds() {
        try {
        	//not emulator
        	if (!(android.os.Build.MODEL.equals("google_sdk") || android.os.Build.MODEL.equals("sdk"))) { 
            	playerGameSound = MediaPlayer.create(this, R.raw.game_sound);
    	        playerGameSound.setAudioStreamType(AudioManager.STREAM_MUSIC);
    	        playerGameSound.setLooping(true);
    			playerGameSound.start();        		   
        	}
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
}