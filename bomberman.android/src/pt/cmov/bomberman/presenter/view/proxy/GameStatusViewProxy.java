package pt.cmov.bomberman.presenter.view.proxy;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.model.GameLevel;
import android.view.View;
import android.widget.TextView;
import android.os.CountDownTimer;

public class GameStatusViewProxy {

	private final View gameStatusView;
	private final TextView txtPlayerName;
	private final TextView txtPlayerScore;
	private final TextView txtGameTimeLeft;
	private final TextView txtNumberOfPlayers;
	
	private boolean initialized;

	public GameStatusViewProxy(View gameStatusView) {
		this.gameStatusView = gameStatusView;

		txtPlayerName = (TextView) this.gameStatusView.findViewById(R.id.txtPlayerName);
		txtPlayerScore = (TextView) this.gameStatusView.findViewById(R.id.txtScore);
		txtGameTimeLeft = (TextView) this.gameStatusView.findViewById(R.id.txtTime);
		txtNumberOfPlayers = (TextView) this.gameStatusView.findViewById(R.id.txtNumPlayers);
		
		initialized = false;
	}

	private void init(final GameLevel gameState) {
		txtPlayerName.setText(gameState.getPlayer_name());
		txtPlayerScore.setText("000"); // TODO score
		txtNumberOfPlayers.setText(String.format("%3d", 1)); // TODO count players
		txtGameTimeLeft.setText(Integer.toString(gameState.getTimeLeft()));
		(new CountDownTimer(1000 * gameState.getTimeLeft(), 1000) {
			public void onTick(final long millisUntilFinished) {
				int left;
				txtGameTimeLeft.setText(Integer.toString(left = ((int) millisUntilFinished / 1000)));
				gameState.setTimeLeft(left);
			}
			
			public void onFinish() {
				txtGameTimeLeft.setText("000");
				gameState.setTimeLeft(0);
				gameState.getBoard().setWinner();
				gameState.setGameOver();
			}
		}).start();		
	}
	
	public void update(final GameLevel gameState) {
		gameStatusView.getHandler().post(new Runnable() {
			@Override
			public void run() {
				if (!initialized) {
					initialized = true;
					init(gameState);
				}
				int newScore = gameState.getPlayerScore();
				if (newScore != -1)
					txtPlayerScore.setText(Integer.toString(newScore));
			}
		});
	}
}
