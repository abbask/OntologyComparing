package edu.uga.cs.ontologycomparision.model;

public class ObjectProperty {
	
	private int ID;
	private String uri;
	private String label;
	private String comment;
	private Version version;
	private ObjectProperty parent;
	private Class domain;
	private Class range;
	
	public ObjectProperty() {
	}

	public ObjectProperty(String uri, String label, String comment, Version version,
			ObjectProperty parent, Class domain, Class range) {
		super();
		this.uri = uri;
		this.label = label;
		
		this.comment = comment;
		this.version = version;
		this.parent = parent;
		this.domain = domain;
		this.range = range;
	}
	
	public ObjectProperty(int iD, String url, String label, String comment, Version version,
			ObjectProperty parent, Class domain, Class range) {
		super();
		ID = iD;
		this.uri = uri;
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

	public String getUri() {
		return uri;
	}

	public void setUrl(String uri) {
		this.uri = uri;
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

	public ObjectProperty getParent() {
		return parent;
	}

	public void setParent(ObjectProperty parent) {
		this.parent = parent;
	}

	public Class getDomain() {
		return domain;
	}

	public void setDomain(Class domain) {
		this.domain = domain;
	}

	public Class getRange() {
		return range;
	}

	public void setRange(Class range) {
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
		ObjectProperty other = (ObjectProperty) obj;
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
		return "ObjectProperty [ID=" + ID + ", uri=" + uri + ", label=" + label + ", comment=" + comment + ", version="
				+ version + ", parent=" + parent + ", domain=" + domain + ", range=" + range + "]";
	}
	
	
	
}
