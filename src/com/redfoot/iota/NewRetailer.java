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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewRetailer extends Activity {
	
//	private static String SAVED_CITY = "";
	private static String SAVED_BARANGAY = "";
	private static String NRMAP = "";
	
	private MainApp m_maApp;
	private BroadcastReceiver m_brSMS;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newretailer);
        
        m_maApp = (MainApp) this.getApplication();
        m_brSMS = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				processBroadcast(intent, this.getResultCode());
			} 
			
        };
		
		IntentFilter filter = new IntentFilter(this.getPackageName() + MainApp.INTENT_ACTION_SENT);
		
		filter.addAction(this.getPackageName() + MainApp.INTENT_DIALOG_CLOSED);
		this.registerReceiver(m_brSMS, filter);

        //fillCustSubtypes();
//        fillProvinces();
//        buildSalesData();
        
//        Spinner sProvince = (Spinner)this.findViewById(R.id.d_ddlProvince);
        Spinner sCity = (Spinner)this.findViewById(R.id.d_ddlCity);
        Button bSend = (Button)this.findViewById(R.id.d_btnSend);
        Button bCancel = (Button)this.findViewById(R.id.d_btnCancel);
        
//        sProvince.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				CodeDescPair cdpTemp = (CodeDescPair)arg0.getSelectedItem();
//				fillCities(cdpTemp.Code);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			} 
//		
//        });
        
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
								Utility.alertNoGPS(NewRetailer.this);
							} else {
								sendNRMAP(sNum);
							}
						} else {
							sendNRMAP(sNum);
						}
					}
				}
			} 
		
        });
        
        bCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NewRetailer.this.finish();
			}
		
        });
        
        // get last selected province
//    	setSpinnerSelected(R.id.d_ddlProvince, m_maApp.getDatabase().getSystemParam(Database.PARAM_LAST_PROVINCE));
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
				        	new AlertDialog.Builder(NewRetailer.this)
				        	.setTitle(R.string.alert)
				        	.setMessage(R.string.NoIotaMobtel)
				        	.setNeutralButton(R.string.OK, null)
							.show();
						} else if (sNum.length() != 11) {
							bValid = false;
				        	new AlertDialog.Builder(NewRetailer.this)
				        	.setTitle(R.string.alert)
				        	.setMessage(R.string.InvIotaMobtel)
				        	.setNeutralButton(R.string.OK, null)
							.show();
						} else {
							if (!sNum.startsWith("09")) {
								bValid = false;
					        	new AlertDialog.Builder(NewRetailer.this)
					        	.setTitle(R.string.alert)
					        	.setMessage(R.string.InvIotaMobtel)
					        	.setNeutralButton(R.string.OK, null)
								.show();
							} else {
								new AlertDialog.Builder(NewRetailer.this)
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
								Utility.alertNoGPS(NewRetailer.this);
							} else {
								sendNRMAP(sNum);
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

//		NewRetailer.SAVED_CITY = savedInstanceState.getString(this.getString(R.string.City));
		NewRetailer.SAVED_BARANGAY = savedInstanceState.getString(this.getString(R.string.Barangay));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
//		Spinner sCity = (Spinner)this.findViewById(R.id.d_ddlCity);
		Spinner sBarangay = (Spinner)this.findViewById(R.id.d_ddlBarangay);
		
//		outState.putString(this.getString(R.string.City), ((CodeDescPair)sCity.getSelectedItem()).Code);
		outState.putString(this.getString(R.string.Barangay), ((CodeDescPair)sBarangay.getSelectedItem()).Code);
		
		super.onSaveInstanceState(outState);
	}
	
//	private void buildSalesData() {
//		ArrayList<CodeDescPair> cdpProdList = m_maApp.getDatabase().getProductList();
//		LinearLayout llLayout = (LinearLayout)this.findViewById(R.id.d_llMain);
//		int iIndex = llLayout.indexOfChild(this.findViewById(R.id.d_tvSalesData)) + 1;
//		TextView tvTemp;
//		CheckBox cbTemp;
//		String sCurrProdCat = "";
//		
//		for (CodeDescPair cdp : cdpProdList) {
//			if (!sCurrProdCat.equals(cdp.Code)) {
//				tvTemp = new TextView(this);
//				tvTemp.setText(cdp.Code);
//				tvTemp.setTextAppearance(this, android.R.style.TextAppearance_Large);
//				llLayout.addView(tvTemp, iIndex);
//
//				sCurrProdCat = cdp.Code;
//				iIndex++;
//			}
//			
//			cbTemp = new CheckBox(this);
//			cbTemp.setTag(cdp);
//			cbTemp.setText(cdp.Description);
//			cbTemp.setTextAppearance(this, android.R.style.TextAppearance_Large);
//			llLayout.addView(cbTemp, iIndex);
//			
//			iIndex++;
//		}
//	}

	private boolean validateFields() {
		boolean bValid = true;
		String sMessage = this.getString(R.string.ValidateError);
		String sName = ((EditText)this.findViewById(R.id.d_etName)).getEditableText().toString().trim();
		String sMobtel = ((EditText)this.findViewById(R.id.d_etMobtel)).getEditableText().toString().trim();
		String sNo = ((EditText)this.findViewById(R.id.d_etNo)).getEditableText().toString().trim();
		String sStreet = ((EditText)this.findViewById(R.id.d_etStreet)).getEditableText().toString().trim();
		String sAveSmart = ((EditText)this.findViewById(R.id.d_etAveSalesSm)).getEditableText().toString().trim();
		String sAveSun = ((EditText)this.findViewById(R.id.d_etAveSalesSun)).getEditableText().toString().trim();
		
		
		if (sName.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoRetName);
		}
		
		if (sMobtel.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoRetMobtel);
		} else if (sMobtel.length() != 11) {
			bValid = false;
			sMessage += this.getString(R.string.InvRetMobtel);
		} else {
			if (!sMobtel.startsWith("09")) {
				bValid = false;
				sMessage += this.getString(R.string.InvRetMobtel);
			}
		}
		
		if (sNo.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoRetNo);
		}
		
		if (sStreet.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoRetStreet);
		}
		
		if (sAveSmart.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoAveSmTnT);
		} else {
			try {
				if (Double.valueOf(sAveSmart) < 0) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveSmTnT);
				}
			} catch (Exception e) {
				bValid = false;
				sMessage += this.getString(R.string.InvAveSmTnT);
			}
		}
		
		if (sAveSun.length() == 0) {
			bValid = false;
			sMessage += this.getString(R.string.NoAveSun);
		} else {
			try {
				if (Double.valueOf(sAveSun) < 0) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveSun);
				}
			} catch (Exception e) {
				bValid = false;
				sMessage += this.getString(R.string.InvAveSun);
			}
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
	
	private void sendNRMAP(String iotaNum) {
		PendingIntent pi = PendingIntent.getBroadcast(NewRetailer.this, MainApp.REQUEST_ACTION_SENT, 
													  new Intent(this.getPackageName() + MainApp.INTENT_ACTION_SENT), 0);
		SmsManager sms = SmsManager.getDefault();
		EditText etMobtel = (EditText)this.findViewById(R.id.d_etMobtel);
		EditText etName = (EditText)this.findViewById(R.id.d_etName);
		EditText etNo = (EditText)this.findViewById(R.id.d_etNo);
		EditText etStreet = (EditText)this.findViewById(R.id.d_etStreet);
		Spinner sBarangay = (Spinner)this.findViewById(R.id.d_ddlBarangay);
		Spinner sCity = (Spinner)this.findViewById(R.id.d_ddlCity);
		Spinner sProvince = (Spinner)this.findViewById(R.id.d_ddlProvince);
		Spinner sSubtype = (Spinner)this.findViewById(R.id.d_ddlCustSubType);
		EditText etRemarks = (EditText)this.findViewById(R.id.d_etRemarks);
		EditText etAveSmart = (EditText)this.findViewById(R.id.d_etAveSalesSm);
		EditText etAveSun = (EditText)this.findViewById(R.id.d_etAveSalesSun);
		String sChecks = "";
//		LinearLayout llLayout = (LinearLayout)this.findViewById(R.id.d_llMain);
//		int iIndex = llLayout.indexOfChild(this.findViewById(R.id.d_tvSalesData)) + 1;
//		View vTemp = llLayout.getChildAt(iIndex);
		ArrayList<CodeDescPair> alSubtypes = m_maApp.getDatabase().getCustSubTypes();
		String sSubtypeCode = "";
		
//		while (vTemp.getId() != R.id.d_tvAveSales) {
//			if (vTemp instanceof CheckBox) {
//				CheckBox cbTemp = (CheckBox)vTemp;
//				sChecks += cbTemp.isChecked() ? "1" : "0";
//			}
//			
//			iIndex++;
//			vTemp = llLayout.getChildAt(iIndex);
//		}
		
		for (CodeDescPair cdp : alSubtypes) {
			if (cdp.Description.equalsIgnoreCase(sSubtype.getSelectedItem().toString())) {
				sSubtypeCode = cdp.Code;
				break;
			}
		}
		
		NRMAP = this.getString(R.string.NRMAP) + " " + etMobtel.getEditableText().toString() + "/" + etName.getEditableText().toString() + 
				"/" + etNo.getEditableText().toString() + "," + etStreet.getEditableText().toString() + "," + 
				((CodeDescPair)sBarangay.getSelectedItem()).Code + "," + ((CodeDescPair)sCity.getSelectedItem()).Code + 
				"/" + sSubtypeCode + "/" + m_maApp.getGPSLong() + "/" + m_maApp.getGPSLat() + "/" + sChecks + "/" + 
				etAveSmart.getEditableText().toString() + "/" + etAveSun.getEditableText().toString() + "/" + 
				etRemarks.getEditableText().toString();
		
		m_maApp.getDatabase().setSystemParam(Database.PARAM_LAST_PROVINCE, ((CodeDescPair)sProvince.getSelectedItem()).Code);
		m_maApp.getDatabase().setSystemParam(Database.PARAM_LAST_CITY, ((CodeDescPair)sCity.getSelectedItem()).Code);
		m_maApp.getDatabase().setSystemParam(Database.PARAM_LAST_BARANGAY, ((CodeDescPair)sBarangay.getSelectedItem()).Code);
		
		sms.sendTextMessage(iotaNum, null, NRMAP, pi, null);
	}

//	private void fillCustSubtypes() {
//    	ArrayList<CodeDescPair> alSubtypes = m_maApp.getDatabase().getCustSubTypes();
//    	Spinner sSubtypes = (Spinner)this.findViewById(R.id.d_ddlCustSubType);
//    	ArrayAdapter<CodeDescPair> aaAdapter = new ArrayAdapter<CodeDescPair>(this, android.R.layout.simple_spinner_item);
//    	
//    	aaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    	for (CodeDescPair cdp : alSubtypes) {
//    		aaAdapter.add(cdp);
//    	}
//    	sSubtypes.setAdapter(aaAdapter);
//    }
//    
//    private void setSpinnerSelected(int id, String code) {
//    	Spinner sTemp = (Spinner)this.findViewById(id);
//    	int iItems = sTemp.getCount();
//    	CodeDescPair cdpTemp;
//    	
//    	for (int i = 0; i < iItems; i++) {
//    		cdpTemp = (CodeDescPair)sTemp.getItemAtPosition(i);
//    		if (cdpTemp.Code.equals(code)) {
//    			sTemp.setSelection(i);
//    			break;
//    		}
//    	}
//    }
//    
//    private void fillProvinces() {
//    	ArrayList<CodeDescPair> alProvinces = m_maApp.getDatabase().getProvinces();
//    	Spinner sProvinces = (Spinner)this.findViewById(R.id.d_ddlProvince);
//    	ArrayAdapter<CodeDescPair> aaAdapter = new ArrayAdapter<CodeDescPair>(this, android.R.layout.simple_spinner_item);
//    	
//    	aaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    	for (CodeDescPair cdp : alProvinces) {
//    		aaAdapter.add(cdp);
//    	}
//    	sProvinces.setAdapter(aaAdapter);
//    }
    
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
    
    private void fillBarangays(String city) {
    	ArrayList<CodeDescPair> alBarangays = m_maApp.getDatabase().getBarangays(city);
    	Spinner sBarangays = (Spinner)this.findViewById(R.id.d_ddlBarangay);
    	ArrayAdapter<CodeDescPair> aaAdapter = new ArrayAdapter<CodeDescPair>(this, android.R.layout.simple_spinner_item);
    	String sLastBrgy = m_maApp.getDatabase().getSystemParam(Database.PARAM_LAST_BARANGAY);
    	int iIndex = 0;
    	int iLast = 0;
    	
    	aaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	for (CodeDescPair cdp : alBarangays) {
    		aaAdapter.add(cdp);
    		if (cdp.Code.equals(NewRetailer.SAVED_BARANGAY)) {
    			iIndex = aaAdapter.getCount() - 1;
    		}
    		if (cdp.Code.equals(sLastBrgy)) {
    			iLast = aaAdapter.getCount() - 1;
    		}
    	}
    	sBarangays.setAdapter(aaAdapter);
    	
    	if (NewRetailer.SAVED_BARANGAY.length() > 0) {
			sBarangays.setSelection(iIndex, true);
    	} else {
    		sBarangays.setSelection(iLast, true);
    	}
		NewRetailer.SAVED_BARANGAY = ((CodeDescPair)sBarangays.getSelectedItem()).Code;
    }
    
    private void processBroadcast(Intent intent, int result) {
    	if (intent.getAction().equals(this.getPackageName() + MainApp.INTENT_ACTION_SENT)) {
	    	switch (result) {
	    		case Activity.RESULT_OK:
	    			ContentValues cvTemp = new ContentValues();
	    			
	    			Toast.makeText(this, R.string.msgsent, Toast.LENGTH_LONG).show();
	    			cvTemp.put("address", m_maApp.getDatabase().getIotaNum());
	    			cvTemp.put("body", NRMAP);
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
    			this.sendNRMAP(m_maApp.getDatabase().getIotaNum());
    		}
    	}
    }
    
}
