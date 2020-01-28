package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Version;

public class VersionService {
	
	final static Logger logger = Logger.getLogger(VersionService.class);
	
	private Connection connection;	
	
	public VersionService(Connection connection) {
		this.connection = connection;
	}
	
	public List<Version> getListAll() throws SQLException {
		
		List<Version> list = new LinkedList<Version>();
				
		
		connection.setAutoCommit(false);
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM version ORDER BY ID";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new Version(rs.getInt("ID"), rs.getString("name"), rs.getString("number"), rs.getDate("date")));
		}
//		logger.info("VersionService.listAll : list version commited.");
		return list;						
				
	}
	
	public void add(Version version) {		
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO version (name,number,date) VALUES (?,?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,version.getName());
			statement.setString(2,version.getNumber());
			statement.setDate(3,new java.sql.Date(version.getDate().getTime()));
			
			statement.executeUpdate();
			connection.commit();
			
			
			logger.info("VersionService.add : new version commited.");
		} catch (Exception sqlException) {
			try {
				connection.rollback();
				logger.info("VersionService.add : new version is rolled back.");
				
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
			logger.error(sqlException.getMessage(), sqlException);
		}
		
	}
	
	/**
	 * Get version using ID
	 * @param ID
	 * @return Version
	 * @throws SQLException
	 */
	
	public Version get(int ID) throws SQLException {
		
		List<Version> list = new LinkedList<Version>();	
		
		connection.setAutoCommit(false);
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM version WHERE ID=" + ID;
//		System.out.println("query: " + query);
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new Version(rs.getInt("ID"), rs.getString("name"), rs.getString("number"), rs.getDate("date")));
		}
		
		return list.get(0);				
	}
	
	public boolean dataExist(int versionId) throws SQLException{
		ClassService classService = new ClassService(connection);
		PropertyService propertyService = new PropertyService(connection);
		ObjectTripleTypeService objectService = new ObjectTripleTypeService(connection);
		DataTypeTripleTypeService datatypeService = new DataTypeTripleTypeService(connection);
		
		if (classService.checkExist(versionId) && propertyService.checkExist(versionId) && objectService.checkExist(versionId) && datatypeService.checkExist(versionId))
			return true;
		
		return false;
		
	}

}
