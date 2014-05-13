package pt.cmov.bomberman.presenter.activity;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.util.DebouncedOnClickListener;
import pt.cmov.bomberman.net.wifidirect.SimWifiP2pBroadcastReceiver;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDevice;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pInfo;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.Channel;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.GroupInfoListener;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.utl.ist.cmov.wifidirect.service.SimWifiP2pService;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameStartActivity extends Activity implements PeerListListener,
		GroupInfoListener {

	private boolean mBound = false;
	private SimWifiP2pManager mManager = null;
	private Channel mChannel = null;
	private Messenger mService = null;

	public SimWifiP2pManager getManager() {
		return mManager;
	}

	public Channel getChannel() {
		return mChannel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_game_start);

		((Button) this.findViewById(R.id.btnNewGame))
				.setOnClickListener(new DebouncedOnClickListener(1000) {
					@Override
					public void onDebouncedClick(View v) {
						btnHostGame_DebouncedClick(v);
					}
				});

		((Button) this.findViewById(R.id.btnPlayOnline))
				.setOnClickListener(new DebouncedOnClickListener(1000) {
					@Override
					public void onDebouncedClick(View v) {
						btnPlayOnline_DebouncedClick(v);
					}
				});

		// initialize the WDSim API
		SimWifiP2pSocketManager.Init(getApplicationContext());

		// register broadcast receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
		filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
		SimWifiP2pBroadcastReceiver receiver = new SimWifiP2pBroadcastReceiver(
				this);
		registerReceiver(receiver, filter);
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

				GameLevel.getInstance().setPlayer_name(
						input.getText().toString());
				startActivity(intent);
			}
		});
	}

	private void queryPlayerName(final Intent intent, EditText input,
			OnClickListener okClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Player Name");

		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", okClickListener);

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.setCancelable(false);
		builder.show();
	}

	/*
	 * Listeners associated to buttons
	 */

	public void wifiOnButton(View v) {

		Intent intent = new Intent(this.getBaseContext(),
				SimWifiP2pService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mBound = true;

		// spawn the chat server background task
		// new IncommingCommTask().execute();

		guiUpdateDisconnectedState();
	}

	public void wifiOffButton(View v) {

		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	public void inGroupList(View v) {
		if (mBound) {
			mManager.requestGroupInfo(mChannel,
					(GroupInfoListener) GameStartActivity.this);
		} else {
			Toast.makeText(v.getContext(), "Service not bound",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void guiUpdateDisconnectedState() {
		// TODO Auto-generated method stub

	}

	private void enableButton(Button btn, boolean enable) {
		btn.setEnabled(enable);
		btn.setClickable(enable);
		btn.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		// callbacks for service binding, passed to bindService()

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			mManager = new SimWifiP2pManager(mService);
			mChannel = mManager.initialize(getApplication(), getMainLooper(),
					null);
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mService = null;
			mManager = null;
			mChannel = null;
			mBound = false;
		}
	};

	@Override
	public void onGroupInfoAvailable(SimWifiP2pDeviceList devices,
			SimWifiP2pInfo groupInfo) {
		
		// compile list of network members
		StringBuilder peersStr = new StringBuilder();
		for (String deviceName : groupInfo.getDevicesInNetwork()) {
			SimWifiP2pDevice device = devices.getByName(deviceName);
			String devstr = "" + deviceName + " (" + 
				((device == null)?"??":device.getVirtIp()) + ")\n";
			peersStr.append(devstr);
		}

		// display list of network members
		new AlertDialog.Builder(this)
	    .setTitle("Devices in WiFi Network")
	    .setMessage(peersStr.toString())
	    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        }
	     })
	     .show();

	}

	@Override
	public void onPeersAvailable(SimWifiP2pDeviceList peers) {
		StringBuilder peersStr = new StringBuilder();
		
		// compile list of devices in range
		for (SimWifiP2pDevice device : peers.getDeviceList()) {
			String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
			peersStr.append(devstr);
		}

		// display list of devices in range
		new AlertDialog.Builder(this)
	    .setTitle("Devices in WiFi Range")
	    .setMessage(peersStr.toString())
	    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        }
	     })
	     .show();

	}
}