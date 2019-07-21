package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Result;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.Property;

public class CompareService {
	
	final static String ObjectPropertyType = "ObjectProperty";
	final static String DatatypePropertyType = "DatatypeProperty";
	
	final static Logger logger = Logger.getLogger(CompareService.class);
	
	private Version ver1;
	private Version ver2;
	private Connection connection;
	
	public CompareService(Version ver1, Version ver2) {
		this.ver1 = ver1;
		this.ver2 = ver2;	
		MySQLConnection mySQLConnection = new MySQLConnection();
		connection = mySQLConnection.openConnection();
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
			
		ClassService classService = new ClassService(connection);
		long classCount1 = classService.count(ver1.getID());
		long classCount2 = classService.count(ver2.getID());
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of classes of version " + ver1.getID(), classCount1));
		results.add(new Result<String, Long>("Number of classes of version " + ver2.getID(), classCount2));
		
		return results;
	}
	
	public List<Result<String, Long>> compareObjectPropertyCount() throws SQLException  {
		PropertyService propertyService = new PropertyService(connection, ObjectPropertyType);
		long propertyCount1 = propertyService.count(ver1.getID());
		long propertyCount2 = propertyService.count(ver2.getID());
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of Object Property of version " + ver1.getID(), propertyCount1));
		results.add(new Result<String, Long>("Number of Object Property of version " + ver2.getID(), propertyCount2));
		
		return results;
	}
	
	public List<Result<String, Long>> compareDatatypePropertyCount() throws SQLException  {
		PropertyService propertyService = new PropertyService(connection, DatatypePropertyType);
		long propertyCount1 = propertyService.count(ver1.getID());
		long propertyCount2 = propertyService.count(ver2.getID());
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of Object Property of version " + ver1.getID(), propertyCount1));
		results.add(new Result<String, Long>("Number of Object Property of version " + ver2.getID(), propertyCount2));
		
		return results;
	}
	
	public List<Result<String, String>> compareClasses() throws SQLException{
		ClassService classService = new ClassService(connection);

		Set<Class> class1Set = classService.listAll(ver1.getID()).stream().collect(Collectors.toSet());
		Set<Class> class2Set = classService.listAll(ver2.getID()).stream().collect(Collectors.toSet());
		
		Set<Class> class1SetTemp = new HashSet<Class>(class1Set);
		
		class1Set.removeAll(class2Set);
		class2Set.removeAll(class1SetTemp);
		
		
		List<Result<String, String>> class1List = class1Set.stream()
				.map(myClass -> new Result<String, String>(myClass.getLabel(), "Only in Version " + ver1.getNumber()))
				.collect(Collectors.toList());
		
		List<Result<String, String>> class2List = class2Set.stream()
				.map(myClass -> new Result<String, String>(myClass.getLabel(), "Only in Version " + ver2.getNumber()))
				.collect(Collectors.toList());
				
		List<Result<String, String>> result = new ArrayList<>();
		result.addAll(class1List);
		result.addAll(class2List);

		return result;
	}
	
	public List<Result<String, String>> compareObjectProperties() throws SQLException{
		PropertyService service = new PropertyService(connection, ObjectPropertyType);

		Set<Property> property1Set = service.listAll(ver1.getID()).stream().collect(Collectors.toSet());
		Set<Property> property2Set = service.listAll(ver2.getID()).stream().collect(Collectors.toSet());
		
		Set<Property> property1SetTemp = new HashSet<Property>(property1Set);
		
		property1Set.removeAll(property2Set);
		property2Set.removeAll(property1SetTemp);
		
		
		List<Result<String, String>> property1List = property1Set.stream()
				.map(myProperty -> new Result<String, String>(myProperty.getLabel(), "Only in Version " + ver1.getNumber()))
				.collect(Collectors.toList());
		
		List<Result<String, String>> property2List = property2Set.stream()
				.map(myProperty -> new Result<String, String>(myProperty.getLabel(), "Only in Version " + ver2.getNumber()))
				.collect(Collectors.toList());
				
		List<Result<String, String>> result = new ArrayList<>();
		result.addAll(property1List);
		result.addAll(property2List);

		return result;
	}
	
	public List<Result<String, String>> compareDatetypeProperties() throws SQLException{
		PropertyService service = new PropertyService(connection, DatatypePropertyType);

		Set<Property> property1Set = service.listAll(ver1.getID()).stream().collect(Collectors.toSet());
		Set<Property> property2Set = service.listAll(ver2.getID()).stream().collect(Collectors.toSet());
		
		Set<Property> property1SetTemp = new HashSet<Property>(property1Set);
		
		property1Set.removeAll(property2Set);
		property2Set.removeAll(property1SetTemp);
		
		
		List<Result<String, String>> property1List = property1Set.stream()
				.map(myProperty -> new Result<String, String>(myProperty.getLabel(), "Only in Version " + ver1.getNumber()))
				.collect(Collectors.toList());
		
		List<Result<String, String>> property2List = property2Set.stream()
				.map(myProperty -> new Result<String, String>(myProperty.getLabel(), "Only in Version " + ver2.getNumber()))
				.collect(Collectors.toList());
				
		List<Result<String, String>> result = new ArrayList<>();
		result.addAll(property1List);
		result.addAll(property2List);

		return result;
	}
}
