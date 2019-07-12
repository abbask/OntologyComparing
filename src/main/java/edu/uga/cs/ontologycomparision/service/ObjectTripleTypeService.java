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
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Class;

public class ObjectTripleTypeService {
	
	final static Logger logger = Logger.getLogger(ObjectTripleTypeService.class);
	
	public void add(ObjectTripleType objectTriple) {
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		try {
			c.setAutoCommit(false);
			
			String queryString = "INSERT INTO triple_type (count,domain_id,predicate_id,object_range_id) VALUES (?,?,?,?)";
			PreparedStatement statement= c.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1,objectTriple.getCount());
			statement.setInt(2,objectTriple.getDomain().getID());
			statement.setInt(3,objectTriple.getPredicate().getID());
			statement.setInt(2,objectTriple.getRange().getID());
			
			statement.executeUpdate();
			c.commit();
			
			c.close();
			logger.info("ObjectTripleTypeService.add : new ObjectTripleType commited.");
		} catch (Exception sqlException) {
			try {
				c.rollback();
				logger.info("ObjectTripleTypeService.add : new ObjectTripleType is rolled back.");
				c.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
			logger.error(sqlException.getMessage(), sqlException);
		}
		
	}
	
	public ObjectTripleType getByID(int id) throws SQLException {
		
		List<ObjectTripleType> list = new LinkedList<ObjectTripleType>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM triple_type where id='" + id + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			ClassService classService = new ClassService();
			Class domain = classService.getByID(rs.getInt("domain_id")); 
			
			PropertyService propertyService = new PropertyService();
			Property predicate = propertyService.getByID(rs.getInt("predicate_id"));
			
			Class range = classService.getByID(rs.getInt("object_range_id"));
//			if (rs.getInt("object_range_id") != 0 ){
//				
//			}
			
			list.add(new ObjectTripleType(rs.getInt("ID"), domain, predicate, range, rs.getLong("count")));
		}
		return list.get(0);						
				
	}

}
