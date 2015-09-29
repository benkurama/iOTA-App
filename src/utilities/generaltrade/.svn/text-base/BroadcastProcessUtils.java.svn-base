package utilities.generaltrade;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

public class BroadcastProcessUtils {

	public static void doResultOK(Context core, ContentValues cv, int index, int size){
		
		core.getContentResolver().insert(Uri.parse("content://sms/sent"), cv);
		Toast.makeText(core, "Message Part "+index+" of "+size+" is Sent", Toast.LENGTH_SHORT).show();
		//
	}
	
	public static void doErrorGenericFailure(Context core, ContentValues cv, int index, int size){
		
		core.getContentResolver().insert(Uri.parse("content://sms/failed"), cv);
		Toast.makeText(core, "Error sending message for "+index+" of "+size+". Please make sure you have enough load", 
				Toast.LENGTH_SHORT).show();
	}
	
	public static void doErrorNoService(Context core, ContentValues cv, int index, int size){
		
		core.getContentResolver().insert(Uri.parse("content://sms/failed"), cv);
		Toast.makeText(core, "No network coverage for Message part of "+index+" of "+size+"", 
				Toast.LENGTH_SHORT).show();
	}
	
	public static void doErrorRadioOff(Context core, ContentValues cv, int index, int size){
		
		core.getContentResolver().insert(Uri.parse("content://sms/failed"), cv);
		Toast.makeText(core, "No radio signal for Message part of "+index+" of "+size+"", 
				Toast.LENGTH_SHORT).show();
	}
}
