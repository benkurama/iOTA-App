package com.redfoot.iota;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ChainAccountAct extends Activity{
// =========================================================================
// TODO Variables
// =========================================================================
	private MainApp m_maApp;
	private BroadcastReceiver brSMS;
	
	private Spinner spnChainAcc;
	private Button btnSend, btnCancel;
	
	private static String NCAMAP = "";
// =========================================================================
// TODO Activity Life Cycle
// =========================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chain_account_act);

		SetupWidgets();
		//--
		brSMS = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				processBroadcast(intent, this.getResultCode());
			} 
        };
        //--
        _fillChainAccountList();
	}
	// =========================================================================
	@Override
	protected void onPause() {
		super.onPause();
		
		this.unregisterReceiver(brSMS);
	}
	// =========================================================================
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter(this.getPackageName() + MainApp.INTENT_ACTION_SENT);
		
		filter.addAction(this.getPackageName() + MainApp.INTENT_DIALOG_CLOSED);
		this.registerReceiver(brSMS, filter);
	}
// =========================================================================
// TODO Overrides
// =========================================================================
	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch (id) {
		
		case MainApp.DIALOG_IOTA_NUM_ID:
			
			final Dialog dTemp = new Dialog(this);
			dTemp.setContentView(R.layout.dialogiotanum);
			dTemp.setTitle(R.string.question);

			Button bOK = (Button) dTemp.findViewById(R.id.d_btnOK);
			Button bCancel = (Button) dTemp.findViewById(R.id.d_btnCancel);
			bOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String sNum = ((EditText) dTemp
							.findViewById(R.id.d_etMobtel)).getEditableText()
							.toString();
					boolean bValid = true;

					if (sNum.length() == 0) {
						bValid = false;
						new AlertDialog.Builder(ChainAccountAct.this)
								.setTitle(R.string.alert)
								.setMessage(R.string.NoIotaMobtel)
								.setNeutralButton(R.string.OK, null).show();
					} else if (sNum.length() != 11) {
						bValid = false;
						new AlertDialog.Builder(ChainAccountAct.this)
								.setTitle(R.string.alert)
								.setMessage(R.string.InvIotaMobtel)
								.setNeutralButton(R.string.OK, null).show();
					} else {
						if (!sNum.startsWith("09")) {
							bValid = false;
							new AlertDialog.Builder(ChainAccountAct.this)
									.setTitle(R.string.alert)
									.setMessage(R.string.InvIotaMobtel)
									.setNeutralButton(R.string.OK, null).show();
						} else {
							new AlertDialog.Builder(ChainAccountAct.this)
									.setTitle(R.string.confirmation)
									.setMessage(
											getString(R.string.updatediotanum)
													.replace("@NUMBER", sNum))
									.setNeutralButton(R.string.OK, null).show();
						}
					}

					if (bValid) {
						m_maApp.getDatabase().setIotaNum(sNum);
						dTemp.dismiss();
						// - temp comment

						if (m_maApp.getGPSStatus() != LocationProvider.AVAILABLE) {
							Utility.alertNoGPS(ChainAccountAct.this);
						} else {
							sendMessage(sNum);
						}

					}
				}

			});
			// ---------------------------------------------------------------------
			bCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dTemp.dismiss();
				}
			});
			return dTemp;
		default:
			return super.onCreateDialog(id);
		}
	}
// =========================================================================
// TODO Menu Option
// =========================================================================

// =========================================================================
// TODO onClick View
// =========================================================================
	@SuppressWarnings("unused")
	public void onSend(View v){
		
		String sNum = m_maApp.getDatabase().getIotaNum();
		
		if (sNum.length() == 0) {
			
			showDialog(MainApp.DIALOG_IOTA_NUM_ID);
		} else {
			
			if (MainApp.DEBUG == 0) {
				
				if (m_maApp.getGPSStatus() != LocationProvider.AVAILABLE) {
					
					Utility.alertNoGPS(this);
				} else {
					sendMessage(sNum);
				}
			} else {
				
				sendMessage(sNum);
			}
		}
	}
	// =========================================================================
	public void onCancel(View v){
		
		this.finish();
	}
// =========================================================================
// TODO Main Functions
// =========================================================================
	public void SetupWidgets() {
		
		spnChainAcc = (Spinner)findViewById(R.id.spnStoreName);
		
		btnSend = (Button)findViewById(R.id.btnSend_CA);
		btnCancel = (Button)findViewById(R.id.btnCancel_CA);
		
		 m_maApp = (MainApp) this.getApplication();
		 
	}
// =========================================================================
	public void sendMessage(String sNum){
		
		// ---------------------------------------------------------------------
		//region reserver codes
		/*ArrayList<CodeDescPair> allChainAcc = m_maApp.getDatabase().getChainAccountList();
		String codeChainAcc = "";
		for (CodeDescPair cdp : allChainAcc) {
			if (cdp.Description.equalsIgnoreCase(spnChainAcc.getSelectedItem().toString())) {
				codeChainAcc = cdp.Code;
				break;
			}
		}*/
		//endregion
		// ---------------------------------------------------------------------
		String selectedStoreName = spnChainAcc.getSelectedItem().toString();
		// ---------------------------------------------------------------------
		NCAMAP = getString(R.string.NCAMAP) +" "+ selectedStoreName +"/"+ m_maApp.getGPSLong() +"/"+ m_maApp.getGPSLat();
		
		PendingIntent pi = PendingIntent.getBroadcast(this, MainApp.REQUEST_ACTION_SENT,  new Intent(this.getPackageName() + MainApp.INTENT_ACTION_SENT), 0);
		SmsManager sms = SmsManager.getDefault();
		
		sms.sendTextMessage(sNum, null, NCAMAP, pi, null);
		
		btnSend.setEnabled(false);
		btnCancel.setEnabled(false);
		
		//setTitle(NCAMAP);
		
	}
// =========================================================================
	protected void processBroadcast(Intent intent, int resultCode) {
		
    	if (intent.getAction().equals(this.getPackageName() + MainApp.INTENT_ACTION_SENT)) {
	    	switch (resultCode) {
	    		case Activity.RESULT_OK:
	    			ContentValues cvTemp = new ContentValues();
	    			
	    			Toast.makeText(this, R.string.msgsent, Toast.LENGTH_LONG).show();
	    			cvTemp.put("address", m_maApp.getDatabase().getIotaNum());
	    			cvTemp.put("body", NCAMAP);
	    			this.getContentResolver().insert(Uri.parse("content://sms/sent"), cvTemp);
	    			finish();
	    			break;
	    		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	    			Toast.makeText(this, R.string.senderror, Toast.LENGTH_LONG).show();
	    			btnSend.setEnabled(true);
	    			btnCancel.setEnabled(true);
	    			break;
	    		case SmsManager.RESULT_ERROR_NO_SERVICE:
	    		case SmsManager.RESULT_ERROR_RADIO_OFF:
	    			Toast.makeText(this, R.string.noservice, Toast.LENGTH_LONG).show();
	    			btnSend.setEnabled(true);
	    			btnCancel.setEnabled(true);
	    			break;
	    	}
    	} else if (intent.getAction().equals(this.getPackageName() + MainApp.INTENT_DIALOG_CLOSED)) {
    		if (Utility.getDialogResult()) {
    			this.sendMessage(m_maApp.getDatabase().getIotaNum());
    		}
    	}
	}
// =========================================================================
// TODO Implementation
// =========================================================================

// =========================================================================
// TODO Sub Functions
// =========================================================================
	private void _fillChainAccountList(){
		
		ArrayList<CodeDescPair> allChainAcc = m_maApp.getDatabase().getChainAccountList();
		ArrayAdapter<CodeDescPair> aaAdapter = new ArrayAdapter<CodeDescPair>(this, android.R.layout.simple_spinner_item);
		aaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for(CodeDescPair cdp : allChainAcc){
			aaAdapter.add(cdp);
		}
		
		spnChainAcc.setAdapter(aaAdapter);
	}
	// =========================================================================
	@SuppressWarnings("unused")
	private void validate(){
		
	}
// =========================================================================
// TODO Inner Class
// =========================================================================

// =========================================================================
// TODO Final Destination
}
