package com.redfoot.iota.utils;

import android.os.Environment;

public class Configs {
	
public static final String TAG = "iOTA";

public static final String IMAGE_PATH = Environment.getExternalStorageDirectory()+"/iOTA_Pics/";
public static final String DIRECTORY_PATH = Environment.getExternalStorageDirectory()+"/iOTA_Pics";

public static final String PHOTO_EXT = "/SyncMobilePhoto.aspx";

	private Configs()
	{
		super();
	}
}