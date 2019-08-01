package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.ObjectProperty;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.Class;

public class ObjectPropertyService {
	
final static Logger logger = Logger.getLogger(ObjectPropertyService.class);
	
	private Connection connection;	
	
	public ObjectPropertyService(Connection connection) {
		this.connection = connection;
	}
	
	public ObjectProperty addIfNotExist(ObjectProperty myProperty) throws SQLException {
		
		ObjectProperty retrieveProperty = getByLabel(myProperty.getLabel(), myProperty.getVersion().getID());
		
		if (retrieveProperty == null) {
			int id = add(myProperty);
			myProperty.setID(id);
			return myProperty;

		}
		else {
			return retrieveProperty;
		}
		
	}
	
	public int add(ObjectProperty property) {
		
		
		int candidateId = 0;
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO property (url,label,domain_id, object_range_id, comment,version_id,parent_id) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1,property.getUri());
			statement.setString(2,property.getLabel());
			
			if (property.getDomain() != null) 
				statement.setInt(3,property.getDomain().getID());
			else
				statement.setNull(3, java.sql.Types.INTEGER);
			
			if (property.getRange() != null) 
				statement.setInt(4,property.getRange().getID());
			else
				statement.setNull(4, java.sql.Types.INTEGER);
						
			statement.setString(5,property.getComment());
			statement.setInt(6, property.getVersion().getID());		
			
			if (property.getParent() != null)
				statement.setInt(7, property.getParent().getID());
			else
				statement.setNull(7, java.sql.Types.INTEGER);
			
			int rowAffected = statement.executeUpdate();
			if(rowAffected == 1)
            {
				ResultSet rs = null;
                rs = statement.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }			
			
			connection.commit();
			
			logger.info("ObjectPropertyService.add : new Object Property commited.");
			
			return candidateId;
			
		} catch (Exception sqlException) {
			try {
				connection.rollback();
				logger.info("ObjectPropertyService.add : new Object Property is rolled back.");
				
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
		}
		
	}
	

	public ObjectProperty getByLabel(String label, int versionId) throws SQLException {
		
		List<ObjectProperty> list = new LinkedList<ObjectProperty>();		
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM property where version_id =" + versionId  + " and label='" + label + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		VersionService versionService = new VersionService(connection);
		
		while(rs.next()) {
			
			Version version = versionService.get(rs.getInt("version_id"));
			ObjectProperty parent = null;
			if (rs.getInt("parent_id") != 0)
				parent = getByID(rs.getInt("parent_id") );
			
			Class domain = null, range = null;
			ClassService classService = new ClassService(connection);
			if (rs.getInt("domain_id") != 0)
				domain = classService.getByID(rs.getInt("domain_id") );
			if (rs.getInt("object_range_id") != 0)
				range = classService.getByID(rs.getInt("object_range_id") );
						
			list.add(new ObjectProperty(rs.getInt("ID"), rs.getString("url"), rs.getString("label"), rs.getString("comment"), version,parent,domain,range));
		}
		
		ObjectProperty result ;
		if (list.size() > 0) 
			result = list.get(0);
		else
			result = null;
		
		
		logger.info("ObjectPropertyService.getByLabel : retrieved property.");
		return result;					
				
	}
	
	public ObjectProperty getByID(int ID) throws SQLException {
		
		List<ObjectProperty> list = new LinkedList<ObjectProperty>();		
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM property where ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		VersionService versionService = new VersionService(connection);
		
		while(rs.next()) {
			
			Version version = versionService.get(rs.getInt("version_id"));
			ObjectProperty parent = null;
			if (rs.getInt("parent_id") != 0)
				parent = getByID(rs.getInt("parent_id") );
			
			Class domain = null, range = null;
			ClassService classService = new ClassService(connection);
			if (rs.getInt("domain_id") != 0)
				domain = classService.getByID(rs.getInt("domain_id") );
			if (rs.getInt("object_range_id") != 0)
				range = classService.getByID(rs.getInt("object_range_id") );
			
			
			list.add(new ObjectProperty(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("comment"), version,parent, domain, range));
		}
		logger.info("ObjectPropertyService.getByID : retrieved property.");
		return list.get(0);						
				
	}


}
