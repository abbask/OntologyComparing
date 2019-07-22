package edu.uga.cs.ontologycomparision.model;

public class DataTypeTripleType {

	private int ID;
	private Class domain;
	private Property predicate;
	private XSDType range;
	private long count;
	private Version version;
	
	public DataTypeTripleType(Class domain, Property predicate, XSDType range, long count, Version version) {
		super();
		this.domain = domain;
		this.predicate = predicate;
		this.range = range;
		this.count = count;
		this.version = version;
	}
	
	public DataTypeTripleType(int iD, Class domain, Property predicate, XSDType range, long count, Version version) {
		super();
		ID = iD;
		this.domain = domain;
		this.predicate = predicate;
		this.range = range;
		this.count = count;
		this.version = version;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Class getDomain() {
		return domain;
	}

	public void setDomain(Class domain) {
		this.domain = domain;
	}

	public Property getPredicate() {
		return predicate;
	}

	public void setPredicate(Property predicate) {
		this.predicate = predicate;
	}

	public XSDType getRange() {
		return range;
	}

	public void setRange(XSDType range) {
		this.range = range;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		DataTypeTripleType other = (DataTypeTripleType) obj;
		
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;

		return true;
	}

//	@Override
//	public String toString() {
//		return "DataTypeTripleType [ID=" + ID + ", domain=" + domain + ", predicate=" + predicate + ", range=" + range
//				+ ", count=" + count + ", version=" + version + "]";
//	}
	@Override
	public String toString() {
		return "DataTypeTripleType [ID=" + ID + ", domain=" + domain + ", predicate=" + predicate + ", range=" + range
				+ "]";
	}
		
}
