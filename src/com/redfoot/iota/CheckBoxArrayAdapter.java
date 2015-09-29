package com.redfoot.iota;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class CheckBoxArrayAdapter extends ArrayAdapter<ProvinceItem> {

	private final Activity m_cContext;
	private final ArrayList<ProvinceItem> m_alItems;

	public CheckBoxArrayAdapter(Activity context, ArrayList<ProvinceItem> alItems) {
		super(context, R.layout.provincelistitem, alItems);
		
		m_cContext = context;
		m_alItems = alItems;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vTemp = null;
		
		if (convertView == null) {
			LayoutInflater inflater = m_cContext.getLayoutInflater();

			vTemp = inflater.inflate(R.layout.provincelistitem, null);
			final CheckBox cb = (CheckBox) vTemp.findViewById(R.id.d_cbCheck);
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					ProvinceItem piTemp = (ProvinceItem)cb.getTag();
					piTemp.setSelected(buttonView.isChecked());
				}
			});
			cb.setTag(m_alItems.get(position));
		} else {
			vTemp = convertView;
			CheckBox cb = (CheckBox) vTemp.findViewById(R.id.d_cbCheck);
			cb.setTag(m_alItems.get(position));
		}
		
		CheckBox cbTemp = (CheckBox) vTemp.findViewById(R.id.d_cbCheck);
		cbTemp.setText(((ProvinceItem)cbTemp.getTag()).getItem().Description);
		cbTemp.setChecked(((ProvinceItem)cbTemp.getTag()).isSelected());
		
		return vTemp;
	}	

}
