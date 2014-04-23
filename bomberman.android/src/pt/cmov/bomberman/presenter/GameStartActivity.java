package pt.cmov.bomberman.presenter;

import pt.cmov.bomberman.R;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameStartActivity extends ActionBarActivity {

	Handler updateConversationHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_start);

		if (getActionBar() != null)
			getActionBar().hide();

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.ctnScreenContainer, new PlaceholderFragment())
					.commit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_game_start,
					container, false);
			return rootView;
		}
	}

	public void buttonCreateRoom(View v) {
				
		/*Intent intent = new Intent(this, GameArenaActivity.class);
		startActivity(intent);*/
	}

	public void buttonMultiPlayer(View v) {
		// TODO Show form to connect to existing server
		/*
		Intent intent = new Intent(this, GameArenaActivity.class);
		startActivity(intent);
		String message = "coisas";
		new ClientComunicatorTask().execute(message);
		*/
	}

	protected void onStop() {
		super.onStop();
		/*try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
