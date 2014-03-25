package com.intermec.barcodeexample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.intermec.aidc.*;

public class MainActivity extends Activity {
	
	private static com.intermec.aidc.BarcodeReader bcr;
	private static com.intermec.aidc.VirtualWedge wedge;

	Button btnBarcode, btnSymbology, btnSymbologyOptions;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	//set lock the orientation 
		//otherwise, the onDestory will trigger
		//when orientation changes
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
        // Make sure the BarcodeReader depended service is connected and
        // register a callback for service connect and disconnect events.
        AidcManager.connectService(this, new AidcManager.IServiceListener() {
            public void onConnect()
            {      	
            	
                // The depended service is connected and it is ready
                // to receive bar code requests and virtual wedge
            	try {
            		//Initial bar code reader instance
					bcr = new BarcodeReader();
					
					//disable virtual wedge
					wedge = new VirtualWedge();
	            	wedge.setEnable(false);				
					
				} catch (BarcodeReaderException e) 
				{					
					e.printStackTrace();
				}
            	catch (VirtualWedgeException e)
				{
					e.printStackTrace();
				}  
            	
            	//set action for other activities
            	ActivitySetting();
            }
             
            public void onDisconnect()
            {
            	//add disconnect message/action here
            }
        
        });	   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    } 
        
    static BarcodeReader getBarcodeObject()
    {
    	return bcr;
    }
        
    public void ActivitySetting()
    {
		btnBarcode = (Button)findViewById(R.id.buttonBarcode);
	    btnSymbology = (Button)findViewById(R.id.buttonSymbology);
	    btnSymbologyOptions = (Button)findViewById(R.id.buttonSymbologyOptions);
	      
	      
	    btnBarcode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				//get the intent action string from AndroidManifest.xml
				Intent barcodeIntent = new Intent("android.intent.action.BARCODEACTIVITY");
				startActivity(barcodeIntent);				
			}
	    });
	      
	    btnSymbology.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				//get the intent action string from AndroidManifest.xml
				Intent symbologyIntent = new Intent("android.intent.action.SYMBOLOGYACTIVITY");
				startActivity(symbologyIntent);
			}
		});
	      
	    btnSymbologyOptions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				//get the intent action string from AndroidManifest.xml
				Intent symbologyOptionsIntent = new Intent("android.intent.action.SYMBOLOGYOPTIONSACTIVITY");
				startActivity(symbologyOptionsIntent);
			}
		});       
    }
    
    @Override
    protected void onDestroy() {
       	super.onDestroy();   	
    	
		try 
		{			
			if(wedge != null)
			{
				wedge.setEnable(true);
				wedge = null;
			}
			
			if(bcr != null)
	 		{						
	 			bcr.close();
	 			bcr = null;			
	 		}	
			
		} catch (VirtualWedgeException e) {			
			e.printStackTrace();
		}	
 		
 		//disconnect from data collection service
 		AidcManager.disconnectService();         
    }
    
}
