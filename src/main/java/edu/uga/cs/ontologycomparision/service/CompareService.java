package edu.uga.cs.ontologycomparision.service;

import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.XSDType;
import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Result;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.ClassSortByLabel;
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
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
	
	public List<Result<String, Long>> compareIndividualCount() throws SQLException  {
		ClassService classService = new ClassService(connection);
		long classCount1 = classService.individualCount(ver1.getID());
		long classCount2 = classService.individualCount(ver2.getID());
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of individuals of version " + ver1.getID(), classCount1));
		results.add(new Result<String, Long>("Number of individuals of version " + ver2.getID(), classCount2));
		
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
		
		System.out.println("property1Set: " + property1Set.size());
		System.out.println("property2Set: " + property2Set.size());
		
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
		
		System.out.println("property1Set: " + property1Set.size());
		System.out.println("property2Set: " + property2Set.size());
		
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
	
	public List<Result<Class, Integer>> compareIndividualCountEachClass() throws SQLException, UnexpectedException  {
		ClassService classService = new ClassService(connection);

		List<Class> class1Set = classService.listAll(ver1.getID());
		List<Class> class2Set = classService.listAll(ver2.getID());
		
		List<Class> class1SetTemp = new ArrayList<Class>(class1Set);
		
		class1Set.retainAll(class2Set);
		class2Set.retainAll(class1SetTemp);
		
		System.out.printf("class1Set: %d, class2Set: %d%n", class1Set.size(), class2Set.size());
		
		Collections.sort(class1Set, new ClassSortByLabel()); 
		Collections.sort(class2Set, new ClassSortByLabel()); 
		
		List<Result<Class, Integer>> results = new ArrayList<>();
		
		for (int i = 0 ; i < class1Set.size() ; i++) {
			int diff = class2Set.get(i).compareTo(class1Set.get(i));
			if (diff < 0 ) {
				results.add(new Result<Class, Integer>(class1Set.get(i), diff));
			}
			else if (diff > 0) {
				results.add(new Result<Class, Integer>(class1Set.get(i), diff));
			}
			if (class2Set.get(i).getLabel().compareTo(class1Set.get(i).getLabel()) != 0) {
				throw new UnexpectedException("sort did not work: " + class2Set.get(i).getLabel() + ", " + class1Set.get(i).getLabel());
			}
		}						
		
		return results;
	}
	
	public List<Result<String, Long>> compareObjectTripleTypeCount() throws SQLException  {
		
		ObjectTripleTypeService service = new ObjectTripleTypeService(connection);
		long objectTripleCount1 = service.count(ver1.getID());
		long objectTripleCount2 = service.count(ver2.getID());
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of Object Triple Types of version " + ver1.getID(), objectTripleCount1));
		results.add(new Result<String, Long>("Number of Object Triple Types of version " + ver2.getID(), objectTripleCount2));
		
		return results;
	}
	
	public List<Result<String, Long>> compareDatatypeTripleTypeCount() throws SQLException  {
		
		DataTypeTripleTypeService service = new DataTypeTripleTypeService(connection);
		long datatypeTripleCount1 = service.count(ver1.getID());
		long datatypeTripleCount2 = service.count(ver2.getID());
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of Datatype Triple Types of version " + ver1.getID(), datatypeTripleCount1));
		results.add(new Result<String, Long>("Number of Datatype Triple Types of version " + ver2.getID(), datatypeTripleCount2));
		
		return results;
	}
	
	public List<Result<String, String>> compareObjectTripleTypes() throws SQLException{
		ObjectTripleTypeService service = new ObjectTripleTypeService(connection);

		Set<ObjectTripleType> object1Set = service.listAll(ver1.getID()).stream().collect(Collectors.toSet());
		Set<ObjectTripleType> object2Set = service.listAll(ver2.getID()).stream().collect(Collectors.toSet());
		
		Set<ObjectTripleType> class1SetTemp = new HashSet<ObjectTripleType>(object1Set);
		
		System.out.println("object1Set: " + object1Set.size());
		System.out.println("object2Set: " + object2Set.size());
		
		object1Set.removeAll(object2Set);
		object2Set.removeAll(class1SetTemp);
		
		System.out.println("object1Set: " + object1Set);
		System.out.println("object2Set: " + object2Set);
		
		
		List<Result<String, String>> object1List = object1Set.stream()
				.map(object -> new Result<String, String>("( " + object.getDomain().getLabel() + ", " 
						+ object.getPredicate().getLabel() + ", " + object.getRange().getLabel() + " )", "Only in Version " + ver1.getNumber()))
				.collect(Collectors.toList());
		
		List<Result<String, String>> object2List = object2Set.stream()
				.map(object -> new Result<String, String>("( " + object.getDomain().getLabel() + ", " 
						+ object.getPredicate().getLabel() + ", " + object.getRange().getLabel() + " )", "Only in Version " + ver2.getNumber()))
				.collect(Collectors.toList());
				
		List<Result<String, String>> result = new ArrayList<>();
		result.addAll(object1List);
		result.addAll(object2List);

		return result;
	}
	
	public List<Result<String, String>> compareDatatypeTripleTypes() throws SQLException{
		DataTypeTripleTypeService service = new DataTypeTripleTypeService(connection);

		Set<DataTypeTripleType> datatype1Set = service.listAll(ver1.getID()).stream().collect(Collectors.toSet());
		Set<DataTypeTripleType> datatype2Set = service.listAll(ver2.getID()).stream().collect(Collectors.toSet());
		
		Set<DataTypeTripleType> datatype1SetTemp = new HashSet<DataTypeTripleType>(datatype1Set);
		
		datatype1Set.removeAll(datatype2Set);
		datatype2Set.removeAll(datatype1SetTemp);
		
		System.out.println("datatype1Set: " + datatype1Set.size());
		System.out.println("datatype2Set: " + datatype2Set.size());	


		List<Result<String, String>> datatype1List = datatype1Set.stream()				
				.map(d -> 
					new Result<String, String>("( " 
						+ (d.getDomain()==null ? "" : d.getDomain().getLabel())+ ", " 
						+ (d.getPredicate()== null ? "" : d.getPredicate().getLabel()) + ", " 
						+ (d.getRange() == null ? "" : d.getRange().getType() ) + " )",
						"Only in Version " + ver1.getNumber())
				)
				.collect(Collectors.toList());
		
		List<Result<String, String>> datatype2List = datatype1Set.stream()				
				.map(d -> 
					new Result<String, String>("( " 
						+ (d.getDomain()==null ? "" : d.getDomain().getLabel())+ ", " 
						+ (d.getPredicate()== null ? "" : d.getPredicate().getLabel()) + ", " 
						+ (d.getRange() == null ? "" : d.getRange().getType() ) + " )",
						"Only in Version " + ver2.getNumber())
				)
				.collect(Collectors.toList());
				
		List<Result<String, String>> result = new ArrayList<>();
		result.addAll(datatype1List);
		result.addAll(datatype2List);

		return result;
	}
	
}
