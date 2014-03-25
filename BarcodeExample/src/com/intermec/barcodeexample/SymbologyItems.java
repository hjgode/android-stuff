package com.intermec.barcodeexample;

public class SymbologyItems {

	int id = 0;
	String name = null;	
	Boolean enabled = false;
	
	SymbologyItems(int id, String name, Boolean checked)
	{
		this.id = id;
		this.name = name;
		this.enabled = checked;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Boolean isEnabled()
	{
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) 
	{
		  this.enabled = enabled;
	}

}
