package edu.uga.cs.ontologycomparision.model;

import java.util.List;

public class Property {
	
	private int ID;
	private String url;
	private String label;
	private String type;
	private String comment;
	private Version version;
	private Property parent;
	private List<DomainRange> domainRanges;
	
	public Property() {
	
	}
	
	

	public Property(String url, String label, String type, String comment, Version version, Property parent,
			List<DomainRange> domainRanges) {
		super();
		this.url = url;
		this.label = label;
		this.type = type;
		this.comment = comment;
		this.version = version;
		this.parent = parent;
		this.domainRanges = domainRanges;
	}
	
	

	public Property(String url, String label, String type, String comment, Version version, Property parent) {
		super();
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



	public Property(int iD, String url, String label, String type, String comment, Version version, Property parent,
			List<DomainRange> domainRanges) {
		super();
		ID = iD;
		this.url = url;
		this.label = label;
		this.type = type;
		this.comment = comment;
		this.version = version;
		this.parent = parent;
		this.domainRanges = domainRanges;
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

	public List<DomainRange> getDomainRanges() {
		return domainRanges;
	}

	public void setDomainRanges(List<DomainRange> domainRanges) {
		this.domainRanges = domainRanges;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		
//		result = prime * result + ((label == null) ? 0 : label.hashCode());
//		
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		
//		Property other = (Property) obj;
//		
//		if (label == null) {
//			if (other.label != null)
//				return false;
//		} else if (!label.equals(other.label))
//			return false;
//		
//		return true;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		
		result = prime * result + ((domainRanges == null) ? 0 : domainRanges.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
		Property other = (Property) obj;

		if (domainRanges == null) {
			if (other.domainRanges != null)
				return false;
		} else if (!domainRanges.equals(other.domainRanges))
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

		return true;
	}

	@Override
	public String toString() {
		return "Property [ID=" + ID + ", label=" + label + ", type=" + type + ", version=" + version + ", parent="
				+ parent + ", domainRanges=" + domainRanges + "]";
	}
	
	
}
