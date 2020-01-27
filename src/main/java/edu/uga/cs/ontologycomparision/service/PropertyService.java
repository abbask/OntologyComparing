package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.DomainRange;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;

public class PropertyService {
	
	final static Logger logger = Logger.getLogger(ClassService.class);
	
	private Connection connection;	
	
	private String type;
	
	public PropertyService(Connection connection) {
		this.connection = connection;
	}
	
	public PropertyService(Connection connection, String type) {
		this.connection = connection;
		this.type = type;
	}
	
	public Property addIfNotExist(Property myProperty) throws SQLException {
		
		Property retrieveProperty = getByLabel(myProperty.getLabel(), myProperty.getVersion().getID());
		
//		System.out.println(retrieveProperty);
		
		if (retrieveProperty == null) {
			int id = add(myProperty);
			myProperty.setID(id);
			return myProperty;

		}
		else {
			return retrieveProperty;
		}
		
	}
	
	public int add(Property property) {
		
			
		int candidateId = 0;
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO property (url,label,type,comment,version_id,parent_id) VALUES (?,?,?,?,?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,property.getUrl());
			statement.setString(2,property.getLabel());
			statement.setString(3,property.getType());
			statement.setString(4,property.getComment());
			statement.setInt(5, property.getVersion().getID());		
			if (property.getParent() != null)
				statement.setInt(6, property.getParent().getID());
			else
				statement.setNull(6, java.sql.Types.INTEGER);
			
			int rowAffected = statement.executeUpdate();
			if(rowAffected == 1)
            {
				ResultSet rs = null;
                rs = statement.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }
			
			for(DomainRange dr : property.getDomainRanges()) {
				String queryStringParent = "INSERT INTO domain_range (property_id, type, class_id) VALUES (?,?,?)";
				PreparedStatement statementParent= connection.prepareStatement(queryStringParent, Statement.RETURN_GENERATED_KEYS);
				statementParent.setInt(1,candidateId);
				statementParent.setString(2, dr.getType());
				statementParent.setInt(3,dr.getTheClass().getID());
				statementParent.executeUpdate();
			}
			
			connection.commit();
			
			logger.info("PropertyService.add : new Property commited.");
			
			return candidateId;
			
		} catch (Exception sqlException) {
			try {
				connection.rollback();
				logger.info("PropertyService.add : new Property is rolled back.");
				
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
		}
		
	}
	
	public Property getByLabel(String label, int versionId) throws SQLException {
		
		List<Property> list = new LinkedList<Property>();		
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM property where version_id =" + versionId  + " and label='" + label + "'";
//		System.out.println(query);
		
		ResultSet rs = stmtSys.executeQuery(query); 
		
		ClassService classService = new ClassService(connection);
		VersionService versionService = new VersionService(connection);
		
		while(rs.next()) {
			
			Version version = versionService.get(rs.getInt("version_id"));
			Property prop = null;
			if (rs.getInt("parent_id") != 0)
				prop = getByID(rs.getInt("parent_id") );

			Property property = new Property(rs.getInt("ID"), rs.getString("url"), rs.getString("label"), rs.getString("type"), rs.getString("comment"), version, prop);
			
			int propertyId = rs.getInt("ID");
			List<DomainRange> domainRanges = new ArrayList<DomainRange>();
			Statement stmtParent = connection.createStatement();			
			String queryParent = "SELECT * FROM domain_range where property_id =" + propertyId ;
			ResultSet rsP = stmtParent.executeQuery(queryParent); 
			while(rsP.next()) {
				int domainRangeId = rsP.getInt("ID");
				
				Class theClass = classService.getByID(rsP.getInt("class_id"));				
				DomainRange domainRange = new DomainRange(domainRangeId, property, rsP.getString("type"), theClass);
				
				domainRanges.add(domainRange);
				
			}
			property.setDomainRanges(domainRanges);
			list.add(property);
		}
		Property result ;
		if (list.size() > 0) 
			result = list.get(0);
		else
			result = null;
		
		
		logger.info("PropertyService.getByLabel : retrieved property.");
		return result;					
				
	}
	
	public Property getByID(int ID) throws SQLException {
		
		List<Property> list = new LinkedList<Property>();		
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM property where ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		VersionService versionService = new VersionService(connection);
		ClassService classService = new ClassService(connection);
		
//		System.out.println("QUERY: " + query);
		Property result = null;
		
		while(rs.next()) {
			
			Version version = versionService.get(rs.getInt("version_id"));
			Property prop = null;
			if (rs.getInt("parent_id") != 0)
				prop = getByID(rs.getInt("parent_id") );
			
			Property property = new Property(rs.getInt("ID"), rs.getString("url"), rs.getString("label"), rs.getString("type"), rs.getString("comment"), version, prop);
			
			int propertyId = rs.getInt("ID");
			List<DomainRange> domainRanges = new ArrayList<DomainRange>();
			Statement stmtParent = connection.createStatement();			
			String queryParent = "SELECT * FROM domain_range where property_id =" + propertyId ;
			ResultSet rsP = stmtParent.executeQuery(queryParent); 
			while(rsP.next()) {
				int domainRangeId = rsP.getInt("ID");
				
				Class theClass = classService.getByID(rsP.getInt("class_id"));				
				DomainRange domainRange = new DomainRange(domainRangeId, property, rsP.getString("type"), theClass);
				
				domainRanges.add(domainRange);
				
			}
			property.setDomainRanges(domainRanges);
			
		}
		if (list.size() > 0) 
			result = list.get(0);
		else
			result = null;
		
		logger.info("PropertyService.getByID : retrieved property.");
		return result;						
				
	}

	public long count(int versionId) throws SQLException  {
		long count = 0 ;			
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT count(*) as count FROM property where type='" + type + "' and version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		if (rs.next())
			count = rs.getLong("count");
		return count;
		
	}
	
	public List<Property> listAll(int versionId) throws SQLException{
		List<Property> results = new ArrayList<Property>();
		
		Statement stmtSys = connection.createStatement();	
		String query = "SELECT * FROM property where type='" + type + "' AND version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		VersionService versionService = new VersionService(connection);
		ClassService classService = new ClassService(connection);
		
		while (rs.next()) {
			Version version = versionService.get(versionId);
			Property parentProperty = null;
			if (rs.getInt("parent_id") != 0)
				parentProperty = getByID(rs.getInt("parent_id") );

			Property property = new Property(rs.getInt("ID"), rs.getString("url"), rs.getString("label"), rs.getString("type"), rs.getString("comment"), version, parentProperty);
			
			int propertyId = rs.getInt("ID");
			List<DomainRange> domainRanges = new ArrayList<DomainRange>();
			Statement stmtParent = connection.createStatement();			
			String queryParent = "SELECT * FROM domain_range where property_id =" + propertyId ;
			ResultSet rsP = stmtParent.executeQuery(queryParent); 
			while(rsP.next()) {
				int domainRangeId = rsP.getInt("ID");
				
				Class theClass = classService.getByID(rsP.getInt("class_id"));				
				DomainRange domainRange = new DomainRange(domainRangeId, property, rsP.getString("type"), theClass);
				
				domainRanges.add(domainRange);
				
			}
			property.setDomainRanges(domainRanges);
			
		}
		
		return results;
			
	}
	
	public boolean checkExist(int versionId) throws SQLException{
				
		Statement stmtSys = connection.createStatement();	
		String query = "SELECT * FROM property where version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		if (rs.next() == true)
			return true;
		
		return false;
			
	}
}
