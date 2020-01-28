package edu.uga.cs.ontologycomparision.model;

import java.util.List;

public class Class implements Comparable<Class>{
	
	private int ID;
	private String url;
	private String label;
	private String comment;
	private long count;
	private Version version;
	private List<Class> parents;
	
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

	public Class(String url, String label, String comment, long count, Version version, List<Class> parents) {
		
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.count = count;
		this.version = version;
		this.parents = parents;
	}
	
	public Class(int iD, String url, String label, String comment, long count, Version version, List<Class> parents) {
		
		ID = iD;
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.count = count;
		this.version = version;
		this.parents = parents;
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

	public List<Class> getParents() {
		return parents;
	}


	public void setParents(List<Class> parents) {
		this.parents = parents;
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
		
		int pSize  = 0 ;
		if (parents != null)
			pSize = parents.size();
		
		return "Class [ID=" + ID + ", label=" + label + ", parents size=" + pSize + "]";
	}


	@Override
	public int compareTo(Class o) {
		return (int)(this.count - o.getCount());
		
	}
	
	
	

}
