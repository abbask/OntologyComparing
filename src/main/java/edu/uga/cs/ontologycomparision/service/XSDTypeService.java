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
import edu.uga.cs.ontologycomparision.model.XSDType;

public class XSDTypeService {
	
	final static Logger logger = Logger.getLogger(XSDTypeService.class);
	
	public void add(XSDType type) {
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		try {
			c.setAutoCommit(false);
			
			String queryString = "INSERT INTO xsd_type (url,type) VALUES (?,?)";
			PreparedStatement statement= c.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,type.getUrl());
			statement.setString(2,type.getType());
			
			statement.executeUpdate();
			c.commit();
			
			c.close();
			logger.info("XSDTypeService.add : new XSDType commited.");
		} catch (Exception sqlException) {
			try {
				c.rollback();
				logger.info("XSDTypeService.add : new XSDType is rolled back.");
				c.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
			logger.error(sqlException.getMessage(), sqlException);
		}
		
	}
	
	public XSDType getByType(String type) throws SQLException {
		
		List<XSDType> list = new LinkedList<XSDType>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM xsd_type where type='" + type + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new XSDType(rs.getInt("ID"), rs.getString("url"), rs.getString("type")));
		}
		return list.get(0);						
				
	}
	
	public XSDType getByURI(String uri) throws SQLException {
		
		List<XSDType> list = new LinkedList<XSDType>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM xsd_type where url='" + uri + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new XSDType(rs.getInt("ID"), rs.getString("url"), rs.getString("type")));
		}
		return list.get(0);						
				
	}
	
	public XSDType getByID(int ID) throws SQLException {
		
		List<XSDType> list = new LinkedList<XSDType>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM xsd_type where ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new XSDType(rs.getInt("ID"), rs.getString("url"), rs.getString("type")));
		}
		return list.get(0);						
				
	}

}
