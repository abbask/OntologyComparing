package edu.uga.cs.ontologycomparision.model;

import java.util.Comparator;

public class DatatypeTripleSortByLabel implements Comparator<DataTypeTripleType>{

	@Override
	public int compare(DataTypeTripleType arg0, DataTypeTripleType arg1) {
		String domain0 = (arg0.getDomain() == null) ? "" : arg0.getDomain().getLabel();
		String domain1 = (arg1.getDomain() == null) ? "" : arg1.getDomain().getLabel();
		
		String predicate0 = (arg0.getPredicate() == null) ? "" : arg0.getPredicate().getLabel();
		String predicate1 = (arg1.getPredicate() == null) ? "" : arg1.getPredicate().getLabel();
		
		String range0 = (arg0.getRange() == null) ? "" : arg0.getRange().getType();
		String range1 = (arg1.getRange() == null) ? "" : arg1.getRange().getType();
		
		if (domain0.compareTo(domain1) == 0)
			if(predicate0.compareTo(predicate1) == 0)
				return range0.compareTo(range1);
			else
				return predicate0.compareTo(predicate1);
		else
			return domain0.compareTo(domain1);
	}
	
	
}
