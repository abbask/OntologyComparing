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
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Restriction;
import edu.uga.cs.ontologycomparision.model.RestrictionType;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.XSDType;


public class RestrictionService {
	
	private Connection connection;	
	
	public RestrictionService(Connection connection) {
		this.connection = connection;
	}
	
	final static Logger logger = Logger.getLogger(RestrictionService.class);
	
	public Restriction addIfNotExist(Restriction restriction) throws SQLException {
		
		Restriction retrievedRestriction = getByRestriction(restriction);
		
		if (retrievedRestriction == null) {
			int id = add(restriction);
			restriction.setID(id);
			return restriction;

		}
		else
			return retrievedRestriction;
		
	}
	
	public int add(Restriction restriction) {
		
		int candidateId = 0;
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO restriction (property_id,type_id,class_id, value, version_id) VALUES (?,?,?,?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1,restriction.getOnProperty().getID());
			statement.setInt(2,restriction.getType().getID());
			
			if (restriction.getOnClass() != null)
				statement.setInt(3,restriction.getOnClass().getID());
			else
				statement.setNull(3, java.sql.Types.INTEGER);
			
			statement.setString(4,restriction.getValue());
			statement.setInt(5, restriction.getVersion().getID());
						
			int rowAffected = statement.executeUpdate();
			if(rowAffected == 1)
            {
				ResultSet rs = null;
                rs = statement.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }
			
			connection.commit();
			
			logger.info("RestrictionService.add : new Restriction commited.");
			
			return candidateId;
			
		} catch (Exception sqlException) {
			try {
				sqlException.printStackTrace();
				connection.rollback();
				logger.info("RestrictionService.add : new Restriction is rolled back.");
				
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
		}
		
	}
	
	public Restriction getByRestriction(Restriction restriction) throws SQLException {
		
		Statement stmtSys = connection.createStatement();
		
		String query = "SELECT * FROM restriction ";
		String whereClause = "";
		if (restriction.getOnProperty() != null) {
			whereClause += " property_id=" + restriction.getOnProperty().getID() ;
		}
		
		if (restriction.getType() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " type_id=" + restriction.getType().getID();
		}
		
		if (restriction.getOnClass() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " class_id=" + restriction.getOnClass().getID();
		}
		
		if (restriction.getValue() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " value=" + restriction.getValue();
		}
		
		if (restriction.getVersion() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " version_id= " + restriction.getVersion().getID();
		}
		
		if (whereClause != "")
			whereClause = "WHERE " + whereClause;
		
		ResultSet rs = stmtSys.executeQuery(query + whereClause); 
		
		while(rs.next()) {
			restriction.setID(rs.getInt("ID"));
		}
		
		
		if (restriction.getID() == 0) {
			return null;
		}
		
		logger.info("RestrictionService.getByRestriction : retrieved Restriction.");
		return restriction;	
				
	}
	
	public Restriction getByID(int id) throws SQLException {
		
		List<Restriction> list = new LinkedList<Restriction>();
		
		ClassService classService = new ClassService(connection);
		VersionService versionService = new VersionService(connection);
		PropertyService propertyService = new PropertyService(connection);
		RestrictionTypeService restrictionTypeService = new RestrictionTypeService(connection);
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM triple_type where id='" + id + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			
			Property onProperty = propertyService.getByID(rs.getInt("property_id"));
			Class onClass = classService.getByID(rs.getInt("class_id")); 			
						
			RestrictionType type = restrictionTypeService.get(rs.getInt("type_id"));
			String value = rs.getString("value");
			Version version = versionService.get(rs.getInt("version_id")); 
			list.add(new Restriction(id, onProperty, type, value, onClass, version));
					
		}
		return list.get(0);						
				
	}
	
	public List<Restriction> listAll(int versionId) throws SQLException{
		List<Restriction> results = new ArrayList<Restriction>();
		
		Statement stmtSys = connection.createStatement();	
		String query = "SELECT * FROM restriction WHERE version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		
		ClassService classService = new ClassService(connection);
		VersionService versionService = new VersionService(connection);
		PropertyService propertyService = new PropertyService(connection);
		RestrictionTypeService restrictionTypeService = new RestrictionTypeService(connection);
		
		
		
		while (rs.next()) {
			int iD = rs.getInt("id");
			Property onProperty = propertyService.getByID(rs.getInt("property_id"));				
			Class onClass = classService.getByID(rs.getInt("class_id")); 			
			RestrictionType restrictionType = restrictionTypeService.get(rs.getInt("type_id"));
			String value = rs.getString("value");
			
			Version version = versionService.get(rs.getInt("version_id")); 
			results.add(new Restriction(iD, onProperty, restrictionType, value, onClass, version));
			
		}
		
		return results;
			
	}
	
	public long count(int versionId) throws SQLException  {
		long count = 0 ;
				
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT count(*) as count FROM restriction where version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		if (rs.next())
			count = rs.getLong("count");
		
		return count;
		
	}

}
