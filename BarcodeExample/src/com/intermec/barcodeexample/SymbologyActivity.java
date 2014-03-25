package com.intermec.barcodeexample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class SymbologyActivity extends Activity{
	
	com.intermec.aidc.BarcodeReader bcr;
	SymbologyAdapter dataAdapter = null;
	
	//number of symbologies for enabling and disabling
	private static final int SYMBOLOGY_SIZE = 40;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_symbology);
		
		//set lock the orientation 
		//otherwise, the onDestory will trigger
		//when orientation changes
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//get BarcodeReader instance
		bcr = MainActivity.getBarcodeObject();
		
		if(bcr != null)
		{
			//Generate list View from ArrayList
			displaySymbologyListView();			
		}
	}
	
	 ListView listView;
	 int selctedPos;
	 
	 SymbologyItems selectedItem;
	
	private void displaySymbologyListView()
	{
		  //Array list of symbologies
		  ArrayList<SymbologyItems> symbologyList = new ArrayList<SymbologyItems>();
		  
		  for (int i = 1; i<= SYMBOLOGY_SIZE; i++)
		  {
			  ArrayList<Object> itemList = getSymbologyStatus(i);			  
			  SymbologyItems symbItem = new SymbologyItems(i, (String)itemList.get(0), (Boolean)itemList.get(1));
			  symbologyList.add(symbItem);
		  }		
	
		  //create an ArrayAdaptar from the String Array
		  dataAdapter = new SymbologyAdapter(this, R.layout.symbology_info, symbologyList);
		  
		  listView = (ListView) findViewById(R.id.listViewSymbology);
		
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);			  
		  		
		  listView.setOnItemClickListener(new OnItemClickListener() {			  
	
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				//get the selected symbology item 
				SymbologyItems symbItem = (SymbologyItems) parent.getItemAtPosition(position);				
				
				CheckBox chkBox = (CheckBox) view.findViewById(R.id.checkBoxStatus);	
				
				if(chkBox.isChecked())
				{					
					//uncheck item if it was checked
					chkBox.setChecked(false);
					setSymbologyStatus(symbItem.getId(), false);
				}
				else
				{			
					//check item if it was unchecked
					chkBox.setChecked(true);
					setSymbologyStatus(symbItem.getId(), true);
				}				
			}
			  
		  });			    
	}	
	
	private ArrayList<Object> getSymbologyStatus(int id)
	{
		boolean status = false;
		String name = null;			
		ArrayList<Object> dataList = new ArrayList<Object>();
		
		switch (id)
		{
		case 1:
			name = "Australian Post";
			status = bcr.symbology.australianPost.isEnabled();			
			break;
		case 2:
			name = "Aztec";
			status = bcr.symbology.aztec.isEnabled();
			break;
		case 3:
			name = "Bpo";
			status = bcr.symbology.bpo.isEnabled();
			break;
		case 4:
			name = "Canada Post";
			status = bcr.symbology.canadaPost.isEnabled();
			break;
		case 5:
			name = "Codabar";
			status = bcr.symbology.codabar.isEnabled();
			break;
		case 6:
			name = "Codablock A";
			status = bcr.symbology.codablockA.isEnabled();
			break;
		case 7:
			name = "Codablock F";
			status = bcr.symbology.codablockF.isEnabled();
			break;
		case 8:
			name = "Code 11";
			status = bcr.symbology.code11.isEnabled();
			break;
		case 9:
			name = "Code 128";
			status = bcr.symbology.code128.isEnabled();		
			break;			
		case 10:
			name = "Code 39";
			status = bcr.symbology.code39.isEnabled();
			break;	
		case 11:
			name = "Code 93";
			status = bcr.symbology.code93.isEnabled();
			break;	
		case 12:
			name = "Datamatrix";
			status = bcr.symbology.datamatrix.isEnabled();
			break;
		case 13:
			name = "Dutch Post";
			status = bcr.symbology.dutchPost.isEnabled();
			break;
		case 14:
			name = "EanUpc.Ean13";
			status = bcr.symbology.eanUpc.isEan13Enabled();
			break;
		case 15:
			name = "EanUpc.Ean8";
			status = bcr.symbology.eanUpc.isEan8Enabled();
			break;
		case 16:
			name = "EanUpc.UPCA";
			status = bcr.symbology.eanUpc.isUPCAEnabled();
			break;
		case 17:
			name = "EanUpc.UPCE";
			status = bcr.symbology.eanUpc.isUPCEEnabled();
			break;
		case 18:
			name = "EanUpc.UPCE1";
			status = bcr.symbology.eanUpc.isUPCE1Enabled();
			break;			
		case 19:
			name = "Gs1 Composite";
			status = bcr.symbology.gs1Composite.isEnabled();
			break;				
		case 20:
			name = "Gs1 Databar Expanded";
			status = bcr.symbology.gs1DataBarExpanded.isEnabled();
			break;	
		case 21:
			name = "Gs1 Databar Limited";
			status = bcr.symbology.gs1DataBarLimited.isEnabled();
			break;	
		case 22:
			name = "Gs1 Databar Omni Directional";
			status = bcr.symbology.gs1DataBarOmniDirectional.isEnabled();
			break;	
		case 23:
			name = "Han Xin";
			status = bcr.symbology.hanXin.isEnabled();
			break;
		case 24:
			name = "Infomail";
			status = bcr.symbology.infomail.isEnabled();
			break;
		case 25:
			name = "Intelligen Mail";
			status = bcr.symbology.intelligentMail.isEnabled();
			break;
		case 26:
			name = "Interleaved 2 of 5";
			status = bcr.symbology.interleaved2Of5.isEnabled();
			break;
		case 27:
			name = "Japan Post";
			status = bcr.symbology.japanPost.isEnabled();
			break;
		case 28:
			name = "Matrix 2 of 5";
			status = bcr.symbology.matrix2Of5.isEnabled();
			break;
		case 29:
			name = "Maxicode";
			status = bcr.symbology.maxicode.isEnabled();
			break;
		case 30:
			name = "Micro PDF 417";
			status = bcr.symbology.microPdf417.isEnabled();
			break;
		case 31:
			name = "MSI";
			status = bcr.symbology.msi.isEnabled();
			break;
		case 32:
			name = "PDF 417";
			status = bcr.symbology.pdf417.isEnabled();
			break;
		case 33:
			name = "Planet";
			status = bcr.symbology.planet.isEnabled();
			break;
		case 34:
			name = "Plessey";
			status = bcr.symbology.plessey.isEnabled();
			break;
		case 35:
			name = "Postnet";
			status = bcr.symbology.postnet.isEnabled();
			break;
		case 36:
			name = "QR Code";
			status = bcr.symbology.qrCode.isEnabled();
			break;
		case 37:
			name = "Standard 2 of 5";
			status = bcr.symbology.standard2Of5.isEnabled();
			break;
		case 38:
			name = "Sweden Post";
			status = bcr.symbology.swedenPost.isEnabled();
			break;
		case 39:
			name = "Telepen";
			status = bcr.symbology.telepen.isEnabled();
			break;
		case 40:
			name = "TLC 39";
			status = bcr.symbology.tlc39.isEnabled();
			break;
		}			
		
		dataList.add(0, name);
		dataList.add(1, status);
		
		return dataList;
	}
	
	private void setSymbologyStatus(int id, Boolean isEnabled)
	{
		switch (id)
		{
		case 1:
			bcr.symbology.australianPost.setEnable(isEnabled);
			break;
		case 2:
			bcr.symbology.aztec.setEnable(isEnabled);
			break;
		case 3:
			bcr.symbology.bpo.setEnable(isEnabled);
			break;
		case 4:
			bcr.symbology.canadaPost.setEnable(isEnabled);
			break;
		case 5:
			bcr.symbology.codabar.setEnable(isEnabled);
			break;
		case 6:
			bcr.symbology.codablockA.setEnable(isEnabled);
			break;
		case 7:
			bcr.symbology.codablockF.setEnable(isEnabled);
			break;
		case 8:
			bcr.symbology.code11.setEnable(isEnabled);
			break;
		case 9:
			bcr.symbology.code128.setEnable(isEnabled);
			break;			
		case 10:		
			bcr.symbology.code39.setEnable(isEnabled);
			break;	
		case 11:
			bcr.symbology.code93.setEnable(isEnabled);
			break;	
		case 12:
			bcr.symbology.datamatrix.setEnable(isEnabled);
			break;
		case 13:
			bcr.symbology.dutchPost.setEnable(isEnabled);
			break;
		case 14:
			bcr.symbology.eanUpc.setEan13Enable(isEnabled);
			break;
		case 15:
			bcr.symbology.eanUpc.setEan8Enable(isEnabled);
			break;
		case 16:
			bcr.symbology.eanUpc.setUPCAEnable(isEnabled);
			break;
		case 17:
			bcr.symbology.eanUpc.setUPCEEnable(isEnabled);
			break;
		case 18:
			bcr.symbology.eanUpc.setUPCE1Enable(isEnabled);
			break;				
		case 19:			
			bcr.symbology.gs1Composite.setEnable(isEnabled);
			break;				
		case 20:			
			bcr.symbology.gs1DataBarExpanded.setEnable(isEnabled);
			break;	
		case 21:		
			bcr.symbology.gs1DataBarLimited.setEnable(isEnabled);
			break;	
		case 22:			
			bcr.symbology.gs1DataBarOmniDirectional.setEnable(isEnabled);
			break;	
		case 23:		
			bcr.symbology.hanXin.setEnable(isEnabled);
			break;
		case 24:		
			bcr.symbology.infomail.setEnable(isEnabled);
			break;
		case 25:		
			bcr.symbology.intelligentMail.setEnable(isEnabled);
			break;
		case 26:		
			bcr.symbology.interleaved2Of5.setEnable(isEnabled);
			break;
		case 27:			
			bcr.symbology.japanPost.setEnable(isEnabled);
			break;
		case 28:			
			bcr.symbology.matrix2Of5.setEnable(isEnabled);
			break;
		case 29:			
			bcr.symbology.maxicode.setEnable(isEnabled);
			break;
		case 30:			
			bcr.symbology.microPdf417.setEnable(isEnabled);
			break;
		case 31:			
			bcr.symbology.msi.setEnable(isEnabled);
			break;
		case 32:		
			bcr.symbology.pdf417.setEnable(isEnabled);
			break;
		case 33:			
			bcr.symbology.planet.setEnable(isEnabled);
			break;
		case 34:			
			bcr.symbology.plessey.setEnable(isEnabled);
			break;
		case 35:		
			bcr.symbology.postnet.setEnable(isEnabled);
			break;
		case 36:	
			bcr.symbology.qrCode.setEnable(isEnabled);
			break;
		case 37:		
			bcr.symbology.standard2Of5.setEnable(isEnabled);
			break;
		case 38:			
			bcr.symbology.swedenPost.setEnable(isEnabled);
			break;
		case 39:
			bcr.symbology.telepen.setEnable(isEnabled);
			break;
		case 40:
			bcr.symbology.tlc39.setEnable(isEnabled);
			break;
		}
	}
	
	/************************************************************
	 * 
	 * SymbologyAdapter
	 *
	 ***********************************************************/	
	private class SymbologyAdapter extends ArrayAdapter<SymbologyItems>{
		private ArrayList<SymbologyItems> symbologyList;
		
		private Context myContext;
		
		public SymbologyAdapter(Context context, int textViewResourceId, ArrayList<SymbologyItems> symbologyList) 
		{
		    super(context, textViewResourceId, symbologyList);
		    this.symbologyList = new ArrayList<SymbologyItems>();
		    this.symbologyList.addAll(symbologyList);
		    this.myContext = context.getApplicationContext();		    
		}

		private class ViewHolder
		{	
			TextView name;
			CheckBox status;			
		}	
		
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent)	    
	    {	    	

	    	ViewHolder holder = null;			

			if (convertView == null)
			{
			   LayoutInflater vi = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   convertView = vi.inflate(R.layout.symbology_info, null);

			   holder = new ViewHolder();		  
			   holder.name = (TextView) convertView.findViewById(R.id.name);
			   holder.status = (CheckBox) convertView.findViewById(R.id.checkBoxStatus);			
			   convertView.setTag(holder);			    
			}
			else
			{
			    holder = (ViewHolder) convertView.getTag();
			}

			SymbologyItems symItem = symbologyList.get(position);			
			holder.name.setText(symItem.getName());
			holder.status.setChecked(symItem.isEnabled());
			holder.name.setTag(symItem);

			return convertView;
	    } 

	}
}
