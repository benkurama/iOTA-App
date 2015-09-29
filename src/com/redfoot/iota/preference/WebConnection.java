package com.redfoot.iota.preference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.redfoot.iota.MainApp;
import com.redfoot.iota.utils.Configs;
import com.redfoot.iota.utils.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.widget.EditText;
/** HOTSPOT BEGIN*/
public class WebConnection extends EditTextPreference{
	 // =========================================================================
 	 // TODO Constructors
 	 // =========================================================================
	public WebConnection(Context context) {
        super(context);
    }
    public WebConnection(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public WebConnection(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
     // =========================================================================
 	 // TODO Variables
 	 // =========================================================================
    private int resultCode = 0;
     // =========================================================================
 	 // TODO Overrides
 	 // =========================================================================
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
        case DialogInterface.BUTTON_POSITIVE:
        //Utils.MessageBox(getContext(), "OK");
        EditText et = getEditText();
        String val = et.getText().toString();
        
        val += Configs.PHOTO_EXT;
        
        validateURL(val);
        break;
        case DialogInterface.BUTTON_NEGATIVE:
        break;      
        }
        super.onClick(dialog, which);
    }
     // =========================================================================
 	 // TODO Main Functions
 	 // =========================================================================
    @SuppressLint("HandlerLeak")
	private void validateURL(final String URL){
    	
    	final ProgressDialog dialog = ProgressDialog.show(getContext(), "Please Wait..","Validating URL...", true);
		final Handler handler = new Handler() {
		   public void handleMessage(Message msg) {
		      dialog.dismiss();
		      ///// 2nd if the load finish -----
		      getResult();
			  ///// -----
		      }
		};
		Thread checkUpdate = new Thread() {  
			public void run() {	
			  /// 1st main activity here -----
				connectToHttp(URL);
			  ////// -----
		      handler.sendEmptyMessage(0);				      
		      }
		};
		checkUpdate.start();
    }
     // =========================================================================
 	 // TODO Sub Function
 	 // =========================================================================
    private void connectToHttp(String urlLink){
    	
		URL url;
		try {
			url = new URL(urlLink);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			// huc.setRequestMethod("HEAD");
			huc.setConnectTimeout(5000);
			
			resultCode = huc.getResponseCode();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    // =========================================================================
    private void getResult(){
    	
    	if (resultCode == 200) {
			Utils.MessageToast(getContext(), "URL is Valid");
			MainApp.setUrlValidation(getContext(), true);
		} else {
			Utils.MessageToast(getContext(), "URL is Invalid");
			MainApp.setUrlValidation(getContext(), false);
		}
    }
     // =========================================================================
 	 // TODO Final
}
/** HOTSPOT END*/