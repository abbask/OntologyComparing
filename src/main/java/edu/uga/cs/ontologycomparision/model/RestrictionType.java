package edu.uga.cs.ontologycomparision.model;

public class RestrictionType {
	
	private int ID;
	private String type;
	
	public RestrictionType() {
	
	}
	
	public RestrictionType(int iD, String type) {
		super();
		ID = iD;
		this.type = type;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
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
		RestrictionType other = (RestrictionType) obj;
		if (ID != other.ID)
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
		return "RestrictionType [ID=" + ID + ", type=" + type + "]";
	}
	
}
//someValuesFrom,
//allValuesFrom,
//hasValue,
//hasSelf,
//minCardinality, 
//maxCardinality, 
//cardinality, 
//minQualifiedCardinality,
//maxQualifiedCardinality,
//qualifiedCardinality,	
