package edu.uga.cs.ontologycomparision.model;

public class Property {
	
	private int ID;
	private String url;
	private String label;
	private String type;
	private String comment;
	private Version version;
	private Property parent;
	
	public Property(String url, String label, String type, String comment, Version version, Property parent) {
		
		this.url = url;
		this.label = label;
		this.type = type;
		this.comment = comment;
		this.version = version;
		this.parent = parent;
		
	}
	
	public Property(int iD, String url, String label, String type, String comment, Version version, Property parent) {
		super();
		ID = iD;
		this.url = url;
		this.label = label;
		this.type = type;
		this.comment = comment;
		this.version = version;
		this.parent = parent;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Property getParent() {
		return parent;
	}

	public void setParent(Property parent) {
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		Property other = (Property) obj;
		
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		
		return true;
	}

//	@Override
//	public String toString() {
//		return "Property [ID=" + ID + ", url=" + url + ", label=" + label + ", type=" + type + ", comment=" + comment
//				+ ", version=" + version + ", parent=" + parent + "]";
//	}

	@Override
	public String toString() {
		return "Property [ID=" + ID + ", label=" + label + "]";
	}
}
