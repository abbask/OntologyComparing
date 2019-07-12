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
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.XSDType;

public class DataTypeTripleTypeService {
	
	final static Logger logger = Logger.getLogger(DataTypeTripleTypeService.class);
	
	public void add(DataTypeTripleType dataTypeTriple) {
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		try {
			c.setAutoCommit(false);
			
			String queryString = "INSERT INTO triple_type (count,domain_id,predicate_id, datatype_range_id) VALUES (?,?,?,?)";
			PreparedStatement statement= c.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1,dataTypeTriple.getCount());
			statement.setInt(2,dataTypeTriple.getDomain().getID());
			statement.setInt(3,dataTypeTriple.getPredicate().getID());
			statement.setInt(4,dataTypeTriple.getRange().getID());
			
			
			statement.executeUpdate();
			c.commit();
			
			c.close();
			logger.info("DataTypeTripleTypeService.add : new DataTypeTripleType commited.");
		} catch (Exception sqlException) {
			try {
				c.rollback();
				logger.info("DataTypeTripleTypeService.add : new DataTypeTripleType is rolled back.");
				c.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
			logger.error(sqlException.getMessage(), sqlException);
		}
		
	}
	
	public DataTypeTripleType getByID(int id) throws SQLException {
		
		List<DataTypeTripleType> list = new LinkedList<DataTypeTripleType>();
				
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
			
			XSDTypeService typeService = new XSDTypeService();
			XSDType range = typeService.get(rs.getInt("datatype_range_id"));
			

			
			list.add(new DataTypeTripleType(rs.getInt("ID"), domain, predicate, range, rs.getLong("count")));
		}
		return list.get(0);						
				
	}

}
