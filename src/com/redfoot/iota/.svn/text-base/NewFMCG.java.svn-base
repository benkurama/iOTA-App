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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class NewFMCG extends Activity implements OnItemClickListener {

	private static String SAVED_BARANGAY = "";
	private static String NFMAP = "";
	
	private MainApp m_maApp;
	private BroadcastReceiver m_brSMS;
	private ListView m_lvEstablishments;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newfmcg);
        
        m_maApp = (MainApp) this.getApplication();
        m_brSMS = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				processBroadcast(intent, this.getResultCode());
			} 
			
        };

        fillCities();
        fillCheckList();

        Spinner sCity = (Spinner)this.findViewById(R.id.d_ddlCity);
        Button bSend = (Button)this.findViewById(R.id.d_btnSend);
        Button bCancel = (Button)this.findViewById(R.id.d_btnCancel);
        
        
        sCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				CodeDescPair cdpTemp = (CodeDescPair)arg0.getSelectedItem();
				fillBarangays(cdpTemp.Code);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			} 
		
        });
        
        
        bSend.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onClick(View v) {
				if (validateFields()) {
					String sNum = m_maApp.getDatabase().getIotaNum();
					
					if (sNum.length() == 0) {
						showDialog(MainApp.DIALOG_IOTA_NUM_ID);
					} else {
						if (MainApp.DEBUG == 0) {
							if (m_maApp.getGPSStatus() != LocationProvider.AVAILABLE) {
								Utility.alertNoGPS(NewFMCG.this);
							} else {
								sendNFMAP(sNum);
							}
						} else {
							sendNFMAP(sNum);
						}
					}
				}	
			} 
		
        });
        
        bCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NewFMCG.this.finish();
			}
		});
        
        // get last selected City
    	setSpinnerSelected(R.id.d_ddlCity, m_maApp.getDatabase().getSystemParam(Database.PARAM_LAST_CITY));
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		
		this.unregisterReceiver(m_brSMS);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter(this.getPackageName() + MainApp.INTENT_ACTION_SENT);
		
		filter.addAction(this.getPackageName() + MainApp.INTENT_DIALOG_CLOSED);
		this.registerReceiver(m_brSMS, filter);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case MainApp.DIALOG_IOTA_NUM_ID:
				final Dialog dTemp = new Dialog(this);
				dTemp.setContentView(R.layout.dialogiotanum);
				dTemp.setTitle(R.string.question);
				
				Button bOK = (Button)dTemp.findViewById(R.id.d_btnOK);
				Button bCancel = (Button)dTemp.findViewById(R.id.d_btnCancel);
				bOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String sNum = ((EditText)dTemp.findViewById(R.id.d_etMobtel)).getEditableText().toString();
						boolean bValid = true;
						
						if (sNum.length() == 0) {
							bValid = false;
				        	new AlertDialog.Builder(NewFMCG.this)
				        	.setTitle(R.string.alert)
				        	.setMessage(R.string.NoIotaMobtel)
				        	.setNeutralButton(R.string.OK, null)
							.show();
						} else if (sNum.length() != 11) {
							bValid = false;
				        	new AlertDialog.Builder(NewFMCG.this)
				        	.setTitle(R.string.alert)
				        	.setMessage(R.string.InvIotaMobtel)
				        	.setNeutralButton(R.string.OK, null)
							.show();
						} else {
							if (!sNum.startsWith("09")) {
								bValid = false;
					        	new AlertDialog.Builder(NewFMCG.this)
					        	.setTitle(R.string.alert)
					        	.setMessage(R.string.InvIotaMobtel)
					        	.setNeutralButton(R.string.OK, null)
								.show();
							} else {
								new AlertDialog.Builder(NewFMCG.this)
								.setTitle(R.string.confirmation)
								.setMessage(getString(R.string.updatediotanum).replace("@NUMBER", sNum))
								.setNeutralButton(R.string.OK, null)
								.show();
							}
						}
						
						if (bValid) {
							m_maApp.getDatabase().setIotaNum(sNum);
							dTemp.dismiss();
							if (m_maApp.getGPSStatus() != LocationProvider.AVAILABLE) {
								Utility.alertNoGPS(NewFMCG.this);
							} else {
								sendNFMAP(sNum);
							}
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
			default:
				return super.onCreateDialog(id);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		NewFMCG.SAVED_BARANGAY = savedInstanceState.getString(this.getString(R.string.Barangay));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Spinner sBarangay = (Spinner)this.findViewById(R.id.d_ddlBarangay);
		
		outState.putString(this.getString(R.string.Barangay), ((CodeDescPair)sBarangay.getSelectedItem()).Code);

		super.onSaveInstanceState(outState);
	}

	protected void sendNFMAP(String sNum) {
		EditText etName = (EditText)this.findViewById(R.id.d_etName);
		Spinner sSubType = (Spinner)this.findViewById(R.id.d_ddlCustSubType);
		EditText etNo = (EditText)this.findViewById(R.id.d_etNo);
		EditText etStreet = (EditText)this.findViewById(R.id.d_etStreet);
		Spinner sBarangay = (Spinner)this.findViewById(R.id.d_ddlBarangay);
		Spinner sCity = (Spinner)this.findViewById(R.id.d_ddlCity);
		Spinner sSizeStore = (Spinner)this.findViewById(R.id.d_ddlSizeStore);
		CheckBox sCbMerchPresent = (CheckBox)this.findViewById(R.id.d_cbMerchPresent);
		CheckBox sCbMerchDominant = (CheckBox)this.findViewById(R.id.d_cbMerchDominant);
		Spinner sInterview = (Spinner)this.findViewById(R.id.d_ddlInterviewedPerson);
		EditText sInterviewName = (EditText)this.findViewById(R.id.d_etInterviewedName);
		EditText sPhone = (EditText)this.findViewById(R.id.d_etInterviewedTelephone);
		EditText sOwnerName = (EditText)this.findViewById(R.id.d_etOwnerName);
		EditText etRemarks = (EditText)this.findViewById(R.id.d_etRemarks);
		ArrayList<CodeDescPair> alSubtypes = m_maApp.getDatabase().getCustSubTypes();
		String sSubtypeCode = "";
		
		for (CodeDescPair cdp : alSubtypes) {
			if (cdp.Description.equalsIgnoreCase(sSubType.getSelectedItem().toString())) {
				sSubtypeCode = cdp.Code;
				break;
			}
		}
		
		ArrayList<CodeDescPair> alStoreSizes = m_maApp.getDatabase().getStoreSizes();
		String sStoreSizeCode = "";
		
		for (CodeDescPair cdp : alStoreSizes){
			if (cdp.Description.equalsIgnoreCase(sSizeStore.getSelectedItem().toString())){
				sStoreSizeCode = cdp.Code;
				break;
			}
		}
		
		SparseBooleanArray checked = m_lvEstablishments.getCheckedItemPositions();
		ArrayList<CodeDescPair> alEstablishments = m_maApp.getDatabase().getEstablisments();
		int sumList = 0;
		
		for (int i = 0; i < m_lvEstablishments.getCount(); i++){
			if (checked.get(i)){
				sumList += Integer.parseInt(alEstablishments.get(i).Code);
			}
		}
		
		String sCbMerchPresentString = "";
		if(sCbMerchPresent.isChecked()){
			sCbMerchPresentString = "Y";
		} else {
			sCbMerchPresentString = "N";
		}
		
		String sCbMerchDominantString = "";
		if(sCbMerchDominant.isChecked()){
			sCbMerchDominantString = "Y";
		} else {
			sCbMerchDominantString = "N";
		}
		
		ArrayList<CodeDescPair> alInterview = m_maApp.getDatabase().getInterview();
		String sInterviewCode = "";
		
		for (CodeDescPair cdp : alInterview){
			if (cdp.Description.equalsIgnoreCase(sInterview.getSelectedItem().toString())){
				sInterviewCode = cdp.Code;
				break;
			}
		}
		
		NFMAP = this.getString(R.string.NFMAP) + " " + etName.getEditableText().toString() + "/" + sSubtypeCode + "/" + etNo.getEditableText().toString()
				+ "," + etStreet.getEditableText().toString() + "," + ((CodeDescPair)sBarangay.getSelectedItem()).Code + "," + ((CodeDescPair)sCity.getSelectedItem()).Code
				+ "/" + m_maApp.getGPSLong() + "/" +  m_maApp.getGPSLat() + "/" + sStoreSizeCode + "/" + sumList + "/" + sCbMerchPresentString + "/" + sCbMerchDominantString
				+ "/" + sInterviewCode + "/" + sInterviewName.getEditableText().toString() 
				+ "/" + sPhone.getEditableText().toString() + "/" + sOwnerName.getEditableText().toString() + "/" + etRemarks.getEditableText().toString();
		

		
		m_maApp.getDatabase().setSystemParam(Database.PARAM_LAST_CITY, ((CodeDescPair)sCity.getSelectedItem()).Code);
		m_maApp.getDatabase().setSystemParam(Database.PARAM_LAST_BARANGAY, ((CodeDescPair)sBarangay.getSelectedItem()).Code);
		
		ArrayList<PendingIntent> sendPendingIntent = new ArrayList<PendingIntent>();
		PendingIntent pi = PendingIntent.getBroadcast(NewFMCG.this, MainApp.REQUEST_ACTION_SENT, 
				  									  new Intent(this.getPackageName() + MainApp.INTENT_ACTION_SENT), 0);
		
		SmsManager sms = SmsManager.getDefault();
		
		ArrayList<String> parts = sms.divideMessage(NFMAP);
		for (int i = 0; i <parts.size(); i++){
			sendPendingIntent.add(i, pi);
		}
		sms.sendMultipartTextMessage(sNum, null, parts, sendPendingIntent, null);
		
	}

	protected boolean validateFields() {
		boolean bValid = true;
		String sMessage = this.getString(R.string.ValidateError);
		String sName = ((EditText)this.findViewById(R.id.d_etName)).getEditableText().toString().trim();
		String sNo = ((EditText)this.findViewById(R.id.d_etNo)).getEditableText().toString().trim();
		String sStreet = ((EditText)this.findViewById(R.id.d_etStreet)).getEditableText().toString().trim();
		String sInterviewedName = ((EditText)this.findViewById(R.id.d_etInterviewedName)).getEditableText().toString().trim();
		String sInterviewedTelephone = ((EditText)this.findViewById(R.id.d_etInterviewedTelephone)).getEditableText().toString().trim();
		String sOwnerName = ((EditText)this.findViewById(R.id.d_etOwnerName)).getEditableText().toString().trim();
		
		
		if (sName.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoRetName);
		}
		
		if (sNo.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoRetNo);
		}
		
		if (sStreet.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoRetStreet);
		}
				
		if (sInterviewedName.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoInterViewedName);
		}
		
		if (sInterviewedTelephone.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoTelephone);
		}
		
		if (sOwnerName.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoOwnerName);
		}
		

		
		
		
		if (!bValid) {
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.alert)
        	.setMessage(sMessage)
        	.setNeutralButton(this.getString(R.string.OK), null)
			.show();
		}
		
		return bValid;
	}

    private void setSpinnerSelected(int id, String code) {
    	Spinner sTemp = (Spinner)this.findViewById(id);
    	int iItems = sTemp.getCount();
    	CodeDescPair cdpTemp;
    	
    	for (int i = 0; i < iItems; i++) {
    		cdpTemp = (CodeDescPair)sTemp.getItemAtPosition(i);
    		if (cdpTemp.Code.equals(code)) {
    			sTemp.setSelection(i);
    			break;
    		}
    	}
    }
    
    protected void fillCities() {
    	ArrayList<CodeDescPair> alCities = m_maApp.getDatabase().getCities();
    	Spinner sCities = (Spinner)this.findViewById(R.id.d_ddlCity);
    	ArrayAdapter<CodeDescPair> aaAdapter = new ArrayAdapter<CodeDescPair>(this, android.R.layout.simple_spinner_item);
    	
    	aaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	for (CodeDescPair cdp : alCities) {
    		aaAdapter.add(cdp);
    	}
    	sCities.setAdapter(aaAdapter);
	}
    
	protected void fillBarangays(String code) {
    	ArrayList<CodeDescPair> alBarangays = m_maApp.getDatabase().getBarangays(code);
    	Spinner sBarangays = (Spinner)this.findViewById(R.id.d_ddlBarangay);
    	ArrayAdapter<CodeDescPair> aaAdapter = new ArrayAdapter<CodeDescPair>(this, android.R.layout.simple_spinner_item);
    	String sLastBrgy = m_maApp.getDatabase().getSystemParam(Database.PARAM_LAST_BARANGAY);
    	int iIndex = 0;
    	int iLast = 0;
    	
    	aaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	for (CodeDescPair cdp : alBarangays) {
    		aaAdapter.add(cdp);
    		if (cdp.Code.equals(NewFMCG.SAVED_BARANGAY)) {
    			iIndex = aaAdapter.getCount() - 1;
    		}
    		if (cdp.Code.equals(sLastBrgy)) {
    			iLast = aaAdapter.getCount() - 1;
    		}
    	}
    	sBarangays.setAdapter(aaAdapter);

    	if (NewFMCG.SAVED_BARANGAY.length() > 0) {
			sBarangays.setSelection(iIndex, true);
    	} else {
    		sBarangays.setSelection(iLast, true);
    	}
    	NewFMCG.SAVED_BARANGAY = ((CodeDescPair)sBarangays.getSelectedItem()).Code;
	}
	
	private void fillCheckList() {
		// TODO Auto-generated method stub
		m_lvEstablishments = (ListView)this.findViewById(R.id.d_lvEstablishment);
    	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.checklistitem,
    			getResources().getStringArray(R.array.establishments));

        m_lvEstablishments.setAdapter(adapter);
        m_lvEstablishments.setItemChecked(6, true);
        
        m_lvEstablishments.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg2) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				if (checkEstablishment() == false) {
					// If all Items 1-5 is unchecked
					m_lvEstablishments.setItemChecked(6, true); 
				} else {
					// Unchecked "None" Item
					m_lvEstablishments.setItemChecked(6, false); 
				}
				break;

			default:
				m_lvEstablishments.setItemChecked(0, false);
				m_lvEstablishments.setItemChecked(1, false);
				m_lvEstablishments.setItemChecked(2, false);
				m_lvEstablishments.setItemChecked(3, false);
				m_lvEstablishments.setItemChecked(4, false);
				m_lvEstablishments.setItemChecked(5, false);
				m_lvEstablishments.setItemChecked(6, true);
		}

	}
	
	private boolean checkEstablishment() {
		// TODO Auto-generated method stub
		SparseBooleanArray checked = m_lvEstablishments.getCheckedItemPositions();
		
		for (int i = 0; i < checked.size() - 1; i++){
			if (checked.valueAt(i)){
				return true;
			}
		}
		
		return false;
	}
	
	protected void processBroadcast(Intent intent, int resultCode) {
    	if (intent.getAction().equals(this.getPackageName() + MainApp.INTENT_ACTION_SENT)) {
	    	switch (resultCode) {
	    		case Activity.RESULT_OK:
	    			ContentValues cvTemp = new ContentValues();
	    			
	    			Toast.makeText(this, R.string.msgsent, Toast.LENGTH_LONG).show();
	    			cvTemp.put("address", m_maApp.getDatabase().getIotaNum());
	    			cvTemp.put("body", NFMAP);
	    			this.getContentResolver().insert(Uri.parse("content://sms/sent"), cvTemp);
	    			finish();
	    			break;
	    		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	    			Toast.makeText(this, R.string.senderror, Toast.LENGTH_LONG).show();
	    			break;
	    		case SmsManager.RESULT_ERROR_NO_SERVICE:
	    		case SmsManager.RESULT_ERROR_RADIO_OFF:
	    			Toast.makeText(this, R.string.noservice, Toast.LENGTH_LONG).show();
	    			break;
	    	}
    	} else if (intent.getAction().equals(this.getPackageName() + MainApp.INTENT_DIALOG_CLOSED)) {
    		if (Utility.getDialogResult()) {
    			this.sendNFMAP(m_maApp.getDatabase().getIotaNum());
    		}
    	}
	}
	
}