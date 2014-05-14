package pt.cmov.bomberman.presenter.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.model.GameLevel;
import pt.cmov.bomberman.net.wifidirect.SimWifiP2pBroadcastReceiver;
import pt.cmov.bomberman.util.DebouncedOnClickListener;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pBroadcast;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDevice;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pInfo;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.Channel;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.GroupInfoListener;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.utl.ist.cmov.wifidirect.service.SimWifiP2pService;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocketServer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GameStartActivity extends Activity implements PeerListListener,
		GroupInfoListener {
	public static final String TAG = "bomberman";

	private SimWifiP2pManager mManager = null;
	private Channel mChannel = null;
	private Messenger mService = null;
	private boolean mBound = false;
	private SimWifiP2pSocketServer mSrvSocket = null;
	private static ReceiveCommTask mComm = null;
	private static OutgoingCommTask mOutComm = null;
	private static IncommingCommTask mInComm = null;
	private SimWifiP2pSocket mCliSocket = null;
	private TextView mTextInput;
	private TextView mTextOutput;

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
		mTextInput = (TextView) findViewById(R.id.inputIp);

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
		mInComm = (IncommingCommTask) new IncommingCommTask()
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	public void wifiOffButton(View v) {

		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	public void inGroupList(View v) {
		if (mBound) {
			mManager.requestGroupInfo(mChannel, GameStartActivity.this);
		} else {
			Toast.makeText(v.getContext(), "Service not bound",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void sendButton(View v) {
		try {
			mCliSocket.getOutputStream().write(("merdinha" + "\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connectButton(View v) {
		Log.d(TAG, "ip:  " + mTextInput.getText().toString());
		mOutComm = (OutgoingCommTask) new OutgoingCommTask()
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mTextInput
						.getText().toString());
	}

	private void enableButton(Button btn, boolean enable) {
		btn.setEnabled(enable);
		btn.setClickable(enable);
		btn.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	private final ServiceConnection mConnection = new ServiceConnection() {
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

	/*
	 * Classes implementing chat message exchange
	 */

	public class IncommingCommTask extends
			AsyncTask<Void, SimWifiP2pSocket, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

			try {
				mSrvSocket = new SimWifiP2pSocketServer(
						Integer.parseInt(getString(R.string.port)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {
				try {
					SimWifiP2pSocket sock = mSrvSocket.accept();
					if (mCliSocket != null && mCliSocket.isClosed()) {
						mCliSocket = null;
					}
					if (mCliSocket != null) {
						Log.d(TAG,
								"Closing accepted socket because mCliSocket still active.");
						sock.close();
					} else {
						publishProgress(sock);
					}
				} catch (IOException e) {
					Log.d("Error accepting socket:", e.getMessage());
					break;
					// e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(SimWifiP2pSocket... values) {
			mCliSocket = values[0];
			mComm = new ReceiveCommTask();
			mComm.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mCliSocket);
		}
	}

	public void onDestroy() {
		mComm.cancel(true);
		mInComm.cancel(true);
		mOutComm.cancel(true);
	}

	public void destroyTasks(View v) {
		mInComm.cancel(true);
	}

	public class OutgoingCommTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "OutgoingCommTask connecting........");
			try {
				mCliSocket = new SimWifiP2pSocket(params[0],
						Integer.parseInt(getString(R.string.port)));
			} catch (UnknownHostException e) {
				return "Unknown Host:" + e.getMessage();
			} catch (IOException e) {
				return "IO error:" + e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				mTextOutput.setText(result);
			} else {
				mComm = new ReceiveCommTask();
				mComm.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						mCliSocket);
			}
		}
	}

	public class ReceiveCommTask extends
			AsyncTask<SimWifiP2pSocket, String, Void> {
		SimWifiP2pSocket s;

		@Override
		protected Void doInBackground(SimWifiP2pSocket... params) {
			BufferedReader sockIn;
			String st;

			s = params[0];
			try {
				sockIn = new BufferedReader(new InputStreamReader(
						s.getInputStream()));

				while ((st = sockIn.readLine()) != null) {
					Log.d(TAG, "merda + " + st);
					publishProgress(st);
				}
			} catch (IOException e) {
				Log.d("Error reading socket:", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(String... values) {
			Log.d(TAG, (values[0] + "\n"));
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!s.isClosed()) {
				try {
					s.close();
				} catch (Exception e) {
					Log.d("Error closing socket:", e.getMessage());
				}
			}
			s = null;
		}
	}

	@Override
	public void onPeersAvailable(SimWifiP2pDeviceList peers) {
		StringBuilder peersStr = new StringBuilder();

		// compile list of devices in range
		for (SimWifiP2pDevice device : peers.getDeviceList()) {
			String devstr = "" + device.deviceName + " (" + device.getVirtIp()
					+ ")\n";
			peersStr.append(devstr);
		}

		// display list of devices in range
		new AlertDialog.Builder(this)
				.setTitle("Devices in WiFi Range")
				.setMessage(peersStr.toString())
				.setNeutralButton("Dismiss",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();

	}

	@Override
	public void onGroupInfoAvailable(SimWifiP2pDeviceList devices,
			SimWifiP2pInfo groupInfo) {

		// compile list of network members
		StringBuilder peersStr = new StringBuilder();
		for (String deviceName : groupInfo.getDevicesInNetwork()) {
			SimWifiP2pDevice device = devices.getByName(deviceName);
			String devstr = "" + deviceName + " ("
					+ ((device == null) ? "??" : device.getVirtIp()) + ")\n";
			peersStr.append(devstr);
		}

		// display list of network members
		new AlertDialog.Builder(this)
				.setTitle("Devices in WiFi Network")
				.setMessage(peersStr.toString())
				.setNeutralButton("Dismiss",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();

	}
}