package com.redfoot.iota;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.redfoot.iota.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Setup extends Activity {
	 // =========================================================================
 	 // TODO Variables
 	 // =========================================================================
	private ListView lvProvinces;
	
	private TextView tvUrlCaption;
	private EditText etWebUrl;
	private int resultCode = 0;
	 // =========================================================================
 	 // TODO Life Cycle
 	 // ========================================================================= 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MainApp.createDatabase(this);
		
		MainApp maTemp = (MainApp) this.getApplication();
		final Database dbTemp = maTemp.getDatabase();
		
        if (!dbTemp.getProvinces().isEmpty()) {
        	
			this.startActivity(new Intent(this, MainMenu.class));
			maTemp.getDatabase().closeDatabase();
			this.finish();
		} else {
			this.setContentView(R.layout.provinces);
			
			SetupWidgets();  

			Button bOK = (Button)this.findViewById(R.id.d_btnOK);
			lvProvinces = (ListView)this.findViewById(R.id.d_lvProvinces);
			ArrayList<ProvinceItem> cdpProvinces = dbTemp.readProvinces();
			CheckBoxArrayAdapter cbaAdapter = new CheckBoxArrayAdapter(this, cdpProvinces);
			
			lvProvinces.setAdapter(cbaAdapter);
			bOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setupDatabase();
				} 
			});
		}
	}
	// =========================================================================
    @Override
	protected void onPause() {
		super.onPause();
		
		this.unregisterReceiver(brodRec);
	}
    // =========================================================================
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter(this.getPackageName() + MainApp.INTENT_SETUP_FINISHED);
		this.registerReceiver(brodRec, filter);
	}
	 // =========================================================================
 	 // TODO Overrides
 	 // =========================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.exit(RESULT_OK);
			break;

		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}
	 // =========================================================================
 	 // TODO Main Functions
 	 // =========================================================================
	private void SetupWidgets(){
		
		tvUrlCaption = (TextView)findViewById(R.id.tvUrlCaption);
		etWebUrl = (EditText)findViewById(R.id.etWebUrl);
		
		if(MainApp.PHOTOSYNC_MODE == 0){
			
			tvUrlCaption.setVisibility(View.GONE);
			etWebUrl.setVisibility(View.GONE);
		}
	}
	// =========================================================================
	private void setupDatabase() {
		
		Display display = this.getWindowManager().getDefaultDisplay();
		
		if (display.getWidth() > display.getHeight()) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		String sNum = ((EditText)this.findViewById(R.id.d_etMobtel)).getEditableText().toString();
		boolean bValid = true;
		boolean isProvCheck = checkProv();
		
		if (sNum.length() == 0) {
			
			bValid = false;
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.alert)
        	.setMessage(R.string.NoIotaMobtel)
        	.setNeutralButton(R.string.OK, null)
			.show();
        	
		} else if (sNum.length() != 11 && sNum.length() != 4) {
			
			bValid = false;
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.alert)
        	.setMessage(R.string.InvIotaMobtel)
        	.setNeutralButton(R.string.OK, null)
			.show();
        	
		} else {
			
			if (sNum.length() == 11 && !sNum.startsWith("09")) {
				
				bValid = false;
	        	new AlertDialog.Builder(this)
	        	.setTitle(R.string.alert)
	        	.setMessage(R.string.InvIotaMobtel)
	        	.setNeutralButton(R.string.OK, null)
				.show();
	        	
			} else if (isProvCheck == false){
				
				bValid = false;
				new AlertDialog.Builder(this).setTitle(R.string.alert)
						.setMessage(R.string.NoProvCheck)
						.setNeutralButton(R.string.OK, null).show();
				
			} else if (checkProvCount() > 1){
				
				bValid = false;
				new AlertDialog.Builder(this).setTitle(R.string.alert)
						.setMessage(R.string.NumProvCheck)
						.setNeutralButton(R.string.OK, null).show();
				
			} else {
//				new AlertDialog.Builder(this)
//				.setTitle(R.string.confirmation)
//				.setMessage(getString(R.string.updatediotanum).replace("@NUMBER", sNum))
//				.setNeutralButton(R.string.OK, null)
//				.show();
			}
		}
		if (bValid) {
			if(MainApp.PHOTOSYNC_MODE == 1){
				
				if(etWebUrl.length() != 0){
					validateURL(etWebUrl.getText().toString());	
				} else {
					skipUrlSet();
				}
			} else {
				//old codes
				Database dbTemp = ((MainApp)this.getApplication()).getDatabase();
				dbTemp.setupDatabase(lvProvinces, sNum);
			}
		}
	}
	// =========================================================================
	private boolean checkProv() {
		// TODO Auto-generated method stub
    	CheckBoxArrayAdapter cbaAdapter = (CheckBoxArrayAdapter) lvProvinces.getAdapter();
		int iItemCount = cbaAdapter.getCount();

		for (int i = 0; i < iItemCount; i++) {
			CheckBox cbTemp = (CheckBox) cbaAdapter.getView(i, null, null)
					.findViewById(R.id.d_cbCheck);

			if (cbTemp.isChecked()) {
				return true;
			}
		}

    	return false;
	}
	// =========================================================================
	private int checkProvCount() {
			// TODO Auto-generated method stub
		CheckBoxArrayAdapter cbaAdapter = (CheckBoxArrayAdapter) lvProvinces
				.getAdapter();
		int iItemCount = cbaAdapter.getCount();
		int provinceCount = 0;

		for (int i = 0; i < iItemCount; i++) {
			CheckBox cbTemp = (CheckBox) cbaAdapter.getView(i, null, null)
					.findViewById(R.id.d_cbCheck);

			if (cbTemp.isChecked()) {
				provinceCount++;
			}
		}

		return provinceCount;
	 }
	// =========================================================================
	private void processBroadcast(Intent intent, int result) {
    	if (intent.getAction().equals(this.getPackageName() + MainApp.INTENT_SETUP_FINISHED)) {
			this.startActivity(new Intent(this, MainMenu.class));
			this.finish();
    	}
    }
	 // =========================================================================
	@SuppressLint("HandlerLeak")
	private void validateURL(final String URL){
		
		final ProgressDialog dialog = ProgressDialog.show(this, "Please Wait..","Processing...", true);
		final Handler handler = new Handler() {
		   public void handleMessage(Message msg) {
		      dialog.dismiss();
		      ///// 2nd if the load finish -----
		      getResult();
			  ///// -----
		      }
		};
		Thread checkUpdate = new Thread() {  
			public void run() {	
			  /// 1st main activity here -----
				connectToHttp(URL);
			  ////// -----
		      handler.sendEmptyMessage(0);				      
		      }
		};
		checkUpdate.start();
	}
	// =========================================================================
    private void connectToHttp(String urlLink){
    	
		URL url;
		try {
			url = new URL(urlLink);
			
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setConnectTimeout(5000);
			resultCode = huc.getResponseCode();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    // =========================================================================
    private void getResult(){
    	
    	if (resultCode == 200) {
    		//Utils.MessageToast(this, "URL is Valid");
    		String sNum = ((EditText)this.findViewById(R.id.d_etMobtel)).getEditableText().toString();
    		
			MainApp.setUrlValidation(this, true);
			MainApp.setWebconfig(this, etWebUrl.getText().toString());
			// -- 
			Database dbTemp = ((MainApp)this.getApplication()).getDatabase();
			dbTemp.setupDatabase(lvProvinces, sNum);
			
		} else {
			//Utils.MessageToast(this, "URL is Invalid");
			MainApp.setUrlValidation(this, false);
			Utils.MessageBox(this, "Web url is invalid or no internet connection");
		}
    }
    // =========================================================================
    private void skipUrlSet(){
    	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Alert");
		alertDialogBuilder
				.setMessage("Continue without Web Url?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								setupWithoutUrl();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
    }
    // =========================================================================
    private void setupWithoutUrl(){
    	
    	String sNum = ((EditText)this.findViewById(R.id.d_etMobtel)).getEditableText().toString();
		
		MainApp.setUrlValidation(this, false);
		Database dbTemp = ((MainApp)this.getApplication()).getDatabase();
		dbTemp.setupDatabase(lvProvinces, sNum);
    }
     // =========================================================================
 	 // TODO Implementation
 	 // =========================================================================
    private BroadcastReceiver brodRec = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			processBroadcast(intent, this.getResultCode());
		}
	};
	 // =========================================================================
 	 // TODO Final Codes
}
