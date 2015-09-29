package com.redfoot.iota;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.redfoot.iota.objects.MyUncaughtException;
import com.redfoot.iota.objects.NetBroadcast;
import com.redfoot.iota.objects.Notify;
import com.redfoot.iota.objects.SampObj;
import com.redfoot.iota.objects.UploadImagesIntentServices;
import com.redfoot.iota.preference.PreferrenceSettings;
import com.redfoot.iota.utils.Configs;
import com.redfoot.iota.utils.JsonParser;
import com.redfoot.iota.utils.Utils;

public class MainMenu extends Activity implements Observer{
// =========================================================================
// TODO Variables
// =========================================================================	
	private Button btnPhotoSync, btnOpenDrafts, btnChainAcc, btnGeneralTrade;
	
	private MainApp mainApp;
	private ImageView ivImageIcon;
	
	private boolean isUploading = false;
	private boolean haveInternet = false;
// =========================================================================
// TODO Activity Life Cycle
// =========================================================================	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Utils.iLogCat(this.getClass().getSimpleName() +": onCreate", "Begin");
        
		//set debug mode
		Thread.currentThread();
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtException(this));
        //
		MainApp.createGPSListner(this);
		//
		setContentView(R.layout.main);
        
        //MainApp.setDialogParam(this);
        
        SetupWidgets(); 
  
        if(MainApp.SAVEDRAFT_MODE == 1){
        	btnOpenDrafts.setVisibility(View.VISIBLE);
        } else {
        	btnOpenDrafts.setVisibility(View.GONE);
        }
        
        //
		new NetBroadcast(getBaseContext(), this);
		//
		Utils.iLogCat(this.getClass().getSimpleName() +": onCreate", "End");
    }
// =========================================================================
	@Override
	protected void onPause() {
		super.onPause();
		
		this.unregisterReceiver(imageReceiver);
		btnPhotoSync.setEnabled(true);
	}
// =========================================================================
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UploadImagesIntentServices.TRANSACTION_DONE);
		//
        registerReceiver(imageReceiver, intentFilter);
        //
		int count = mainApp.getDatabase().countDrafts();
		
		if(count == 0){
			btnOpenDrafts.setEnabled(false);
			btnOpenDrafts.setText("Drafts[" + count + "]");
		} else {
			btnOpenDrafts.setEnabled(true);
			btnOpenDrafts.setText("Drafts[" + count + "]");
		}
		
	}
// =========================================================================
// TODO Override
// =========================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
	        	new AlertDialog.Builder(this)
	        	.setTitle(R.string.question)
	        	.setMessage(R.string.exitapp)
	        	.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						Intent i = new Intent(Intent.ACTION_MAIN);
						i.addCategory(Intent.CATEGORY_HOME);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						System.exit(RESULT_OK);
					}
	        	})
	        	.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
	        	break;
			case KeyEvent.KEYCODE_MENU:
				openOptionsMenu();
				break;
			default:
				return super.onKeyDown(keyCode, event);
		}
		return true;
	}
// =========================================================================
// TODO Menu Option
// =========================================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater miTemp = getMenuInflater();
		
		miTemp.inflate(R.menu.menu, menu);
		
		return true;
	}
// ---------------------------------------------------------------------
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//			case R.id.UpdateIotaNum:
//				showDialog(MainApp.DIALOG_IOTA_NUM_ID);
//				return true;
				
			case R.id.Settings:
				
				showDialog(MainApp.DIALOG_SETTINGS_ID);
				
//				if(MainApp.PHOTOSYNC_MODE == 1){
//					
//					if(haveInternet){
//						showDialog(MainApp.DIALOG_SETTINGS_ID);
//					}
//				}
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
// ---------------------------------------------------------------------
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case MainApp.DIALOG_IOTA_NUM_ID:
				final Dialog dTemp = new Dialog(this);

				dTemp.setContentView(R.layout.dialogiotanum);
				dTemp.setTitle(R.string.updateiotanum);
				
				Button bOK = (Button)dTemp.findViewById(R.id.d_btnOK);
				Button bCancel = (Button)dTemp.findViewById(R.id.d_btnCancel);
				bOK.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View v) {
						String sNum = ((EditText)dTemp.findViewById(R.id.d_etMobtel)).getEditableText().toString();
						boolean bValid = true;
						
						if (sNum.length() == 0) {
							bValid = false;
				        	new AlertDialog.Builder(MainMenu.this)
				        	.setTitle(R.string.alert)
				        	.setMessage(R.string.NoIotaMobtel)
				        	.setNeutralButton(R.string.OK, null)
							.show();
						} else if (sNum.length() != 11 && sNum.length() != 4) {
							bValid = false;
				        	new AlertDialog.Builder(MainMenu.this)
				        	.setTitle(R.string.alert)
				        	.setMessage(R.string.InvIotaMobtel)
				        	.setNeutralButton(R.string.OK, null)
							.show();
						} else {
							if (sNum.length() == 11 && !sNum.startsWith("09")) {
								bValid = false;
					        	new AlertDialog.Builder(MainMenu.this)
					        	.setTitle(R.string.alert)
					        	.setMessage(R.string.InvIotaMobtel)
					        	.setNeutralButton(R.string.OK, null)
								.show();
							} else {
								new AlertDialog.Builder(MainMenu.this)
								.setTitle(R.string.confirmation)
								.setMessage(getString(R.string.updatediotanum).replace("@NUMBER", sNum))
								.setNeutralButton(R.string.OK, null)
								.show();
							}
						}
						
						if (bValid) {
							((MainApp)MainMenu.this.getApplication()).getDatabase().setIotaNum(sNum);
							dTemp.dismiss();
						}
					}
					
				});
				bCancel.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View v) {
						dTemp.dismiss();
					}
				});
				return dTemp;
				
			case MainApp.DIALOG_SETTINGS_ID:
				
				startActivity(new Intent(this, PreferrenceSettings.class));
			default:
				return super.onCreateDialog(id);
		}
	}
// =========================================================================
// TODO onClick View
// =========================================================================
	public void onGeneralTrade(View v){
		
		if(!isUploading){
			startActivity(new Intent(this, GeneralTradeAct.class));	
		} else {
			Utils.MessageBox(this, "Please wait until the uploading is finished.");
		}
		
	}
	// ---------------------------------------------------------------------
	public void onChainAccount(View v){
		
		if(!isUploading){
			startActivity(new Intent(this, ChainAccountAct.class));	
		} else {
			Utils.MessageBox(this, "Please wait until the uploading is finished.");
		}
		
	}
	// =========================================================================
	public void onPhotoSync(View v){
		
		if(MainApp.getUrlValidation(this)){
			
			String[] filenames = getAllFiles();
			// -----=-----=-----=----- ><
			Intent i = new Intent(this, UploadImagesIntentServices.class);
			i.putExtra(UploadImagesIntentServices.STORENAME, filenames);

			startService(i);
			isUploading = true;
			// -----=-----=-----=----- ><
	        btnPhotoSync.setEnabled(false);
			
		} else {
			Utils.MessageBox(this, "Please check your web URL is valid");
		}
        
	}
	// =========================================================================
	public void onOpenDraft(View v){
		Utils.iLogCat(this.getClass().getSimpleName() +": onOpenDraft", "Begin");
		
		if(!isUploading){
			startActivity(new Intent(this, DraftListView.class));	
		} else {
			Utils.MessageBox(this, "Please wait until the uploading is finished.");
		}
		
		Utils.iLogCat(this.getClass().getSimpleName() +": onOpenDraft", "End");
	}
// =========================================================================
// TODO Main Functions
// =========================================================================
	private void SetupWidgets(){
		
		btnChainAcc = (Button)findViewById(R.id.btnChainAccount);
		btnGeneralTrade = (Button)findViewById(R.id.btnGeneralTrade);
		btnPhotoSync = (Button)findViewById(R.id.btnPhotoSync);
		btnOpenDrafts = (Button)findViewById(R.id.btnOpenDraft);
		
		ivImageIcon = (ImageView)findViewById(R.id.ivImageIcon);
		ivImageIcon.setOnLongClickListener(longClick);
		
		mainApp = (MainApp) this.getApplication();
		// for Webconfig settings
		String ip = MainApp.getWebconfig(this);
		
		//boolean tr = MainApp.getUrlValidation(this);
		
		if(ip.equals("null")){
			MainApp.setDefaultWebconfig(this);
		}
		
	}
// =========================================================================
// TODO Implementation
// =========================================================================
	@Override
	public void update(Observable observable, Object data) {
		Utils.iLogCat(this.getClass().getSimpleName() +": update", "Begin");
		
		String value = data.toString();
		
		if(value.equals("1")){
			
			setTitle("iOTA GPS Mapping : Online");
			if(MainApp.PHOTOSYNC_MODE == 1){
				btnPhotoSync.setVisibility(View.VISIBLE);
			}
			haveInternet = true;
			MainApp.setOnline(this, true);
		} else if(value.equals("2")){
			
			setTitle("iOTA GPS Mapping : Offline");
			btnPhotoSync.setVisibility(View.GONE);
			haveInternet = false;
			MainApp.setOnline(this, false);
		} else if (value.equals("0")){
			
			setTitle("iOTA GPS Mapping : No Port for Internet");
			btnPhotoSync.setVisibility(View.GONE);
			haveInternet = false;
			MainApp.setOnline(this, false);
		}
		
		Utils.iLogCat(this.getClass().getSimpleName() +": update", "End");
	}
	
	private OnLongClickListener longClick = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			
			callDialog();
			return true;
		}
	};
	

	// =========================================================================
	private BroadcastReceiver imageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String val = intent.getExtras().getString("result");

			if (val.equals("Upload")) {

				Utils.MessageToast(getApplicationContext(), "Upload Complete");
				btnPhotoSync.setEnabled(true);
				
			} else {

				Utils.MessageToast(getApplicationContext(), "No New Picture Found");
				btnPhotoSync.setEnabled(true);
			}
			
			isUploading = false;
			
		}
	};
// =========================================================================
// TODO Sub Functions
// =========================================================================
	private String[] getAllFiles(){
		Utils.iLogCat(this.getClass().getSimpleName() +": getAllFiles", "Begin");
		
		String[] children = null;
		
		File dir = new File(Configs.DIRECTORY_PATH);
		
		if(dir.exists()){
			
			if(dir.isDirectory()){
				
				children = dir.list();
			}
		}
		
		Utils.iLogCat(this.getClass().getSimpleName() +": getAllFiles", "End");
		return children;
	}
	
	private void callDialog(){
		
		final EditText et = new EditText(this);
		final boolean stats = MainApp.getDebug(getBaseContext());
		
		String adminMsg;
		if(stats){
			adminMsg = "Debug Mode is ON";
		} else{
			adminMsg = "Debug Mode is OFF";
		}
		
		new AlertDialog.Builder(this)
		.setTitle(adminMsg)
		.setView(et)
		.setMessage("Set Passcode")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				
				if(!stats){
					
					if(et.getText().toString().equals("debug on")){
						MainApp.setDebug(getBaseContext(), true);
						Toast.makeText(getBaseContext(), "Debug is ON", Toast.LENGTH_SHORT).show();
					}
				}else{
					if(et.getText().toString().equals("debug off")){
						MainApp.setDebug(getBaseContext(), false);
						Toast.makeText(getBaseContext(), "Debug is OFF", Toast.LENGTH_SHORT).show();
					}
				}
				
				
			}
		})
		.show();
	}
// =========================================================================
// TODO Inner Class
// =========================================================================

// =========================================================================
// TODO Final Code
}