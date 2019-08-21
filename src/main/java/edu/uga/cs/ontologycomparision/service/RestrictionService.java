package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Restriction;


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
			
			String queryString = "INSERT INTO class (property_id,type_id,class_id, value, version_id) VALUES (?,?,?,?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1,restriction.getOnProperty().getID());
			statement.setInt(2,restriction.getType().getID());
			
			if (restriction.getClass() != null)
				statement.setInt(3,restriction.getOnClass().getID());
			else
				statement.setNull(3, java.sql.Types.INTEGER);
			
			statement.setInt(4,restriction.getCardinalityValue());
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
		
		if (restriction.getCardinalityValue() != 0) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " value=" + restriction.getCardinalityValue();
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

}
