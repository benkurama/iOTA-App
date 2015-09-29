package com.redfoot.iota;

public class CodeDescPair {

	public String Code;
	public String Description;
	
	public CodeDescPair(String code, String desc) {
		Code = code;
		Description = desc;
	}
	
	@Override
	public String toString() {
		return Description;
	}
	
}
