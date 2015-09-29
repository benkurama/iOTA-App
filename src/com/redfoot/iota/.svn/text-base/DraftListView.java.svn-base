package com.redfoot.iota;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.redfoot.iota.objects.DraftObj;
import com.redfoot.iota.utils.Utils;

public class DraftListView extends Activity{
// =========================================================================
// TODO Variables
// =========================================================================	
	private ListView lvListDraft;
	
	private ArrayList<HashMap<String,String>> ItemHash = new ArrayList<HashMap<String,String>>();
	private ArrayList<DraftObj> DraftList = new ArrayList<DraftObj>();
	
	private MainApp mainApp;
// =========================================================================
// TODO Activity Life Cycle
// =========================================================================
	
   /** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_draft_listview);
		
		SetupWidgets();
		
		callingList();
	}
// =========================================================================
// TODO onClick View
// =========================================================================
	public void onExit(View v){
		
		this.finish();
	}
// =========================================================================
// TODO Main Functions
// =========================================================================
   public void SetupWidgets(){
	   
	   setTitle("Draft Selection List");
   	   
	   mainApp = (MainApp) this.getApplication();
	   
	   lvListDraft = (ListView)findViewById(R.id.lvDraftlist);
	   lvListDraft.setOnItemClickListener(DraftSelectedItem);
	   lvListDraft.setOnItemLongClickListener(DraftLongSelectedItem);
	   
   }
// =========================================================================
	@SuppressLint("HandlerLeak")
	private void callingList() {
		
		DraftList.clear();

		final ProgressDialog dialog = ProgressDialog.show(this,
				"Please Wait..", "Processing...", true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				dialog.dismiss();
				// /// 2nd if the load finish -----
				processToListview();
				// /// -----
			}
		};
		Thread checkUpdate = new Thread() {
			public void run() {
				// / 1st main activity here -----
				DraftList = mainApp.getDatabase().getAllDrafts();
				// //// -----
				handler.sendEmptyMessage(0);
			}
		};
		checkUpdate.start();
	}
// =========================================================================
   private void processToListview(){
	   
	   HashMap<String, String> ItemMap;
	   
	   ItemHash.clear();
	   

		for (DraftObj ValueStr : DraftList) {

			ItemMap = new HashMap<String, String>();
			
			String[] splitVal = ValueStr.DraftContent.split("/");
			
			String correctMap = getCorrectMap(splitVal[0]);
			
			ItemMap.put("Map", correctMap);
			ItemMap.put("Storename", splitVal[1]);
			ItemMap.put("Date", ValueStr.Date);

			ItemHash.add(ItemMap);
		}

		String[] DraftColumns = new String[] { "Map","Storename","Date" };
		int[] DraftControls = new int[] { R.id.tvDraftMap , R.id.tvDraftStorename, R.id.tvDraftDate};

		ListAdapter Adapter = new SimpleAdapter(this, ItemHash,	R.layout.draft_rows, DraftColumns, DraftControls);
		
		//
		View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_view_footer, null, false);
		
		if(DraftList.size() >= 10){
			
			//lvListDraft.addHeaderView(footerView);
		}
		//
		lvListDraft.setAdapter(Adapter);
		//lvListDraft.addFooterView(footerView);
   }
// =========================================================================
// TODO Implementation
// =========================================================================
	private OnItemClickListener DraftSelectedItem = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			preserveDraft(pos);
		}
	};
// =========================================================================
	private OnItemLongClickListener DraftLongSelectedItem = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long arg3) {

			deleteDraft(pos);
			return true;
		}
	};
// =========================================================================
// TODO Sub Functions
// =========================================================================
   private String getCorrectMap(String str){
	   String result = "";
	   
	   if (str.equals("NDSOMAP4")){
		   result = "NDSO";
	   } else if(str.equals("NSMAP4")){
		   result = "NS";
	   } else if (str.equals("NFMAP2")){
		   result = "FMCG";
	   }
	   
	   return result;
   }
   // =========================================================================
   private void preserveDraft(int pos){
	   
	   int id = DraftList.get(pos).ID;
	   
	   Intent intent = new Intent(this, GeneralTradeAct.class);
	   intent.putExtra("DraftID", id);
	   
	   startActivity(intent);
	   
	   this.finish();
   }
   // =========================================================================
   private void deleteDraft(final int pos){
	   
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Draft Delete");
		alert.setMessage("Are you sure you wanna delete this item?");

		alert.setPositiveButton("Yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int id = DraftList.get(pos).ID;
				// detect if draft has image and delete it with full row data
				String imagepath = mainApp.getDatabase().checkImagePath(id);
				if(!imagepath.equals("null")){
					File deleteFile = new File(imagepath);
					deleteFile.delete();
				}
				//
				mainApp.getDatabase().deleteSaveDraft(id);
				callingList();
			}
		});

		alert.setNegativeButton("No", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alert.show();
	   
   }
// =========================================================================
// TODO Inner Class
// =========================================================================

// =========================================================================
// TODO Final Destination
}
