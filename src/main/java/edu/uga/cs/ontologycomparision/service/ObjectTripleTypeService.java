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
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.Class;

public class ObjectTripleTypeService {
	
	final static Logger logger = Logger.getLogger(ObjectTripleTypeService.class);
	
	public ObjectTripleType addIfNotExist(ObjectTripleType objectTripleType) throws SQLException {
		
		ObjectTripleType retrieveObjectTripleType = getByTriple(objectTripleType);
		
		if (retrieveObjectTripleType == null) {
			int id = add(objectTripleType);
			objectTripleType.setID(id);
			return objectTripleType;
		}
		else {
			
			return objectTripleType;
		}
		
	}
	
	public int add(ObjectTripleType objectTriple) {
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();
		int candidateId = 0;
		
		try {
			c.setAutoCommit(false);
			
			String queryString = "INSERT INTO triple_type (count,domain_id,predicate_id,object_range_id,version_id) VALUES (?,?,?,?,?)";
			PreparedStatement statement= c.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1,objectTriple.getCount());
			statement.setInt(2,objectTriple.getDomain().getID());
			statement.setInt(3,objectTriple.getPredicate().getID());
			statement.setInt(4,objectTriple.getRange().getID());
			statement.setInt(5,objectTriple.getVersion().getID());
			
			statement.executeUpdate();			
			
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
			mySQLConnection.closeConnection();
			
			logger.info("ObjectTripleTypeService.add : new ObjectTripleType commited.");
			
			return candidateId;
		} catch (Exception sqlException) {
			try {
				
				sqlException.printStackTrace();
				c.rollback();
				logger.info("ObjectTripleTypeService.add : new ObjectTripleType is rolled back.");
				c.close();
				mySQLConnection.closeConnection();
				return 0;
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
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
			
			VersionService versionService = new VersionService();
			Version version = versionService.get(rs.getInt("version_id")); 
			
			list.add(new ObjectTripleType(rs.getInt("ID"), domain, predicate, range, rs.getLong("count"),version));
		}
		return list.get(0);						
				
	}
	
	public ObjectTripleType getByTriple(ObjectTripleType objectTripleType) throws SQLException {
		
		List<ObjectTripleType> list = new LinkedList<ObjectTripleType>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();
		
		String query = "SELECT * FROM triple_type ";
		String whereClause = "";
		if (objectTripleType.getDomain() != null) {
			whereClause += " domain_id=" + objectTripleType.getDomain().getID() ;
		}
		
		if (objectTripleType.getPredicate() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " predicate_id= " + objectTripleType.getPredicate().getID();
		}
		
		if (objectTripleType.getRange() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " object_range_id= " + objectTripleType.getRange().getID();
		}
		
		if (objectTripleType.getVersion() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " version_id= " + objectTripleType.getVersion().getID();
		}
		
		if (whereClause != "")
			whereClause = "WHERE " + whereClause;
		
		ResultSet rs = stmtSys.executeQuery(query + whereClause); 
		
		while(rs.next()) {
			ClassService classService = new ClassService();
			Class domain = classService.getByID(rs.getInt("domain_id")); 
			
			PropertyService propertyService = new PropertyService();
			Property predicate = propertyService.getByID(rs.getInt("predicate_id"));
			
			Class range = classService.getByID(rs.getInt("object_range_id"));
			
			VersionService versionService = new VersionService();
			Version version = versionService.get(rs.getInt("version_id")); 
			
			list.add(new ObjectTripleType(rs.getInt("ID"), domain, predicate, range, rs.getLong("count"),version));
		}
		
		ObjectTripleType result = null;
		if (list.size()>0) {
			result = list.get(0);
		}
		
		logger.info("ObjectTripleTypeService.getByTriple : retrieved ObjectTripleType.");
		return result;						
				
	}

}
