package com.intermec.lineprint.sample;

import com.intermec.print.lp.LinePrinter;
import com.intermec.print.lp.LinePrinterException;
import com.intermec.print.lp.PrintProgressEvent;
import com.intermec.print.lp.PrintProgressListener;

import android.os.AsyncTask;
import android.os.Bundle;
//import android.os.Environment;
import android.app.Activity;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.*;


/**
 * This sample demonstrates printing on Android using the Intermec LinePrinter
 * class. You may enter or scan an Intermec mobile printer's MAC address and
 * click the Print button to print. The MAC Address text should have the format
 * of "nn:nn:nn:nn:nn:nn" or "nnnnnnnnnnnn" where each n is a hex digit.
 * <p>
 * The printing progress will be displayed in the Progress and Status text box.
 */
public class PrintActivity extends Activity {
	private Button buttonPrint;
	private TextView textMsg;
	private EditText editMacAddr;
	private EditText editUserText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_print);
	
		textMsg = (TextView) findViewById(R.id.textMsg);
		editMacAddr = (EditText) findViewById(R.id.editMacAddr);
		// Set a default Mac Address
		editMacAddr.setText("00:1D:DF:55:6C:27");
		editUserText = (EditText) findViewById(R.id.editUserText);
		
		CopyAssetFiles();

		buttonPrint = (Button) findViewById(R.id.buttonPrint);
		buttonPrint.setOnClickListener(new OnClickListener() {
			public void onClick(View view)
			{
				// Create a PrintTask to do printing on a separate thread.
				PrintTask task = new PrintTask();
				
				// Executes PrintTask with the specified parameter which is passed
				// to the PrintTask.doInBackground method.
				task.execute(editMacAddr.getText().toString());
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.print, menu);
		return true;
	}
	
	private void CopyAssetFiles() 
	{
		// Copy the asset files we delivered with the application to a location
		// where the LinePrinter can access them.
		try
		{
			AssetManager assetManager = getAssets();
			String[] files = { "printer_profiles.JSON", "Signature.bmp" };
			
			for (String filename : files)
			{
				InputStream input = null;			
				OutputStream output = null;
						
				input = assetManager.open(filename);			
				File outputFile = new File(getExternalFilesDir(null), filename);
			
				output = new FileOutputStream(outputFile);
						
				byte[] buf = new byte[1024];			
				int len;			
				while ((len = input.read(buf)) > 0)			
				{				
					output.write(buf, 0, len);			
				}						
				input.close();			
				input = null;
						
				output.flush();			
				output.close();			
				output = null;
			}
		}
		catch (Exception ex)
		{
			textMsg.append("Error copying asset files.");	
		}		
	}

	/**
	 * This class demonstrates printing in a background thread and updates
	 * the UI in the UI thread.
	 */
	public class PrintTask extends AsyncTask<String, Integer, String> {
		private static final String PROGRESS_CANCEL_MSG = "Printing cancelled\n";
		private static final String PROGRESS_COMPLETE_MSG = "Printing completed\n";
		private static final String PROGRESS_ENDDOC_MSG = "End of document\n";
		private static final String PROGRESS_FINISHED_MSG = "Printer connection closed\n";
		private static final String PROGRESS_NONE_MSG = "Unknown progress message\n";
		private static final String PROGRESS_STARTDOC_MSG = "Start printing document\n";


		/**
		 * Runs on the UI thread before doInBackground(Params...).
		 */
		@Override
		protected void onPreExecute()
		{
			// Clears the Progress and Status text box.
			textMsg.setText("");
			
			// Disables the Print button.
			buttonPrint.setEnabled(false);
			
			// Shows a progress icon on the title bar to indicate
			// it is working on something.
			setProgressBarIndeterminateVisibility(true);
		}

		/**
		 * This method runs on a background thread. The specified parameters
		 * are the parameters passed to the execute method by the caller of 
		 * this task. This method can call publishProgress to publish updates
		 * on the UI thread.
		 */
		@Override
		protected String doInBackground(String... args)
		{
			String sResult = null;
			String sMacAddr = args[0];
			
			if (sMacAddr.contains(":") == false && sMacAddr.length() == 12)
			{
				// If the MAC address only contains hex digits without the
				// ":" delimiter, then add ":" to the MAC address string.
				char[] cAddr = new char[17];
				
				for (int i=0, j=0; i < 12; i += 2)
				{
					sMacAddr.getChars(i, i+2, cAddr, j);
					j += 2;
					if (j < 17)
					{
						cAddr[j++] = ':';
					}
				}
				
				sMacAddr = new String(cAddr);
			}
			
			String sPrinterURI = "bt://" + sMacAddr;
			String sUserText = editUserText.getText().toString();

			LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();
			
			exSettings.setContext(PrintActivity.this);

			try
			{
				File profiles = new File (getExternalFilesDir(null), "printer_profiles.JSON");
				
				LinePrinter  lp = new LinePrinter(
						profiles.getAbsolutePath(),
						"PR2",     
						sPrinterURI,
						exSettings);
				
				// Registers to listen for the print progress events.
				lp.addPrintProgressListener(new PrintProgressListener() {
					public void receivedStatus(PrintProgressEvent aEvent)
					{
						// Publishes updates on the UI thread.
						publishProgress(aEvent.getMessageType());
					}
				});
				
				//A retry sequence in case the bluetooth socket is temporarily not ready
				int numtries = 0;
				int maxretry = 2;
				while(numtries < maxretry)
				{
					try{
						lp.connect();  // Connects to the printer
						break;
					}
					catch(LinePrinterException ex){
						numtries++;
						Thread.sleep(1000);
					}
				}
				if (numtries == maxretry) lp.connect();//Final retry 
				
				// Set font style to Bold + Double Wide + Double High.
				lp.setBold(true);
				lp.setDoubleWide(true);
				lp.setDoubleHigh(true);
				lp.write("SALES ORDER");
				lp.setDoubleWide(false);
				lp.setDoubleHigh(false);
				lp.newLine(2);

				// The following text shall be printed in Bold font style.
				lp.write("CUSTOMER: Casual Step");
				lp.setBold(false);  // Returns to normal font.
				lp.newLine(2);

				// Set font style to Compressed + Double High.
				lp.setDoubleHigh(true);
				lp.setCompress(true);
				lp.write("DOCUMENT# 123456789012");
				lp.setCompress(false);
				lp.setDoubleHigh(false);
				lp.newLine(2);

				// The following text shall be printed in Normal font style.
				lp.write(" PRD. DESCRIPT.   PRC.  QTY.    NET.");
				lp.newLine(2);

				lp.write(" 1501 Timer-Md1  13.15     1   13.15");
				lp.newLine(1);
				lp.write(" 1502 Timer-Md2  13.15     3   39.45");
				lp.newLine(1);
				lp.write(" 1503 Timer-Md3  13.15     1   13.15");
				lp.newLine(1);
				lp.write(" 1504 Timer-Md4  13.15     4   52.60");
				lp.newLine(1);
				lp.write(" 1505 Timer-Md5  13.15     5   65.75");
				lp.newLine(1);
				lp.write("                        ----  ------");
				lp.newLine(1);
				lp.write("              SUBTOTAL    15  197.25");
				lp.newLine(2);

				lp.write("                        ----  ------");
				lp.newLine(1);
				lp.write("           TOTAL SALES    15  197.15");
				lp.newLine(4);
				lp.write("               PRODUCT        179.25");
				lp.newLine(1);
				lp.write("               DEPOSIT         18.00");
				lp.newLine(1);
				lp.write("                              ------");
				lp.newLine(1);
				lp.write("              SUBTOTAL        197.25");
				lp.newLine(1);
				lp.write("      SUBTOTAL CREDITS          0.00");
				lp.newLine(1);
				lp.write("           OTHER ITEMS          2.14");
				lp.newLine(1);
				lp.write("                 LABOR          4.44");
				lp.newLine(1);
				lp.write("          5% State Tax          9.86");
				lp.newLine(2);

				lp.write("                              ------");
				lp.newLine(1);
				lp.write("           BALANCE DUE        213.74");
				lp.newLine(1);
				lp.newLine(1);

				lp.write(" PAYMENT TYPE: CASH");
				lp.newLine(2);

				lp.setDoubleHigh(true);
				lp.write("       SIGNATURE / STORE STAMP");
				lp.setDoubleHigh(false);
				lp.newLine(2);
				
				// Print a signature file if it exists
				File graphicFile = new File (getExternalFilesDir(null), "Signature.bmp");
				if (graphicFile.exists())
				{
					lp.writeGraphic(graphicFile.getAbsolutePath(),
							LinePrinter.GraphicRotationDegrees.DEGREE_0,
							72,  // Offset in printhead dots from the left of the page
							180, // Desired graphic width on paper in printhead dots
							90); // Desired graphic height on paper in printhead dots
				}

				lp.setBold(true);
				if (sUserText.length() > 0)
				{
					// Print the text entered by user in the Optional Text field.
					lp.write(sUserText);
					lp.newLine(2);
				}
				

				lp.write("          ORIGINAL");
				lp.setBold(false);
				lp.newLine(4);
				
				sResult = "Number of bytes sent to printer: " + lp.getBytesWritten();
				 
				lp.disconnect();  // Disconnects from the printer
				
				lp.close();  // Releases resources
			}
			catch (LinePrinterException ex)
			{
				sResult = "LinePrinterException: " + ex.getMessage();
			}
			catch (Exception ex)
			{
				if (ex.getMessage() != null)
					sResult = "Unexpected exception: " + ex.getMessage();
				else
					sResult = "Unexpected exception.";
			}

			// The result string will be passed to the onPostExecute method
			// for display in the the Progress and Status text box.
			return sResult;
		}

		/**
		 * Runs on the UI thread after publishProgress is invoked. The
		 * specified values are the values passed to publishProgress.
		 */
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// Access the values array.
			int progress = values[0];
			
			switch (progress)
			{
			case PrintProgressEvent.MessageTypes.CANCEL:
				textMsg.append(PROGRESS_CANCEL_MSG);
				break;
			case PrintProgressEvent.MessageTypes.COMPLETE:
				textMsg.append(PROGRESS_COMPLETE_MSG);
				break;
			case PrintProgressEvent.MessageTypes.ENDDOC:
				textMsg.append(PROGRESS_ENDDOC_MSG);
				break;
			case PrintProgressEvent.MessageTypes.FINISHED:
				textMsg.append(PROGRESS_FINISHED_MSG);
				break;
			case PrintProgressEvent.MessageTypes.STARTDOC:
				textMsg.append(PROGRESS_STARTDOC_MSG);
				break;
			default:
				textMsg.append(PROGRESS_NONE_MSG);
				break;
			}
		}

		/**
		 * Runs on the UI thread after doInBackground method. The specified
		 * result parameter is the value returned by doInBackground.
		 */
		@Override
		protected void onPostExecute(String result)
		{
			// Displays the result (number of bytes sent to the printer or
			// exception message) in the Progress and Status text box.
			if (result != null)
			{
				textMsg.append(result);
			}
			
			// Dismisses the progress icon on the title bar.
			setProgressBarIndeterminateVisibility(false);
			
			// Enables the Print button.
			buttonPrint.setEnabled(true);
		}
	} //endofclass PrintTask
}
