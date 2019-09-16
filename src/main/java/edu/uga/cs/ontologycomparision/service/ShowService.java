package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.Expression;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Restriction;
import edu.uga.cs.ontologycomparision.model.Result;
import edu.uga.cs.ontologycomparision.model.Version;

public class ShowService {
	
	final static String ObjectPropertyType = "ObjectProperty";
	final static String DatatypePropertyType = "DatatypeProperty";
	
	final static Logger logger = Logger.getLogger(CompareService.class);
	
	private Version ver;
	private Connection connection;
	
	public ShowService(Version ver) {
		this.ver = ver;

		MySQLConnection mySQLConnection = new MySQLConnection();
		connection = mySQLConnection.openConnection();
	}

	public Version getVer1() {
		return ver;
	}

	public void setVer1(Version ver) {
		this.ver = ver;
	}

	
	public Map<String, Object> getAllCounts(Map<String, Object> root) throws SQLException{
		root.put("classCount", getClassCount());
		root.put("objectPropertyCount", getObjectPropertyCount());
		root.put("datatypePropertyCount", getDatatypePropertyCount());
		root.put("individualCount", getIndividualCount());
		root.put("objectTripleTypeCount", getObjectTripleTypeCount());
		root.put("datatypeTripleTypeCount", getDatatypeTripleTypeCount());
		root.put("restrictionCount", getRestrictionCount());
		root.put("expressionCount", getExpressionCount());
		
		return root;
	}
	
	public Result<String, Long> getClassCount() throws SQLException  {
		
		ClassService classService = new ClassService(connection);
		long classCount1 = classService.count(ver.getID());
				
		Result<String, Long> result = new Result<String, Long>("Number of classe for " + ver.getID(), classCount1);
				
		return result;
	}
	
	public Result<String, Long> getObjectPropertyCount() throws SQLException  {
		PropertyService propertyService = new PropertyService(connection, ObjectPropertyType);
		long propertyCount1 = propertyService.count(ver.getID());
		
		Result<String, Long> result = new Result<String, Long>("Number of Object Property of version " + ver.getID(), propertyCount1);
		
		return result;
	}
	
	public Result<String, Long> getDatatypePropertyCount() throws SQLException  {
		PropertyService propertyService = new PropertyService(connection, DatatypePropertyType);
		long propertyCount1 = propertyService.count(ver.getID());
		
		Result<String, Long> result = new Result<String, Long>("Number of Object Property of version " + ver.getID(), propertyCount1);
		
		return result;
	}
	
	public Result<String, Long> getIndividualCount() throws SQLException  {
		ClassService classService = new ClassService(connection);
		long classCount1 = classService.individualCount(ver.getID());
				
		Result<String, Long> result = new Result<String, Long>("Number of individuals of version " + ver.getID(), classCount1);		
		
		return result;
	}
	
	public Result<String, Long> getObjectTripleTypeCount() throws SQLException  {
		
		ObjectTripleTypeService service = new ObjectTripleTypeService(connection);
		long objectTripleCount1 = service.count(ver.getID());
		
		Result<String, Long> result = new Result<String, Long>("Number of Object Triple Types of version " + ver.getID(), objectTripleCount1);
		
		return result;
	}
	
	public Result<String, Long> getDatatypeTripleTypeCount() throws SQLException  {
		
		DataTypeTripleTypeService service = new DataTypeTripleTypeService(connection);
		long datatypeTripleCount1 = service.count(ver.getID());
				
		Result<String, Long> result = new Result<String, Long>("Number of Datatype Triple Types of version " + ver.getID(), datatypeTripleCount1);
		
		return result;
	}
	
	public Result<String, Long> getRestrictionCount() throws SQLException  {
		
		RestrictionService restrictionService = new RestrictionService(connection);
		
		long restrictionCount1 = restrictionService.count(ver.getID());		
		
		Result<String, Long> result = new Result<String, Long>("Number of restrictions of version " + ver.getID(), restrictionCount1);
		
		return result;
	}

	public Result<String, Long> getExpressionCount() throws SQLException  {
		
		ExpressionService service = new ExpressionService(connection);
		
		long expressionCount1 = service.count(ver.getID());
		
		Result<String, Long> result = new Result<String, Long>("Number of expressions of version " + ver.getID(), expressionCount1);
		
		return result;
	}
	
	public List<Class> getClasses() throws SQLException{
		ClassService classService = new ClassService(connection);

		List<Class> classList = classService.listAll(ver.getID());

		return classList;
	}
	
	public List<Property> getObjectProperties() throws SQLException{
		PropertyService service = new PropertyService(connection, ObjectPropertyType);

		List<Property> propertyList = service.listAll(ver.getID());
				
		return propertyList;
	}
	
	public List<Property> getDatatypeProperties() throws SQLException{
		PropertyService service = new PropertyService(connection, DatatypePropertyType);

		List<Property> propertyList = service.listAll(ver.getID());
				
		return propertyList;
	}
	
	public List<ObjectTripleType> getObjectTripleTypes() throws SQLException{
		ObjectTripleTypeService service = new ObjectTripleTypeService(connection);

		List<ObjectTripleType> objectList = service.listAll(ver.getID());
		return objectList;
	}
	
	public List<DataTypeTripleType> getDataTypeTripleTypes() throws SQLException{
		DataTypeTripleTypeService service = new DataTypeTripleTypeService(connection);

		List<DataTypeTripleType> datatypeList = service.listAll(ver.getID());
		return datatypeList;
	}
	
	public List<Restriction> getRestrictions() throws SQLException{
		RestrictionService restrictionService = new RestrictionService(connection);
		
		List<Restriction> restrictionList = restrictionService.listAll(ver.getID());
		
		return restrictionList;
	}

	public List<Expression> getExpressions() throws SQLException{
		ExpressionService expressionService = new ExpressionService(connection);
		
		List<Expression> expressionList = expressionService.listAll(ver.getID());
		
		return expressionList;
	}

}
