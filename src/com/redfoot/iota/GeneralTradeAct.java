package com.redfoot.iota;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import utilities.generaltrade.BroadcastProcessUtils;
import utilities.generaltrade.DialogPrompt;
import utilities.generaltrade.Implements;
import utilities.generaltrade.IntoNumber;
import utilities.generaltrade.PopulateData;
import utilities.generaltrade.SendMessageUtils;
import utilities.generaltrade.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.redfoot.iota.objects.DraftObj;
import com.redfoot.iota.objects.Enums;
import com.redfoot.iota.objects.MyUncaughtException;
import com.redfoot.iota.objects.NetObservable;
import com.redfoot.iota.objects.NumberWithCommaTextWatcher;
import com.redfoot.iota.utils.Configs;
import com.redfoot.iota.utils.Utils;

public class GeneralTradeAct extends Activity implements Observer{
// =========================================================================
// TODO Variables 
// =========================================================================
	private MainApp mainApp;
	private static String MAINMESSAGE = "";
	private static String MAPCODE = "";
	private static String SAVEDRAFTMESSAGE = "";
	
	private EditText etOutput, etNameOfStore, etInterviewee, etUnitNo, etStreet,
			etGlobeRetailerNo, etGlobeLoad, etSmartLoad, etSunLoad, etGPStockSim,
			etTMStockSim, etABSStockSim, etSmartStockSim, etTntStockSim, etSunStockSim,
			etGPSimSales, etTMSimSales, etABSSimSales, etSmartSimSales, etTNTSimSales,
			etSUNSimSales, etTelMobtel, etOwner,
			etOneSimTraceNo, etGlobeRetailerNum, etOneSimRetailerNum,
			etStoreTypeOthers;
	 
	private Spinner spnStoreType, spnStoreLocation, spnInterviewedPerson, spnProvinces, spnCities,
			spnBarangay, spnGlobeSellingLoad, spnGPTMABS, spnSMARTtnt, spnSun, spnSellingSim,
			spnSmartSellingLoad, spnSunSellingLoad,
			spnLoadCategory;
	
	private ListView lvNearEstablishment;
	
	private RadioGroup rgGlobeSelections, rgOneSimSelections;
	
	private RadioButton rbGlobeRetailer, rbRetailerRefuse, rbNonGlobe;
	
	private LinearLayout llVisbleSim, llSimSales, llLinearGlobe, llLinearOneSim;
	
	private Button btnSend, btnCancel;
	
	private TableLayout tblTakePhoto;
	
	private CheckBox cbRefusedTestBuy, cbOneSimRefuse;
	
	private int DEBUG_FIELD = 0;
	
	static final int REQUEST_IMAGE_CAPTURE = 2;
	static final int REQUEST_TAKE_PHOTO = 1;

	private String mCurrentPhotoPath, mOldPhotoPath;
	//private int RETURN = 0;
	private ImageView ivImageIcon;
	// for Draft Variables
	private boolean isDraft = false;
	private ArrayList<DraftObj> DraftList = new ArrayList<DraftObj>();
	private String[] draftCities;
	private String[] draftOptions;
	private int twiceOnlyCitiesDraft = 1;
	private boolean onceOnlyBarangayDraft = true;
	private int isDraftRefuseToGiveSet = 1;
	private String OwnerName = "";
	private int OwnerNameCount = 1;
	
	private NetObservable txtObserver = new NetObservable();
	private ProgressBar pbSendProgress;
	//
	private ArrayList<String> TextMessagePart = new ArrayList<String>();
	private boolean[] isAllPartSend;
	private int currentTextPart = 1;
	private int incTextPart = 1;
// =========================================================================
// TODO Activity Life Cycle
// =========================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.iLogCat(this.getClass().getSimpleName() +": onCreate", "LifeCyle - Begin");
		//
		setContentView(R.layout.mapping_form);
		// --
		//set debug mode if true
		Thread.currentThread();
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtException(this));
		//
		SetupWidgets();
		//
		if(MainApp.getDebug(this)){
			Utils.MessageToast(this, "DebugMode is ON");
		}
		//
		etOutput.setVisibility(View.GONE);
		
		if(MainApp.PHOTOSYNC_MODE == 1){
			tblTakePhoto.setVisibility(View.VISIBLE);
		}else {
			tblTakePhoto.setVisibility(View.GONE);
		}
		
		// test error make only ---@
//		String test = "1.1";
//		int num = Integer.parseInt(test);
		// --
		PopulateData.fillChecklist(this, lvNearEstablishment);
		PopulateData.fillProvince(this, mainApp, spnProvinces);
		// --
		if(getIntent().hasExtra("DraftID")){
			isDraft = true;
			
			int id = getIntent().getIntExtra("DraftID", 0);
			populateDraft(id);
		}
		Utils.iLogCat(this.getClass().getSimpleName() +": onCreate", "LifeCyle - End");
		// test only dialog
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

		IntentFilter filter = new IntentFilter(this.getPackageName()+ MainApp.INTENT_ACTION_SENT);
		filter.addAction(this.getPackageName() + MainApp.INTENT_DIALOG_CLOSED);
		
		this.registerReceiver(brSMS, filter);
	}
// =========================================================================
// TODO Overrides
// =========================================================================
	@Override
	protected Dialog onCreateDialog(int id) {
		//
		switch (id) {
		case MainApp.DIALOG_IOTA_NUM_ID:
			
			Dialog dial = DialogPrompt.IotaNumber.Edit(this, mainApp);
			return dial;
		default:
			return super.onCreateDialog(id);
		}
	} 
	// =========================================================================
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
	    	
	    	ProcessImageCapture();
	    }
	}
	// =========================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(MainApp.SAVEDRAFT_MODE == 1){
				if (isSaveDraftValid()) {
					dialogSaveDraftPrompt();
				}
			} else {
				callLeaveDialog();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	// =========================================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater miTemp = getMenuInflater();

		miTemp.inflate(R.menu.savedraft, menu);

		return true;
	}
	// =========================================================================
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.Draft:
			if (isSaveDraftValid()) {
				//dialogSaveDraftPrompt();
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
// =========================================================================
// TODO onClick View  
// =========================================================================
	@SuppressWarnings("unused")
	public void onSend(View v){
		//
		if (validate()) {
			String sNum = mainApp.getDatabase().getIotaNum();
			if (sNum.length() == 0) {
				showDialog(MainApp.DIALOG_IOTA_NUM_ID);
			} else {
				if (mainApp.getGPSStatus() != LocationProvider.AVAILABLE) {
					//
					if(MainApp.DEBUG == 0){
						Utility.alertNoGPS(this);
					} else {
						sendMessage(sNum);
					}
				} else {
					sendMessage(sNum);
				}
			}
		}
	}
	// =========================================================================
	public void onCancel(View v){
		
		if(MainApp.SAVEDRAFT_MODE == 1){
			if (isSaveDraftValid()) {
				
				//dialogSaveDraftPrompt();
			} else {
				this.finish();
			}
		} else {
			this.finish();
		}
		
	}
	// =========================================================================
	public void onCameraOpen(View v){
		//
		if(photoValidate()){
			dispatchTakePictureIntent();
		}
	}
// =========================================================================
// TODO Main Functions 
// =========================================================================
	public void SetupWidgets() { 
		//
		mainApp = (MainApp) this.getApplication();
		txtObserver.addObserver(this);
		//
		etOutput = (EditText)findViewById(R.id.etMessagOut);
		//
		etNameOfStore = (EditText)findViewById(R.id.d_etName);
		//
		//Object test = etNameOfStore;
		// test only reserved
//		if(test instanceof EditText){
//			//Toast.makeText(this, "EditText is it", Toast.LENGTH_LONG).show();
//			EditText txt = (EditText)test;
//			if(txt.getId() == R.id.d_btnCancel){
//				Toast.makeText(this, "Store Name is it", Toast.LENGTH_LONG).show();
//			}
//		} else {
//			Toast.makeText(this, "NULL", Toast.LENGTH_LONG).show();
//		}
		//
		//All Text to Upper Case codes
		etNameOfStore.setFilters(new InputFilter[]{ new InputFilter.AllCaps(),
				new InputFilter.LengthFilter(25) });
		
		spnStoreType = (Spinner)findViewById(R.id.d_ddlCustSubType);
		spnStoreType.setOnItemSelectedListener(storeTypeSelected);
		
		etStoreTypeOthers = (EditText)findViewById(R.id.etStoreTypeOthers);
		etStoreTypeOthers.setFilters(new InputFilter[]{	new InputFilter.AllCaps(), 
				new InputFilter.LengthFilter(25)});
		etStoreTypeOthers.setVisibility(View.GONE);
		
		spnStoreLocation = (Spinner)findViewById(R.id.spnStoreLocation);
		lvNearEstablishment = (ListView)findViewById(R.id.lvNearEstablishment);
		//lvNearEstablishment.setOnItemClickListener(SelectCheckList);
		lvNearEstablishment.setOnItemClickListener(
				new Implements.SelectEstablismentCheckList(lvNearEstablishment));
		
		etInterviewee = (EditText)findViewById(R.id.d_etInterviewedName);
		etInterviewee.setFilters(new InputFilter[]{	new InputFilter.AllCaps(), 
				new InputFilter.LengthFilter(25) });
		etInterviewee.addTextChangedListener(IntervieweeWatch);
		
		spnInterviewedPerson = (Spinner)findViewById(R.id.d_ddlInterviewedPerson);
		spnInterviewedPerson.setOnItemSelectedListener(IntervieweeSelectedItem);
		etUnitNo = (EditText)findViewById(R.id.d_etNo);
		etUnitNo.setFilters(new InputFilter[]{	new InputFilter.AllCaps(), 
				new InputFilter.LengthFilter(20)});
		
		etStreet = (EditText)findViewById(R.id.d_etStreet);
		etStreet.setFilters(new InputFilter[]{	new InputFilter.AllCaps(),
				new InputFilter.LengthFilter(25)});
		
		spnProvinces = (Spinner)findViewById(R.id.spnProvince);
		spnProvinces.setOnItemSelectedListener(ProvinceSelectedItem);
		spnCities = (Spinner)findViewById(R.id.d_ddlCity);
		spnCities.setOnItemSelectedListener(CitySelectedItem);
		spnBarangay = (Spinner)findViewById(R.id.d_ddlBarangay);
		
		spnGlobeSellingLoad = (Spinner)findViewById(R.id.spnSellingGlobeLoad);
		spnGlobeSellingLoad.setOnItemSelectedListener(GlobeLoadSelectedItem);
		spnSmartSellingLoad = (Spinner)findViewById(R.id.spnSellingSmartLoad);
		spnSmartSellingLoad.setOnItemSelectedListener(SmartLoadSelectedItem);
		spnSunSellingLoad = (Spinner)findViewById(R.id.spnSellingSunLoad);
		spnSunSellingLoad.setOnItemSelectedListener(SunLoadSelectedItem);
		
		etGlobeLoad = (EditText)findViewById(R.id.etGlobeLoad);
		etGlobeLoad.addTextChangedListener(new NumberWithCommaTextWatcher(etGlobeLoad));
		
		etSmartLoad = (EditText)findViewById(R.id.etSmartLoad);
		etSmartLoad.addTextChangedListener(new NumberWithCommaTextWatcher(etSmartLoad));
		 
		etSunLoad = (EditText)findViewById(R.id.etSunLoad);
		etSunLoad.addTextChangedListener(new NumberWithCommaTextWatcher(etSunLoad));
		
		spnGPTMABS = (Spinner)findViewById(R.id.spnFopGlobe);
		spnSMARTtnt = (Spinner)findViewById(R.id.spnSMARTtnt);
		spnSun = (Spinner)findViewById(R.id.spnSun);
		
		spnSellingSim = (Spinner)findViewById(R.id.spnSellingSim);
		spnSellingSim.setOnItemSelectedListener(SellingSimSelectedItem);
		llVisbleSim = (LinearLayout)findViewById(R.id.llVisbleSims);
		llSimSales = (LinearLayout)findViewById(R.id.llSimSales);
		//
		etGPStockSim = (EditText)findViewById(R.id.d_etStockSim_GP);
		etGPStockSim.addTextChangedListener(new NumberWithCommaTextWatcher(etGPStockSim));
		
		etTMStockSim = (EditText)findViewById(R.id.d_etStockSim_TM);
		etTMStockSim.addTextChangedListener(new NumberWithCommaTextWatcher(etTMStockSim));
		
		etABSStockSim = (EditText)findViewById(R.id.etStockSim_Abscbn);
		etABSStockSim.addTextChangedListener(new NumberWithCommaTextWatcher(etABSStockSim));
		
		etSmartStockSim = (EditText)findViewById(R.id.d_etStockSim_Sm);
		etSmartStockSim.addTextChangedListener(new NumberWithCommaTextWatcher(etSmartStockSim));
		
		etTntStockSim = (EditText)findViewById(R.id.d_etStockSim_TnT);
		etTntStockSim.addTextChangedListener(new NumberWithCommaTextWatcher(etTntStockSim));
		
		etSunStockSim = (EditText)findViewById(R.id.d_etStockSim_Sun);
		etSunStockSim.addTextChangedListener(new NumberWithCommaTextWatcher(etSunStockSim));
		//  
		etGPSimSales = (EditText)findViewById(R.id.etSimSalesGP_Sim);
		etGPSimSales.addTextChangedListener(new NumberWithCommaTextWatcher(etGPSimSales));
		
		etTMSimSales = (EditText)findViewById(R.id.etSimSalesTM_Sim);
		etTMSimSales.addTextChangedListener(new NumberWithCommaTextWatcher(etTMSimSales));
		
		etABSSimSales = (EditText)findViewById(R.id.etSimSalesAbscbn);
		etABSSimSales.addTextChangedListener(new NumberWithCommaTextWatcher(etABSSimSales));
		
		etSmartSimSales = (EditText)findViewById(R.id.etSimSalesSm_Sim);
		etSmartSimSales.addTextChangedListener(new NumberWithCommaTextWatcher(etSmartSimSales));
		
		etTNTSimSales = (EditText)findViewById(R.id.etSimSalesTnT_Sim);
		etTNTSimSales.addTextChangedListener(new NumberWithCommaTextWatcher(etTNTSimSales));
		
		etSUNSimSales = (EditText)findViewById(R.id.etSimSalesSun_Sim);
		etSUNSimSales.addTextChangedListener(new NumberWithCommaTextWatcher(etSUNSimSales));
		
		etTelMobtel = (EditText)findViewById(R.id.etTelOrMobtel);
		etTelMobtel.setOnFocusChangeListener(TelMobtelOnFocusChange);
		etOwner = (EditText)findViewById(R.id.etOwnerName);
		etOwner.setFilters(new InputFilter[]{ new InputFilter.AllCaps(),
				new InputFilter.LengthFilter(25) });
		// -->
		spnLoadCategory = (Spinner)findViewById(R.id.spnLoadCateg);
		spnLoadCategory.setOnItemSelectedListener(onLoadCategoryListener);
		
		llLinearGlobe = (LinearLayout)findViewById(R.id.llLinearGlobe);
		llLinearGlobe.setVisibility(View.GONE);
		llLinearOneSim = (LinearLayout)findViewById(R.id.llLinear1Sim);
		llLinearOneSim.setVisibility(View.GONE);
		
		rgGlobeSelections = (RadioGroup)findViewById(R.id.rgGlobeSelections);
		rgGlobeSelections.setOnCheckedChangeListener(GlobeGroupCheckListener);
		rgOneSimSelections = (RadioGroup)findViewById(R.id.rg1SimSelections);
		rgOneSimSelections.setOnCheckedChangeListener(OneSimGroupCheckListener);
		
		cbOneSimRefuse = (CheckBox)findViewById(R.id.cbOneSimRefuse);
		cbOneSimRefuse.setOnCheckedChangeListener(onCheckOneSim);
		etOneSimTraceNo = (EditText)findViewById(R.id.etOneSimTraceNo);
		etOneSimTraceNo.addTextChangedListener(TraceNumberAddWatcher);
		
		etGlobeRetailerNum = (EditText)findViewById(R.id.etGlobeRetailerNum);
		etOneSimRetailerNum = (EditText)findViewById(R.id.etOneSimRetailerNum);
		
		// set disable first the non globe on 1 Sim for all radio button
		rgOneSimSelections.getChildAt(7).setEnabled(false);
		rgGlobeSelections.getChildAt(4).setVisibility(View.GONE);
		rgOneSimSelections.getChildAt(4).setVisibility(View.GONE);
		rgOneSimSelections.getChildAt(5).setVisibility(View.GONE);
		rgOneSimSelections.getChildAt(6).setVisibility(View.GONE);
		// -- <
		pbSendProgress = (ProgressBar)findViewById(R.id.pbSendProgress);
		pbSendProgress.setVisibility(View.GONE);
		
		ivImageIcon = (ImageView)findViewById(R.id.ivImageIcon);
		
		btnSend = (Button)findViewById(R.id.btnSend_GT);
		btnCancel = (Button)findViewById(R.id.btnCancel_GT);
		
		tblTakePhoto = (TableLayout)findViewById(R.id.tblTakePhoto);
	}
	// =========================================================================
	protected void BroadcastProcess(Intent intent, int resultCode){
		//
		int index = 1;
		int pagePos = 11;
		int pageNeg = 10;
		int pageSize = TextMessagePart.size();
		//
		for (String msg : TextMessagePart){
			//
			if (intent.getExtras().getInt("id"+index) == index){
				//
				ContentValues cv = new ContentValues();
				cv.put("address", mainApp.getDatabase().getIotaNum());
				cv.put("body", msg);
				//
				switch(resultCode){
				//
				case Activity.RESULT_OK:
					//
					BroadcastProcessUtils.doResultOK(this, cv, index, pageSize);
					//
					txtObserver.setValue(pagePos);
					txtObserver.Commit();
					pbSendProgress.setVisibility(View.GONE);
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					//
					BroadcastProcessUtils.doErrorGenericFailure(this, cv, index, pageSize);
					//
					txtObserver.setValue(pageNeg);
					txtObserver.Commit();
					pbSendProgress.setVisibility(View.GONE);
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					//
					BroadcastProcessUtils.doErrorNoService(this, cv, index, pageSize);
					//
					txtObserver.setValue(pageNeg);
					txtObserver.Commit();
					pbSendProgress.setVisibility(View.GONE);
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					//
					BroadcastProcessUtils.doErrorRadioOff(this, cv, index, pageSize);
					//
					txtObserver.setValue(pageNeg);
					txtObserver.Commit();
					pbSendProgress.setVisibility(View.GONE);
					break;
				}
			}
			//
			index++;
			pagePos +=10;
			pageNeg +=10;
		}
		//
	}
	// =========================================================================
	public void sendMessage(String sNum) {
		//
		String sSubtypeCode = Utilities.getSelectedAdapter(mainApp.getDatabase().getCustSubTypes(), spnStoreType);
		// -----=-----=-----=----- ><
		String sStoreLocCode = Utilities.getSelectedAdapter(mainApp.getDatabase().getStoreLocationList(), spnStoreLocation);
		// -----=-----=-----=----- ><
		int sumList = Utilities.getEstablismentChecks(lvNearEstablishment, mainApp);
		// -----=-----=-----=----- ><
		String sInterviewedType = Utilities.getSelectedAdapter(mainApp.getDatabase().getInterview(), spnInterviewedPerson);
		// -----=-----=-----=----- ><
		String sGlobeSellingLoad = Utilities.getSelSellingLoadEnums(spnGlobeSellingLoad.getSelectedItem().toString());
		String sGlobeLoad = removeCommaStr(etGlobeLoad);
		String sCodeGptmabs = Utilities.getSelHowOftenEnums(spnGPTMABS.getSelectedItem().toString());
		// -----=-----=-----=----- ><
		String sSmartSellingLoad = Utilities.getSelSellingLoadEnums(spnSmartSellingLoad.getSelectedItem().toString());
		String sSmartLoad = removeCommaStr(etSmartLoad);
		String sCodeSmart = Utilities.getSelHowOftenEnums(spnSMARTtnt.getSelectedItem().toString());
		// -----=-----=-----=----- ><
		String sSunSellingLoad = Utilities.getSelSellingLoadEnums(spnSunSellingLoad.getSelectedItem().toString());
		String sSunLoad = removeCommaStr(etSunLoad);
		String sCodeSun = Utilities.getSelHowOftenEnums(spnSun.getSelectedItem().toString());
		
		String sCodeSellSim = Utilities.getSelSellingSimEnums(spnSellingSim.getSelectedItem().toString());
		// -----=-----=-----=----- ><
		String sGPStockSim = removeCommaStr(etGPStockSim);
		String sTMStockSim = removeCommaStr(etTMStockSim);
		String sABSStockSim = removeCommaStr(etABSStockSim);
		String sSmartStockSim = removeCommaStr(etSmartStockSim);
		String sTntStockSim = removeCommaStr(etTntStockSim);
		String sSunStockSim = removeCommaStr(etSunStockSim);
		// -----=-----=-----=----- ><
		String sGPSSimSales = removeCommaStr(etGPSimSales);
		String sTMSimSales = removeCommaStr(etTMSimSales);
		String sABSSimSales = removeCommaStr(etABSSimSales);
		String sSmartSimSales = removeCommaStr(etSmartSimSales);
		String sTntSimSales = removeCommaStr(etTNTSimSales);
		String sSunSimSales = removeCommaStr(etSUNSimSales);
		// -----=-----=-----=----- ><
		String sTelMobtel = etTelMobtel.getText().toString();
		String sOwnername = etOwner.getText().toString();
		// -----=-----=-----=----- >< 
		String date = "";
		if(MainApp.PHOTOSYNC_MODE == 1){
			date = Utils.trimDateFromImage(mCurrentPhotoPath);
		}
		// ---------------------------------------------------------------------
		MAINMESSAGE = mainApp.getGPSLong() +"/"+ 
		mainApp.getGPSLat() +"/"+
		etNameOfStore.getText() +"/";
		//
		if(spnStoreType.getSelectedItem().toString().equals("Others")){
			MAINMESSAGE += sSubtypeCode+","+etStoreTypeOthers.getText().toString();
		}else {
			MAINMESSAGE += sSubtypeCode+",0";
		}
		//
		MAINMESSAGE += "/"+sStoreLocCode +"/"+
		sumList +"/"+ 
		etInterviewee.getText() +"/"+ 
		sInterviewedType +"/"+
		etUnitNo.getText() +","+
		etStreet.getText() +","+
		((CodeDescPair)spnProvinces.getSelectedItem()).Code +","+
		((CodeDescPair)spnCities.getSelectedItem()).Code +","+
		((CodeDescPair)spnBarangay.getSelectedItem()).Code;
		
		// -----=-----=-----=----- ><
		if(spnGlobeSellingLoad.getSelectedItem().toString().equals("Yes")){
			MAINMESSAGE += "/"+ sGlobeSellingLoad +","+
					sGlobeLoad +","+
					sCodeGptmabs;
		} else {
			MAINMESSAGE += "/"+ sGlobeSellingLoad +","+
					"0" +","+
					"0";
		}
		// -----=-----=-----=----- ><
		if(spnSmartSellingLoad.getSelectedItem().toString().equals("Yes")){
			MAINMESSAGE += "/"+ sSmartSellingLoad +","+
					sSmartLoad +","+
					sCodeSmart;
		} else {
			MAINMESSAGE += "/"+ sSmartSellingLoad +","+
					"0" +","+
					"0";
		}
		// -----=-----=-----=----- ><
		if(spnSunSellingLoad.getSelectedItem().toString().equals("Yes")){
			MAINMESSAGE += "/"+ sSunSellingLoad +","+
					sSunLoad +","+
					sCodeSun;
		} else {
			MAINMESSAGE += "/"+ sSunSellingLoad +","+
					"0" +","+
					"0";
		}
		// -----=-----=-----=----- ><
		if(!spnSellingSim.getSelectedItem().toString().equals("Not selling")){
			
			//if(spnGlobeSellingLoad.getSelectedItem().toString().equals("Yes")) old codes
			if(MAPCODE.equals("NDSOMAP4") || MAPCODE.equals("NSMAP4")){
				
				MAINMESSAGE += "/"+ sCodeSellSim +"/"+ sGPStockSim +","+ sTMStockSim +","+
						sABSStockSim +","+ sSmartStockSim +","+	sTntStockSim +","+ sSunStockSim +"/"+
						sGPSSimSales +","+ sTMSimSales +","+ sABSSimSales +","+	sSmartSimSales +","+
						sTntSimSales +","+
						sSunSimSales;
			} else{ // 
				MAINMESSAGE += "/"+ sCodeSellSim +"/0,0,0,0,0,0/0,0,0,0,0,0";
			}
		} else{
			MAINMESSAGE += "/"+ sCodeSellSim +"/0,0,0,0,0,0/0,0,0,0,0,0";
		}
		// -----=-----=-----=----- ><
		// new revision 2 GlobeRet vs 1 sim for all
		if(spnGlobeSellingLoad.getSelectedItem().toString().equals("Yes")){
			
			if(spnLoadCategory.getSelectedItemPosition() == 0){
				// for Globe Retailer Coding
				CheckBox cbRef = (CheckBox)rgGlobeSelections.getChildAt(4);
				String globeRefuse = "0";
				if(cbRef.isChecked()){
					globeRefuse = "2";
				} else{
					globeRefuse = "1";
				}
				//
				RadioButton rdoRet = (RadioButton)rgGlobeSelections.getChildAt(0);
				if(rdoRet.isChecked()){
					String globeRetNum = etGlobeRetailerNum.getText().toString();
					//MAINMESSAGE += "/" + "1,1,"+globeRetNum+","+globeRefuse+",0";
					MAINMESSAGE += "/" + "1,1,"+globeRetNum+","+"0"+",0";
				}
				//
				RadioButton rdoRef = (RadioButton)rgGlobeSelections.getChildAt(3);
				if(rdoRef.isChecked()){
					
					MAINMESSAGE += "/" + "1,2,0,"+globeRefuse+",0";
				}
			} else {
				// for 1 sim for all coding
				CheckBox cbRef2 = (CheckBox)rgOneSimSelections.getChildAt(4);
				String OneSimRefuse = "0";
				if(cbRef2.isChecked()){
					OneSimRefuse = "2";
				}else{
					OneSimRefuse = "1";
				}
				//
				RadioButton rdoRet = (RadioButton) rgOneSimSelections.getChildAt(0);
				if (rdoRet.isChecked()) {
					String OneSimNum = etOneSimRetailerNum.getText().toString();
					String OneSimTrace = etOneSimTraceNo.length() != 0 ? etOneSimTraceNo.getText().toString() : "0";
					MAINMESSAGE += "/" + "2,1," + OneSimNum + ","+"0"+","+ OneSimTrace;
				}
				//
				RadioButton rdoRef = (RadioButton)rgOneSimSelections.getChildAt(3);
				if(rdoRef.isChecked()){
					
					String OneSimTrace = etOneSimTraceNo.length() != 0 ? etOneSimTraceNo.getText().toString() : "0";
					MAINMESSAGE += "/" + "2,2,0,"+OneSimRefuse+","+ OneSimTrace;
				}
			}
		}else {
			
			MAINMESSAGE += "/" + "0,0,0,0,0";
		}
		// -----=-----=-----=----- ><
		MAINMESSAGE += "/" + sTelMobtel +"/"+
				sOwnername;
		
		if(MainApp.PHOTOSYNC_MODE == 1){
			MAINMESSAGE += "/"+ date;  
		} else {
			MAINMESSAGE += "/"+ " *ImgFile";
		}
		// -----=-----=-----=----- ><
		TextMessagePart = SendMessageUtils.divideMainMessage(this, MAPCODE, MAINMESSAGE);
		isAllPartSend = new boolean[TextMessagePart.size()];
		// -----=-----=-----=----- ><
		MainApp.setAndIncrementTextID(this);
		// -----=-----=-----=----- ><
		
		SendMessageUtils.sendTextPart
		(this, currentTextPart, sNum, TextMessagePart.get(currentTextPart-1), pbSendProgress);
		//
		// benkurama added codes on 10-7-2014 -->
		MainApp.setCitySpnPos(this, spnCities.getSelectedItemPosition());
		// -- <
		//
//		if(MainApp.DEBUG == 0){
//			
//		} else {
//			
//			String allPart = "";
//			for(String page : TextMessagePart){
//				
//				allPart += page + "\n\n";
//			}
//			//
//			etOutput.setText(MAINMESSAGE);
//			//
//			if(MainApp.PHOTOSYNC_MODE == 1){
//				renameFilename();
//			}
//		}		
		
		if(isDraft){
			
			int id = getIntent().getIntExtra("DraftID", 0);
			mainApp.getDatabase().deleteSaveDraft(id);
		}
	}
	// ============================================ =============================
	private void dispatchTakePictureIntent() {
		Utils.iLogCat(this.getClass().getSimpleName() +": dispatchTakePictureIntent", "MainFunction - Begin");
		
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	    	
	    	// Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = Utils.createImageFile(this, MAPCODE,etNameOfStore.getText().toString());
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	           
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	        	
	        	mCurrentPhotoPath = photoFile.getAbsolutePath();
	        	
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	    
	    Utils.iLogCat(this.getClass().getSimpleName() +": dispatchTakePictureIntent", "MainFunction - End");
	}
	// ==================================No=======================================
	@SuppressLint("SimpleDateFormat")
	private void saveToDraft(){
		
		String sStorename = Utilities.detectNull(etNameOfStore.getText().toString());
		// -----=-----=-----=----- ><
		String sSubtypeCode = Utilities.getSelectedAdapter(mainApp.getDatabase().getCustSubTypes(), spnStoreType);
		// -----=-----=-----=----- ><
		String sStoreLocCode = Utilities.getSelectedAdapter(mainApp.getDatabase().getStoreLocationList(), spnStoreLocation);
		// -----=-----=-----=----- ><
		SparseBooleanArray checked = lvNearEstablishment.getCheckedItemPositions();
		ArrayList<CodeDescPair> alEstablishments = mainApp.getDatabase().getEstablisments();
		int sumList = 0;
		for (int i = 0; i < lvNearEstablishment.getCount(); i++) {
			if (checked.get(i)) {
				sumList += Integer.parseInt(alEstablishments.get(i).Code);
			}
		}
		// -----=-----=-----=----- ><
		String sInterviewee = Utilities.detectNull(etInterviewee.getText().toString());
		// -----=-----=-----=----- ><
		String sInterviewedType = Utilities.getSelectedAdapter(mainApp.getDatabase().getInterview(), spnInterviewedPerson);
		// -----=-----=-----=----- ><
		String sUnit = Utilities.detectNull(etUnitNo.getText().toString());
		// -----=-----=-----=----- ><
		String sStreet = Utilities.detectNull(etStreet.getText().toString());
		// -----=-----=-----=----- ><
		String sGlobeLoad = Utilities.getSelSellingLoadEnums(spnGlobeSellingLoad.getSelectedItem().toString());
		String sGlobeloadAmt = Utilities.detectNull(etGlobeLoad.getText().toString());
		String sGlobeFrq = Utilities.getSelHowOftenEnums(spnGPTMABS.getSelectedItem().toString());
		// -----=-----=-----=----- ><
		String sSmartLoad = Utilities.getSelSellingLoadEnums(spnSmartSellingLoad.getSelectedItem().toString());
		String sSmartloadAmt = Utilities.detectNull(etSmartLoad.getText().toString());
		String sSmartFrq = Utilities.getSelHowOftenEnums(spnSMARTtnt.getSelectedItem().toString());
		// -----=-----=-----=----- ><
		String sSunLoad = Utilities.getSelSellingLoadEnums(spnSunSellingLoad.getSelectedItem().toString());
		String sSunloadAmt = Utilities.detectNull(etSunLoad.getText().toString());
		String sSunFrq = Utilities.getSelHowOftenEnums(spnSun.getSelectedItem().toString());
		// -----=-----=-----=----- ><
		String sSellingSim = Utilities.getSelSellingSimEnums(spnSellingSim.getSelectedItem().toString());
		// -----=-----=-----=----- ><
		String sStockGP = Utilities.detectNull(etGPStockSim.getText().toString());
		String sStockTM = Utilities.detectNull(etTMStockSim.getText().toString());
		String sStockABS = Utilities.detectNull(etABSStockSim.getText().toString());
		String sStockSmart = Utilities.detectNull(etSmartStockSim.getText().toString());
		String sStockTnt = Utilities.detectNull(etTntStockSim.getText().toString());
		String sStockSun = Utilities.detectNull(etSunStockSim.getText().toString());
		// -----=-----=-----=----- ><
		String sSalesGP = Utilities.detectNull(etGPSimSales.getText().toString());
		String sSalesTM = Utilities.detectNull(etTMSimSales.getText().toString());
		String sSalesABS = Utilities.detectNull(etABSSimSales.getText().toString());
		String sSalesSmart = Utilities.detectNull(etSmartSimSales.getText().toString());
		String sSalesTnt = Utilities.detectNull(etTNTSimSales.getText().toString());
		String sSalesSun = Utilities.detectNull(etSUNSimSales.getText().toString());
		// -----=-----=-----=----- ><
		String sGlobeRetNum = Utilities.detectNull(etGlobeRetailerNo.getText().toString());
		// -----=-----=-----=----- ><
		String sGlobeRet = rbGlobeRetailer.isChecked() ? "true" : "false" ;
		String sGlobeRef = rbRetailerRefuse.isChecked() ? "true" : "false" ;
		String sNonGlobe = rbNonGlobe.isChecked() ? "true" : "false";
		String sRefuseTestBuy = cbRefusedTestBuy.isChecked() ? "true" : "false";
		// -----=-----=-----=----- ><
		String sTelOrMobtel = Utilities.detectNull(etTelMobtel.getText().toString());
		String sOwner = Utilities.detectNull(etOwner.getText().toString());
		// -----=-----=-----=----- ><
		// ---------------------------------------------------------------------
		SAVEDRAFTMESSAGE = MAPCODE +"/"+
						sStorename +"/"+
						sSubtypeCode +"/"+
						sStoreLocCode +"/"+
						sumList  +"/"+
						sInterviewee  +"/"+
						sInterviewedType  +"/"+
						sUnit  +"/"+
						sStreet  +"/"+
						((CodeDescPair)spnProvinces.getSelectedItem()).Code +","+
						((CodeDescPair)spnCities.getSelectedItem()).Code +","+
						((CodeDescPair)spnBarangay.getSelectedItem()).Code +"/"+
						sGlobeLoad +","+
						sGlobeloadAmt +","+
						sGlobeFrq +"/"+
						sSmartLoad +","+
						sSmartloadAmt +","+
						sSmartFrq +"/"+
						sSunLoad +","+
						sSunloadAmt +","+
						sSunFrq +"/"+
						
						sSellingSim +"/"+
						
						sStockGP +","+
						sStockTM +","+
						sStockABS +","+
						sStockSmart +","+
						sStockTnt +","+
						sStockSun +"/"+
						
						sSalesGP +","+
						sSalesTM +","+
						sSalesABS +","+
						sSalesSmart +","+
						sSalesTnt +","+
						sSalesSun +"/"+
						
						sGlobeRetNum +"/"+
						sGlobeRet +","+
						sGlobeRef +","+
						sNonGlobe +","+
						sRefuseTestBuy
						;
		// -----=-----=-----=----- ><
		SAVEDRAFTMESSAGE += "/" + sTelOrMobtel +"/"+
		sOwner;		
		// -----=-----=-----=----- ><
		String timeStamp = new SimpleDateFormat("MM.dd.yyyy-HH.mm.ss.SSS").format(new Date());
		// -----=-----=-----=----- ><
		String imagePath = mCurrentPhotoPath == null ? "null" : mCurrentPhotoPath;
		// -----=-----=-----=----- ><
		if(isDraft){
			int id = getIntent().getIntExtra("DraftID", 0);
			mainApp.getDatabase().updateSaveDraft(SAVEDRAFTMESSAGE, timeStamp, imagePath, "Update Draft", id);
		}else{
			mainApp.getDatabase().insertSaveDraft(SAVEDRAFTMESSAGE, timeStamp, imagePath, "New Draft");
		}
		// -----=-----=-----=----- ><
		this.finish();
		
	}
	// =========================================================================
	@SuppressLint("HandlerLeak")
	private void populateDraft(int id){
	
		DraftList = mainApp.getDatabase().getQueryDraft(id);
		
		String[] draftItems = DraftList.get(0).DraftContent.split("/");
		
		MAPCODE = draftItems[0];
		// -----=-----=-----=----- ><
		etNameOfStore.setText(Utilities.convertNullToBlank(draftItems[1]));
		// -----=-----=-----=----- ><
		spnStoreType.setSelection(Utilities.getPositionByCode( mainApp.getDatabase().getCustSubTypes(), spnStoreType, draftItems[2] ) );
		// -----=-----=-----=----- ><
		spnStoreLocation.setSelection( Utilities.getPositionByCode( mainApp.getDatabase().getStoreLocationList(), spnStoreLocation, draftItems[3] ) );
		// -----=-----=-----=----- ><
		setChecklistFromDraft(draftItems[4]);
		// -----=-----=-----=----- ><
		etInterviewee.setText(Utilities.convertNullToBlank(draftItems[5]));
		// -----=-----=-----=----- ><
		spnInterviewedPerson.setSelection( Utilities.getPositionByCode( mainApp.getDatabase().getInterview(), spnInterviewedPerson, draftItems[6] ) );
		// -----=-----=-----=----- ><
		etUnitNo.setText(Utilities.convertNullToBlank(draftItems[7]));
		// -----=-----=-----=----- ><
		etStreet.setText(Utilities.convertNullToBlank(draftItems[8]));
		// -----=-----=-----=----- ><
		draftCities = draftItems[9].split(",");
		// -----=-----=-----=----- ><
		// -----=-----=-----=----- ><
		String[] draftSellingGlobe = draftItems[10].split(",");
		spnGlobeSellingLoad.setSelection(Utilities.getPositionSellingloadEnums(draftSellingGlobe[0], spnGlobeSellingLoad));
		etGlobeLoad.setText(Utilities.convertNullToBlank(draftSellingGlobe[1]));
		spnGPTMABS.setSelection(Utilities.getPositionHowOftenEnums(draftSellingGlobe[2], spnGPTMABS));
		// -----=-----=-----=----- >< 
		String[] draftSellingSmart = draftItems[11].split(",");
		spnSmartSellingLoad.setSelection(Utilities.getPositionSellingloadEnums(draftSellingSmart[0], spnSmartSellingLoad));
		etSmartLoad.setText(Utilities.convertNullToBlank(draftSellingSmart[1]));
		spnSMARTtnt.setSelection(Utilities.getPositionHowOftenEnums(draftSellingSmart[2], spnSMARTtnt));
		// -----=-----=-----=----- ><
		String[] draftSellingSun = draftItems[12].split(",");
		spnSunSellingLoad.setSelection(Utilities.getPositionSellingloadEnums(draftSellingSun[0], spnSunSellingLoad));
		etSunLoad.setText(Utilities.convertNullToBlank(draftSellingSun[1]));
		spnSun.setSelection(Utilities.getPositionHowOftenEnums(draftSellingSun[2], spnSun));
		// -----=-----=-----=----- ><
		spnSellingSim.setSelection(Utilities.getPositionSellingSimEnums(draftItems[13], spnSellingSim));
		// -----=-----=-----=----- ><
		String[] draftStockSim = draftItems[14].split(",");
		etGPStockSim.setText(Utilities.convertNullToBlank(draftStockSim[0]));
		etTMStockSim.setText(Utilities.convertNullToBlank(draftStockSim[1]));
		etABSStockSim.setText(Utilities.convertNullToBlank(draftStockSim[2]));
		etSmartStockSim.setText(Utilities.convertNullToBlank(draftStockSim[3]));
		etTntStockSim.setText(Utilities.convertNullToBlank(draftStockSim[4]));
		etSunStockSim.setText(Utilities.convertNullToBlank(draftStockSim[5]));
		// -----=-----=-----=----- ><
		String[] draftSalesSim = draftItems[15].split(",");
		etGPSimSales.setText(Utilities.convertNullToBlank(draftSalesSim[0]));
		etTMSimSales.setText(Utilities.convertNullToBlank(draftSalesSim[1]));
		etABSSimSales.setText(Utilities.convertNullToBlank(draftSalesSim[2]));
		etSmartSimSales.setText(Utilities.convertNullToBlank(draftSalesSim[3]));
		etTNTSimSales.setText(Utilities.convertNullToBlank(draftSalesSim[4]));
		etSUNSimSales.setText(Utilities.convertNullToBlank(draftSalesSim[5]));
		// -----=-----=-----=----- ><
		etGlobeRetailerNo.setText(Utilities.convertNullToBlank(draftItems[16]));
		draftOptions = draftItems[17].split(",");
		
		rbGlobeRetailer.setChecked(draftOptions[0].equals("true") ? true : false);
		rbRetailerRefuse.setChecked(draftOptions[1].equals("true") ? true : false);
		rbNonGlobe.setChecked(draftOptions[2].equals("true") ? true : false);
		cbRefusedTestBuy.setChecked(draftOptions[3].equals("true") ? true : false);
		// -----=-----=-----=----- ><
		etTelMobtel.setText(Utilities.convertNullToBlank(draftItems[18]));
		OwnerName = draftItems[19];
		etOwner.setText(Utilities.convertNullToBlank(OwnerName));
		// -----=-----=-----=----- ><
		String pathImage = DraftList.get(0).ImagePath;
		
		if(!pathImage.equals("null")){ // for retrieving the image store
			
			Bitmap bmp = BitmapFactory.decodeFile(pathImage);
			
			ivImageIcon.setImageBitmap(bmp);
			
			mCurrentPhotoPath = pathImage;
			mOldPhotoPath = pathImage;
		}
		
	}
// =========================================================================
// TODO Implementation  
// =========================================================================
	private BroadcastReceiver brSMS = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//
			BroadcastProcess(intent, this.getResultCode());
		}
	};
	// =========================================================================
	private OnItemSelectedListener ProvinceSelectedItem = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			
			CodeDescPair cdpTemp = (CodeDescPair)arg0.getSelectedItem();
			//fillCities(cdpTemp.Code);
			PopulateData.fillCities(getBaseContext(), mainApp, cdpTemp.Code, spnCities);
			
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	// ========================== ===============================================
	private OnItemSelectedListener CitySelectedItem = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			//
			CodeDescPair cdpTemp = (CodeDescPair)arg0.getSelectedItem();
			PopulateData.fillBarangay(getBaseContext(), cdpTemp.Code, mainApp, spnBarangay);
			//
			//region old code for draft mode
//			if(isDraft){// if draft is currently selected 
//				
//				if(twiceOnlyCitiesDraft == 1){ // once execute the code only
//					
//					int pos = getPosCitiesDraft();
//		    		//
//		        	spnCities.setSelection(pos);
//		        	fillBarangay(draftCities[1]);
//		        	//
//		        	twiceOnlyCitiesDraft += 1;
//				} else if (twiceOnlyCitiesDraft == 2){
//					//
//					twiceOnlyCitiesDraft += 1;
//					if(draftCities[1].equals("0")){
//						fillBarangay(cdpTemp.Code);	
//					}
//					
//				} else {
//					fillBarangay(cdpTemp.Code);
//				}
//	    	} else {
//	    		fillBarangay(cdpTemp.Code);
//	    	}
			//endregion
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	// =========================================================================
	private OnItemSelectedListener GlobeLoadSelectedItem = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> v, View arg1, int pos, long arg3) {
			
			if (pos == 0){ // if yes
				// --<
				etGlobeLoad.setEnabled(true);
				spnGPTMABS.setEnabled(true);
				//
				setMappingCode();
				
			}else if (pos == 1){ // if no
				
				etGlobeLoad.setEnabled(false);
				etGlobeLoad.setText("");
				spnGPTMABS.setSelection(0);
				spnGPTMABS.setEnabled(false);
				//
				setMappingCode();
			}
			
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}; 
	// =========================================================================
	private OnItemSelectedListener SmartLoadSelectedItem = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,long arg3) {
			Utils.iLogCat("OnItemSelectedListener" +": onItemSelected", "SmartLoadSelectedItem - Begin");
			if(pos == 0){ // if yes
				
				etSmartLoad.setEnabled(true);
				spnSMARTtnt.setEnabled(true);
				
				setMappingCode();
			}else{ // if no
				etSmartLoad.setEnabled(false);
				etSmartLoad.setText("");
				
				spnSMARTtnt.setSelection(0);
				spnSMARTtnt.setEnabled(false);
				
				setMappingCode();
			}
			Utils.iLogCat("OnItemSelectedListener" +": onItemSelected", "SmartLoadSelectedItem - End");
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	// =========================================================================
	private OnItemSelectedListener SunLoadSelectedItem = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
			Utils.iLogCat("OnItemSelectedListener" +": onItemSelected", "SunLoadSelectedItem - Begin");
			if(pos == 0){//if yes
				etSunLoad.setEnabled(true);
				spnSun.setEnabled(true);
				
				setMappingCode();
			}else{// if no
				etSunLoad.setEnabled(false);
				etSunLoad.setText("");
				
				spnSun.setSelection(0);
				spnSun.setEnabled(false);
				
				setMappingCode();
			}
			Utils.iLogCat("OnItemSelectedListener" +": onItemSelected", "SunLoadSelectedItem - End");
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	// =========================================================================
	private OnItemSelectedListener SellingSimSelectedItem = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
			
			if(pos == 3){
				
				for (int i = 0; i < llVisbleSim.getChildCount(); i++) { /** HOTSPOT BEGIN*/
				    View child = llVisbleSim.getChildAt(i);
				    //child.setVisibility(View.GONE);
				    if(child instanceof EditText){
				    	EditText et = (EditText)child;
				    	et.setText("");
				    }
				    child.setEnabled(false);
				} /** HOTSPOT END*/
				
				for (int i = 0; i < llSimSales.getChildCount(); i++) {
				    View child = llSimSales.getChildAt(i);
				    //child.setVisibility(View.GONE);
				    if(child instanceof EditText){
				    	EditText et = (EditText)child;
				    	et.setText("");
				    }
				    child.setEnabled(false);
				}
			} else {
				
				for (int i = 0; i < llVisbleSim.getChildCount(); i++) {
				    View child = llVisbleSim.getChildAt(i);
				    //child.setVisibility(View.VISIBLE);
				    child.setEnabled(true);
				}
				
				for (int i = 0; i < llSimSales.getChildCount(); i++) {
				    View child = llSimSales.getChildAt(i);
				    //child.setVisibility(View.VISIBLE);
				    child.setEnabled(true);
				}
			}
		
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	// =========================================================================
	private OnItemSelectedListener IntervieweeSelectedItem = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
			
			if(pos == 0){
				etOwner.setText(etInterviewee.getText().toString());
				
				if(isDraft){
					OwnerNameCount += 1;
				}
				
			} else {
				etOwner.setText("");
				if(isDraft){
					
					if (OwnerNameCount < 2) {
						etOwner.setText(Utilities.convertNullToBlank(OwnerName));
						OwnerNameCount += 1;
					}else {
						etOwner.setText("");
					}
					
				}
			}
		
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	// ===========================]==============================================
	private TextWatcher IntervieweeWatch = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable s) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			//
			if(spnInterviewedPerson.getSelectedItem().toString().equals("Owner")){
				etOwner.setText(etInterviewee.getText().toString());
			}
		}
	};
	// =========================================================================
	private OnFocusChangeListener TelMobtelOnFocusChange = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			//
			if(!hasFocus){
				validateTelMobtel();
			}
		}
	};
	// ========================================================================= 
	private OnItemSelectedListener onLoadCategoryListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int pos, long arg3) {
			//
			if(pos == 0){
				llLinearGlobe.setVisibility(view.VISIBLE);
				llLinearOneSim.setVisibility(view.GONE);
			} else if(pos == 1){
				llLinearGlobe.setVisibility(view.GONE);
				llLinearOneSim.setVisibility(view.VISIBLE);
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	// =========================================================================
	private OnCheckedChangeListener GlobeGroupCheckListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			
			switch (checkedId) {
			
			case R.id.rdoGlobeRet:
				group.getChildAt(1).setVisibility(View.VISIBLE);
				group.getChildAt(2).setVisibility(View.VISIBLE);
				group.getChildAt(4).setVisibility(View.GONE);
				break;
				
			case R.id.rdoGlobeRefuse:
				group.getChildAt(1).setVisibility(View.GONE);
				group.getChildAt(2).setVisibility(View.GONE);
				group.getChildAt(4).setVisibility(View.VISIBLE);
				break;
			}
			
		}
	}; 
	// =========================================================================
	private OnCheckedChangeListener OneSimGroupCheckListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			
			switch (checkedId) {
			
			case R.id.rdoOneSimRet:
				group.getChildAt(1).setVisibility(View.VISIBLE);
				group.getChildAt(2).setVisibility(View.VISIBLE);
				group.getChildAt(4).setVisibility(View.GONE);
				group.getChildAt(5).setVisibility(View.GONE);
				group.getChildAt(6).setVisibility(View.GONE);
				// newly added 07/24/2014 -->
				cbOneSimRefuse.setChecked(false);
				etOneSimTraceNo.setEnabled(true);
				// -- <
				break;
			case R.id.rdoOneSimRefuse:
				group.getChildAt(1).setVisibility(View.GONE);
				group.getChildAt(2).setVisibility(View.GONE);
				group.getChildAt(4).setVisibility(View.VISIBLE);
				group.getChildAt(5).setVisibility(View.VISIBLE);
				group.getChildAt(6).setVisibility(View.VISIBLE);
				break;
			}
		}
	};
	// =========================================================================
	private android.widget.CompoundButton.OnCheckedChangeListener onCheckOneSim = new
			android.widget.CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						etOneSimTraceNo.setEnabled(false);
						etOneSimTraceNo.setText("");
					} else {
						etOneSimTraceNo.setEnabled(true);
					}
				}
	};
	// =========================================================================	
	private TextWatcher TraceNumberAddWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(s.toString().length() == 0){
				cbOneSimRefuse.setEnabled(true);
			} else {
				cbOneSimRefuse.setEnabled(false);
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
		}
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	// =========================================================================
	private OnItemSelectedListener storeTypeSelected = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
//
			Object item = parent.getItemAtPosition(pos);
			
			if(item.toString().equals("Others")){
				etStoreTypeOthers.setVisibility(View.VISIBLE);
				etStoreTypeOthers.setText("");
			} else {
				etStoreTypeOthers.setVisibility(View.GONE);
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	// =========================================================================
	@Override
	public void update(Observable observable, Object data) {
		
		String val = data.toString(); 
		// determine if a text send, is true or else false
		if (currentTextPart <= TextMessagePart.size()) {
			//
			int pagePos = 11;
			int pageNeg = 10;
			int multPage = 10 * (currentTextPart - 1);
			//
			int positive = pagePos += multPage;
			int negative = pageNeg += multPage;
			//
			if (val.equals(positive + "")) {
				isAllPartSend[currentTextPart - 1] = true;
				//isAllPartSend[currentTextPart - 1] = false;
			} else if (val.equals(negative + "")) {
				isAllPartSend[currentTextPart - 1] = false;
			}
			//
		} 
		// doing resend if the text send is false
		if(isAllPartSend[currentTextPart - 1]){
			currentTextPart++;
			incTextPart = 1;
			//
			if (currentTextPart <= TextMessagePart.size()){
				//
				String iotaNum = mainApp.getDatabase().getIotaNum();
				SendMessageUtils.sendTextPart
				(this, currentTextPart, iotaNum, TextMessagePart.get(currentTextPart-1), pbSendProgress);
			} else {
				currentTextPart = 1;
				Toast.makeText(this, "Message Sent Successfully!", Toast.LENGTH_LONG).show();
				this.finish();
			}
		} else {
			// resend again the text message upto 3 times
			if (incTextPart <= 3) {
				Toast.makeText(this,
						"Resend Again, for " + incTextPart + " attemp...",
						Toast.LENGTH_SHORT).show();
				//
				String iotaNum = mainApp.getDatabase().getIotaNum();
				SendMessageUtils.sendTextPart
				(this, currentTextPart, iotaNum, TextMessagePart.get(currentTextPart-1), pbSendProgress);
				//
				incTextPart++;
			} else {
				incTextPart = 1;
				Toast.makeText(this, "Message Sent Failed, Try Again", Toast.LENGTH_LONG).show();
				//this.finish();
			} 
			
		}
	}
// =========================================================================
// TODO Sub Functions 
// =========================================================================
	private boolean validate(){
		//
		boolean bValid = true;
		String sMessage = this.getString(R.string.ValidateError);
		
			if (etNameOfStore.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoRetName);
			}
			
			if(spnStoreType.getSelectedItem().toString().equals("Others")){
				if(etStoreTypeOthers.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoStoreTypeVal);
				}
			}
			// ---------------------------------------------------------------------
			if(etInterviewee.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoInterViewedName);
			}
			if(etUnitNo.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoRetNo);
			}
			if (etStreet.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoRetStreet);
			}
			// -----=-----=-----=----- ><
			if(((CodeDescPair)spnCities.getSelectedItem()).Code.equals("0")){
				bValid = false;
				sMessage += this.getString(R.string.NoCities);
			}
			if(((CodeDescPair)spnBarangay.getSelectedItem()).Code.equals("0")){
				bValid = false;
				sMessage += this.getString(R.string.NoBarangay);
			}
			// ---------------------------------------------------------------------
			if (spnGlobeSellingLoad.getSelectedItem().toString().equals("Yes")){
				
				if(etGlobeLoad.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoGlobeLoad);
				}
				// --
				String sCodeGp = Utilities.getSelHowOftenEnums(spnGPTMABS.getSelectedItem().toString());
				if(sCodeGp.equals("0")){
					bValid = false;
					sMessage += this.getString(R.string.NoFrequencyGlobeSel);
				}
				// -- validation for Globe retailer and 1 Sim For All
				
				// new revision added for Globe retailer & 1 Sim For All
				if(spnLoadCategory.getSelectedItemPosition() == 0){
					// For Globe Retailer coding
					RadioButton rdo = (RadioButton)rgGlobeSelections.getChildAt(0);
					if(rdo.isChecked()){
						if(etGlobeRetailerNum.length() == 0){
							bValid = false;
							sMessage += this.getString(R.string.NoGlobeRetailerNo);
						}else {
							if(etGlobeRetailerNum.length() < 11 && etGlobeRetailerNum.length() > 0){
								bValid = false;
								sMessage += this.getString(R.string.MissingGlobeRetailerNo);
							}
						} 
					} else {
						//
						CheckBox cb = (CheckBox)rgGlobeSelections.getChildAt(4);
						if(!cb.isChecked()){
							bValid = false;
							sMessage += this.getString(R.string.RefuseTestBuyMandadory);
						}
					}
				} else {
					// For One Sim for all coding
					RadioButton rdo = (RadioButton)rgOneSimSelections.getChildAt(0);
					// 1 sim for all retailer is selected
					if(rdo.isChecked()){
						if(etOneSimRetailerNum.length() == 0){
							bValid = false;
							sMessage += this.getString(R.string.NoOneSimRetailerNum);
						}else{
							if(etOneSimRetailerNum.length() < 11 && etOneSimRetailerNum.length() > 0){
								bValid = false;
								sMessage += this.getString(R.string.IncompleteOnSimRetailerNum);
							}
						}
					} else {
						//1 sim for all refuse to give number is selected
//						CheckBox cb = (CheckBox)rgOneSimSelections.getChildAt(4);
//						if(!cb.isChecked()){
//							bValid = false;
//							sMessage += this.getString(R.string.RefuseTestBuyMandadory);
//						}
						//
						CheckBox cb = (CheckBox)rgOneSimSelections.getChildAt(4);
						if(!cb.isChecked() && etOneSimTraceNo.length() == 0){
							bValid = false;
							sMessage += this.getString(R.string.SelectEitherCheckOrNumber);
						}
					}
					//
//					CheckBox cb = (CheckBox)rgOneSimSelections.getChildAt(4);
//					if(!cb.isChecked()){
//						if(etOneSimTraceNo.length() == 0){
//							bValid = false;
//							sMessage += this.getString(R.string.NoTraceNumber);
//						}
//					}
				}
				
			}
			if (spnSmartSellingLoad.getSelectedItem().toString().equals("Yes")){
				
				if(etSmartLoad.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoSmartLoad);
				}
				// --
				String sCodeSmart = Utilities.getSelHowOftenEnums(spnSMARTtnt.getSelectedItem().toString());
				if(sCodeSmart.equals("0")){
					bValid = false;
					sMessage += this.getString(R.string.NoFrequencySmartSel);
				}
			}
			if (spnSunSellingLoad.getSelectedItem().toString().equals("Yes")){
				
				if(etSunLoad.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoSunLoad);
				}
				// --
				String sCodeSun = Utilities.getSelHowOftenEnums(spnSun.getSelectedItem().toString());
				if (sCodeSun.equals("0")) {
					bValid = false;
					sMessage += this.getString(R.string.NoFrequencySunSel);
				}
			}
			//---------------------------------------------------------------------
			if(!spnSellingSim.getSelectedItem().toString().equals("Not selling")){
				//--
				if(etGPStockSim.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoStockGP);
				}
				if(etTMStockSim.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoStockTM);
				}
				if(etABSStockSim.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoStockAbs);
				}
				if(etSmartStockSim.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoStockSm);
				}
				if(etTntStockSim.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoStockTnT);
				}
				if(etSunStockSim.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoStockSun);
				}
				// ---------------------------------------------------------------------
				if (etGPSimSales.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoSimSalesGP);
				}
				if (etTMSimSales.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoSimSalesTM);
				}
				if (etABSSimSales.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoSimSalesABS);
				}
				if (etSmartSimSales.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoSimSalesSmart);
				}
				if (etTNTSimSales.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoSimSalesTnt);
				}
				if (etSUNSimSales.length() == 0){
					bValid = false;
					sMessage += this.getString(R.string.NoSimSalesSun);
				}
			}
			//---------------------------------------------------------------------
			if(etTelMobtel.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoRetTelMobtel);
			} else {
				if(etTelMobtel.length() < 7){
					if(!etTelMobtel.getText().toString().equals("0")){
						bValid = false;
						sMessage += this.getString(R.string.MissingTelMobtel);
					}
				}
			}
			//-- 
			if(etOwner.length() == 0){
				bValid = false;
				sMessage += this.getString(R.string.NoOwnerName);
			}
			
			// --
			if(MainApp.PHOTOSYNC_MODE == 1){
				if(mCurrentPhotoPath == null){
					bValid = false;
					sMessage += this.getString(R.string.NoImagePhoto);
				}
			}
		// ---------------------------------------------------------------------
		
		if (!bValid) {
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.alert)
        	.setMessage(sMessage)
        	.setNeutralButton(this.getString(R.string.OK), null)
			.show();
		}
		//
		return bValid;
	}
	// =========================================================================
	private String getSelRefuseOptionEnums(String compare) {
		// region
		String code = "";

		for (int x = 0; x < Enums.RefuseOption.values().length; x++) {

			if (Enums.RefuseOption.values()[x].equalsName(compare)) {
				code = Enums.RefuseOption.values()[x].toID();
			}
		}
		// endregion
		return code;
	}
	// =========================================================================
	// =========================================================================
	private boolean photoValidate(){
		
		boolean bValid = true;
		String sMessage = this.getString(R.string.ValidateError);
		
		if (etNameOfStore.length() == 0){
			bValid = false;
			sMessage += this.getString(R.string.NoRetName);
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
	// =========================================================================
	// =========================================================================
	private void dialogSaveDraftPrompt(){
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Save to Draft?");
		//alert.setMessage("");
		alert.setPositiveButton("Save",  new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveToDraft();
			}
		});
		//
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finishActivity();
			}
		});
		//
		alert.show();
	}
	// =========================================================================
	private boolean isSaveDraftValid(){
		
		boolean valid = false;
		
		if(etNameOfStore.getText().length() != 0 
				|| etInterviewee.getText().length() != 0
				|| etUnitNo.getText().length() != 0
				|| etStreet.getText().length() !=0 ){
			
			valid = true;
		}
		return valid;
	}
	// =========================================================================
	private void setChecklistFromDraft(String numDraft){
		
		
		int num = Integer.parseInt(numDraft);
		String binary = Integer.toBinaryString(num);
		
		char[] charArray = binary.toCharArray();
		ArrayList<String> str = new ArrayList<String>();
		ArrayList<String> strTemp = new ArrayList<String>();
		
		int countChar = charArray.length;
		
		
		for(int x = 8; x > 0 ; x--){
			
			if(countChar < x){
				strTemp.add("0");
			} else {
				String st = Character.toString(charArray[x-1]);
				str.add(st);
			}
		}
		
		for(int y = 0; y < strTemp.size(); y++){
			str.add("0");
		}
		
		int nume = str.size();
		
		for(int y = 0; y < str.size(); y++){
			
			if(str.get(y).toString().equals("1")){
				lvNearEstablishment.setItemChecked(y, true);
				// if all check is not selected
				if(y != str.size() - 1){
					lvNearEstablishment.setItemChecked(7, false);
				}
				
				
			} else {
				lvNearEstablishment.setItemChecked(y, false);
			}
		}
		
	}
	// =========================================================================
	private int getPosCitiesDraft(){
		
		ArrayList<CodeDescPair> allCity = mainApp.getDatabase().getCitiesByCode(draftCities[0]);
		int pos = 0;
		
		allCity.add(0,new CodeDescPair("0", ""));
		
		for(int x = 0; x < allCity.size(); x++){
			
			if(allCity.get(x).Code.equals(draftCities[1])){
				pos = x;
			}
		}
		allCity.clear();
		return pos;
	}
	// =========================================================================
	private void ProcessImageCapture(){
		
		Bitmap bitmap = Utils.setCustomizeImage(ivImageIcon, mCurrentPhotoPath);
    	ivImageIcon.setImageBitmap(bitmap);
    	//
    	if(mOldPhotoPath != null){
    		File deleteFile = new File(mOldPhotoPath);
    		deleteFile.delete();
    	}
    	//
    	if(isDraft){
    		int id = getIntent().getIntExtra("DraftID", 0);
    		mainApp.getDatabase().updateSelectedImagePath(id, mCurrentPhotoPath);
    	}
    	//
    	mOldPhotoPath = mCurrentPhotoPath;
	}
	// =========================================================================
	private void finishActivity(){
		// delete if user takes photo and decide to exit then delete current photo
		if(!isDraft){
			if(mCurrentPhotoPath != null){
				File deleteFile = new File(mCurrentPhotoPath);
				deleteFile.delete();
			}
		}
		
		this.finish();
	}
	// =========================================================================
	private boolean validateGlobeRetailer(){
		
		boolean bValid = true;
		
		String[] GlobeCode = new String[] {"0917", "0939", "0930", "0940", "0915", "0916", "0927", "0926","0907","0905","0906","0935"};
		
		if(etGlobeRetailerNo.length() <= 10){
			Utils.MessageToast(getBaseContext(), "Globe Retailer No.: Accept 11 Numbers Only");
			etGlobeRetailerNo.setText("");
			bValid = false;
		}else {
			
		}
		
		return bValid;
	}
	// =========================================================================
	private boolean validateTelMobtel(){
		
		 boolean bValid = true;
		
		if(etTelMobtel.length() <= 6 && etTelMobtel.length() > 1){
			Utils.MessageToast(getBaseContext(), "Tel of Mobtel: Accept 7 - 11 Numbers Only");
			
			etTelMobtel.setText("");
			bValid = false;
		}
		
		return bValid;
	}
	// =========================================================================
	private void renameFilename(){
		
		File imageFile = new File(mCurrentPhotoPath);
		
		String file = imageFile.getName();
		
		String[] str = file.split("_");
		
		String filename = str[0];
		
		String[] params = str[1].split("-");
		String map = params[0];
		String status = params[1];
		String[] strOne = params[2].split("\\.");
		String storename = strOne[0];
		// --
		File renameFile = new File(Configs.IMAGE_PATH + filename + "_"+ map + "-S-"+ storename+".jpg");
		imageFile.renameTo(renameFile);
	}
	// =========================================================================
	// =========================================================================
	private void setMappingCode(){
		
		String GlobeSel = spnGlobeSellingLoad.getSelectedItem().toString();
		String SmartSel = spnSmartSellingLoad.getSelectedItem().toString();
		String SunSel = spnSunSellingLoad.getSelectedItem().toString();
		
		// FMCG map
		if(GlobeSel.equals("No") && SmartSel.equals("No") && SunSel.equals("No")){
			
			setFMCGmap();
		}
		// NDSO map
		if ((GlobeSel.equals("Yes") && SmartSel.equals("Yes") && SunSel.equals("Yes")) || 
		    (GlobeSel.equals("Yes") && SmartSel.equals("Yes") && SunSel.equals("No")) ||
		    (GlobeSel.equals("Yes") && SmartSel.equals("No") && SunSel.equals("Yes"))) {

			setNDSOmap();
		}
		// NS map
		if ((GlobeSel.equals("No") && SmartSel.equals("Yes") && SunSel.equals("Yes")) || 
		    (GlobeSel.equals("No") && SmartSel.equals("Yes") && SunSel.equals("No")) ||
		    (GlobeSel.equals("No") && SmartSel.equals("No") && SunSel.equals("Yes"))) {
			
			setNSmap();
		}
		
	}
	// =========================================================================
	private void setNDSOmap(){
		
		MAPCODE = "NDSOMAP4";
		if(MainApp.DEBUG == 1){
			setTitle("iOTA GPS Mapping : NDSO");
		}
		// ---------------------------------------
		// -- > revision 2
		if(spnLoadCategory.getSelectedItemPosition() == 0){
			setGlobeCategoryEnable();
		} else {
			setOneSimCategoryEnable();
		}
		// -- <
		//--
		if(isDraft){
			
			if(isDraftRefuseToGiveSet < 4){
				
				rbRetailerRefuse.setChecked(draftOptions[1].equals("true") ? true : false);
				cbRefusedTestBuy.setChecked(draftOptions[3].equals("true") ? true : false);
			}
			isDraftRefuseToGiveSet += 1;
		}
		//--
	}
	// =========================================================================
	private void setNSmap(){
		
		MAPCODE = "NSMAP4";
		if(MainApp.DEBUG == 1){
			setTitle("iOTA GPS Mapping : NS");
		}
		// ---------------------------------------*/
		// -- > revision 2
		if(spnLoadCategory.getSelectedItemPosition() == 0){
			setGlobeCategoryDisable();
		} else {
			setOneSimCategoryDisable();
		}
		// -- < 2
	}
	// =========================================================================
	private void setFMCGmap(){
		
		MAPCODE = "NFMAP2";
		if(MainApp.DEBUG == 1){
			setTitle("iOTA GPS Mapping : FMCG");
		}
		
		// -- > revision 2
		if(spnLoadCategory.getSelectedItemPosition() == 0){
			setGlobeCategoryDisable();
		} else {
			setOneSimCategoryDisable();
		}
		// -- < 2
	}
	// =========================================================================
	private void setGlobeCategoryDisable() {
		
		spnLoadCategory.setEnabled(false);
		
		for (int x = 0; x < rgGlobeSelections.getChildCount(); x++) {
			
			if(x != 5){
				rgGlobeSelections.getChildAt(x).setEnabled(false);
			} else {
				rgGlobeSelections.getChildAt(x).setEnabled(true);
			}
			//
			if (rgGlobeSelections.getChildAt(x) instanceof EditText) {
				EditText txt = (EditText) rgGlobeSelections.getChildAt(x);
				txt.setText("");
			}
		}
		RadioButton rdo = (RadioButton) rgGlobeSelections.getChildAt(5);
		rdo.setChecked(true);
	}
	// =========================================================================
	private void setGlobeCategoryEnable(){
		
		spnLoadCategory.setEnabled(true);
		
		for(int x = 0; x < rgGlobeSelections.getChildCount(); x++){
			
			if(x != 5){
				rgGlobeSelections.getChildAt(x).setEnabled(true);
			} else {
				rgGlobeSelections.getChildAt(x).setEnabled(false);
			}
		}

		RadioButton rdo = (RadioButton)rgGlobeSelections.getChildAt(0);
		rdo.setChecked(true);
	}
	// =========================================================================
	private void setOneSimCategoryDisable(){
		
		spnLoadCategory.setEnabled(false);
		
		for (int x = 0; x < rgOneSimSelections.getChildCount(); x++) {
			
			if(x != 7){
				rgOneSimSelections.getChildAt(x).setEnabled(false);
			} else {
				rgOneSimSelections.getChildAt(x).setEnabled(true);
			}
			//
			if (rgOneSimSelections.getChildAt(x) instanceof EditText) {
				EditText txt = (EditText) rgOneSimSelections.getChildAt(x);
				txt.setText("");
			}
		}

		RadioButton rdo = (RadioButton)rgOneSimSelections.getChildAt(7);
		rdo.setChecked(true);
	}
	// =========================================================================
	private void setOneSimCategoryEnable(){
		
		spnLoadCategory.setEnabled(true);
		
		for(int x = 0; x < rgOneSimSelections.getChildCount(); x++){
			
			if(x!=7){
				rgOneSimSelections.getChildAt(x).setEnabled(true);
			}else {
				rgOneSimSelections.getChildAt(x).setEnabled(false);
			}
		}
		 
		RadioButton rdo = (RadioButton)rgOneSimSelections.getChildAt(0);
		rdo.setChecked(true);
		//
		CheckBox cb = (CheckBox)rgOneSimSelections.getChildAt(4);
		cb.setChecked(false);
	}
	// =========================================================================
	private String removeCommaStr(EditText et){
		
		String txt = et.getText().toString();
		return txt = txt.replace(",", "");
	}
	// =========================================================================
	private void callLeaveDialog(){
		 
		if (etNameOfStore.length() != 0){
			
			new AlertDialog.Builder(this)
			.setTitle("Leave the form?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finishAct();
				}
			})
			.setNegativeButton("NO", null)
			.show();
		}
		
	}
	private void finishAct(){
		this.finish();
	}
// =========================================================================
// TODO Inner Class
// =========================================================================

// =========================================================================
// TODO Final Codes
}
