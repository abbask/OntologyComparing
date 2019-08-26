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
	private Class onClass;
	private Property onProperty;
	private List<Class> classes;
	private Version version;
	
	
	public Expression() { }

	public Expression(String type, Class onClass, Property onProperty, List<Class> classes, Version version) {
		this.type = type;
		this.onClass = onClass;
		this.onProperty = onProperty;
		this.classes = classes;
		this.version = version;
	}

	public Expression(int id, String type, Class onClass, Property onProperty, List<Class> classes, Version version) {
		super();
		this.id = id;
		this.type = type;
		this.onClass = onClass;
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

	public Class getOnClass() {
		return onClass;
	}

	public void setOnClass(Class onClass) {
		this.onClass = onClass;
	}

	public Property getOnProperty() {
		return onProperty;
	}

	public void setOnProperty(Property onProperty) {
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
		result = prime * result + ((onClass == null) ? 0 : onClass.hashCode());
		result = prime * result + ((onProperty == null) ? 0 : onProperty.hashCode());
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
		} else if (!classes.equals(other.classes))
			return false;
		if (id != other.id)
			return false;
		if (onClass == null) {
			if (other.onClass != null)
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

		return true;
	}

	@Override
	public String toString() {
		return "Expression [id=" + id + ", type=" + type + ", onClass=" + onClass + ", onProperty=" + onProperty
				+ ", classes=" + classes + ", version=" + version + "]";
	}
	
	
		
}
