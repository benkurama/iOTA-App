package com.redfoot.iota.objects;

import java.util.Observer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetBroadcast extends BroadcastReceiver{
 // =========================================================================
 // TODO Variables
 // =========================================================================
	private Context Core;
	private Activity Act;
	
	NetObservable iotaObservale = new NetObservable();
 // =========================================================================
 // TODO Constructor
 // =========================================================================
	public NetBroadcast(Context core, Activity act){
		
		this.Core = core;
		StartBroadcast();
		this.Act = act;
		iotaObservale.addObserver((Observer)this.Act);
	}
 // =========================================================================
 // TODO Overrides
 // =========================================================================
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(checkWifiConnection()){
			
			if(isInternetOn()){
				iotaObservale.setValue(1);
				iotaObservale.Commit();
			}else{
				iotaObservale.setValue(2);
				iotaObservale.Commit();
			}
			
		} else {
			iotaObservale.setValue(0);
			iotaObservale.Commit();
		}
	}
 // =========================================================================
 // TODO Main Functions
 // =========================================================================
	public void StartBroadcast(){
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		Core.registerReceiver(this, filter);
	}
	// =========================================================================
	public boolean checkWifiConnection(){
		
		boolean haveConnectedWifi,haveConnectedMobile;
		haveConnectedWifi = haveConnectedMobile = false;
	    ConnectivityManager cm = (ConnectivityManager) Core.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
	// =========================================================================
	public final boolean isInternetOn(){
		
		ConnectivityManager connec = (ConnectivityManager) Core
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		}

		return false;
	}
	 // =========================================================================
 	 // TODO Final Code
}
