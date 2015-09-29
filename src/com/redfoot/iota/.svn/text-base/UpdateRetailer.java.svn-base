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

public class UpdateRetailer extends Activity implements OnItemClickListener {
 // =========================================================================
 // TODO Variables
 // =========================================================================
	private static String SAVED_BARANGAY = "";
	//private static String URMAP = "";
	
	private static String URMAP001 = "";
	private static String URMAP002 = "";
	
	private MainApp m_maApp;
	private BroadcastReceiver m_brSMS;
	private ListView m_lvEstablishments;
 // =========================================================================
 // TODO Life Cycles
 // =========================================================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateretailer);
        
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
								Utility.alertNoGPS(UpdateRetailer.this);
							} else {
								sendURMAP(sNum);
							}
						} else {
							sendURMAP(sNum);
						}
					}
				}
			} 
		
        });
        
        bCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UpdateRetailer.this.finish();
			}
		});
        
        // get last selected City
    	setSpinnerSelected(R.id.d_ddlCity, m_maApp.getDatabase().getSystemParam(Database.PARAM_LAST_CITY));
    }
    // ---------------------------------------------------------------------
	@Override
	protected void onPause() {
		super.onPause();
		
		this.unregisterReceiver(m_brSMS);
	}
// ---------------------------------------------------------------------
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter(this.getPackageName() + MainApp.INTENT_ACTION_SENT);
		
		filter.addAction(this.getPackageName() + MainApp.INTENT_DIALOG_CLOSED);
		this.registerReceiver(m_brSMS, filter);
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
				
				Button bOK = (Button)dTemp.findViewById(R.id.d_btnOK);
				Button bCancel = (Button)dTemp.findViewById(R.id.d_btnCancel);
				bOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String sNum = ((EditText)dTemp.findViewById(R.id.d_etMobtel)).getEditableText().toString();
						boolean bValid = true;
						
						if (sNum.length() == 0) {
							bValid = false;
				        	new AlertDialog.Builder(UpdateRetailer.this)
				        	.setTitle(R.string.alert)
				        	.setMessage(R.string.NoIotaMobtel)
				        	.setNeutralButton(R.string.OK, null)
							.show();
						} else if (sNum.length() != 11) {
							bValid = false;
				        	new AlertDialog.Builder(UpdateRetailer.this)
				        	.setTitle(R.string.alert)
				        	.setMessage(R.string.InvIotaMobtel)
				        	.setNeutralButton(R.string.OK, null)
							.show();
						} else {
							if (!sNum.startsWith("09")) {
								bValid = false;
					        	new AlertDialog.Builder(UpdateRetailer.this)
					        	.setTitle(R.string.alert)
					        	.setMessage(R.string.InvIotaMobtel)
					        	.setNeutralButton(R.string.OK, null)
								.show();
							} else {
								new AlertDialog.Builder(UpdateRetailer.this)
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
								Utility.alertNoGPS(UpdateRetailer.this);
							} else {
								sendURMAP(sNum);
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
// ---------------------------------------------------------------------
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		UpdateRetailer.SAVED_BARANGAY = savedInstanceState.getString(this.getString(R.string.Barangay));
	}
// ---------------------------------------------------------------------
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Spinner sBarangay = (Spinner)this.findViewById(R.id.d_ddlBarangay);
		
		outState.putString(this.getString(R.string.Barangay), ((CodeDescPair)sBarangay.getSelectedItem()).Code);

		super.onSaveInstanceState(outState);
	}
 // =========================================================================
 // TODO Main Functions
 // =========================================================================
	protected void sendURMAP(String sNum) {
		Spinner sType = (Spinner)this.findViewById(R.id.d_ddlType);
		EditText etMobtel = (EditText)this.findViewById(R.id.d_etMobtel); 
		EditText etName = (EditText)this.findViewById(R.id.d_etName);
		Spinner sSubType = (Spinner)this.findViewById(R.id.d_ddlCustSubType);
		EditText etNo = (EditText)this.findViewById(R.id.d_etNo);
		EditText etStreet = (EditText)this.findViewById(R.id.d_etStreet);
		Spinner sBarangay = (Spinner)this.findViewById(R.id.d_ddlBarangay);
		Spinner sCity = (Spinner)this.findViewById(R.id.d_ddlCity);
		Spinner sSizeStore = (Spinner)this.findViewById(R.id.d_ddlSizeStore);
		CheckBox sCbMerchPresent = (CheckBox)this.findViewById(R.id.d_cbMerchPresent);
		CheckBox sCbMerchDominant = (CheckBox)this.findViewById(R.id.d_cbMerchDominant);
		EditText stockGP = (EditText)this.findViewById(R.id.d_etStockSim_GP);
		EditText stockTM = (EditText)this.findViewById(R.id.d_etStockSim_TM);
		EditText stockSm = (EditText)this.findViewById(R.id.d_etStockSim_Sm);
		EditText stockTnT = (EditText)this.findViewById(R.id.d_etStockSim_TnT);
		EditText stockSun = (EditText)this.findViewById(R.id.d_etStockSim_Sun);
		EditText aveReloadGP = (EditText)this.findViewById(R.id.d_etAveSalesGP_Reload);
		EditText aveReloadTM = (EditText)this.findViewById(R.id.d_etAveSalesTM_Reload);
		EditText aveReloadSm = (EditText)this.findViewById(R.id.d_etAveSalesSm_Reload);
		EditText aveReloadTnT = (EditText)this.findViewById(R.id.d_etAveSalesTnT_Reload);
		EditText aveReloadSun = (EditText)this.findViewById(R.id.d_etAveSalesSun_Reload);
		EditText aveSimGP = (EditText)this.findViewById(R.id.d_etAveSalesGP_Sim);
		EditText aveSimTM = (EditText)this.findViewById(R.id.d_etAveSalesTM_Sim);
		EditText aveSimSm = (EditText)this.findViewById(R.id.d_etAveSalesSm_Sim);
		EditText aveSimTnT = (EditText)this.findViewById(R.id.d_etAveSalesTnT_Sim);
		EditText aveSimSun = (EditText)this.findViewById(R.id.d_etAveSalesSun_Sim);
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
		
		//region Old Codes
		/*URMAP = this.getString(R.string.URMAP) + " " + etMobtel.getEditableText().toString() + "/" + etName.getEditableText().toString() + "/" + etNo.getEditableText().toString() + "," 
				+ etStreet.getEditableText().toString() + "," + ((CodeDescPair)sBarangay.getSelectedItem()).Code + "," + ((CodeDescPair)sCity.getSelectedItem()).Code + "/" + sSubtypeCode 
				+ "/" + m_maApp.getGPSLong() + "/" + m_maApp.getGPSLat() + "/" + sStoreSizeCode + "/" +  sumList + "/" + sCbMerchPresentString + "/" + sCbMerchDominantString + "/"
				+ stockGP.getEditableText().toString() + "," + stockTM.getEditableText().toString() + "," + stockSm.getEditableText().toString() + "," + stockTnT.getEditableText().toString() 
				+ "," + stockSun.getEditableText().toString() + "/" + aveReloadGP.getEditableText().toString() + "," + aveReloadTM.getEditableText().toString() + "," 
				+ aveReloadSm.getEditableText().toString() + "," + aveReloadTnT.getEditableText().toString() + "," + aveReloadSun.getEditableText().toString() + "/" + aveSimGP.getEditableText().toString() 
				+ "," + aveSimTM.getEditableText().toString() + "," + aveSimSm.getEditableText().toString() + "," + aveSimTnT.getEditableText().toString() + "," + aveSimSun.getEditableText().toString() 
				+ "/" + sInterviewCode + "/" + sInterviewName.getEditableText().toString() + "/" + sPhone.getEditableText().toString() + "/" + sOwnerName.getEditableText().toString() + "/" 
				+ etRemarks.getEditableText().toString() +  "/" + sType.getSelectedItem().toString();
				*/
		//endregion 
		
		int txtID = MainApp.getTextID(this);
		
		URMAP001 = this.getString(R.string.URMAP) + "A "+ txtID + " " + etMobtel.getEditableText().toString() + "/" + etName.getEditableText().toString() + "/" + etNo.getEditableText().toString() + "," 
				+ etStreet.getEditableText().toString() + "," + ((CodeDescPair)sBarangay.getSelectedItem()).Code + "," + ((CodeDescPair)sCity.getSelectedItem()).Code + "/" + sSubtypeCode 
				+ "/" + m_maApp.getGPSLong() + "/" + m_maApp.getGPSLat() + "/" + sStoreSizeCode + "/" +  sumList + "/" + sCbMerchPresentString + "/" + sCbMerchDominantString + "/"
				+ stockGP.getEditableText().toString() + "," + stockTM.getEditableText().toString() + "," + stockSm.getEditableText().toString() + "," + stockTnT.getEditableText().toString() 
				+ "," + stockSun.getEditableText().toString() + "/" + aveReloadGP.getEditableText().toString() + "," + aveReloadTM.getEditableText().toString() + "," 
				+ aveReloadSm.getEditableText().toString() + "," + aveReloadTnT.getEditableText().toString() + "," + aveReloadSun.getEditableText().toString();
		
		URMAP002 = this.getString(R.string.URMAP) + "B "+ txtID + " /" + aveSimGP.getEditableText().toString() + "," + aveSimTM.getEditableText().toString() + "," + aveSimSm.getEditableText().toString() + "," + aveSimTnT.getEditableText().toString() + "," + aveSimSun.getEditableText().toString() 
				+ "/" + sInterviewCode + "/" + sInterviewName.getEditableText().toString() + "/" + sPhone.getEditableText().toString() + "/" + sOwnerName.getEditableText().toString() + "/" 
				+ etRemarks.getEditableText().toString() +  "/" + sType.getSelectedItem().toString();
		MainApp.setAndIncrementTextID(this);
		
		m_maApp.getDatabase().setSystemParam(Database.PARAM_LAST_CITY, ((CodeDescPair)sCity.getSelectedItem()).Code);
		m_maApp.getDatabase().setSystemParam(Database.PARAM_LAST_BARANGAY, ((CodeDescPair)sBarangay.getSelectedItem()).Code);
		
		//ArrayList<PendingIntent> sendPendingIntent = new ArrayList<PendingIntent>();
		PendingIntent pi = PendingIntent.getBroadcast(UpdateRetailer.this, MainApp.REQUEST_ACTION_SENT, 
				  									  new Intent(this.getPackageName() + MainApp.INTENT_ACTION_SENT), 0);
		
		SmsManager sms = SmsManager.getDefault();
		//region Old Codes
		/*ArrayList<String> parts = sms.divideMessage(URMAP);
		for (int i = 0; i <parts.size(); i++){
			sendPendingIntent.add(i, pi);
		}
		sms.sendMultipartTextMessage(sNum, null, parts, sendPendingIntent, null);
		*/
		//endregion
	
		sms.sendTextMessage(sNum, null, URMAP001, null, null);
		sms.sendTextMessage(sNum, null, URMAP002, pi, null);
	}
// ---------------------------------------------------------------------
	protected boolean validateFields() {
		boolean bValid = true;
		String sMessage = this.getString(R.string.ValidateError);
		String sMobtel = ((EditText)this.findViewById(R.id.d_etMobtel)).getEditableText().toString().trim();
		String sName = ((EditText)this.findViewById(R.id.d_etName)).getEditableText().toString().trim();
		String sNo = ((EditText)this.findViewById(R.id.d_etNo)).getEditableText().toString().trim();
		String sStreet = ((EditText)this.findViewById(R.id.d_etStreet)).getEditableText().toString().trim();
		String sStockGP = ((EditText)this.findViewById(R.id.d_etStockSim_GP)).getEditableText().toString().trim();
		String sStockTM = ((EditText)this.findViewById(R.id.d_etStockSim_TM)).getEditableText().toString().trim();
		String sStockSm = ((EditText)this.findViewById(R.id.d_etStockSim_Sm)).getEditableText().toString().trim();
		String sStockTnT = ((EditText)this.findViewById(R.id.d_etStockSim_TnT)).getEditableText().toString().trim();
		String sStockSun = ((EditText)this.findViewById(R.id.d_etStockSim_Sun)).getEditableText().toString().trim();
		String sAveReload = ((EditText)this.findViewById(R.id.d_etAveSalesReload)).getEditableText().toString().trim();
		String sAveReload_GP = ((EditText)this.findViewById(R.id.d_etAveSalesGP_Reload)).getEditableText().toString().trim();
		String sAveReload_TM = ((EditText)this.findViewById(R.id.d_etAveSalesTM_Reload)).getEditableText().toString().trim();
		String sAveReload_Sm = ((EditText)this.findViewById(R.id.d_etAveSalesSm_Reload)).getEditableText().toString().trim();
		String sAveReload_TnT = ((EditText)this.findViewById(R.id.d_etAveSalesTnT_Reload)).getEditableText().toString().trim();
		String sAveReload_Sun = ((EditText)this.findViewById(R.id.d_etAveSalesSun_Reload)).getEditableText().toString().trim();
		String sAveSim = ((EditText)this.findViewById(R.id.d_etAveSalesSim)).getEditableText().toString().trim();
		String sAveSim_GP = ((EditText)this.findViewById(R.id.d_etAveSalesGP_Sim)).getEditableText().toString().trim();
		String sAveSim_TM = ((EditText)this.findViewById(R.id.d_etAveSalesTM_Sim)).getEditableText().toString().trim();
		String sAveSim_Sm = ((EditText)this.findViewById(R.id.d_etAveSalesSm_Sim)).getEditableText().toString().trim();
		String sAveSim_TnT = ((EditText)this.findViewById(R.id.d_etAveSalesTnT_Sim)).getEditableText().toString().trim();
		String sAveSim_Sun = ((EditText)this.findViewById(R.id.d_etAveSalesSun_Sim)).getEditableText().toString().trim();
		String sInterviewedName = ((EditText)this.findViewById(R.id.d_etInterviewedName)).getEditableText().toString().trim();
		String sInterviewedTelephone = ((EditText)this.findViewById(R.id.d_etInterviewedTelephone)).getEditableText().toString().trim();
		String sOwnerName = ((EditText)this.findViewById(R.id.d_etOwnerName)).getEditableText().toString().trim();
		
		
		if (sMobtel.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoRetMobtel);
		} else if (sMobtel.length() != 11){
			bValid = false;
			sMessage += this.getString(R.string.InvRetMobtel);
		} else {
			if (!sMobtel.startsWith("09")){
				bValid = false;
				sMessage += this.getString(R.string.InvRetMobtel);
			}
		}
		
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
		
		if(sStockGP.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoStockGP);
		} else {
			try {
				if (Double.valueOf(sStockGP) < 0){
					bValid = false;
					sMessage += this.getString(R.string.InvStockGP);
				}
			} catch (Exception e) {
				bValid = false;
				sMessage += this.getString(R.string.InvStockGP);
			}
		}
		
		if(sStockTM.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoStockTM);
		} else {
			try {
				if (Double.valueOf(sStockTM) < 0){
					bValid = false;
					sMessage += this.getString(R.string.InvStockTM);
				}
			} catch (Exception e) {
				bValid = false;
				sMessage += this.getString(R.string.InvStockTM);
			}
		}
		
		if(sStockSm.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoStockSm);
		} else {
			try {
				if (Double.valueOf(sStockSm) < 0){
					bValid = false;
					sMessage += this.getString(R.string.InvStockSm);
				}
			} catch (Exception e) {
				bValid = false;
				sMessage += this.getString(R.string.InvStockSm);
			}
		}
		
		if(sStockTnT.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoStockTnT);
		} else {
			try {
				if (Double.valueOf(sStockTnT) < 0){
					bValid = false;
					sMessage += this.getString(R.string.InvStockTnT);
				}
			} catch (Exception e) {
				bValid = false;
				sMessage += this.getString(R.string.InvStockTnT);
			}
		}
		
		if(sStockSun.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoStockSun);
		} else {
			try {
				if (Double.valueOf(sStockSun) < 0){
					bValid = false;
					sMessage += this.getString(R.string.InvStockSun);
				}
			} catch (Exception e) {
				bValid = false;
				sMessage += this.getString(R.string.InvStockSun);
			}
		}
		
		if ((sAveReload.length() != 0 && !(Double.valueOf(sAveReload) < 0)) && (sAveReload_GP.length() != 0 && !(Double.valueOf(sAveReload_GP) < 0))
				&& (sAveReload_TM.length() != 0 && !(Double.valueOf(sAveReload_TM) < 0)) && (sAveReload_Sm.length() != 0 && !(Double.valueOf(sAveReload_Sm) < 0))
				&& (sAveReload_TnT.length() != 0 && !(Double.valueOf(sAveReload_TnT) < 0)) && (sAveReload_Sun.length() != 0 && !(Double.valueOf(sAveReload_Sun) < 0))){
				
			int sumTemp = (Integer.parseInt(sAveReload_GP) + Integer.parseInt(sAveReload_TM) + Integer.parseInt(sAveReload_Sm) 
					+ Integer.parseInt(sAveReload_TnT) + Integer.parseInt(sAveReload_Sun));
			
			int ave = Integer.parseInt(sAveReload);
			
			if (ave != sumTemp){
				bValid = false;
				sMessage += this.getString(R.string.ave_reload);
			}
				
		} else {
			
			if (sAveReload.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveReload);
			} else {
				try {
					if (Double.valueOf(sAveReload) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveReload);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveReload);
				}
			}
			
			if (sAveReload_GP.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveReloadGP);
			} else {
				try {
					if (Double.valueOf(sAveReload_GP) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveReloadGP);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveReloadGP);
				}
			}
			
			if (sAveReload_TM.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveReloadTM);
			} else {
				try {
					if (Double.valueOf(sAveReload_TM) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveReloadTM);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveReloadTM);
				}
			}
			
			if (sAveReload_Sm.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveReloadSm);
			} else {
				try {
					if (Double.valueOf(sAveReload_Sm) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveReloadSm);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveReloadSm);
				}
			}
			
			
			if (sAveReload_TnT.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveReloadTnT);
			} else {
				try {
					if (Double.valueOf(sAveReload_TnT) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveReloadTnT);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveReloadTnT);
				}
			}
			
			if (sAveReload_Sun.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveReloadSun);
			} else {
				try {
					if (Double.valueOf(sAveReload_Sun) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveReloadSun);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveReloadSun);
				}
			}
			
		}
		

		if ((sAveSim.length() != 0 && !(Double.valueOf(sAveSim) < 0)) && (sAveSim_GP.length() != 0 && !(Double.valueOf(sAveSim_GP) < 0))
				&& (sAveSim_TM.length() != 0 && !(Double.valueOf(sAveSim_TM) < 0)) && (sAveSim_Sm.length() != 0 && !(Double.valueOf(sAveSim_Sm) < 0))
				&& (sAveSim_TnT.length() != 0 && !(Double.valueOf(sAveSim_TnT) < 0)) && (sAveSim_Sun.length() != 0 && !(Double.valueOf(sAveSim_Sun) < 0))){
				
			int sumTemp = (Integer.parseInt(sAveSim_GP) + Integer.parseInt(sAveSim_TM) + Integer.parseInt(sAveSim_Sm) 
					+ Integer.parseInt(sAveSim_TnT) + Integer.parseInt(sAveSim_Sun));
			
			int ave = Integer.parseInt(sAveSim);
			
			if (ave != sumTemp){
				bValid = false;
				sMessage += this.getString(R.string.ave_sim);
			}
				
		} else {
			
			if (sAveSim.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveSim);
			} else {
				try {
					if (Double.valueOf(sAveReload) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveSim);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveSim);
				}
			}
			
			if (sAveSim_GP.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveSimGP);
			} else {
				try {
					if (Double.valueOf(sAveSim_GP) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveSimGP);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveSimGP);
				}
			}
			
			if (sAveSim_TM.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveSimTM);
			} else {
				try {
					if (Double.valueOf(sAveSim_TM) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveSimTM);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveSimTM);
				}
			}
			
			if (sAveSim_Sm.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveSimSm);
			} else {
				try {
					if (Double.valueOf(sAveSim_Sm) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveSimSm);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveSimSm);
				}
			}
			
			
			if (sAveSim_TnT.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveSimTnT);
			} else {
				try {
					if (Double.valueOf(sAveSim_TnT) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveSimTnT);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveSimTnT);
				}
			}
			
			if (sAveSim_Sun.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoAveSimSun);
			} else {
				try {
					if (Double.valueOf(sAveSim_Sun) < 0){
						bValid = false;
						sMessage += this.getString(R.string.InvAveSimSun);
					}
				} catch (Exception e) {
					bValid = false;
					sMessage += this.getString(R.string.InvAveSimSun);
				}
			}
			
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
// ---------------------------------------------------------------------
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
    // ---------------------------------------------------------------------
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
    // ---------------------------------------------------------------------
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
    		if (cdp.Code.equals(UpdateRetailer.SAVED_BARANGAY)) {
    			iIndex = aaAdapter.getCount() - 1;
    		}
    		if (cdp.Code.equals(sLastBrgy)) {
    			iLast = aaAdapter.getCount() - 1;
    		}
    	}
    	sBarangays.setAdapter(aaAdapter);

    	if (UpdateRetailer.SAVED_BARANGAY.length() > 0) {
			sBarangays.setSelection(iIndex, true);
    	} else {
    		sBarangays.setSelection(iLast, true);
    	}
    	UpdateRetailer.SAVED_BARANGAY = ((CodeDescPair)sBarangays.getSelectedItem()).Code;
	}
	// ---------------------------------------------------------------------
	private void fillCheckList() {
		// TODO Auto-generated method stub
		m_lvEstablishments = (ListView)this.findViewById(R.id.d_lvEstablishment);
    	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.checklistitem,
    			getResources().getStringArray(R.array.establishments));

        m_lvEstablishments.setAdapter(adapter);
        m_lvEstablishments.setItemChecked(6, true);
        
        m_lvEstablishments.setOnItemClickListener(this);
	}
 // =========================================================================
 // TODO Implementations
 // =========================================================================	
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
 // =========================================================================
 // TODO Sub Functions
 // =========================================================================
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
	// ---------------------------------------------------------------------
	protected void processBroadcast(Intent intent, int resultCode) {
    	if (intent.getAction().equals(this.getPackageName() + MainApp.INTENT_ACTION_SENT)) {
	    	switch (resultCode) {
	    		case Activity.RESULT_OK:
	    			ContentValues cvTemp = new ContentValues();
	    			
	    			Toast.makeText(this, R.string.msgsent, Toast.LENGTH_LONG).show();
	    			cvTemp.put("address", m_maApp.getDatabase().getIotaNum());
	    			cvTemp.put("body", URMAP001 + URMAP002);
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
    			this.sendURMAP(m_maApp.getDatabase().getIotaNum());
    		}
    	}
	}
 // =========================================================================
 // TODO Final Codes
}
