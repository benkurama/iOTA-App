package com.redfoot.iota;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CrashPageAct extends Activity{
// =========================================================================
// TODO Variables
// =========================================================================	
	private EditText etCrashText;
// =========================================================================
// TODO Activity Life Cycle
// =========================================================================		
   /** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_crash_page);
		
		SetupWidgets();
	}
// =========================================================================
// TODO onClick View
// =========================================================================
	public void onMainBack(View v){
		startActivity(new Intent(this, MainMenu.class));
	}
	// =========================================================================
	public void onExit(View v){
		this.finish();
	}
// =========================================================================
// TODO Main Functions
// =========================================================================
   public void SetupWidgets(){
   	   	
	   etCrashText = (EditText)findViewById(R.id.etCrashText);
	   
	   String val = getIntent().getStringExtra("error");
	   
	   etCrashText.setText(val);
   }

// =========================================================================
// TODO Implementation
// =========================================================================

// =========================================================================
// TODO Sub Functions
// =========================================================================

// =========================================================================
// TODO Inner Class
// =========================================================================

// =========================================================================
// TODO Final Destination
}
