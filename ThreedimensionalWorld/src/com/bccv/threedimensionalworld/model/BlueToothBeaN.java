package com.bccv.threedimensionalworld.model;

public class BlueToothBeaN {
	private String name;
	private boolean isSiri;
	
	private int type;
	private String adress;
	
	

	public BlueToothBeaN(String name, boolean isSiri, int type, String adress) {
		super();
		this.name = name;
		this.isSiri = isSiri;
		this.type = type;
		this.adress = adress;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isSiri() {
		return isSiri;
	}
	public void setSiri(boolean isSiri) {
		this.isSiri = isSiri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
}
