package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.XSDType;

public class XSDTypeService {
	
	final static Logger logger = Logger.getLogger(XSDTypeService.class);
	
	private Connection connection;	
	
	public XSDTypeService(Connection connection) {
		this.connection = connection;
	}
	
	public XSDType addIfNotExist(XSDType type) throws SQLException {
		
		XSDType retrieveXSDType = getByURI(type.getUrl());
		
		if (retrieveXSDType == null) {
			int id = add(type);
			type.setID(id);
			return type;

		}
		else
			return retrieveXSDType;
		
	}
	
	public int add(XSDType type) {
			
		int candidateId = 0;
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO xsd_type (url,type) VALUES (?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,type.getUrl());
			statement.setString(2,type.getType());
			
			int rowAffected = statement.executeUpdate();
			if(rowAffected == 1)
            {
				ResultSet rs = null;
                rs = statement.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }
			
			connection.commit();
			
			logger.info("XSDTypeService.add : new XSDType commited.");
			return candidateId;
		} catch (Exception sqlException) {
			try {
				connection.rollback();
				logger.info("XSDTypeService.add : new XSDType is rolled back.");
				
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
		}
		
	}
	
	public XSDType getByType(String type) throws SQLException {
		
		List<XSDType> list = new LinkedList<XSDType>();		
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM xsd_type where type='" + type + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new XSDType(rs.getInt("ID"), rs.getString("url"), rs.getString("type")));
		}
		return list.get(0);						
				
	}
	
	public XSDType getByURI(String uri) throws SQLException {
		
		List<XSDType> list = new LinkedList<XSDType>();		
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM xsd_type where url='" + uri + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		XSDType type = null;
		
		while(rs.next()) {
			list.add(new XSDType(rs.getInt("ID"), rs.getString("url"), rs.getString("type")));
		}
		
		if (list.size() > 0 )
			type = list.get(0);
		return type;						
				
	}
	
	public XSDType getByID(int ID) throws SQLException {
		
		List<XSDType> list = new LinkedList<XSDType>();		
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM xsd_type where ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new XSDType(rs.getInt("ID"), rs.getString("url"), rs.getString("type")));
		}
		return list.get(0);						
				
	}

}
