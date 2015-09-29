package utilities.generaltrade;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.redfoot.iota.MainApp;
import com.redfoot.iota.R;

public class DialogPrompt {
	
	public static class IotaNumber{
		
		private static EditText numIota;
		private static Dialog dialog;
		private static Context context;
		private static MainApp mainApi;
		
		public static Dialog Edit(Context core, MainApp mainApp){
			//
			context = core;
			mainApi = mainApp;
			dialog = new Dialog(core);
			dialog.setContentView(R.layout.dialogiotanum);
			dialog.setTitle(R.string.question);
			//
			Button btnOK = (Button)dialog.findViewById(R.id.d_btnOK);
			Button btnCancel = (Button)dialog.findViewById(R.id.d_btnCancel);
			numIota = (EditText)dialog.findViewById(R.id.d_etMobtel);
			
			btnOK.setOnClickListener(onClickEdit);
			btnCancel.setOnClickListener(onClickCancel);
			
			return dialog;
		}
		
		private static OnClickListener onClickEdit = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String number = numIota.getText().toString();
				boolean valid = true;
				
				if(number.length() == 0){
					valid = false;
					noNumber();
				} else if(number.length() > 4 && number.length() < 11){
					valid = false;
					invalidNumber();
				} else if(number.length() > 4 && !number.startsWith("09")){
					valid = false;
					invalidNumber();
				} else {
					confirmation(number);
				}
				
				if(valid){
					mainApi.getDatabase().setIotaNum(number);
					dialog.dismiss();
				} else {
					
				}
			}
		};
		
		private static OnClickListener onClickCancel = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		};
		
		private static void noNumber(){
			//
			new AlertDialog.Builder(context)
			.setTitle(R.string.alert)
			.setMessage(R.string.NoIotaMobtel)
			.setNeutralButton(R.string.OK, null).show();
		}
		
		private static void confirmation(String number){
			//
			new AlertDialog.Builder(context)
			.setTitle(R.string.confirmation)
			.setMessage(context.getString(R.string.updatediotanum)
			.replace("@NUMBER", number))
			.setNeutralButton(R.string.OK, null).show();
		}
		
		private static void invalidNumber(){
			//
			new AlertDialog.Builder(context)
			.setTitle(R.string.alert)
			.setMessage(R.string.InvIotaMobtel)
			.setNeutralButton(R.string.OK, null).show();
		}
	}
}
