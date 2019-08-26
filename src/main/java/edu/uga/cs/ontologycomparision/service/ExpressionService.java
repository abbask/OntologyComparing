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
import edu.uga.cs.ontologycomparision.model.Expression;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;

public class ExpressionService {
	
private Connection connection;	
	
	public ExpressionService(Connection connection) {
		this.connection = connection;
	}
	
	final static Logger logger = Logger.getLogger(ExpressionService.class);
	
	public Expression addIfNotExist(Expression expression) throws SQLException {
		
		Expression retrievedExpression = getByExpression(expression);
		
		if (retrievedExpression == null) {
			int id = add(expression);
			expression.setId(id);
			return expression;

		}
		else
			return retrievedExpression;
		
	}
	
	public Expression getByExpression(Expression expression) throws SQLException {
		
		Statement stmtSys = connection.createStatement();
		
		String query = "SELECT * FROM expression ";
		String whereClause = "";
		if (expression.getOnProperty() != null) {
			whereClause += " property_id=" + expression.getOnProperty().getID() ;
		}
		
		if (expression.getType() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " type='" + expression.getType() + "' ";
		}
		
		if (expression.getOnClass() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " class_id=" + expression.getOnClass().getID();
		}
		
		if (expression.getVersion() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " version_id= " + expression.getVersion().getID();
		}
		
		if (whereClause != "")
			whereClause = "WHERE " + whereClause;
		
		ResultSet rs = stmtSys.executeQuery(query + whereClause); 
		
		while(rs.next()) {
			expression.setId(rs.getInt("ID"));
		}
		
		
		if (expression.getId() == 0) {
			return null;
		}
		
		logger.info("ExpressionService.getByExpression : retrieved Expression.");
		return expression;	
				
	}
	
	public int add(Expression expression) {
		
		int candidateId = 0;
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO expression (type,class_id, property_id, version_id) VALUES (?,?,?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,expression.getType());
			
			if (expression.getOnClass() != null)
				statement.setInt(2,expression.getOnClass().getID());
			else
				statement.setNull(2, java.sql.Types.INTEGER);
			
			if (expression.getOnProperty() != null)
				statement.setInt(3,expression.getOnProperty().getID());
			else
				statement.setNull(3, java.sql.Types.INTEGER);
			
			statement.setInt(4,expression.getVersion().getID());

						
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
	
	public List<Expression> listAll(int versionId) throws SQLException{
		List<Expression> results = new ArrayList<Expression>();
		
		Statement stmtSys = connection.createStatement();	
		String query = "SELECT * FROM expression WHERE version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		
		ClassService classService = new ClassService(connection);
		PropertyService propertyService = new PropertyService(connection);
		VersionService versionService = new VersionService(connection);				
		
		while (rs.next()) {
			int iD = rs.getInt("id");
			String type = rs.getString("type");
			
			Statement stmtExp = connection.createStatement();	
			String queryExp = "SELECT * FROM expression_class WHERE expression_id=" + iD;
			ResultSet rsExp = stmtExp.executeQuery(queryExp); 
			
			List<Class> classes = new LinkedList<Class>();
			while (rsExp.next()) {
				int classId = rsExp.getInt("id");
				Class myClass= classService.getByID(classId);
				classes.add(myClass);
			}
			Version version = versionService.get(rs.getInt("version_id")); 		
			Class onClass = classService.getByID(rs.getInt("class_id"));
			Property onProperty = propertyService.getByID(rs.getInt("property_id"));
			
			Expression expression = new Expression(type,onClass, onProperty, classes, version);	
			results.add(expression);
			
		}
		
		return results;
			
	}

}
