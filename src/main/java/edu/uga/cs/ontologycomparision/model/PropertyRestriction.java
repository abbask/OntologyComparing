package edu.uga.cs.ontologycomparision.model;

import java.util.Arrays;

public class PropertyRestriction {
	
	private int ID;
	private Property property;
	private Restriction type;
	private Class[] classes;
	
	public PropertyRestriction() {	
	}

	public PropertyRestriction(int iD, Property property, Restriction type, Class[] classes) {
		super();
		ID = iD;
		this.property = property;
		this.type = type;
		this.classes = classes;
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

	public Restriction getType() {
		return type;
	}

	public void setType(Restriction type) {
		this.type = type;
	}

	public Class[] getClasses() {
		return classes;
	}

	public void setClasses(Class[] classes) {
		this.classes = classes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + Arrays.hashCode(classes);
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		PropertyRestriction other = (PropertyRestriction) obj;
		if (ID != other.ID)
			return false;
		if (!Arrays.equals(classes, other.classes))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertyRestriction [ID=" + ID + ", property=" + property + ", type=" + type + ", classes="
				+ Arrays.toString(classes) + "]";
	}
	
	
	

}
