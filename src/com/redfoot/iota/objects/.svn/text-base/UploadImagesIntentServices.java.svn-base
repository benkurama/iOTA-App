package com.redfoot.iota.objects;

import java.io.File;

import com.redfoot.iota.MainApp;
import com.redfoot.iota.utils.Configs;
import com.redfoot.iota.utils.JsonParser;
import com.redfoot.iota.utils.Utils;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Base64;

public class UploadImagesIntentServices extends IntentService{
 // =========================================================================
 // TODO Variables
 // =========================================================================
	public static final String TRANSACTION_DONE = "com.redfoot.iota.TRANSACTION_DONE";
	
	public static final String STORENAME = "storename";
	public static final String PHOTO = "photo";
	public static final String RESULT = "result";
// =========================================================================
	public UploadImagesIntentServices() {
        super("UploadImagesIntentServices");
    }
// =========================================================================
	@Override
	public void onCreate() {
		super.onCreate();
	}
// =========================================================================
	@Override
	protected void onHandleIntent(Intent intent) {

		String[] filenames = intent.getExtras().getStringArray(STORENAME);
		
		boolean isUpload = false;

		for (int x = 0; x < filenames.length; x++) {
			
			String[] str = filenames[x].split("_");
			String filename = str[0];
			
			String[] params = str[1].split("-");
			String map = params[0];
			String status = params[1];
			String[] strOne = params[2].split("\\.");
			String storename = strOne[0];
			//
			String STATS = "";
			if(MainApp.DEBUG == 1){
				STATS = "P";
			} else{
				STATS = "S";
			}
			//
			if(status.equals(STATS)){
				
				Notify Notification = new Notify(this, storename + "  is uploading",	"Upload iOTA Pics", storename + "is Currently Uploading", Notify.UPLOADING);

				File image = new File(Configs.IMAGE_PATH + filenames[x]);
				byte[] bytes = Utils.imagesToBytes(image.getAbsolutePath());
				String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);

				JsonParser.sampleData(this,filename, encodedImage);
				
				SystemClock.sleep(1000);

				Notification.cancelNotify();
				// New Revision
				File renameFile = new File (Configs.IMAGE_PATH + filename + "_"+ map + "-U-"+ storename+".jpg");
				
				image.renameTo(renameFile);
				isUpload = true;
			}
		}
		//
		if(isUpload){
			notifyFinished("Upload");
			new Notify(this, "Uploading is Complete",	"Finished", "iOTA Latest Pics is Uploaded", Notify.FINISHED);
		} else {
			notifyFinished("exist");
		}
	}
// =========================================================================
	private void notifyFinished(String result){
		
	    Intent i = new Intent(TRANSACTION_DONE);
	    i.putExtra("result", result);
	   
	    sendBroadcast(i);
	}
}
