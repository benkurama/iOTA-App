package com.redfoot.iota.preference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.redfoot.iota.MainApp;
import com.redfoot.iota.MainMenu;
import com.redfoot.iota.R;
import com.redfoot.iota.utils.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

public class PreferrenceSettings extends PreferenceActivity{
	 // =========================================================================
 	 // TODO Variables
 	 // =========================================================================
	private SharedPreferences prefs=null;
	private int resultCode = 0;
	
	private boolean isValidIotaNum = true;
	
	@SuppressWarnings("deprecation")
	 // =========================================================================
	 // TODO Life Cycles
	 // =========================================================================
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferrence);
		//
		if(!MainApp.getOnline(this)){
			getPreferenceScreen().findPreference("webconfig").setEnabled(false);
		}
	}
// =========================================================================
	@Override
	public void onResume() {
		super.onResume();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(onChange);
	}
	// =========================================================================
	@Override
	public void onPause() {
		prefs.unregisterOnSharedPreferenceChangeListener(onChange);
		super.onPause();
	}
	 // =========================================================================
	 // TODO Overrides
	 // ========================================================================= 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			
			if(!isValidIotaNum){
				Utils.MessageBox(this, "Check your configuration if valid");
			}else {
				this.finish();
			}
			
			break;
		}
		return true;
	}
	// =========================================================================
 	 // TODO Implementations
 	 // =========================================================================
	SharedPreferences.OnSharedPreferenceChangeListener onChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
		public void onSharedPreferenceChanged(SharedPreferences prefs,	String key) {
			
			if(key.equals("webconfig")){
				
				String url = prefs.getString("webconfig", "null");
				//validateURL(url);
			} else if (key.equals("iotanumber")){
				
				String sNum = prefs.getString("iotanumber", "null");
				boolean Valid = true;
				
				if(sNum.length() == 0){
					Valid = false;
					new AlertDialog.Builder(PreferrenceSettings.this)
					.setTitle(R.string.alert)
					.setMessage(R.string.NoIotaMobtel)
					.setNeutralButton(R.string.OK, null)
					.show();
				} else if(sNum.length() < 4 || sNum.length() > 11){
					Valid = false;
					new AlertDialog.Builder(PreferrenceSettings.this)
					.setTitle(R.string.alert)
					.setMessage(R.string.InvIotaMobtel)
					.setNeutralButton(R.string.OK, null)
					.show();
				} else if (sNum.length() == 11 && !sNum.startsWith("09")){
					Valid = false;
		        	new AlertDialog.Builder(PreferrenceSettings.this)
		        	.setTitle(R.string.alert)
		        	.setMessage(R.string.InvIotaMobtel)
		        	.setNeutralButton(R.string.OK, null)
					.show();
				}
				//
				if(Valid){
					((MainApp)PreferrenceSettings.this.getApplication()).getDatabase().setIotaNum(sNum);
					isValidIotaNum = true;
				} else {
					isValidIotaNum = false;
				}
				//
			}
			
		}
	};
	// =========================================================================
	@SuppressLint("HandlerLeak")
	private void validateURL(final String urlLink){
		
		URL url1;
		/** HOTSPOT BEGIN*/
		try {
			url1 = new URL(urlLink);
			HttpURLConnection huc = (HttpURLConnection) url1.openConnection();
			// huc.setRequestMethod("HEAD");
			resultCode = huc.getResponseCode();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (resultCode == 200) {
			Utils.MessageToast(getBaseContext(), "URL is Valid");
			
		} else {
			Utils.MessageToast(getBaseContext(), "URL is Invalid");
		}
		/** HOTSPOT END*/
		setTitle("iOTA GPS Map");
	}
	 // =========================================================================
 	 // TODO Final
}