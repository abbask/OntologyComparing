package edu.uga.cs.ontologycomparision.model;

public class MyProperty {
	
	private String Name;
	private String Domain;
	private String Range;
	private int count;



	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDomain() {
		return Domain;
	}
	public void setDomain(String domain) {
		Domain = domain;
	}
	public String getRange() {
		return Range;
	}
	public void setRange(String range) {
		Range = range;
	}
	
	

}
