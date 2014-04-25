package pt.cmov.bomberman.presenter.activity;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.model.GameLevel;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameStartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // making it full screen
        getWindow().setFlags(
        	WindowManager.LayoutParams.FLAG_FULLSCREEN,
        	WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_game_start);
	}
	
	public void buttonCreateRoom(View v) {
		Intent intent = new Intent(this, GameArenaActivity.class);
		intent.putExtra("isHost", true);
		startActivity(intent);
	}

	public void buttonMultiPlayer(View v) {
		// TODO Show form to connect to existing server (temporarily hardcoded)
		Intent intent = new Intent(this, GameArenaActivity.class);
		intent.putExtra("BombermanServerIP", "10.0.2.2");
		intent.putExtra("BombermanServerPort", 6000);
		queryPlayerName(intent);
	}
	
	private void queryPlayerName(final Intent intent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Player Name");

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        GameLevel.getInstance().setPlayer_name(input.getText().toString());
		        startActivity(intent);
		    }
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});
		
		builder.setCancelable(false);
		builder.show();
	}
}