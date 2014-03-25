package com.intermec.barcodeexample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.intermec.aidc.*;

public class SymbologyOptionsActivity extends Activity{

	private com.intermec.aidc.BarcodeReader bcr;
	private EditText editPreamble, editPostamble;
	private Spinner spSymbologyId;
	private String array_spinner[];
	private Button btnSet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_symbology_options);
		
		//set lock the orientation 
		//otherwise, the onDestory will trigger
		//when orientation changes
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		bcr = MainActivity.getBarcodeObject();
		if(bcr != null)
		{
			getIntialSymbologyOptions();			
			
		}
		
	}
	
	private void getIntialSymbologyOptions()
	{		
		try
		{
			//get the preamble value
			editPreamble = (EditText)findViewById(R.id.editTextPreamble);
			editPreamble.setText(bcr.symbologyOptions.getPreamble());	
			
			//get postamble value
			editPostamble = (EditText)findViewById(R.id.editTextPostamble);
			editPostamble.setText(bcr.symbologyOptions.getPostamble());	
					
			//get symbology identifier array, 
			//array index values are same as options value
			array_spinner=new String[4];
	        array_spinner[0]="DISABLE";
	        array_spinner[1]="CODE_MARK";
	        array_spinner[2]="AIM_ISO_IEC_STANDARD";
	        array_spinner[3]="USER_DEFINED";      
	        
	        spSymbologyId = (Spinner) findViewById(R.id.spinnerSymbologyIdentifier);   
	        
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
	        									android.R.layout.simple_spinner_item, array_spinner);
	        spSymbologyId.setAdapter(adapter);
	        
	        int symbId = bcr.symbologyOptions.getSymbologyIdentifier();      
	        spSymbologyId.setSelection(symbId);
	     	        
	        //button set
	        btnSet = (Button)findViewById(R.id.buttonSet);  
	        
	        btnSet.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//set options
					setSelectedSymbologyOptions();	
				}
			});   	
	        
		}
		catch (SymbologyOptionsException e)
		{
			e.getStackTrace();
		}
	}
	
	public void setSelectedSymbologyOptions()
	{
		try
		{
			//set preamble
			bcr.symbologyOptions.setPreamble(editPreamble.getText().toString());
			
			//set postamble
			bcr.symbologyOptions.setPostamble(editPostamble.getText().toString());
			
			//set symbology identifier
			bcr.symbologyOptions.setSymbologyIdentifier(spSymbologyId.getSelectedItemPosition());
			
			Toast.makeText(this, "Done symbology optios settings.", Toast.LENGTH_SHORT).show();
			
		}
		catch (SymbologyOptionsException e)
		{
			e.getStackTrace();
		}
	}
}
