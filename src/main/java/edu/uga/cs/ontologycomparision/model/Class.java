package edu.uga.cs.ontologycomparision.model;

public class Class implements Comparable<Class>{
	
	private int ID;
	private String url;
	private String label;
	private String comment;
	private long count;
	private Version version;
	private Class parent;
	
	public Class() {
	}
	
	public Class(String url, String label, String comment, long count) {
		
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.count = count;
	}
	
	public Class(int iD, String url, String label, String comment, long count) {
		
		ID = iD;
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.count = count;
	}

	public Class(String url, String label, String comment, long count, Version version) {
		
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.count = count;
		this.version = version;
	}
	
	public Class(int iD, String url, String label, String comment, long count, Version version) {
		
		ID = iD;
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.count = count;
		this.version = version;
	}

	public Class(String url, String label, String comment, long count, Version version, Class parent) {
		
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.count = count;
		this.version = version;
		this.parent = parent;
	}
	
	public Class(int iD, String url, String label, String comment, long count, Version version, Class parent) {
		
		ID = iD;
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.count = count;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Class getParent() {
		return parent;
	}

	public void setParent(Class parent) {
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
		
		Class other = (Class) obj;

		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		
		return true;
	}

//	@Override
//	public String toString() {
//		return "Class [ID=" + ID + ", url=" + url + ", label=" + label + ", comment=" + comment + ", count=" + count
//				+ ", version=" + version + ", parent=" + parent + "]";
//	}
	
	@Override
	public String toString() {
		return "Class [ID=" + ID + ", label=" + label + "]";
	}


	@Override
	public int compareTo(Class o) {
		return (int)(this.count - o.getCount());
		
	}
	
	
	

}
