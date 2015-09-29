package com.redfoot.iota.objects;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumberWithCommaTextWatcher implements TextWatcher {
	
	private EditText et;
	private String current;
	
	public NumberWithCommaTextWatcher(EditText et){
		this.et = et;
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (!s.toString().equals(current)) {

			if (s.length() != 0) {
				
				et.removeTextChangedListener(this);
				String cleanString = s.toString().replaceAll(",", "");
				double parsed = Double.parseDouble(cleanString);
				// String formated =
				// NumberFormat.getCurrencyInstance().format((parsed/100));
				DecimalFormat formatter = (DecimalFormat) NumberFormat
						.getInstance(Locale.getDefault());
				DecimalFormatSymbols symbols = formatter
						.getDecimalFormatSymbols();
				symbols.setGroupingSeparator(' ');
				String formated = formatter.format((parsed));
				current = formated;
				et.setText(formated);
				et.setSelection(formated.length());
				et.addTextChangedListener(this);
			}
		}
	}

}
