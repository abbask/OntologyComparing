package edu.uga.cs.ontologycomparision.model;

import java.util.Comparator;

public class ClassSortByLabel implements Comparator<Class> {

	@Override
	public int compare(Class o1, Class o2) {
		
		return (int)(o1.getLabel().compareTo(o2.getLabel()) );
	}

}
