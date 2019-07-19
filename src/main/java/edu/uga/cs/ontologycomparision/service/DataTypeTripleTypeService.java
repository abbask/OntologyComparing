package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.xml.crypto.dsig.keyinfo.X509Data;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.XSDType;

public class DataTypeTripleTypeService {
	
	final static Logger logger = Logger.getLogger(DataTypeTripleTypeService.class);
	
	public DataTypeTripleType addIfNotExist(DataTypeTripleType dataTypeTripleType) throws SQLException {
		
		DataTypeTripleType retrieveDataTypeTripleType = getByTriple(dataTypeTripleType);
		
		if (retrieveDataTypeTripleType == null) {
			int id = add(dataTypeTripleType);
			dataTypeTripleType.setID(id);
			return dataTypeTripleType;
		}
		else {
			
			return dataTypeTripleType;
		}
		
	}
	
	public int add(DataTypeTripleType dataTypeTriple) {
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		int candidateId = 0;
		
		try {
			c.setAutoCommit(false);
			
			int domainID = 0 ;
			if (dataTypeTriple.getDomain() != null) {
				domainID = dataTypeTriple.getDomain().getID();
			}
			
			int predicateID = 0 ;
			if (dataTypeTriple.getPredicate() != null) {
				predicateID = dataTypeTriple.getPredicate().getID();
			}
			
			int rangeID = 0 ;
			if (dataTypeTriple.getRange() != null) {
				rangeID = dataTypeTriple.getRange().getID();
			}
			
			String queryString = "INSERT INTO triple_type (count,domain_id,predicate_id, xsd_type_id, version_id) VALUES (?,?,?,?,?)";
			PreparedStatement statement= c.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1,dataTypeTriple.getCount());
			statement.setInt(2,domainID);
			statement.setInt(3,predicateID);
			statement.setInt(4,rangeID);
			statement.setInt(5,dataTypeTriple.getVersion().getID());
			
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
			logger.info("DataTypeTripleTypeService.add : new DataTypeTripleType commited.");
			return candidateId;
		} catch (Exception sqlException) {
			try {
				c.rollback();
				logger.info("DataTypeTripleTypeService.add : new DataTypeTripleType is rolled back.");
				c.close();
				return 0;
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
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
			XSDType range = typeService.getByID(rs.getInt("datatype_range_id"));
			
			VersionService versionService = new VersionService();
			Version version = versionService.get(rs.getInt("version_id")); 
			
			list.add(new DataTypeTripleType(rs.getInt("ID"), domain, predicate, range, rs.getLong("count"),version));
		}
		return list.get(0);						
				
	}
	
	public DataTypeTripleType getByTriple(DataTypeTripleType dataTypeTripleType) throws SQLException {
		
		List<DataTypeTripleType> list = new LinkedList<DataTypeTripleType>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();
		
		String query = "SELECT * FROM triple_type ";
		String whereClause = "";
		if (dataTypeTripleType.getDomain() != null) {
			whereClause += " domain_id=" + dataTypeTripleType.getDomain().getID() ;
		}
		
		if (dataTypeTripleType.getPredicate() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " predicate_id= " + dataTypeTripleType.getPredicate().getID();
		}
		
		if (dataTypeTripleType.getRange() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " xsd_type_id= " + dataTypeTripleType.getRange().getID();
		}
		
		if (dataTypeTripleType.getVersion() != null) {
			if (whereClause != "")
				whereClause += " AND";
			whereClause += " version_id= " + dataTypeTripleType.getVersion().getID();
		}
		
		if (whereClause != "")
			whereClause = "WHERE " + whereClause;
		
		ResultSet rs = stmtSys.executeQuery(query + whereClause); 
		
		while(rs.next()) {
			ClassService classService = new ClassService();
			Class domain = classService.getByID(rs.getInt("domain_id")); 
			
			PropertyService propertyService = new PropertyService();
			Property predicate = propertyService.getByID(rs.getInt("predicate_id"));
			
			XSDTypeService xsdTypeService = new XSDTypeService();
			XSDType range = xsdTypeService.getByID(rs.getInt("xsd_type_id"));
			
			VersionService versionService = new VersionService();
			Version version = versionService.get(rs.getInt("version_id")); 
			
			list.add(new DataTypeTripleType(rs.getInt("ID"), domain, predicate, range, rs.getLong("count"),version));
		}
		
		DataTypeTripleType result = null;
		if (list.size()>0) {
			result = list.get(0);
		}
		
		logger.info("DataTypeTripleTypeService.getByTriple : retrieved DataTypeTripleType.");
		return result;						
				
	}

}
