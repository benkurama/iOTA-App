package com.redfoot.iota.objects;

public class Enums {
	 // =========================================================================
 	 // TODO Variables
 	 // =========================================================================
	public static enum HowOften {
		BLANK("0",""),
	    EVERYDAY ("1","Every day"),
	    EVERY2DAYS ("2","Every 2 days"),
	    TWICE_A_WEEK ("3","Twice a week"),
	    ONCE_A_WEEK ("4","Once a week"),
	    X3_A_MONTH ("5","3x a month"),
	    TWICE_A_MONTH ("6","Twice a month"),
	    ONCE_A_MONTH_LESS ("7","Once a month or less");
	    // =========================================================================
	    private final String name;
	    private final String id;

	    private HowOften(String i, String s) {
	        name = s;
	        id = i;
	    }

	    public boolean equalsName(String otherName){
	        return (otherName == null)? false:name.equals(otherName);
	    }

	    public String toString(){
	       return name;
	    }
	    
	    public String toID(){
	    	return id;
	    }
	}
	// =========================================================================
	public static enum SellingSim{
		
		SELLING_BUT_NOT_DISPLAYED ("1","Selling but not displayed"),
		SELLING_AND_DISPLAYED ("2","Selling and displayed"),
		SELLING_BUT_NO_STOCKS ("3","Selling but no stocks"),
		NOT_SELLING ("4", "Not selling");
		
		private final String name;
	    private final String id;

	    private SellingSim(String i, String s) {
	        name = s;
	        id = i;
	    }

	    public boolean equalsName(String otherName){
	        return (otherName == null)? false:name.equals(otherName);
	    }

	    public String toString(){
	       return name;
	    }
	    
	    public String toID(){
	    	return id;
	    }
	}
	// =========================================================================
	public static enum SellingLoad{
		
		SELLING_LOAD_YES ("Y","Yes"),
		SELLING_LOAD_NO ("N","No");
		
		private final String name;
	    private final String id;

	    private SellingLoad(String i, String s) {
	        name = s;
	        id = i;
	    }

	    public boolean equalsName(String otherName){
	        return (otherName == null)? false:name.equals(otherName);
	    }

	    public String toString(){
	       return name;
	    }
	    
	    public String toID(){
	    	return id;
	    }
	}
	// =========================================================================
		public static enum RefuseOption{
			
			REFUSE_TO_GIVE_NUMBER ("1","Refused to give number"),
			REFUSE_TEST_BUY ("2","Refused test buy");
			
			private final String name;
		    private final String id;

		    private RefuseOption(String i, String s) {
		        name = s;
		        id = i;
		    }

		    public boolean equalsName(String otherName){
		        return (otherName == null)? false:name.equals(otherName);
		    }

		    public String toString(){
		       return name;
		    }
		    
		    public String toID(){
		    	return id;
		    }
		}
	 // =========================================================================
 	 // TODO Final Codes
}
