package edu.uga.cs.ontologycomparision.model;

import java.util.Arrays;

public class Restriction {
	
	private int ID;
	private String URI;
	private Property onProperty;
	private RestrictionType type;
	private String value;
	private Class onClass;
	private Version version;
	
	public Restriction() {
	
	}
	
	public Restriction(String URI, Property onProperty, RestrictionType type, String value, Class onClass,
			Version version) {
		
		this.URI = URI; 
		this.onProperty = onProperty;
		this.type = type;
		this.value = value;
		this.onClass = onClass;
		this.version = version;
	}

	public Restriction(int iD, String URI, Property onProperty, RestrictionType type, String value, Class onClass,
			Version version) {
		
		ID = iD;
		this.URI = URI; 
		this.onProperty = onProperty;
		this.type = type;
		this.value = value;
		this.onClass = onClass;
		this.version = version;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public Property getOnProperty() {
		return onProperty;
	}

	public void setOnProperty(Property onProperty) {
		this.onProperty = onProperty;
	}

	public RestrictionType getType() {
		return type;
	}

	public void setType(RestrictionType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Class getOnClass() {
		return onClass;
	}

	public void setOnClass(Class onClass) {
		this.onClass = onClass;
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
		result = prime * result + ((URI == null) ? 0 : URI.hashCode());
		result = prime * result + ((onClass == null) ? 0 : onClass.hashCode());
		result = prime * result + ((onProperty == null) ? 0 : onProperty.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		Restriction other = (Restriction) obj;

		if (URI == null) {
			if (other.URI != null)
				return false;
		} else if (!URI.equals(other.URI))
			return false;
		if (URI == null) {
			if (other.URI != null)
				return false;
			
		} else if (!onClass.equals(other.onClass))
			return false;
		if (onProperty == null) {
			if (other.onProperty != null)
				return false;
		} else if (!onProperty.equals(other.onProperty))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "Restriction [ID=" + ID + ", URI=" + URI + ", onProperty=" + onProperty + ", type=" + type + ", value=" + value
				+ ", onClass=" + onClass + ", version=" + version + "]";
	}
	
	
	
	
		
}
