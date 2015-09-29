package com.redfoot.iota.objects;

import java.util.Observable;

public class NetObservable extends Observable{
 // =========================================================================
 // TODO Variables
 // =========================================================================
	private int value = 0;
	
	public NetObservable(){
		
		
	}
	// =========================================================================
	public void setValue(int value){
		this.value = value;
	}
	// =========================================================================
	public void Commit(){
		
		setChanged();
		notifyObservers(this.value);
		clearChanged();
	}
 // =========================================================================
 // TODO Final Codes
 // =========================================================================
}
