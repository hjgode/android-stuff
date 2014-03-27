package com.paad.bluetooth;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	String _selectedItem="";
	  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		  _selectedItem=parent.getItemAtPosition(pos).toString();
		  TextView tv = (TextView)view.findViewById(R.id.editMac);
		  if(tv!=null)
			  tv.setText(_selectedItem);
		  
		  //((Spinner)view).setSelection(pos);
		//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
	  }
	 
	  @Override
	  public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	  }
	  public String getSelectedItem(){
		  return _selectedItem;
	  }
	}