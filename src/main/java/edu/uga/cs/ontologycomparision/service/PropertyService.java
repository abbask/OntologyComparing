package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;

public class PropertyService {
	
	final static Logger logger = Logger.getLogger(ClassService.class);
	
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
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();		
		int candidateId = 0;
		
		try {
			c.setAutoCommit(false);
			
			String queryString = "INSERT INTO property (url,label,comment,count, version_id,parent_id) VALUES (?,?,?,?,?,?)";
			PreparedStatement statement= c.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,property.getUrl());
			statement.setString(2,property.getLabel());
			statement.setString(3,property.getComment());
			statement.setLong(4,property.getCount());
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
			
			
			c.commit();
			
			c.close();
			logger.info("PropertyService.add : new Property commited.");
			
			return candidateId;
			
		} catch (Exception sqlException) {
			try {
				c.rollback();
				logger.info("PropertyService.add : new Property is rolled back.");
				c.close();
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
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM property where version_id =" + versionId  + " and label='" + label + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			VersionService versionService = new VersionService();
			Version version = versionService.get(rs.getInt("version_id"));
			Property prop = null;
			if (rs.getInt("parent_id") != 0)
				prop = getByID(rs.getInt("parent_id") );
			
			list.add(new Property(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("comment"), rs.getLong("count"), version,prop));
		}
		Property result ;
		if (list.size() > 0) 
			result = list.get(0);
		else
			result = null;
		
		mySQLConnection.closeConnection();
		c.close();
		
		logger.info("PropertyService.getByLabel : retrieved property.");
		return result;					
				
	}
	
	public Property getByID(int ID) throws SQLException {
		
		List<Property> list = new LinkedList<Property>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM property where ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			VersionService versionService = new VersionService();
			Version version = versionService.get(rs.getInt("version_id"));
			Property prop = null;
			if (rs.getInt("parent_id") != 0)
				prop = getByID(rs.getInt("parent_id") );
			
			list.add(new Property(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("comment"), rs.getLong("count"), version,prop));
		}
		logger.info("PropertyService.getByID : retrieved property.");
		return list.get(0);						
				
	}


}
