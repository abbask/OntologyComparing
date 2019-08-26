package edu.uga.cs.ontologycomparision.service;

import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Result;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.ClassSortByLabel;
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.DatatypeTripleSortByLabel;
import edu.uga.cs.ontologycomparision.model.Expression;
import edu.uga.cs.ontologycomparision.model.ObjectTripleSortByLabel;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Restriction;

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
	
	public List<Result<Class, Integer>> compareIndividualCountEachClass() throws SQLException, UnexpectedException  {
		ClassService classService = new ClassService(connection);

		List<Class> class1Set = classService.listAll(ver1.getID());
		List<Class> class2Set = classService.listAll(ver2.getID());
		
		List<Class> class1SetTemp = new ArrayList<Class>(class1Set);
		
		class1Set.retainAll(class2Set);
		class2Set.retainAll(class1SetTemp);
		
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
		
		object1Set.removeAll(object2Set);
		object2Set.removeAll(class1SetTemp);
		
		List<Result<String, String>> object1List = object1Set.stream()
				.map(obj -> 
				new Result<String, String>("( " 
					+ (obj.getDomain()==null ? "" : obj.getDomain().getLabel())+ ", " 
					+ (obj.getPredicate()== null ? "" : obj.getPredicate().getLabel()) + ", " 
					+ (obj.getRange() == null ? "" : obj.getRange().getLabel() ) + " )",
					"Only in Version " + ver1.getNumber())
			)
			.collect(Collectors.toList());
		
		List<Result<String, String>> object2List = object2Set.stream()
				.map(obj -> 
				new Result<String, String>("( " 
					+ (obj.getDomain()==null ? "" : obj.getDomain().getLabel())+ ", " 
					+ (obj.getPredicate()== null ? "" : obj.getPredicate().getLabel()) + ", " 
					+ (obj.getRange() == null ? "" : obj.getRange().getLabel() ) + " )",
					"Only in Version " + ver2.getNumber())
			)
			.collect(Collectors.toList());
		
		Collections.sort(object1List, Comparator.comparing(Result::getElement));
		Collections.sort(object2List, Comparator.comparing(Result::getElement));
				
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
		
		List<Result<String, String>> datatype1List = datatype1Set.stream()				
				.map(d -> 
					new Result<String, String>("( " 
						+ (d.getDomain()==null ? "" : d.getDomain().getLabel())+ ", " 
						+ (d.getPredicate()== null ? "" : d.getPredicate().getLabel()) + ", " 
						+ (d.getRange() == null ? "" : d.getRange().getType() ) + " )",
						"Only in Version " + ver1.getNumber())
				)
				.collect(Collectors.toList());
		
		List<Result<String, String>> datatype2List = datatype2Set.stream()				
				.map(d -> 
					new Result<String, String>("( " 
						+ (d.getDomain()==null ? "" : d.getDomain().getLabel())+ ", " 
						+ (d.getPredicate()== null ? "" : d.getPredicate().getLabel()) + ", " 
						+ (d.getRange() == null ? "" : d.getRange().getType() ) + " )",
						"Only in Version " + ver2.getNumber())
				)
				.collect(Collectors.toList());
				
		
		Collections.sort(datatype1List, Comparator.comparing(Result::getElement));
		Collections.sort(datatype2List, Comparator.comparing(Result::getElement));
		
		List<Result<String, String>> result = new ArrayList<>();
		result.addAll(datatype1List);
		result.addAll(datatype2List);

		return result;
	}
	
	public List<Result<ObjectTripleType, Integer>> compareObjectTripleTypeCountEachTriple() throws Exception  {
		ObjectTripleTypeService service = new ObjectTripleTypeService(connection);

		List<ObjectTripleType> object1List = service.listAll(ver1.getID());
		List<ObjectTripleType> object2List = service.listAll(ver2.getID());
		List<ObjectTripleType> object1ListTemp = new ArrayList<ObjectTripleType>(object1List);
		object1List.retainAll(object2List);
		object2List.retainAll(object1ListTemp);
		
		object1List.retainAll(object2List);
		object2List.retainAll(object1ListTemp);
		
		Collections.sort(object1List, new ObjectTripleSortByLabel()); 
		Collections.sort(object2List, new ObjectTripleSortByLabel()); 
		
		List<Result<ObjectTripleType, Integer>> results = new ArrayList<>();
		
		for (int i = 0 ; i < object1List.size() ; i++) {
			int diff = object2List.get(i).compareTo(object1List.get(i));
			if (diff < 0 ) {
				results.add(new Result<ObjectTripleType, Integer>(object1List.get(i), diff));
			}
			else if (diff > 0) {
				results.add(new Result<ObjectTripleType, Integer>(object1List.get(i), diff));
			}
//			if (!(object1List.get(i).getDomain().getLabel().equals( object2List.get(i).getDomain().getLabel()) 
//					&& object1List.get(i).getPredicate().getLabel().equals( object2List.get(i).getPredicate().getLabel())
//					&& object1List.get(i).getRange().getLabel().equals( object2List.get(i).getRange().getLabel()))) {
//				throw new UnexpectedException("Objects are not equal after sort.");
//			}
		}						
		
		return results;
	}
	
	public List<Result<ObjectTripleType, Integer>> compareObjectTripleTypeCountEachTriple_old() throws SQLException, UnexpectedException  {
		ObjectTripleTypeService service = new ObjectTripleTypeService(connection);

		List<ObjectTripleType> object1List = service.listAll(ver1.getID());
		List<ObjectTripleType> object2List = service.listAll(ver2.getID());
		
		List<ObjectTripleType> object1ListTemp = new ArrayList<ObjectTripleType>(object1List);

		object1List.retainAll(object2List);
		object2List.retainAll(object1ListTemp);
			
//		Collections.sort(object1List, new ObjectTripleSortByLabel()); 
//		Collections.sort(object2List, new ObjectTripleSortByLabel()); 
		
		List<Result<ObjectTripleType, Integer>> results = new ArrayList<>();
		
		for (int i = 0 ; i < object1List.size() ; i++) {
			int list2Index = object2List.indexOf(object1List.get(i));
			int diff = object2List.get(list2Index).compareTo(object1List.get(i));
			results.add(new Result<ObjectTripleType, Integer>(object1List.get(i), diff));

		}						
		
		return results;
	}
	
	public List<Result<DataTypeTripleType, Integer>> compareDatatypeTripleTypeCountEachTriple() throws SQLException, UnexpectedException  {
		DataTypeTripleTypeService service = new DataTypeTripleTypeService(connection);

		List<DataTypeTripleType> datatype1Set = service.listAll(ver1.getID());
		List<DataTypeTripleType> datatype2Set = service.listAll(ver2.getID());
		
		List<DataTypeTripleType> datatype1SetTemp = new ArrayList<DataTypeTripleType>(datatype1Set);
		
		datatype1Set.retainAll(datatype2Set);
		datatype2Set.retainAll(datatype1SetTemp);
		
		
//		Collections.sort(datatype1Set, new DatatypeTripleSortByLabel()); 
		//Collections.sort(datatype2Set, new DatatypeTripleSortByLabel()); 
				
		List<Result<DataTypeTripleType, Integer>> results = new ArrayList<>();
		
		for (int i = 0 ; i < datatype1Set.size() ; i++) {
			int set2Index = datatype2Set.indexOf(datatype1Set.get(i));
			int diff = datatype2Set.get(set2Index).compareTo(datatype1Set.get(i));
			results.add(new Result<DataTypeTripleType, Integer>(datatype1Set.get(i), diff));
						
		}						
		
		return results;
	}
	
	public List<Result<String, Long>> compareRestrictionCount() throws SQLException  {
		
		RestrictionService restrictionService = new RestrictionService(connection);
		
		long restrictionCount1 = restrictionService.count(ver1.getID());
		long restrictionCount2 = restrictionService.count(ver2.getID());
		
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of restrictions of version " + ver1.getID(), restrictionCount1));
		results.add(new Result<String, Long>("Number of restrictions of version " + ver2.getID(), restrictionCount2));
		
		return results;
	}
	
	public List<Result<String, String>> compareRestrictions() throws SQLException{
		RestrictionService restrictionService = new RestrictionService(connection);
		
		Set<Restriction> restriction1Set = restrictionService.listAll(ver1.getID()).stream().collect(Collectors.toSet());
		Set<Restriction> restriction2Set = restrictionService.listAll(ver2.getID()).stream().collect(Collectors.toSet());

		Set<Restriction> restriction1SetTemp = new HashSet<Restriction>(restriction1Set);
		
		restriction1Set.removeAll(restriction2Set);
		restriction2Set.removeAll(restriction1SetTemp);
				
		List<Result<String, String>> restriction1List = restriction1Set.stream()				
				.map(d -> 
					new Result<String, String>("( " 
						+ (d.getOnProperty()==null ? "" : d.getOnProperty().getLabel())+ ", " 
						+ (d.getOnClass()== null ? "" : d.getOnClass().getLabel()) + ", " 
						+ (d.getType() == null ? "" : d.getType().getType() ) + " )",
						"Only in Version " + ver1.getNumber())
				)
				.collect(Collectors.toList());
		
		List<Result<String, String>> restriction2List = restriction2Set.stream()				
				.map(d -> 
				new Result<String, String>("( " 
						+ (d.getOnProperty()==null ? "" : d.getOnProperty().getLabel())+ ", " 
						+ (d.getOnClass()== null ? "" : d.getOnClass().getLabel()) + ", " 
						+ (d.getType() == null ? "" : d.getType().getType() ) + " )",
						"Only in Version " + ver2.getNumber())
				)
				.collect(Collectors.toList());
						
		Collections.sort(restriction1List, Comparator.comparing(Result::getElement));
		Collections.sort(restriction2List, Comparator.comparing(Result::getElement));
		
		List<Result<String, String>> result = new ArrayList<>();
		result.addAll(restriction1List);
		result.addAll(restriction2List);

		return result;
	}
	
	public List<Result<String, Long>> compareExpressionCount() throws SQLException  {
		
		ExpressionService service = new ExpressionService(connection);
		
		long expressionCount1 = service.count(ver1.getID());
		long expressionCount2 = service.count(ver2.getID());
		
		
		List<Result<String, Long>> results = new ArrayList<>();
		results.add(new Result<String, Long>("Number of expressions of version " + ver1.getID(), expressionCount1));
		results.add(new Result<String, Long>("Number of expressions of version " + ver2.getID(), expressionCount2));
		
		return results;
	}
	
	public List<Result<String, String>> compareExpressions() throws SQLException{
		ExpressionService expressionService = new ExpressionService(connection);
		
		Set<Expression> expression1Set = expressionService.listAll(ver1.getID()).stream().collect(Collectors.toSet());
		Set<Expression> expression2Set = expressionService.listAll(ver2.getID()).stream().collect(Collectors.toSet());

		Set<Expression> restriction1SetTemp = new HashSet<Expression>(expression1Set);
		
		expression1Set.removeAll(expression2Set);
		expression2Set.removeAll(restriction1SetTemp);
				
		List<Result<String, String>> expression1List = expression1Set.stream()				
				.map(d -> 
					new Result<String, String>("( " 
						+ (d.getType() ==null ? "" : d.getType())+ ", " 
						+ (d.getClasses()== null ? "" : d.getClasses().toString()) + ", ",
						"Only in Version " + ver1.getNumber())
				)
				.collect(Collectors.toList());
		
		List<Result<String, String>> expression2List = expression2Set.stream()				
				.map(d -> 
				new Result<String, String>("( " 
					+ (d.getType() ==null ? "" : d.getType())+ ", " 
					+ (d.getClasses()== null ? "" : d.getClasses().toString()) + ", ",
					"Only in Version " + ver2.getNumber())
			)
			.collect(Collectors.toList());
						
		Collections.sort(expression1List, Comparator.comparing(Result::getElement));
		Collections.sort(expression2List, Comparator.comparing(Result::getElement));
		
		List<Result<String, String>> result = new ArrayList<>();
		result.addAll(expression1List);
		result.addAll(expression2List);

		return result;
	}
	
}
