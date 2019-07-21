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
		ResultSet rs = stmtSys.executeQuery(query); 
		
		VersionService versionService = new VersionService(connection);
		
		while(rs.next()) {
			
			Version version = versionService.get(rs.getInt("version_id"));
			Property prop = null;
			if (rs.getInt("parent_id") != 0)
				prop = getByID(rs.getInt("parent_id") );
			
			list.add(new Property(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("type"), rs.getString("comment"), version,prop));
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
		
		while(rs.next()) {
			
			Version version = versionService.get(rs.getInt("version_id"));
			Property prop = null;
			if (rs.getInt("parent_id") != 0)
				prop = getByID(rs.getInt("parent_id") );
			
			list.add(new Property(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("type"), rs.getString("comment"), version,prop));
		}
		logger.info("PropertyService.getByID : retrieved property.");
		return list.get(0);						
				
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
		while (rs.next()) {
			Version version = versionService.get(versionId);
			Property parentProperty = null;
			if (rs.getInt("parent_id") != 0)
				parentProperty = getByID(rs.getInt("parent_id") );
			results.add(new Property(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("type"),rs.getString("comment"), version,parentProperty));
		}
		
		return results;
			
	}
}
