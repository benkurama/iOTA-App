package com.redfoot.iota.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class Utils {
	 // =========================================================================
 	 // TODO Main Utilities
 	 // =========================================================================
	public static void MessageToast(Context core, String msg) {

		Toast.makeText(core, msg, Toast.LENGTH_LONG).show();
	}
	// =========================================================================
	public static void MessageBox(Context core, String msg) {

		AlertDialog.Builder alert = new AlertDialog.Builder(core);
		alert.setTitle(msg);
		// alert.setMessage("");
		alert.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alert.show();
	}
	// =========================================================================
	public static void iLogCat(String txt1, String txt2) {
		Log.i(txt1, txt2);
	}
	// =========================================================================
	public static String trimDateFromImage(String imagePath){
		//region Old Codes
//		String filename = new File(imagePath).getName();
//		String[] splitFile = filename.split("_");
//		String[] date = splitFile[2].split("\\.");
//		String trimDate = "";
//		
//		for(int x = 0; x < date.length; x++){
//			trimDate += date[x];
//		}
//		
//		String[] dateTrim = trimDate.split("-");
//		
//		String result = dateTrim[0] + dateTrim[1];
		//endregion
		
		// New Revision
		String filename = new File(imagePath).getName();
		String[] file = filename.split("_");
		
		String result = file[0];
		
		return result;
	}
	 // =========================================================================
 	 // TODO File Utilities
 	 // =========================================================================
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	// =========================================================================
	@SuppressLint("SimpleDateFormat")
	public static File createImageFile(Context core, String mapcode, String storename)throws IOException{
		 // Create an image file name
		String MapCode = "";
		
		if(mapcode.equals("NDSOMAP4")){
			MapCode = "NDSO";
		} else if (mapcode.equals("NSMAP4")){
			MapCode = "NS";
		} else if (mapcode.equals("NFMAP2")){
			MapCode = "NF";
		}
		
		// New Revision
		String timeStamp = new SimpleDateFormat("MMddyyyyHHmmssSSS").format(new Date());
		String imageFileName = timeStamp +"_"+ MapCode +"-P-"+ storename;
	    
	    File storageDir = null;
	    
	    if(Utils.isExternalStorageReadable()){// detect if SDCard is Available
	    	
	    	storageDir = new File(Environment.getExternalStorageDirectory()+"/iOTA_Pics");
	    	if(!storageDir.exists()){
		    	storageDir.mkdir();
		    }
	    } else {
	    	 storageDir = new File (core.getFilesDir()+"/iOTA_Pics");
	    	 if(!storageDir.exists()){
			    	storageDir.mkdir();
			    }
	    }
	    //
	    File image = File.createTempFile(
	        "temp", 		 /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );
	    //
	    File finalImage = new File(
	    		storageDir.toString()+"/"+imageFileName+".jpg");

	    storageDir.renameTo(finalImage);
	    image.delete();
	    
	    return finalImage;
	}
	// =========================================================================
	public static Bitmap setCustomizeImage(ImageView img, String imgPath) {
	    // Get the dimensions of the View 
		/** HOTSPOT BEGIN*/
	    int targetW = img.getWidth();
	    int targetH = img.getHeight();
	    
	    // Get the dimensions of the bitmap 
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imgPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(imgPath, bmOptions);
	    
	    // try
	    OutputStream stream;
		try {
			stream = new FileOutputStream(imgPath);
			bitmap.compress(CompressFormat.JPEG, 100, stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    //
	    /** HOTSPOT END*/
	    //region Saving Bitmap icon to sdcard icon folder RESERVED CODES FOR CREATING THUMBNAILS
//	    String root = Environment.getExternalStorageDirectory().toString();
//	    File myDir = new File(root + "/iOTA_Thumbs");    
//	    
//	    if(!myDir.exists()){
//	    	myDir.mkdirs();
//	    }
//	    
//	    String fname = new File(imgPath).getName();
//	    
//	    File file = new File (myDir, fname);
//	    
//	    //if (file.exists ()) file.delete (); 
//	    try {
//	           FileOutputStream out = new FileOutputStream(file);
//	           bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//	           out.flush();
//	           out.close();
//
//	    } catch (Exception e) {
//	           e.printStackTrace();
//	    }
	    //endregion
	    return bitmap;
	}
	// =========================================================================
	public static byte[] imagesToBytes(String ImagePath) {

		File file = new File(ImagePath);
		byte[] bytes = null;

		try {

			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] buf = new byte[1024];

			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}

			bytes = bos.toByteArray();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bytes;
	}
	 // =========================================================================
 	 // TODO Phone Access Utilities
 	 // =========================================================================
	public static String getSimNumber(Context core){
		
		TelephonyManager telemamanger = (TelephonyManager) core.getSystemService(Context.TELEPHONY_SERVICE);
		String getSimNumber = telemamanger.getLine1Number();
		return getSimNumber;
	}
	 // =========================================================================
 	 // TODO Network Utilities
 	 // =========================================================================
	public static boolean hasInternetConnection(Context core)
	{
	    ConnectivityManager cm = (ConnectivityManager) core.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiNetwork != null && wifiNetwork.isConnected())
	    {
	        return true;
	    }
	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null && mobileNetwork.isConnected())
	    {
	        return true;
	    }
	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    if (activeNetwork != null && activeNetwork.isConnected())
	    {
	        return true;
	    }
	    return false;
	}
	 // =========================================================================
 	 // TODO Final Codes
}
