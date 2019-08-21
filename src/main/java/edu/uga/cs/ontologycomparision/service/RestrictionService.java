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
	
	public int add(Restriction restriction) {
		
		int candidateId = 0;
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO class (property_id,type_id,class_id, value) VALUES (?,?,?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1,restriction.getOnProperty().getID());
			statement.setInt(2,restriction.getType().getID());
			
			if (restriction.getClass() != null)
				statement.setInt(3,restriction.getOnClass().getID());
			else
				statement.setNull(3, java.sql.Types.INTEGER);
			
			statement.setInt(4,restriction.getCardinalityValue());
						
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

}
