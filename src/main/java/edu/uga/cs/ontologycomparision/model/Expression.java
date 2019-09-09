package edu.uga.cs.ontologycomparision.model;

import java.util.Arrays;
import java.util.List;

/**
 * a model class for class Expressions including unionOf and intersentionOf
 * @author abbas
 *
 */
public class Expression {
	
	private int id;
	private String type;
	private Property property;
	private String onProperty;
	private List<Class> classes;
	private Version version;
	
	public Expression() {
	
	}
	
	public Expression(String type, Property property, String onProperty, List<Class> classes, Version version) {

		this.type = type;
		this.property = property;
		this.onProperty = onProperty;
		this.classes = classes;
		this.version = version;
	}

	public Expression(int id, String type, Property property, String onProperty, List<Class> classes, Version version) {

		this.id = id;
		this.type = type;
		this.property = property;
		this.onProperty = onProperty;
		this.classes = classes;
		this.version = version;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getOnProperty() {
		return onProperty;
	}

	public void setOnProperty(String onProperty) {
		this.onProperty = onProperty;
	}

	public List<Class> getClasses() {
		return classes;
	}

	public void setClasses(List<Class> classes) {
		this.classes = classes;
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
		result = prime * result + ((classes == null) ? 0 : classes.hashCode());
		
		result = prime * result + ((onProperty == null) ? 0 : onProperty.hashCode());
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

		Expression other = (Expression) obj;
		if (classes == null) {
			if (other.classes != null)
				return false;
		} 

		if (onProperty == null) {
			if (other.onProperty != null)
				return false;
		} else if (!onProperty.equals(other.onProperty))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "Expression [id=" + id + ", type=" + type + ", property=" + property + ", onProperty=" + onProperty
				+ ", classes=" + classes + ", version=" + version + "]";
	}
	
	
	
	
	
}
