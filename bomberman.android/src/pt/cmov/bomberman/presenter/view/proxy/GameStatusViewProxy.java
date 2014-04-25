package pt.cmov.bomberman.presenter.view.proxy;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.model.GameLevel;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class GameStatusViewProxy {

	private final View gameStatusView;
	private final TextView txtPlayerName;
	private final TextView txtPlayerScore;
	private final TextView txtGameTimeLeft;
	private final TextView txtNumberOfPlayers;
	private boolean initialized;

	public GameStatusViewProxy(View gameStatusView) {
		this.gameStatusView = gameStatusView;
		initialized = false;

		txtPlayerName = (TextView) this.gameStatusView.findViewById(R.id.txtPlayerName);
		txtPlayerScore = (TextView) this.gameStatusView.findViewById(R.id.txtScore);
		txtGameTimeLeft = (TextView) this.gameStatusView.findViewById(R.id.txtTime);
		txtNumberOfPlayers = (TextView) this.gameStatusView.findViewById(R.id.txtNumPlayers);
	}

	private void init(final GameLevel gameState) {
		initialized = true;
		
		gameStatusView.getHandler().post(new Runnable() {
			@Override
			public void run() {
				
				txtPlayerName.setText(gameState.getPlayer_name());
				txtPlayerScore.setText("000");
				txtGameTimeLeft.setText("100");
				txtNumberOfPlayers.setText(String.format("%3d", 1));
				
				(new CountDownTimer(1000 * 100, 1000) {
					public void onTick(final long millisUntilFinished) {
						txtGameTimeLeft.setText("" + ((int) millisUntilFinished / 1000));
					}

					public void onFinish() {
						txtGameTimeLeft.setText("000");
					}
				}).start();
			}
		});
	}

	public void update(GameLevel gameState) {
		if (!initialized)
			init(gameState);

		// TODO
	}
}
