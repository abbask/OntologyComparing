package edu.uga.cs.ontologycomparision.model;

public class DatatypeProperty {

	private int ID;
	private String url;
	private String label;
	private String comment;
	private Version version;
	private Property parent;
	private Class domain;
	private XSDType range;
	
	public DatatypeProperty() {
	}

	public DatatypeProperty(String url, String label, String comment, Version version, Property parent,
			Class domain, XSDType range) {
		super();
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.version = version;
		this.parent = parent;
		this.domain = domain;
		this.range = range;
	}
	
	public DatatypeProperty(int iD, String url, String label, String comment, Version version, Property parent,
			Class domain, XSDType range) {
		super();
		ID = iD;
		this.url = url;
		this.label = label;
		this.comment = comment;
		this.version = version;
		this.parent = parent;
		this.domain = domain;
		this.range = range;
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

	public Class getDomain() {
		return domain;
	}

	public void setDomain(Class domain) {
		this.domain = domain;
	}

	public XSDType getRange() {
		return range;
	}

	public void setRange(XSDType range) {
		this.range = range;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
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
		DatatypeProperty other = (DatatypeProperty) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
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
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DatatypeProperty [ID=" + ID + ", url=" + url + ", label=" + label + ", comment=" + comment
				+ ", version=" + version + ", parent=" + parent + ", domain=" + domain + ", range=" + range + "]";
	}
	
	
	
}
