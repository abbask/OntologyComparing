package edu.uga.cs.ontologycomparision.model;

public class Class {
	
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
		result = prime * result + ID;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (int) (count ^ (count >>> 32));
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Class other = (Class) obj;
		if (ID != other.ID)
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (count != other.count)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Class [ID=" + ID + ", url=" + url + ", label=" + label + ", comment=" + comment + ", count=" + count
				+ ", version=" + version + ", parent=" + parent + "]";
	}
	
	
	

}
