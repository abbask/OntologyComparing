package edu.uga.cs.ontologycomparision.model;

public class XSDType {
	
	private int ID;
	private String url;
	private String type;
	
	public XSDType() {
	}
	
	public XSDType(String url, String type) {
		this.url = url;
		this.type = type;
	}
	
	public XSDType(int iD, String url, String type) {
		ID = iD;
		this.url = url;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		XSDType other = (XSDType) obj;

		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

//	@Override
//	public String toString() {
//		return "XSDType [ID=" + ID + ", url=" + url + ", type=" + type + "]";
//	}
//	
	
	@Override
	public String toString() {
		return "XSDType [ID=" + ID + ", type=" + type + ", uri="+ url +  "]";
	}
	
	
	

}
