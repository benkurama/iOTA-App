package utilities.generaltrade;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class IntoNumber implements TextWatcher{
	
	private EditText et;
	
	public IntoNumber(EditText et){
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
		
		if(s.length() != 0){
			//
			et.removeTextChangedListener(this);
			int number = Integer.parseInt(s.toString());
			String numberStr = String.valueOf(number);
			et.setText(numberStr);
			et.setSelection(numberStr.length());
			//
			et.addTextChangedListener(this);
		}
	}

}
