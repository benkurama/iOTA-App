package com.redfoot.iota.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.channels.AsynchronousCloseException;
import java.security.AccessControlException;
import java.security.acl.AclNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.auth.AuthenticationException;

import android.accounts.AccountsException;
import android.accounts.AuthenticatorException;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.os.Build;
import android.os.Environment;
import android.test.AssertionFailedError;
import android.widget.RemoteViews.ActionException;

import com.redfoot.iota.CrashPageAct;
import com.redfoot.iota.MainApp;
import com.redfoot.iota.utils.Utils;

import dalvik.system.AllocationLimitError;

public class MyUncaughtException implements Thread.UncaughtExceptionHandler 
{
	
	private final String LINE_SEPARATOR = "\n";
	
	private Context Core;
	public MyUncaughtException(Context core){
		Core = core;
	}
	// =========================================================================
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 * List of Most Used Error Classes and Exceptions:
	 * 
	 *	 ArrayIndexOutOfBoundsException
	 * 	 ClassNotFoundException
	 *   IndexOutOfBoundsException
	 *   NullPointerException
	 *   NumberFormatException
	 *   RuntimeException
	 *   
	 *   OutOfMemoryError
	 *   ActivityNotFoundException
	 *   AndroidsecurityException
	 *   StackOverflowError
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		String errorReport = ErrorInfo(ex);
		// --
		generateErrorLogs("Logs.txt", errorReport);
		// ---
		try {
			Thread.sleep(1000);
			
			// if debug mode is on, prompt the error logs
			if (MainApp.getDebug(Core)) {
				
				Intent i = new Intent(Core, CrashPageAct.class);
				i.putExtra("error", errorReport);
				Core.startActivity(i);
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	// =========================================================================
	private String ErrorInfo(Throwable ex){
		
		StringWriter stackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(stackTrace));
		
		StringBuilder errorReport = new StringBuilder();
		
		
		errorReport.append("************ TYPE OF ERROR ************\n");
		errorReport.append(createDateTimeFormat());
		errorReport.append(getActivityName());
		errorReport.append(typeError(ex));
		
		errorReport.append("************ CAUSE OF ERROR ************\n\n");
		errorReport.append(stackTrace.toString());
		
		//region Reserved Codes
//		errorReport.append("\n************ DEVICE INFORMATION ***********\n");
//        errorReport.append("Brand: ");
//        errorReport.append(Build.BRAND);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Device: ");
//        errorReport.append(Build.DEVICE);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Model: ");
//        errorReport.append(Build.MODEL);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Id: ");
//        errorReport.append(Build.ID);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Product: ");
//        errorReport.append(Build.PRODUCT);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("\n************ FIRMWARE ************\n");
//        errorReport.append("SDK: ");
//        errorReport.append(Build.VERSION.SDK);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Release: ");
//        errorReport.append(Build.VERSION.RELEASE);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Incremental: ");
//        errorReport.append(Build.VERSION.INCREMENTAL);
//        errorReport.append(LINE_SEPARATOR);
		//endregion
		
		errorReport.append(">>>>>>************ END OF THE LINE ************<<<<<\n\n");
		
        return errorReport.toString();
	}
	 // =========================================================================
	private String typeError(Throwable ex){
		String type = "";
		
		if (ex.getClass().equals(OutOfMemoryError.class)) {
			type += "OutOfMemoryError\n\n";
		} else if(ex.getClass().equals(ActivityNotFoundException.class)){
			type += "ActivityNotFoundException\n\n";
		} else if(ex.getClass().equals(SecurityException.class)){
			type += "SecurityException\n\n";
		} else if(ex.getClass().equals(StackOverflowError.class)){
			type += "StackOverflowError\n\n";
		} else if(ex.getClass().equals(ArrayIndexOutOfBoundsException.class)){
			type += "ArrayIndexOutOfBoundsException\n\n";
		} else if(ex.getClass().equals(ClassNotFoundException.class)){
			type += "ClassNotFoundException\n\n";
		} else if(ex.getClass().equals(IndexOutOfBoundsException.class)){
			type += "IndexOutOfBoundsException\n\n";
		} else if(ex.getClass().equals(NullPointerException.class)){
			type += "NullPointerException\n\n";
		} else if(ex.getClass().equals(NumberFormatException.class)){
			type += "NumberFormatException\n\n";
		} else if(ex.getClass().equals(RuntimeException.class)){
			type += "RuntimeException\n\n";
		}
		
		return type;
	}
	// =========================================================================
	private void generateErrorLogs(String filename, String contentBody){
		
		try
	    {
	        File root = new File(Environment.getExternalStorageDirectory(), "Errorlogs");
	        if (!root.exists()) {
	            root.mkdirs();
	        }
	        
	        File gpxfile = new File(root, filename);
	        
	        double bytes = gpxfile.length();
	        double kilobytes = (bytes / 1024);
	        int byteKilo = (int) Math.ceil(kilobytes);
	        
	        Utils.iLogCat("bytes", bytes+"");
	        Utils.iLogCat("kilobytes", kilobytes+"");
	        Utils.iLogCat("Round of Kilobytes", byteKilo+"");
	        
	        boolean isAppend = byteKilo < 200 ? true: false ;
	        
	        FileWriter writer = new FileWriter(gpxfile, isAppend);
	        
	        writer.append(contentBody);
	        writer.flush();
	        writer.close();
	    }
	    catch(IOException e)
	    {
	         //e.printStackTrace();
	    }
	}
	// =========================================================================
	@SuppressLint("SimpleDateFormat")
	private String createDateTimeFormat(){
		
		String timeStamp = new SimpleDateFormat("MM.dd.yyyy:HH.mm").format(new Date());
		
		return "Date: " + timeStamp +"\n";
	}
	// =========================================================================
	private String getActivityName(){
		
		String name = Core.getClass().getSimpleName();
		
		return "Actvity Name: " +name+ "\n";
	}
	 // =========================================================================
 	 // TODO Final
}
