package pt.cmov.bomberman.presenter.activity;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.util.DebouncedOnClickListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameStartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_game_start);

		((Button) this.findViewById(R.id.btnNewGame)).setOnClickListener(new DebouncedOnClickListener(1000) {
			@Override
			public void onDebouncedClick(View v) {
				btnHostGame_DebouncedClick(v);
			}
		});
		
		((Button) this.findViewById(R.id.btnPlayOnline)).setOnClickListener(new DebouncedOnClickListener(1000) {
			@Override
			public void onDebouncedClick(View v) {
				btnPlayOnline_DebouncedClick(v);
			}
		});
	}

	public void btnHostGame_DebouncedClick(View v) {
		enableButton((Button) v, false);

		final Intent intent = new Intent(this, GameArenaActivity.class);
		intent.putExtra("isHost", true);
		startGame(intent);

		enableButton((Button) v, true);
	}

	public void btnPlayOnline_DebouncedClick(View v) {
		enableButton((Button) v, false);

		// TODO Show form to connect to existing server (temporarily hardcoded)
		final Intent intent = new Intent(this, GameArenaActivity.class);
		intent.putExtra("BombermanServerIP", "10.0.2.2");
		intent.putExtra("BombermanServerPort", 6000);
		startGame(intent);

		enableButton((Button) v, true);
	}

	private void startGame(final Intent intent) {
		// gets the player name and starts the activity
		final EditText input = new EditText(this);
		queryPlayerName(intent, input, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GameLevel.getInstance().setPlayer_name(input.getText().toString());
				startActivity(intent);
			}
		});
	}

	private void queryPlayerName(final Intent intent, EditText input, OnClickListener okClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Player Name");

		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", okClickListener);

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.setCancelable(false);
		builder.show();
	}

	private void enableButton(Button btn, boolean enable) {
		btn.setEnabled(enable);
		btn.setClickable(enable);
		btn.setVisibility(enable ? View.VISIBLE : View.GONE);
	}
}