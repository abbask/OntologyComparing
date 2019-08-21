package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.RestrictionType;

public class RestrictionTypeService {
	
final static Logger logger = Logger.getLogger(RestrictionTypeService.class);
	
	private Connection connection;	
	
	public RestrictionTypeService(Connection connection) {
		this.connection = connection;
	}
	
	public RestrictionType addIfNotExist(RestrictionType restrictionType) throws SQLException {
		
		RestrictionType retrievedRestrictionType = getByType(restrictionType.getType());
		
		if (retrievedRestrictionType == null) {
			int id = add(restrictionType);
			restrictionType.setID(id);
			return restrictionType;

		}
		else
			return retrievedRestrictionType;
		
	}
	
	public int add(RestrictionType restrictionType) {
		
		int candidateId = 0;
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO restriction_type (type) VALUES (?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			
			statement.setString(1,restrictionType.getType());
						
			int rowAffected = statement.executeUpdate();
			if(rowAffected == 1)
            {
				ResultSet rs = null;
                rs = statement.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }
			
			connection.commit();
			
			logger.info("RestrictionTypeService.add : new RestrictionType commited.");
			
			return candidateId;
			
		} catch (Exception sqlException) {
			try {
				sqlException.printStackTrace();
				connection.rollback();
				logger.info("RestrictionTypeService.add : new RestrictionType is rolled back.");
				
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
		}
		
	}
	
	public RestrictionType get(int ID) throws SQLException {
		
		List<RestrictionType> list = new LinkedList<RestrictionType>();	
		
		connection.setAutoCommit(false);
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM restriction_type WHERE ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new RestrictionType(rs.getInt("ID"), rs.getString("type")));
		}
		
		return list.get(0);				
	}
	
	public List<RestrictionType> getListAll() throws SQLException {
		
		List<RestrictionType> list = new LinkedList<RestrictionType>();		
		
		connection.setAutoCommit(false);
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM restriction_type ORDER BY ID";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new RestrictionType(rs.getInt("ID"), rs.getString("type")));
		}
		return list;						
				
	}
	
	public RestrictionType getByType(String type) throws SQLException {
		
		List<RestrictionType> list = new LinkedList<RestrictionType>();
						
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM restriction_type where type='" + type + "'";
		ResultSet rs = stmtSys.executeQuery(query); 			
		
		while(rs.next()) {
			
			RestrictionType retrictionType = new RestrictionType(rs.getInt("id"), type);
			
			list.add(retrictionType);
		}
		RestrictionType result ;
		if (list.size() > 0) 
			result = list.get(0);
		else {
			
			result = null;
		}
					
		logger.info("RestrictionTypeService.getByType : retrieved Restriction Type.");
		return result;					
				
	}

}
