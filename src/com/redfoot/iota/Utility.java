package com.redfoot.iota;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

public class Utility {
	
	private static boolean m_bDialogResult;
	
	public static boolean getDialogResult() { return m_bDialogResult; }
	public static void setDialogResult(boolean value) { m_bDialogResult = value; }

	public static void alertNoGPS(final Context context) {
    	new AlertDialog.Builder(context)
    	.setTitle(R.string.alert)
    	.setMessage(R.string.nogpslock)
		.setNeutralButton(R.string.OK, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Utility.setDialogResult(false);
				//context.sendBroadcast(new Intent(context.getPackageName() + MainApp.INTENT_DIALOG_CLOSED));
				arg0.dismiss();
			} 
		})
		.show();
	}
	
}
