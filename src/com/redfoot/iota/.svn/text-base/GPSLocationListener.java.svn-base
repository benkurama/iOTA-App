package com.redfoot.iota;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;

public class GPSLocationListener implements LocationListener {

	private Context m_cContext;
	private Context m_dContext;

	public void setContext(Context context) {
		m_cContext = context;
	}
	
	public void setDialogContext(Context context){
		m_dContext = context;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		MainApp maTemp = (MainApp)m_cContext;
		
		if (arg0.getProvider().matches(LocationManager.GPS_PROVIDER)) {
			maTemp.setGPSStatus(LocationProvider.AVAILABLE);
			maTemp.setGPSLat(arg0.getLatitude());
			maTemp.setGPSLong(arg0.getLongitude());
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		AlertDialog.Builder adb = new AlertDialog.Builder(m_dContext);
		
		adb.setMessage(R.string.nogps);
		adb.setCancelable(false);
		adb.setNeutralButton(R.string.OK, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(m_dContext.getClass().getName().equals(MainApp.SETUP_CLASS)){
					//((Activity)m_dContext).finish();
				} else if (m_dContext.getClass().getName().equals(MainApp.MAIN_MENU_CLASS)){
					//((Activity)m_dContext).finish();
				}
				
				m_dContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				
				dialog.dismiss();
			}
			
		});
		adb.create().show();
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		MainApp maTemp = (MainApp)m_cContext;
		
		if (arg0.matches(LocationManager.GPS_PROVIDER)) {
			maTemp.setGPSStatus(arg1);
		}
	}

}
