package intermec.example.bluetoothprinting;

import android.os.AsyncTask;
import android.os.Bundle;
//import android.os.Environment;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothTexting extends Activity {
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.main);
		// Get the Bluetooth Adapter
		configureBluetooth();
		// Setup the ListView of discovered devices
		setupListView();
		// Setup search button
		setupSearchButton();
		// Setup listen button
		setupListenButton();
	}


	private BluetoothAdapter bluetooth;
	private BluetoothSocket socket;
	private UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
	private void configureBluetooth() {
		bluetooth = BluetoothAdapter.getDefaultAdapter();
	}
	private void switchUI() {
		final TextView messageText = (TextView)findViewById(R.id.text_messages);
		final EditText textEntry = (EditText)findViewById(R.id.text_message);
		messageText.setVisibility(View.VISIBLE);
		list.setVisibility(View.GONE);
		textEntry.setEnabled(true);
		textEntry.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
				if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
					sendMessage(socket, textEntry.getText().toString());
					textEntry.setText("");
					return true;
				}
				return false;
			}
		});
	}
	private void sendMessage(BluetoothSocket socket, String msg) {
		OutputStream outStream;
		try {
			outStream = socket.getOutputStream();
			byte[] byteString = (msg + " ").getBytes();
			stringAsBytes[byteString.length − 1] = 0;
			outStream.write(byteString);
		} catch (IOException e) {
			Log.d("BLUETOOTH_COMMS", e.getMessage());
		}
	}
	private static int DISCOVERY_REQUEST = 1;
	private void setupListenButton() {
		Button listenButton = (Button)findViewById(R.id.button_listen);
		listenButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				intent disc;
				disc = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				startActivityForResult(disc, DISCOVERY_REQUEST);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent
			data) {
		if (requestCode == DISCOVERY_REQUEST) {
			boolean isDiscoverable = resultCode > 0;
			if (isDiscoverable) {
				String name = "bluetoothserver";
				try {
					final BluetoothServerSocket btserver =
							bluetooth.listenUsingRfcommWithServiceRecord(name, uuid);
					AsyncTask<Integer, Void, BluetoothSocket> acceptThread =
							new AsyncTask<Integer, Void, BluetoothSocket>() {
						@Override
						protected BluetoothSocket doInBackground(Integer . . .params) {

							try {
								socket = btserver.accept(params[0]*1000);
								return socket;
							} catch (IOException e) {
								Log.d("BLUETOOTH", e.getMessage());
							}

							return null;
						}
						@Override
						protected void onPostExecute(BluetoothSocket result) {
							if (result != null)
								switchUI();
						}
					};
					acceptThread.execute(resultCode);
				} catch (IOException e) {
					Log.d("BLUETOOTH", e.getMessage());
				}
			}
		}
	}
	private ArrayList<BluetoothDevice> foundDevices;
	private ArrayAdapter<BluetoothDevice> aa;
	private ListView list;
	private void setupListView() {
		aa = new ArrayAdapter<BluetoothDevice>(this,
				android.R.layout.simple_list_item_1,
				foundDevices);
		list = (ListView)findViewById(R.id.list_discovered);
		list.setAdapter(aa);
	}
	BroadcastReceiver discoveryResult = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			BluetoothDevice remoteDevice;
			remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if (bluetooth.getBondedDevices().contains(remoteDevice)) {
				foundDevices.add(remoteDevice);
				aa.notifyDataSetChanged();
			}
		}

	};
	private void setupSearchButton() {
		Button searchButton = (Button)findViewById(R.id.button_search);
		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				registerReceiver(discoveryResult,
						new IntentFilter(BluetoothDevice.ACTION_FOUND));
				if (!bluetooth.isDiscovering()) {
					foundDevices.clear();
					bluetooth.startDiscovery();
				}
			}
		});
	}
	private void setupListView() {
		aa = new ArrayAdapter<BluetoothDevice>(this,
				android.R.layout.simple_list_item_1,
				foundDevices);
		list = (ListView)findViewById(R.id.list_discovered);
		list.setAdapter(aa);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int index, long arg3) {
				AsyncTask<Integer, Void, Void> connectTask =
						new AsyncTask<Integer, Void, Void>() {
					@Override
					protected Void doInBackground(Integer . . . params) {
						try {
							BluetoothDevice device = foundDevices.get(params[0]);
							socket = device.createRfcommSocketToServiceRecord(uuid);
							socket.connect();
						} catch (IOException e) {
							Log.d("BLUETOOTH_CLIENT", e.getMessage());
						}
						return null;
					}
					@Override
					protected void onPostExecute(Void result) {

						switchViews();
					}
				};
				connectTask.execute(index);
			}
		});
	}

}
