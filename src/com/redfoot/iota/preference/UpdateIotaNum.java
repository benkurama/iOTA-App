package com.redfoot.iota.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class UpdateIotaNum extends EditTextPreference{
	// =========================================================================
	// TODO Constructors
	// =========================================================================
	public UpdateIotaNum(Context context) {
		super(context);
	}

	public UpdateIotaNum(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public UpdateIotaNum(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	// =========================================================================
	// TODO Variables
	// =========================================================================
	@Override
    public void onClick(DialogInterface dialog, int which) {
		
        super.onClick(dialog, which);
    }
	// =========================================================================
	// TODO Overrides
	// =========================================================================

}
