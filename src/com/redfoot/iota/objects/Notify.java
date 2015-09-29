package com.redfoot.iota.objects;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Notify {
	
	private NotificationManager notification;
	
	public static final int NOTIFICATION_ID = 1;
	public static final int UPLOADING = 1;
	public static final int FINISHED = 2;
	
	public Notify(Context core, String title, String mainTitle, String desc, int type){
		
		notification = (NotificationManager) core.getSystemService(Context.NOTIFICATION_SERVICE);
		
		String MyNotificationTitle = mainTitle;
        String MyNotificationText  = desc;
		
        Notification mNotification = null;
        PendingIntent StartIntent = null;
        
        if(type == UPLOADING){
        	
        	mNotification = new Notification(android.R.drawable.stat_sys_upload, title, System.currentTimeMillis() );
        	StartIntent = PendingIntent.getActivity(core.getApplicationContext(),0,new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (type == FINISHED){
        	
        	mNotification = new Notification(android.R.drawable.stat_sys_upload_done, title, System.currentTimeMillis() );
        	StartIntent = PendingIntent.getActivity(core.getApplicationContext(),0,new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        	
        	mNotification.flags |= Notification.DEFAULT_LIGHTS |  Notification.FLAG_AUTO_CANCEL;
        }

        mNotification.setLatestEventInfo(core.getApplicationContext(), MyNotificationTitle, MyNotificationText, StartIntent);
        
        notification.notify(NOTIFICATION_ID , mNotification); 
	}
	
	public void cancelNotify(){
		notification.cancel(NOTIFICATION_ID);
	}

}
