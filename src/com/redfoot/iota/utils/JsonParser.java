package com.redfoot.iota.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.redfoot.iota.MainApp;
import com.redfoot.iota.objects.SampObj;


public class JsonParser {
	// =========================================================================
	// TODO Message Box | Page: Universal
	// =========================================================================
	@SuppressWarnings("deprecation")
	public static void sampleData(Context core,String name, String photo) {
		
		//String URL = "http://192.168.1.20/Default.aspx";
		//String URL = "http://192.168.1.20/PhotoSync/Default.aspx";
		String URL = "";
		if(MainApp.DEBUG == 0){
			URL = MainApp.getWebconfig(core) +Configs.PHOTO_EXT;
		} else {
			URL = "http://192.168.1.20/PhotoSync/Default.aspx";
		}
		
		//String URL = "http://192.168.1.168/UATSecurity/SyncMobilePhoto.aspx";
		
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			entity.addPart("Storename", new StringBody(name));
			entity.addPart("Photo", new StringBody(photo));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		JsonParsing jsonParser = new JsonParsing();
		JSONObject json = jsonParser.getJSONFromUrl(URL, "POST", null, entity);
		
		try {
			if (json != null) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 // =========================================================================
	 // TODO Final Destination
}
