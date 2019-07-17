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
import edu.uga.cs.ontologycomparision.model.Version;

public class ClassService {
	
	final static Logger logger = Logger.getLogger(ClassService.class);
	
	public Class addIfNotExist(Class myClass) throws SQLException {
				
		Class retrieveClass = getByLabel(myClass.getLabel(), myClass.getVersion().getID());
		
		if (retrieveClass == null) {
			int id = add(myClass);
			myClass.setID(id);
			return myClass;

		}
		else
			return retrieveClass;
		
	}
	
	public int add(Class myClass) {
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();	
		int candidateId = 0;
		
		try {
			c.setAutoCommit(false);
			
			String queryString = "INSERT INTO class (url,label,comment,count, version_id,parent_id) VALUES (?,?,?,?,?,?)";
			PreparedStatement statement= c.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,myClass.getUrl());
			statement.setString(2,myClass.getLabel());
			statement.setString(3,myClass.getComment());
			statement.setLong(4,myClass.getCount());
			statement.setInt(5, myClass.getVersion().getID());
			if (myClass.getParent() != null)
				statement.setInt(6, myClass.getParent().getID());
			else
				statement.setNull(6, java.sql.Types.INTEGER);
			
			
			int rowAffected = statement.executeUpdate();
			if(rowAffected == 1)
            {
				ResultSet rs = null;
                rs = statement.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }
			
			c.commit();
			System.out.println("Commited id: " + candidateId);
			
			c.close();
			mySQLConnection.closeConnection();
			
			c = null;
			mySQLConnection = null;
			
			logger.info("ClassService.add : new Class commited.");
			
			return candidateId;
			
		} catch (Exception sqlException) {
			try {
				sqlException.printStackTrace();
				c.rollback();
				logger.info("ClassService.add : new Class is rolled back.");
				c.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			logger.error(sqlException.getMessage(), sqlException);
			return 0;
		}
		
	}
	
	public Class getByLabel(String label, int versionId) throws SQLException {
		
		List<Class> list = new LinkedList<Class>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM class where version_id =" + versionId  + " and label='" + label + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			VersionService versionService = new VersionService();
			Version version = versionService.get(rs.getInt("version_id"));
			Class myClass = null;
			if (rs.getInt("parent_id") != 0)
				myClass = getByID(rs.getInt("parent_id") );
			
			list.add(new Class(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("comment"), rs.getLong("count"), version,myClass));
		}
		Class result ;
		if (list.size() > 0) 
			result = list.get(0);
		else
			result = null;
		
		mySQLConnection.closeConnection();
		c.close();
		
		return result;					
				
	}
	
	public Class getByID(int ID) throws SQLException {
		
		List<Class> list = new LinkedList<Class>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM class where ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			VersionService versionService = new VersionService();
			Version version = versionService.get(rs.getInt("version_id"));
			Class myClass = null;
			if (rs.getInt("parent_id") != 0)
				myClass = getByID(rs.getInt("parent_id") );
			
			list.add(new Class(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("comment"), rs.getLong("count"), version,myClass));
		}
		return list.get(0);						
				
	}

}
