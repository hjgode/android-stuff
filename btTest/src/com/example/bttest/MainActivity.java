package com.example.bttest;

import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainActivity extends Activity {

	private static final int ENABLE_BLUETOOTH = 1;
	
	//a log view
	TextView mTxtOutput;
	BluetoothAdapter bluetooth;
    ToggleButton btOnOffButton=null;
    IntentFilter btFilter=new IntentFilter();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		setContentView(R.layout.activity_main);
		
		bluetooth = BluetoothAdapter.getDefaultAdapter(); 
		if(bluetooth==null){
			showToast("No Bluetooth Adapter!");
			addText("No BT Adapter");
		}
		else{
			addText("BT adapter found");
		}
		
		setupBTbroadcastReceiver();
	    
	    //initBluetooth();
		
		btOnOffButton=(ToggleButton)findViewById(R.id.toggleButton1);
		setToggleButtonOnClickListener();
		if(bluetooth!=null){
			if(bluetooth.isEnabled())
				btOnOffButton.setChecked(true);
			else
				btOnOffButton.setChecked(false);
		}
		
		mTxtOutput = (TextView)findViewById(R.id.txtOutput);
		//mTxtOutput.setMovementMethod(ScrollingMovementMethod.getInstance());
		mTxtOutput.setMovementMethod(new ScrollingMovementMethod());
		
		//add some lines of text
		//for(int i=0; i<30; i++)
		//	addText("Line " +  i);
		}catch(Exception ex){
			Log.d("btTest","Exception in OnCreat(): " + ex.getMessage());
		}
	}

	@Override
	public void onPause(){
		super.onPause();
	    // Unregister broadcast listeners
	    this.unregisterReceiver(onBTchange);
	}
    
	@Override
    public void onDestroy(){
    	super.onDestroy();
	    // Unregister broadcast listeners
	    this.unregisterReceiver(onBTchange);
    }
    
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		this.registerReceiver(onBTchange, btFilter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	void setupBTbroadcastReceiver(){
	    // Register for broadcasts on BluetoothAdapter state change
	    // Bluetooth on/off broadcasts
        btFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // Discovery broadcasts
        btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        btFilter.addAction(BluetoothDevice.ACTION_FOUND);
        btFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
	    //register receiver	    
	    this.registerReceiver(onBTchange, btFilter);
	    addText("broadcast receiver setup OK");		
	}
	void showToast(String message){
		Context context = this;
		String msg = message;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, msg, duration);
		toast.show();
	}
	
 	void setToggleButtonOnClickListener(){
		btOnOffButton=(ToggleButton)findViewById(R.id.toggleButton1);
		btOnOffButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(bluetooth.isEnabled()){
					showToast("BT disabling not allowed");
					btOnOffButton.setChecked(true);
					//stopBluetooth();
				}
				else{
					startBluetooth();
					//bluetooth.enable();
				}
				//updateToggleButton();
			}
		});
    }
    void addText(String s){
    	
    	if(mTxtOutput==null){
    		mTxtOutput=(TextView)findViewById(R.id.txtOutput);    		
    	}
    	mTxtOutput.append(s + "\n");

		final Layout layout = mTxtOutput.getLayout();
		if(layout != null){
		int scrollDelta = layout.getLineBottom(mTxtOutput.getLineCount() - 1) - mTxtOutput.getScrollY() - mTxtOutput.getHeight();
		if(scrollDelta > 0)
			mTxtOutput.scrollBy(0, scrollDelta);
		}
        
    	//mTxtOutput.refreshDrawableState();
    	
    	Log.d("btTest", s);
    }
    
    //receiver for BT state changes
    private final BroadcastReceiver onBTchange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	String prevStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
        	String stateExtra = BluetoothAdapter.EXTRA_STATE;
        	int state = intent.getIntExtra(stateExtra, -1);
        	int previousState = intent.getIntExtra(prevStateExtra, -1);
	  	  String action = intent.getAction();
	  	  Log.d("btTest", "onReceive: " + action);
	  	  String tt="";
			switch (state) {
				case BluetoothAdapter.STATE_OFF:
					addText("Bluetooth off");
					tt = "Bluetooth off";
					btOnOffButton.setChecked(false);
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					addText("Turning Bluetooth off...");
					tt = "Bluetooth turning off";
					break;
				case BluetoothAdapter.STATE_ON:
					addText("Bluetooth on");
					tt = "Bluetooth on";
					btOnOffButton.setChecked(true);
					break;
				case BluetoothAdapter.STATE_TURNING_ON:
					tt = "Bluetooth turning on";
					addText("Turning Bluetooth on...");
					break;
			}
			Log.d("btTest", tt);
        }
    };

    private void startBluetooth() {
        if (!bluetooth.isEnabled()) {
      	  addText("Bluetooth was off");
          // Bluetooth isn't enabled, prompt the user to turn it on.
          Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
          startActivityForResult(intent, ENABLE_BLUETOOTH);
        } else {
      	  addText("Bluetooth was on");
          // Bluetooth is enabled, initialize the UI.
          //initBluetoothUI();
        }
        if (bluetooth.isEnabled())
      	  addText("Bluetooth is on");
        else
      	  addText("Bluetooth is off");
      }

    protected void onActivityResult(int requestCode,
		int resultCode, Intent data) {
		if (requestCode == ENABLE_BLUETOOTH)
    		if (resultCode == RESULT_OK) {
    			// Bluetooth has been enabled, initialize the UI.
    			//initBluetoothUI();
    			addText("BT has been enabled");
    			btOnOffButton.setChecked(true);
    		}
    }
    
	private void stopBluetooth() {
	    if (bluetooth.isEnabled()) {
	    	showToast("BT off not implemented");
	        return;
	    } else {
	  	  addText("Bluetooth already off");
	      // Bluetooth is enabled, initialize the UI.
	      //initBluetoothUI();
	    }
	  }
	}

