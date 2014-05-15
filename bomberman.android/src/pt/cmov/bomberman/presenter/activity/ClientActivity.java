package pt.cmov.bomberman.presenter.activity;

import pt.cmov.bomberman.R;
import pt.utl.ist.cmov.wifidirect.SimWifiP2pDeviceList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ClientActivity extends Activity {

	private SimWifiP2pDeviceList devices;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		devices = (SimWifiP2pDeviceList) b.get("groupDeviceList");

	}
}
