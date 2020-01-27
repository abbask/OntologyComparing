package edu.uga.cs.ontologycomparision.model;

public class DomainRange {
	
	private int ID;
	private Property property;
	private String type;
	private Class theClass;
	private XSDType xsdType;
	
	public DomainRange() {
	}

	public DomainRange(Property property, String type, Class theClass) {
		super();
		this.property = property;
		this.type = type;
		this.theClass = theClass;
	}
	

	public DomainRange(Property property, String type, XSDType xsdType) {
		super();
		this.property = property;
		this.type = type;
		this.xsdType = xsdType;
	}

	public DomainRange(int iD, Property property, String type, Class theClass) {
		super();
		ID = iD;
		this.property = property;
		this.type = type;
		this.theClass = theClass;
	}

	public DomainRange(int iD, Property property, String type, XSDType xsdType) {
		super();
		ID = iD;
		this.property = property;
		this.type = type;
		this.xsdType = xsdType;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Class getTheClass() {
		return theClass;
	}

	public void setTheClass(Class theClass) {
		this.theClass = theClass;
	}

	
	public XSDType getXsdType() {
		return xsdType;
	}

	public void setXsdType(XSDType xsdType) {
		this.xsdType = xsdType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((theClass == null) ? 0 : theClass.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((xsdType == null) ? 0 : xsdType.hashCode());
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
		DomainRange other = (DomainRange) obj;

		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (theClass == null) {
			if (other.theClass != null)
				return false;
		} else if (!theClass.equals(other.theClass))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		
		if (xsdType == null) {
			if (other.xsdType != null)
				return false;
		} else if (!xsdType.equals(other.xsdType))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "DomainRange [ID=" + ID + ", property=" + property.getLabel() + ", type=" + type + ", theClass=" + theClass + ", xsdType=" + xsdType +  "]";
	}
	
	

}
