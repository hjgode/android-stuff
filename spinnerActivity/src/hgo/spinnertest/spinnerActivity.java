package hgo.spinnertest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import hgo.spinnertest.*;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.ListActivity;
import android.widget.*;

public class spinnerActivity extends Activity{
	EditText _selectedText=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try{
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.main);
	      
	      _selectedText=(EditText)findViewById(R.id.editText1);
	      
	      //
	      initSpinner();
	
	      setSelectButtonOnClickListener();
	    }
    	catch(Exception ex){
    		Log.d("spinnerTest", "Exception: "+ex.getMessage());
        }
    }
    
    void setSelectButtonOnClickListener(){
    	Button selectDevice=(Button)findViewById(R.id.button1);
    	selectDevice.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(_selectedText==null)
					_selectedText = (EditText)findViewById(R.id.editText1);
				if(spinner1==null)
					spinner1 = (Spinner) findViewById(R.id.spinner1);
				
				_selectedText.setText(spinner1.getSelectedItem().toString());
				_selectedText.refreshDrawableState();
			}
		});
    }
	
	Spinner spinner1;
	String _selectedItem="";
	ArrayList<String> btStringList=new ArrayList<String>();
	ArrayAdapter<String> dataAdapter;
	CustomOnItemSelectedListener listItemListener;
	private void initSpinner() {
		btStringList.clear();
		btStringList.add("Zeile1");
		btStringList.add("Zeile2");
		btStringList.add("Zeile3");
		
		if(spinner1==null)
			spinner1 = (Spinner) findViewById(R.id.spinner1);
		
		dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, btStringList);
		
		//dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spinner1.setAdapter(dataAdapter);

		   spinner1.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  long arg3) {
					// TODO Auto-generated method stub
					_selectedItem = spinner1.getSelectedItem().toString();
					_selectedText.setText(_selectedItem);
				}
		   });
		   
		//listItemListener=new CustomOnItemSelectedListener();		   
		//spinner1.setOnItemSelectedListener(listItemListener); 
	}
	
}