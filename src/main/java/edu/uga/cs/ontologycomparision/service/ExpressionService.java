package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.Expression;
import edu.uga.cs.ontologycomparision.model.Restriction;

public class ExpressionService {
	
private Connection connection;	
	
	public ExpressionService(Connection connection) {
		this.connection = connection;
	}
	
	final static Logger logger = Logger.getLogger(ExpressionService.class);
	
	public int add(Expression expression) {
		
		int candidateId = 0;
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO expression (type, version_id) VALUES (?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,expression.getType());
			statement.setInt(2,expression.getVersion().getID());

						
			int rowAffected = statement.executeUpdate();
			if(rowAffected == 1)
            {
				ResultSet rs = null;
                rs = statement.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }
			
			for (Class c : expression.getClasses()) {
				String queryString2 = "INSERT INTO expression_class (expression_id, class_id) VALUES (?,?)";
				PreparedStatement statement2= connection.prepareStatement(queryString2, Statement.RETURN_GENERATED_KEYS);
				statement2.setInt(1,candidateId);
				statement2.setInt(2,c.getID());
				statement2.executeUpdate();
			}
			
			connection.commit();
			
			logger.info("ExpressionService.add : new Expression commited.");
			
			return candidateId;
			
		} catch (Exception sqlException) {
			try {
				sqlException.printStackTrace();
				connection.rollback();
				logger.info("ExpressionService.add : new Expression is rolled back.");
				
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
		}
		
	}
	
	public long count(int versionId) throws SQLException  {
		long count = 0 ;
				
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT count(*) as count FROM expression where version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		if (rs.next())
			count = rs.getLong("count");
		
		return count;
		
	}

}
