package utilities.generaltrade;

import java.util.ArrayList;

import com.redfoot.iota.CodeDescPair;
import com.redfoot.iota.MainApp;
import com.redfoot.iota.objects.Enums;

import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Utilities {

	public static boolean checkEstablishment(ListView lvNearEstablishment) {

		SparseBooleanArray checked = lvNearEstablishment.getCheckedItemPositions();

		for (int i = 0; i < checked.size() - 1; i++) {
			if (checked.valueAt(i)) {
				return true;
			}
		}

		return false;
	}
	
	public static String getSelectedAdapter(ArrayList<CodeDescPair> codeDescPair, Spinner dropdown){
		//
		String selectedCode = "";
		
		for (CodeDescPair cdp : codeDescPair) {
			
			String sel = cdp.Description;
			
			if (sel.equalsIgnoreCase(dropdown.getSelectedItem().toString())) {
				selectedCode = cdp.Code;
				break;
			}
		}
		//endregion
		return selectedCode;
	}

	public static String getSelHowOftenEnums(String compare){
		
		String code = "";
		
		for (int x = 0; x < Enums.HowOften.values().length; x++) {

			if (Enums.HowOften.values()[x].equalsName(compare)) {
				code = Enums.HowOften.values()[x].toID();
			}
		}
	
		return code;
	}

	public static String getSelSellingSimEnums(String compare) {
		//region
		String code = "";

		for (int x = 0; x < Enums.SellingSim.values().length; x++) {

			if (Enums.SellingSim.values()[x].equalsName(compare)) {
				code = Enums.SellingSim.values()[x].toID();
			}
		}
		//endregion
		return code;
	}
	
	public static String getSelSellingLoadEnums(String compare) {
		//region
		String code = "";

		for (int x = 0; x < Enums.SellingLoad.values().length; x++) {

			if (Enums.SellingLoad.values()[x].equalsName(compare)) {
				code = Enums.SellingLoad.values()[x].toID();
			}
		}
		//endregion
		return code;
	}
	
	public static String detectNull(String value){
		String val = "null";
		
		if (value.length() != 0){
			val = value;
		}
		
		return val;
	}
	
	public static String convertNullToBlank(String str) {

		String val = "";

		if (!str.equals("null")) {
			val = str;
		}

		return val;
	}
	
	public static int getPositionByCode(ArrayList<CodeDescPair> codeDescPair, Spinner spin, String code){
		
		String selectedStr = "";

		for (CodeDescPair cdp : codeDescPair) {

			String sel = cdp.Code;

			if (sel.equalsIgnoreCase(code)) {
				selectedStr = cdp.Description;
				break;
			}
		}
		
		ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter();
		int spinnerPosition = myAdap.getPosition(selectedStr);
		
		return spinnerPosition;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int getPositionSellingloadEnums(String str, Spinner spin){
		
		String resultVal = "";

		for (int x = 0; x < Enums.SellingLoad.values().length; x++) {

			if (Enums.SellingLoad.values()[x].toID().equals(str)) {
				resultVal = Enums.SellingLoad.values()[x].toString();
			}
		}

		ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter();
		int spinnerPosition = myAdap.getPosition(resultVal);
		
		return spinnerPosition;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int getPositionHowOftenEnums(String str, Spinner spin){
		
		String resultVal = "";

		for (int x = 0; x < Enums.HowOften.values().length; x++) {

			if (Enums.HowOften.values()[x].toID().equals(str)) {
				resultVal = Enums.HowOften.values()[x].toString();
			}
		}
		
		ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter();
		int spinnerPosition = myAdap.getPosition(resultVal);
		
		return spinnerPosition;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int getPositionSellingSimEnums(String str, Spinner spin){
		
		String resultVal = "";

		for (int x = 0; x < Enums.SellingSim.values().length; x++) {

			if (Enums.SellingSim.values()[x].toID().equals(str)) {
				resultVal = Enums.SellingSim.values()[x].toString();
			}
		}

		ArrayAdapter myAdap = (ArrayAdapter) spin.getAdapter();
		int spinnerPosition = myAdap.getPosition(resultVal);
		
		return spinnerPosition;
	}
	
	public static int getEstablismentChecks(ListView lvNearEstablishment, MainApp mainApp){
		//
		SparseBooleanArray checked = lvNearEstablishment.getCheckedItemPositions();
		ArrayList<CodeDescPair> alEstablishments = mainApp.getDatabase().getEstablisments();
		int sumList = 0;
		
		for (int i = 0; i < lvNearEstablishment.getCount(); i++){
			if (checked.get(i)){
				sumList += Integer.parseInt(alEstablishments.get(i).Code);
			}
		}
		
		return sumList;
	}
}
