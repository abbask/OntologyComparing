package edu.uga.cs.ontologycomparision.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.Result;

public class CompareService {
	
	private Version ver1;
	private Version ver2;
	
	public CompareService(Version ver1, Version ver2) {
		this.ver1 = ver1;
		this.ver2 = ver2;		
	}

	public Version getVer1() {
		return ver1;
	}

	public void setVer1(Version ver1) {
		this.ver1 = ver1;
	}

	public Version getVer2() {
		return ver2;
	}

	public void setVer2(Version ver2) {
		this.ver2 = ver2;
	}
	
	public List<Result<String, Long>> compareClassCount() throws SQLException  {
		ClassService classService = new ClassService();
		long classCount1 = classService.count(ver1.getID());
		long classCount2 = classService.count(ver2.getID());
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of classes of version " + ver1.getID(), classCount1));
		results.add(new Result<String, Long>("Number of classes of version " + ver2.getID(), classCount2));
		
		return results;
	}
	
	public List<Result<String, Long>> compareObjectPropertyCount() throws SQLException  {
		PropertyService propertyService = new PropertyService();
		long propertyCount1 = propertyService.count(ver1.getID());
		long propertyCount2 = propertyService.count(ver2.getID());
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of Object Property of version " + ver1.getID(), propertyCount1));
		results.add(new Result<String, Long>("Number of Object Property of version " + ver2.getID(), propertyCount2));
		
		return results;
	}
}
