package edu.uga.cs.ontologycomparision.model;

import java.util.Arrays;

public class Restriction {
	
	private int ID;
	private Property onProperty;
	private RestrictionType type;
	private int CardinalityValue;
	private Class onClass;
	
	public Restriction() {
	
	}

	public Restriction(int iD, Property onProperty, RestrictionType type, int cardinalityValue, Class onClass) {
		
		ID = iD;
		this.onProperty = onProperty;
		this.type = type;
		CardinalityValue = cardinalityValue;
		this.onClass = onClass;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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

	public int getCardinalityValue() {
		return CardinalityValue;
	}

	public void setCardinalityValue(int cardinalityValue) {
		CardinalityValue = cardinalityValue;
	}

	public Class getOnClass() {
		return onClass;
	}

	public void setOnClass(Class onClass) {
		this.onClass = onClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + CardinalityValue;
//		result = prime * result + ID;
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
		if (getClass() != obj.getClass())
			return false;
		Restriction other = (Restriction) obj;
		if (CardinalityValue != other.CardinalityValue)
			return false;
//		if (ID != other.ID)
//			return false;
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
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Restriction [ID=" + ID + ", onProperty=" + onProperty + ", type=" + type + ", CardinalityValue="
				+ CardinalityValue + ", onClass=" + onClass + "]";
	}

	
}
