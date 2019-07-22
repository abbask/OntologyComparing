package edu.uga.cs.ontologycomparision.model;

import java.util.Comparator;

public class ObjectTripleSortByLabel implements Comparator<ObjectTripleType> {

	@Override
	public int compare(ObjectTripleType arg0, ObjectTripleType arg1) {			
		
		String domain0 = (arg0.getDomain() == null) ? "" : arg0.getDomain().getLabel();
		String domain1 = (arg1.getDomain() == null) ? "" : arg1.getDomain().getLabel();
		
		String predicate0 = (arg0.getPredicate() == null) ? "" : arg0.getPredicate().getLabel();
		String predicate1 = (arg1.getPredicate() == null) ? "" : arg1.getPredicate().getLabel();
		
		String range0 = (arg0.getRange() == null) ? "" : arg0.getRange().getLabel();
		String range1 = (arg1.getRange() == null) ? "" : arg1.getRange().getLabel();
		
		if (domain0.compareTo(domain1) == 0)
			if(predicate0.compareTo(predicate1) == 0)
				return range0.compareTo(range1);
			else
				return predicate0.compareTo(predicate1);
		else
			return domain0.compareTo(domain1);
		
	}

}
