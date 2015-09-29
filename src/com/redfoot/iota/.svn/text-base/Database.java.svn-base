package com.redfoot.iota;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.redfoot.iota.objects.DraftObj;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CheckBox;
import android.widget.ListView;

public class Database {
 // =========================================================================
 // TODO Variables
 // =========================================================================
	public final static String COL_ID = "id";
	
	public final static String TABLE_BARANGAY = "tblBarangay";
	public final static String COL_BRGY_CODE = "BrgyCode";
	public final static String COL_DESC = "Description";
	
	public final static String TABLE_CITY = "tblCity";
	public final static String COL_CITY_ID = "CityID";
	
	public final static String TABLE_PROVINCE = "tblProvince";
	public final static String COL_PROVINCE_ID = "ProvinceID";
	
	public final static String TABLE_CUST_SUBTYPE = "tblCustSubtype";
	public final static String COL_CUST_SUBTYPE_ID = "CustSubtypeID";
	
	public final static String TABLE_STORE_SIZE = "tblStoreSize";
	public final static String COL_STORE_SIZE_ID = "StoreSizeID";
	
	public final static String TABLE_ESTABLISHMENT = "tblEstablishment";
	public final static String COL_ESTABLISHMENT_ID = "EstablishmentID";
	
	public final static String TABLE_INTERVIEWED = "tblInterviewed";
	public final static String COL_INTERVIEWED_ID = "InterviewedID";
	
	public final static String TABLE_PARAMETERS = "tblParameters";
	public final static String COL_PARAM_CODE = "ParamCode";
	public final static String COL_PARAM_VALUE = "ParamValue";
	
	public final static String TABLE_PRODUCT_CATEGORIES = "tblProdCat";
	public final static String COL_PRODCAT_ID = "ProdCatID";
	
	public final static String TABLE_BRAND = "tblBrand";
	public final static String COL_BRAND_ID = "BrandID";
	
	public final static String TABLE_PRODUCT_LIST = "tblProdList";
	public final static String COL_PRODLIST_ID = "ProdListID";
	
	public final static String PARAM_IOTA_NUM = "IotaNumber";
	public final static String PARAM_LAST_PROVINCE = "LastProvince";
	public final static String PARAM_LAST_CITY = "LastCity";
	public final static String PARAM_LAST_BARANGAY = "LastBarangay";
	// -- new added by benkurama 2014->
	public final static String TABLE_CHAIN_ACCOUNT = "tblChainAccount";
	public final static String COL_CHAINACC_ID = "ChainAccID";
	
	public final static String TABLE_STORE_LOCATION = "tblStoreLocation";
	public final static String COL_STORELOC_ID = "StoreLocID";
	
	public final static String TABLE_SAVE_DRAFT = "tblSaveDraft";
	// -- <
	
	// Database history
	/* Database version 1
	 * 
	 * 		tblBarangay									tblCity										tblParameters
	 *  	-id - integer primary key autoincrement		-id - integer primary key autoincrement		-id - integer primary key autoincrement
	 *  	-BrgyCode - text							-CityID - text								-ParamCode - text
	 *  	-Description - text							-Description - text							-ParamValue - text
	 *  
	 *  	tblProvince									tblCustSubtype
	 *  	-id - integer primary key autoincrement		-id - integer primary key autoincrement
	 *  	-ProvinceID - text							-CustSubtypeID - text
	 *  	-Description - text							-Description - text
	 *  
	 * Preloaded data:
	 *  
	 * 		tblParameters:
	 *  	ParamCode			ParamValue
	 *  	'IotaNumber'		''
	 *  
	 * ---------------------------------------
	 * Database version 2
	 * 
	 * 		tblProdCat									tblBrand									tblProdList
	 * 		-id - integer primary key autoincrement		-id - integer primary key autoincrement		-id - integer primary key autoincrement
	 * 		-ProdCatID - text							-BrandID - text								-ProdListID - text
	 * 		-Description - text							-Description - text							-ProdCatID - text
	 *  																							-BrandID - text
	 *  
	 * Preloaded data:
	 *  
	 * 		tblProdList:					
	 * 		ProdListID	ProdCatID	BrandID
	 * 		1			1			1
	 * 		2			1			4
	 * 		3			1			7
	 * 		4			2			2
	 * 		5			2			3
	 * 		6			2			5
	 * 		7			2			6
	 * 		8			2			7
	 * 		9			2			8
	 * 		10			3			2
	 * 		11			3			3
	 * 		12			3			5
	 * 		13			3			6
	 * 		14			3			7
	 * 
	 * ---------------------------------------
	 * Database version 3
	 * 
	 * Preloaded data:
	 * 
	 * 		tblParameters:
	 * 		ParamCode			ParamValue
	 * 		'LastProvince'		''
	 * 		'LastCity'			''
	 * 		'LastBarangay'		''
	 * 
	 */
 // =========================================================================
 // TODO Initial Setups
 // =========================================================================
	private final static String DATABASE_NAME = "iota.db";
	private final static int DATABASE_VERSION = 7;
	
	private final static String CREATE_STATEMENT_1 = "create table if not exists " + TABLE_BARANGAY + " (" + COL_ID + 
													 " integer primary key autoincrement, " + COL_BRGY_CODE + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_2 = "create table if not exists " + TABLE_CITY + " (" + COL_ID + 
													 " integer primary key autoincrement, " + COL_CITY_ID + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_3 = "create table if not exists " + TABLE_PROVINCE + " (" + COL_ID + 
													 " integer primary key autoincrement, " + COL_PROVINCE_ID + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_4 = "create table if not exists " + TABLE_CUST_SUBTYPE + " (" + COL_ID + 
													 " integer primary key autoincrement, " + COL_CUST_SUBTYPE_ID + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_5 = "create table if not exists " + TABLE_PARAMETERS + " (" + COL_ID + 
													 " integer primary key autoincrement, " + COL_PARAM_CODE + " text, " + COL_PARAM_VALUE + " text);";
	private final static String CREATE_STATEMENT_6 = "create table if not exists " + TABLE_PRODUCT_CATEGORIES + " (" + COL_ID + 
													 " integer primary key autoincrement, " + COL_PRODCAT_ID + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_7 = "create table if not exists " + TABLE_BRAND + " (" + COL_ID +
													 " integer primary key autoincrement, " + COL_BRAND_ID + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_8 = "create table if not exists " + TABLE_PRODUCT_LIST + " (" + COL_ID + 
													 " integer primary key autoincrement, " + COL_PRODLIST_ID + " text, " + 
													 COL_PRODCAT_ID + " text, " + COL_BRAND_ID + " text);";
	private final static String CREATE_STATEMENT_9 = "create table if not exists " + TABLE_STORE_SIZE + " (" + COL_ID + 
			 										 " integer primary key autoincrement, " + COL_STORE_SIZE_ID + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_10 = "create table if not exists " + TABLE_ESTABLISHMENT + " (" + COL_ID + 
			 										  " integer primary key autoincrement, " + COL_ESTABLISHMENT_ID + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_11 = "create table if not exists " + TABLE_INTERVIEWED + " (" + COL_ID + 
			 										  " integer primary key autoincrement, " + COL_INTERVIEWED_ID + " text, " + COL_DESC + " text);";
	// -- added codes 1/9/2014 by benkurama ->
	private final static String CREATE_STATEMENT_12	= "create table if not exists "+ TABLE_CHAIN_ACCOUNT +" (" + COL_ID + 
													" integer primary key autoincrement, " + COL_CHAINACC_ID + " text, " + COL_DESC + " text);";
	private final static String CREATE_STATEMENT_13 = "create table if not exists "+ TABLE_STORE_LOCATION +" (" + COL_ID + 
													" integer primary key autoincrement, " + COL_STORELOC_ID + " text, " + COL_DESC + " text);";
	
	private final static String CREATE_SAVEDRAFT_14 = "create table if not exists " +TABLE_SAVE_DRAFT+ " (" + COL_ID + 
													" integer primary key autoincrement, draft text, date text, imagepath text, remarks text);";
	// -- <
	
	private static Activity m_aContext;
	private static SQLiteDatabase m_sqldDB;

	public Database (Activity context) {
		Database.m_aContext = context;
		
		DatabaseOpenHelper helper = new DatabaseOpenHelper(Database.m_aContext);
		
		Database.m_sqldDB = helper.getWritableDatabase();
	}
	
	public void close() {
		m_sqldDB.close();
	}
	
	public void createDatabase() {
		DatabaseOpenHelper helper = new DatabaseOpenHelper(Database.m_aContext);
		
		helper.onCreate(m_sqldDB);
	}
	
	public void setupDatabase(ListView lvProvinces, String iotanum) {
		DatabaseOpenHelper helper = new DatabaseOpenHelper(Database.m_aContext);
		
		helper.beginSetup(lvProvinces, iotanum);
	}
	
	public void closeDatabase(){
		
		DatabaseOpenHelper helper = new DatabaseOpenHelper(Database.m_aContext);
		helper.close();
	}
 // =========================================================================
 // TODO Main Functions
 // =========================================================================
	public ArrayList<ProvinceItem> readProvinces() {
		DatabaseOpenHelper helper = new DatabaseOpenHelper(Database.m_aContext);
		
		return helper.readFromXml(R.xml.province);
	}	
	// =========================================================================
	public ArrayList<CodeDescPair> getProvinces() {
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();

		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_PROVINCE, new String[] { COL_PROVINCE_ID, COL_DESC }, null, null, null, null, null);
			
			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_PROVINCE_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}

		return alTemp;
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getCities() {
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();

		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_CITY, new String[] { COL_CITY_ID, COL_DESC }, null, null, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_CITY_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}

		return alTemp;
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getCitiesByCode(String codeCity){
		
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();

		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_CITY, new String[] { COL_CITY_ID, COL_DESC },COL_CITY_ID+ " like '"+ codeCity+ "%'", null, null, null, COL_DESC);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_CITY_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}

		return alTemp;
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getBarangays(String city) {
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();
		
		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_BARANGAY, new String[] { COL_BRGY_CODE, COL_DESC }, COL_BRGY_CODE + " like '" + city + "%'", 
					null, null, null, COL_DESC);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_BRGY_CODE)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}

		return alTemp;
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getCustSubTypes() {
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();
		
		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_CUST_SUBTYPE, new String[] { COL_CUST_SUBTYPE_ID, COL_DESC }, null, null, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_CUST_SUBTYPE_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}
		
		return alTemp;
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getStoreSizes() {
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();
		
		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_STORE_SIZE, new String[] { COL_STORE_SIZE_ID, COL_DESC }, null, null, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_STORE_SIZE_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}
		
		return alTemp;
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getEstablisments() {
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();
		
		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_ESTABLISHMENT, new String[] { COL_ESTABLISHMENT_ID, COL_DESC }, null, null, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_ESTABLISHMENT_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}
		
		return alTemp;
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getInterview() {
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();
		
		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_INTERVIEWED, new String[] { COL_INTERVIEWED_ID, COL_DESC }, null, null, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_INTERVIEWED_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}
		
		return alTemp;
	}
	// -- added codes 1/9/2014 by benkurama ->
	// =========================================================================
	public ArrayList<CodeDescPair> getChainAccountList(){
		
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();
		
		try{
			Cursor cursor = Database.m_sqldDB.query(TABLE_CHAIN_ACCOUNT, new String[] { COL_CHAINACC_ID, COL_DESC }, null, null, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_CHAINACC_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}catch (Exception e){
			
		}
		
		return alTemp;
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getStoreLocationList(){
		
		ArrayList<CodeDescPair> alTemp = new ArrayList<CodeDescPair>();
		
		try{
			Cursor cursor = Database.m_sqldDB.query(TABLE_STORE_LOCATION, new String[] { COL_STORELOC_ID, COL_DESC }, null, null, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					alTemp.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_STORELOC_ID)), cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}catch (Exception e){
			
		}
		
		return alTemp;
	}
	// =========================================================================
	public void insertSaveDraft(String draft, String date, String imagepath, String remarks){
		
		try{
			ContentValues addDrafts = new ContentValues();
			
			addDrafts.put("draft", draft);
			addDrafts.put("date", date);
			addDrafts.put("imagepath", imagepath);
			addDrafts.put("remarks", remarks);
			
			Database.m_sqldDB.insert(TABLE_SAVE_DRAFT, null, addDrafts);
			
		}catch(Exception e){
			
		}
	}
	// =========================================================================
	public void updateSaveDraft(String draft, String date, String imagepath, String remarks, int id){
		try{
			ContentValues updateDrafts = new ContentValues();
			
			updateDrafts.put("draft", draft);
			updateDrafts.put("date", date);
			updateDrafts.put("imagepath", imagepath);
			updateDrafts.put("remarks", remarks);
			
			Database.m_sqldDB.update(TABLE_SAVE_DRAFT, updateDrafts, "id = "+id, null);
			
		}catch(Exception e){
			
		}
	}
	// =========================================================================
	public void deleteSaveDraft(int id){
		
		Database.m_sqldDB.delete(TABLE_SAVE_DRAFT, "id=?", new String[]{Integer.toString(id)});
	}
	// ========================================================================= for 2/27/2014 added
	public String checkImagePath(int id){
		
		String imagePath = "";
		try{
			Cursor cur = Database.m_sqldDB.rawQuery("select imagepath from tblSaveDraft where id=" +Integer.toString(id)+"", null);
			if(cur.moveToFirst()){
				
				do{
					imagePath = cur.getString(0);
				} while(cur.moveToNext());
			}
		}catch(Exception e){
		}
		return imagePath;
	}
	// =========================================================================
	public void updateSelectedImagePath(int id, String imagePathNew){
		try{
			
			ContentValues updateImagePath = new ContentValues();
			
			updateImagePath.put("imagepath", imagePathNew);
			
			Database.m_sqldDB.update(TABLE_SAVE_DRAFT, updateImagePath, "id = "+id, null);
			
		}catch(Exception e){
			
		}
	}
	// =========================================================================
	public int countDrafts(){
		
		int count = 0;
		try{
		Cursor cur = Database.m_sqldDB.rawQuery("select * from "+TABLE_SAVE_DRAFT, null);
		
		count = cur.getCount();
		}catch(Exception e){
			
		}
		return count;
	}
	// =========================================================================
	public ArrayList<DraftObj> getAllDrafts(){
		
		ArrayList<DraftObj> listDraft = new ArrayList<DraftObj>();
		
		try{
			
			Cursor cur = Database.m_sqldDB.rawQuery("select * from "+TABLE_SAVE_DRAFT+ " order by id desc", null);
			
			if(cur.getCount() != 0){
				
				for(cur.moveToFirst(); !(cur.isAfterLast()); cur.moveToNext()){
					
					DraftObj draft = new DraftObj();
					
					draft.ID = cur.getInt(0);
					draft.DraftContent = cur.getString(1);
					draft.Date = cur.getString(2);
					
					listDraft.add(draft);
				}
			}
			
		}catch(Exception e){
			
		}
		return listDraft;
	}
	// =========================================================================
	public ArrayList<DraftObj> getQueryDraft(int id){
		
		ArrayList<DraftObj> listDraft = new ArrayList<DraftObj>();
		
		try {

			Cursor cur = Database.m_sqldDB.rawQuery("select * from " + TABLE_SAVE_DRAFT + " where id = " +id, null);

			if (cur.getCount() != 0) {

				for (cur.moveToFirst(); !(cur.isAfterLast()); cur.moveToNext()) {

					DraftObj draft = new DraftObj();

					draft.DraftContent = cur.getString(1);
					draft.Date = cur.getString(2);
					draft.ImagePath = cur.getString(3);

					listDraft.add(draft);
				}
			}

		} catch (Exception e) {
			
		}
		
		return listDraft;
	}
	// -- <
	// =========================================================================
	public String getIotaNum() {
		String number = "";
		
		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_PARAMETERS, new String[] { COL_PARAM_CODE, COL_PARAM_VALUE }, 
							COL_PARAM_CODE + " = '" + PARAM_IOTA_NUM + "'", null, null, null, null);

			if (cursor.moveToFirst()) {
				number = cursor.getString(cursor.getColumnIndex(COL_PARAM_VALUE));
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}
		
		return number;
	}
	// =========================================================================
	public int setIotaNum(String num) {
		ContentValues cvTemp = new ContentValues();
		cvTemp.put(COL_PARAM_VALUE, num);

		return Database.m_sqldDB.update(TABLE_PARAMETERS, cvTemp, COL_PARAM_CODE + "='" + PARAM_IOTA_NUM + "'", null);
	}
	// =========================================================================
	public String getSystemParam(String parameter) {
		String value = "";
		
		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_PARAMETERS, new String[] { COL_PARAM_CODE, COL_PARAM_VALUE }, 
							COL_PARAM_CODE + " = '" + parameter + "'", null, null, null, null);
			
			if (cursor.moveToFirst()) {
				value = cursor.getString(cursor.getColumnIndex(COL_PARAM_VALUE));
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
		}
		
		return value;
	}
	// =========================================================================
	public int setSystemParam(String parameter, String value) {
		ContentValues cvTemp = new ContentValues();
		
		cvTemp.put(COL_PARAM_VALUE, value);
		
		return Database.m_sqldDB.update(TABLE_PARAMETERS, cvTemp, COL_PARAM_CODE + " = '" + parameter + "'", null);
	}
	// =========================================================================
	public ArrayList<CodeDescPair> getProductList() {
		ArrayList<CodeDescPair> cdpProdCat = new ArrayList<CodeDescPair>();
		ArrayList<CodeDescPair> cdpBrand = new ArrayList<CodeDescPair>();
		ArrayList<CodeDescPair> cdpTemp = new ArrayList<CodeDescPair>();
		
		try {
			Cursor cursor = Database.m_sqldDB.query(TABLE_PRODUCT_CATEGORIES, new String[] { COL_PRODCAT_ID, COL_DESC }, null, null, null, null, null);
			
			if (cursor.moveToFirst()) {
				do {
					cdpProdCat.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_PRODCAT_ID)), 
													cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

			cursor = Database.m_sqldDB.query(TABLE_BRAND, new String[] { COL_BRAND_ID, COL_DESC }, null, null, null, null, null);
			
			if (cursor.moveToFirst()) {
				do {
					cdpBrand.add(new CodeDescPair(cursor.getString(cursor.getColumnIndex(COL_BRAND_ID)), 
													cursor.getString(cursor.getColumnIndex(COL_DESC))));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

			cursor = Database.m_sqldDB.query(TABLE_PRODUCT_LIST, new String[] { COL_PRODLIST_ID, COL_PRODCAT_ID, COL_BRAND_ID }, 
											 null, null, null, null, null);
			
			if (cursor.moveToFirst()) {
				do {
					String sProdCat = cursor.getString(cursor.getColumnIndex(COL_PRODCAT_ID));
					String sBrand = cursor.getString(cursor.getColumnIndex(COL_BRAND_ID));
					
					for (CodeDescPair cdp : cdpProdCat) {
						if (cdp.Code.equals(sProdCat)) {
							sProdCat = cdp.Description;
							break;
						}
					}
					
					for (CodeDescPair cdp : cdpBrand) {
						if (cdp.Code.equals(sBrand)) {
							sBrand = cdp.Description;
							break;
						}
					}
					
					cdpTemp.add(new CodeDescPair(sProdCat, sBrand));
				} while (cursor.moveToNext());
			}
			
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {

		}
		
		return cdpTemp;
	}
	 // =========================================================================
 	 // TODO Inner Class
 	 // =========================================================================
	private static class DatabaseOpenHelper extends SQLiteOpenHelper implements Runnable {
	 // =========================================================================
 	 // TODO Variables
 	 // =========================================================================
		private static Context m_cContext;
		private static SQLiteDatabase m_sdDB = SQLiteDatabase.create(null);
		private static ProgressDialog m_pdProgress;
		private static ArrayList<String> m_alProvinces = new ArrayList<String>();
	 // =========================================================================
 	 // TODO Constructor
 	 // =========================================================================
		DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
			m_cContext = context;
		}
	 // =========================================================================
 	 // TODO Life Cycles
 	 // =========================================================================
		@Override
		public void onCreate(SQLiteDatabase db) {
			m_sdDB = db;
		}
		// =========================================================================
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			switch (oldVersion) {
				case 1:
					m_sdDB.execSQL(CREATE_STATEMENT_6);
					m_sdDB.execSQL(CREATE_STATEMENT_7);
					m_sdDB.execSQL(CREATE_STATEMENT_8);
					readFromXml(db, R.xml.prodcat, Database.COL_PRODCAT_ID, Database.TABLE_PRODUCT_CATEGORIES);
					readFromXml(db, R.xml.brand, Database.COL_BRAND_ID, Database.TABLE_BRAND);
					setupProdList(db);
				case 2:
					setupLastAddress(db);
				case 3:
					updateCustSubtype(db);
				case 4:
					updateProdList(db);
				case 5:
					updateCustSubtype(db);
				case 6:
					setupChainAccount(db);
			}
		}
	 // =========================================================================
 	 // TODO Main Functions
 	 // =========================================================================
		public void beginSetup(ListView lvProvinces, String iotanum) {
			
			CheckBoxArrayAdapter cbaAdapter = (CheckBoxArrayAdapter) lvProvinces.getAdapter();
			int iItemCount = cbaAdapter.getCount();

			m_alProvinces.clear();
			for (int i = 0; i < iItemCount; i++) {
				CheckBox cbTemp = (CheckBox)cbaAdapter.getView(i, null, null).findViewById(R.id.d_cbCheck);
				
				if (cbTemp.isChecked()) {
					m_alProvinces.add(cbaAdapter.getItem(i).getItem().Code);
				}
			}
			
			m_pdProgress = new ProgressDialog(m_cContext);
			m_pdProgress.setTitle(R.string.initializing);
			m_pdProgress.setMessage(m_cContext.getString(R.string.initialwait));
			m_pdProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			m_pdProgress.setMax(310);
			m_pdProgress.setIndeterminate(false);
			m_pdProgress.setCancelable(false);
			m_pdProgress.show();
			
			m_sdDB.execSQL(CREATE_STATEMENT_1);
			m_sdDB.execSQL(CREATE_STATEMENT_2);
			m_sdDB.execSQL(CREATE_STATEMENT_3);
			m_sdDB.execSQL(CREATE_STATEMENT_4);
			m_sdDB.execSQL(CREATE_STATEMENT_5);
			m_sdDB.execSQL(CREATE_STATEMENT_6);
			m_sdDB.execSQL(CREATE_STATEMENT_7);
			m_sdDB.execSQL(CREATE_STATEMENT_8);
			m_sdDB.execSQL(CREATE_STATEMENT_9);
			m_sdDB.execSQL(CREATE_STATEMENT_10);
			m_sdDB.execSQL(CREATE_STATEMENT_11);
			// -- added codes 1/9/2014 by benkurama ->
			m_sdDB.execSQL(CREATE_STATEMENT_12);
			m_sdDB.execSQL(CREATE_STATEMENT_13);
			// -----=-----=-----=----- ><
			m_sdDB.execSQL(CREATE_SAVEDRAFT_14);
			// -- <
			
			ContentValues cvTemp = new ContentValues();
			cvTemp.put(COL_PARAM_CODE, PARAM_IOTA_NUM);
			cvTemp.put(COL_PARAM_VALUE, iotanum);
			m_sdDB.insert(TABLE_PARAMETERS, null, cvTemp);
			

			Thread thread = new Thread(this);
			thread.start();
		}
		// =========================================================================
		public void run() {
			populateValues(m_sdDB);
		}
		// =========================================================================		
		private void populateValues(SQLiteDatabase db) {
			
			readFromXml(db, R.xml.barangay_a, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_b, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_c, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_d, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_e, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_f, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_g, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_h, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_i, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_j, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_k, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_l, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_m, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_n, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_o, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_p, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_q, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_r, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_s, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_t, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_u, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.barangay_vwxyz, Database.COL_BRGY_CODE, Database.TABLE_BARANGAY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.city, Database.COL_CITY_ID, Database.TABLE_CITY);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.province, Database.COL_PROVINCE_ID, Database.TABLE_PROVINCE);
			m_pdProgress.incrementProgressBy(10);
			//region old codes
//			readFromXml(db, R.xml.prodcat, Database.COL_PRODCAT_ID, Database.TABLE_PRODUCT_CATEGORIES);
//			readFromXml(db, R.xml.brand, Database.COL_BRAND_ID, Database.TABLE_BRAND);
//			setupProdList(db);
			//endregion
			setupLastAddress(db);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.custsubtype, Database.COL_CUST_SUBTYPE_ID, Database.TABLE_CUST_SUBTYPE);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.storesizes, Database.COL_STORE_SIZE_ID, Database.TABLE_STORE_SIZE);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.establishments, Database.COL_ESTABLISHMENT_ID, Database.TABLE_ESTABLISHMENT);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.interview, Database.COL_INTERVIEWED_ID, Database.TABLE_INTERVIEWED);
			m_pdProgress.incrementProgressBy(10);
			
			// -- added codes 1/9/2014 by benkurama ->
			readFromXml(db, R.xml.chain_account, Database.COL_CHAINACC_ID, Database.TABLE_CHAIN_ACCOUNT);
			m_pdProgress.incrementProgressBy(10);
			
			readFromXml(db, R.xml.store_location, Database.COL_STORELOC_ID, Database.TABLE_STORE_LOCATION);
			m_pdProgress.incrementProgressBy(10);
			// -- <
			
			m_pdProgress.dismiss();
			
			((Activity)m_cContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			m_cContext.sendBroadcast(new Intent(m_cContext.getPackageName() + MainApp.INTENT_SETUP_FINISHED));
		}
		// =========================================================================
		private void updateCustSubtype(SQLiteDatabase db) {
			db.delete(TABLE_CUST_SUBTYPE, null, null);
			readFromXml(db, R.xml.custsubtype, Database.COL_CUST_SUBTYPE_ID, Database.TABLE_CUST_SUBTYPE);
		}
		// =========================================================================
		private void setupLastAddress(SQLiteDatabase db) {
			ContentValues cvTemp = new ContentValues();
			
			cvTemp.put(COL_PARAM_CODE, Database.PARAM_LAST_PROVINCE);
			cvTemp.put(COL_PARAM_VALUE, "");
			db.insert(TABLE_PARAMETERS, null, cvTemp);
			cvTemp.put(COL_PARAM_CODE, Database.PARAM_LAST_CITY);
			cvTemp.put(COL_PARAM_VALUE, "");
			db.insert(TABLE_PARAMETERS, null, cvTemp);
			cvTemp.put(COL_PARAM_CODE, Database.PARAM_LAST_BARANGAY);
			cvTemp.put(COL_PARAM_VALUE, "");
			db.insert(TABLE_PARAMETERS, null, cvTemp);
		}
		// =========================================================================
		private void updateProdList(SQLiteDatabase db) {
			ContentValues cvTemp = new ContentValues();

			db.delete(TABLE_PRODUCT_CATEGORIES, null, null);
			readFromXml(db, R.xml.prodcat, Database.COL_PRODCAT_ID, Database.TABLE_PRODUCT_CATEGORIES);
			db.delete(TABLE_PRODUCT_LIST, null, null);
			setupProdList(db);

			cvTemp.put(COL_PRODLIST_ID, "15");
			cvTemp.put(COL_PRODCAT_ID, "4");
			cvTemp.put(COL_BRAND_ID, "1");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "16");
			cvTemp.put(COL_PRODCAT_ID, "4");
			cvTemp.put(COL_BRAND_ID, "4");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "17");
			cvTemp.put(COL_PRODCAT_ID, "4");
			cvTemp.put(COL_BRAND_ID, "7");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
		}
		// =========================================================================
		private void setupProdList(SQLiteDatabase db) {
			ContentValues cvTemp = new ContentValues();
			
			cvTemp.put(COL_PRODLIST_ID, "1");
			cvTemp.put(COL_PRODCAT_ID, "1");
			cvTemp.put(COL_BRAND_ID, "1");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "2");
			cvTemp.put(COL_PRODCAT_ID, "1");
			cvTemp.put(COL_BRAND_ID, "4");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "3");
			cvTemp.put(COL_PRODCAT_ID, "1");
			cvTemp.put(COL_BRAND_ID, "7");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "4");
			cvTemp.put(COL_PRODCAT_ID, "2");
			cvTemp.put(COL_BRAND_ID, "2");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "5");
			cvTemp.put(COL_PRODCAT_ID, "2");
			cvTemp.put(COL_BRAND_ID, "3");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "6");
			cvTemp.put(COL_PRODCAT_ID, "2");
			cvTemp.put(COL_BRAND_ID, "5");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "7");
			cvTemp.put(COL_PRODCAT_ID, "2");
			cvTemp.put(COL_BRAND_ID, "6");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "8");
			cvTemp.put(COL_PRODCAT_ID, "2");
			cvTemp.put(COL_BRAND_ID, "7");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "9");
			cvTemp.put(COL_PRODCAT_ID, "2");
			cvTemp.put(COL_BRAND_ID, "8");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "10");
			cvTemp.put(COL_PRODCAT_ID, "3");
			cvTemp.put(COL_BRAND_ID, "2");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "11");
			cvTemp.put(COL_PRODCAT_ID, "3");
			cvTemp.put(COL_BRAND_ID, "3");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "12");
			cvTemp.put(COL_PRODCAT_ID, "3");
			cvTemp.put(COL_BRAND_ID, "5");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "13");
			cvTemp.put(COL_PRODCAT_ID, "3");
			cvTemp.put(COL_BRAND_ID, "6");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "14");
			cvTemp.put(COL_PRODCAT_ID, "3");
			cvTemp.put(COL_BRAND_ID, "7");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "15");
			cvTemp.put(COL_PRODCAT_ID, "4");
			cvTemp.put(COL_BRAND_ID, "1");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "16");
			cvTemp.put(COL_PRODCAT_ID, "4");
			cvTemp.put(COL_BRAND_ID, "4");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
			cvTemp.put(COL_PRODLIST_ID, "17");
			cvTemp.put(COL_PRODCAT_ID, "4");
			cvTemp.put(COL_BRAND_ID, "7");
			db.insert(TABLE_PRODUCT_LIST, null, cvTemp);
		}
		// =========================================================================
		// -- added codes 1/9/2014 by benkurama ->
		private void setupChainAccount(SQLiteDatabase db){
			readFromXml(db, R.xml.chain_account, Database.COL_CHAINACC_ID, Database.TABLE_CHAIN_ACCOUNT);
		}
		// -- <
		// =========================================================================
		private void readFromXml(SQLiteDatabase db, int id, String code, String table) {
			try {
				Resources res = m_cContext.getResources();
				XmlResourceParser xpp = res.getXml(id);
				int iEvent;
				String sCurrNode = "";
				String sCode = "";
				String sValue = "";
				ContentValues cvTemp = new ContentValues();
			
				xpp.next();
				iEvent = xpp.getEventType();
				while (iEvent != XmlPullParser.END_DOCUMENT) {
					if (iEvent == XmlPullParser.START_TAG) {
						sCurrNode = xpp.getName();
						if (sCurrNode.equals("string")) {
							sCode = xpp.getAttributeValue(0);
						}
					}
					else if (iEvent == XmlPullParser.TEXT) {
						if (sCurrNode.equals("string")) {
							sValue = xpp.getText();
							// detects if table is equals to City, Baranggay, and Province
							if (!table.equals(Database.TABLE_CUST_SUBTYPE) && !table.equals(Database.TABLE_STORE_SIZE) &&
									!table.equals(Database.TABLE_ESTABLISHMENT) && !table.equals(Database.TABLE_INTERVIEWED) &&
										!table.equals(Database.TABLE_PRODUCT_CATEGORIES) && !table.equals(Database.TABLE_BRAND)&&
										!table.equals(Database.TABLE_CHAIN_ACCOUNT) && !table.equals(Database.TABLE_STORE_LOCATION)) { 
								boolean bFound = false;
								
								for (String str : m_alProvinces) {
									if (sCode.startsWith(str)) {
										bFound = true;
										break;
									}
								}
								
								if (bFound) {
									cvTemp.clear();
									cvTemp.put(code, sCode);
									cvTemp.put(Database.COL_DESC, sValue);
									db.insert(table, null, cvTemp);
								}
							} else {
								cvTemp.clear();
								cvTemp.put(code, sCode);
								cvTemp.put(Database.COL_DESC, sValue);
								db.insert(table, null, cvTemp);
							}
						}
					}
					
					xpp.next();
					iEvent = xpp.getEventType();
				}
			} catch (Exception e) {

			}
		}
		// =========================================================================
		private ArrayList<ProvinceItem> readFromXml(int id) {
			ArrayList<ProvinceItem> alTemp = new ArrayList<ProvinceItem>();
			
			try {
				Resources res = m_cContext.getResources();
				XmlResourceParser xpp = res.getXml(id);
				int iEvent;
				String sCurrNode = "";
				String sCode = "";
				String sValue = "";
				
				xpp.next();
				iEvent = xpp.getEventType();
				while (iEvent != XmlPullParser.END_DOCUMENT) {
					if (iEvent == XmlPullParser.START_TAG) {
						sCurrNode = xpp.getName();
						if (sCurrNode.equals("string")) {
							sCode = xpp.getAttributeValue(0);
						}
					}
					else if (iEvent == XmlPullParser.TEXT) {
						if (sCurrNode.equals("string")) {
							sValue = xpp.getText();
							
							alTemp.add(new ProvinceItem(new CodeDescPair(sCode, sValue)));
						}
					}
					
					xpp.next();
					iEvent = xpp.getEventType();
				}
			} catch (Exception e) {
				
			}
			
			return alTemp;
		}
	 // =========================================================================
 	 // TODO Final Codes
	}
}
