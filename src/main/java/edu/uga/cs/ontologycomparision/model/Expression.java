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
	private List<Class> classes;
	private Version version;
	
	public Expression() { }
	
	public Expression(String type, List<Class> classes, Version version) {
		super();
		this.type = type;
		this.classes = classes;
		this.version = version;
	}

	public Expression(int id, String type, List<Class> classes, Version version) {
		super();
		this.id = id;
		this.type = type;
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Expression [id=" + id + ", type=" + type + ", classes=" + classes + ", version=" + version + "]";
	}
		
}
