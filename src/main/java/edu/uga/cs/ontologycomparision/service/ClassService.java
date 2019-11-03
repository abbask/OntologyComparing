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
import edu.uga.cs.ontologycomparision.model.Version;

public class ClassService {
		
	private Connection connection;	
	
	public ClassService(Connection connection) {
		this.connection = connection;
	}
	
	final static Logger logger = Logger.getLogger(ClassService.class);
	
	public Boolean checkIfExists(String comment, int versionId) throws SQLException {
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM class where version_id =" + versionId  + " and comment='" + comment + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		if (rs.next()) 
			return false;
		else
			return true;
		
	}
	
	public Class addIfNotExist(Class myClass) throws SQLException {
		Class retrieveClass = getByURI(myClass.getUrl(), myClass.getVersion().getID());
		
		if (retrieveClass == null) {
			int id = add(myClass);
			myClass.setID(id);
			return myClass;

		}
		else
			return retrieveClass;
		
	}
	
	public int add(Class myClass) {
		
		int candidateId = 0;
		
		if (myClass.getLabel().isEmpty()) {
			myClass.setLabel(myClass.getComment());
		}
		
		try {
			connection.setAutoCommit(false);
			
			String queryString = "INSERT INTO class (url,label,comment,count, version_id) VALUES (?,?,?,?,?)";
			PreparedStatement statement= connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,myClass.getUrl());
			statement.setString(2,myClass.getLabel());
			statement.setString(3,myClass.getComment());
			statement.setLong(4,myClass.getCount());
			statement.setInt(5, myClass.getVersion().getID());
			
			int rowAffected = statement.executeUpdate();
			if(rowAffected == 1)
            {
				ResultSet rs = null;
                rs = statement.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
 
            }
			
			for(Class parent : myClass.getParents()) {
				String queryStringParent = "INSERT INTO class_parent (class_id,parent_id) VALUES (?,?)";
				PreparedStatement statementParent= connection.prepareStatement(queryStringParent, Statement.RETURN_GENERATED_KEYS);
				statementParent.setInt(1,candidateId);
				statementParent.setInt(2,parent.getID());
				statementParent.executeUpdate();
			}
			
			connection.commit();
			
			logger.info("ClassService.add : new Class commited.");
			
			return candidateId;
			
		} catch (Exception sqlException) {
			try {
				sqlException.printStackTrace();
				connection.rollback();
				logger.info("ClassService.add : new Class is rolled back.");
				
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);				
				return 0;
			}
			logger.error(sqlException.getMessage(), sqlException);			
			return 0;
		}
		
	}
	
	public Class getByURI(String nodeId, int versionId) throws SQLException {
		
		List<Class> list = new LinkedList<Class>();
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM class where version_id =" + versionId  + " and url='" + nodeId + "'";
		ResultSet rs = stmtSys.executeQuery(query); 
		VersionService versionService = new VersionService(connection);
		
//		System.out.println(query);
		
		
		while(rs.next()) {
			
			Version version = versionService.get(rs.getInt("version_id"));
			Class myClass = new Class(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),
					rs.getString("comment"), rs.getLong("count"), version);
			
			int classId = rs.getInt("ID");
			List<Class> parents = new ArrayList<Class>();
			Statement stmtParent = connection.createStatement();			
			String queryParent = "SELECT * FROM class_parent where class_id =" + classId ;
			ResultSet rsP = stmtParent.executeQuery(queryParent); 
			while(rsP.next()) {
				int parentId = rsP.getInt("ID");
				Class parent = getByID(parentId);
				parents.add(parent);
				
			}
			myClass.setParents(parents);
			
			list.add(myClass);
		}
		
		Class result ;
		if (list.size() > 0) 
			result = list.get(0);
		else
			result = null;
	
		logger.info("ClassService.getByLabel : retrieved class.");
		return result;					
				
	}
	
	
	public Class getByID(int ID) throws SQLException {
		
		List<Class> list = new LinkedList<Class>();
				
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM class where ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		VersionService versionService = new VersionService(connection);
		while(rs.next()) {
			
			Version version = versionService.get(rs.getInt("version_id"));
			Class myClass = new Class(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),
					rs.getString("comment"), rs.getLong("count"), version);
						
//			int classId = rs.getInt("ID");
//			List<Class> parents = new ArrayList<Class>();
//			Statement stmtParent = connection.createStatement();			
//			String queryParent = "SELECT * FROM class_parent where class_id =" + classId ;
//			ResultSet rsP = stmtParent.executeQuery(queryParent); 
//			while(rsP.next()) {
//				int parentId = rsP.getInt("ID");
//				Class parent = getByID(parentId);
//				parents.add(parent);
//				
//			}
//			
//			myClass.setParents(parents);
			list.add(myClass);
		}
		
		logger.info("ClassService.getByID : retrieved class.");
		
		Class result = null;
		if (list.size() > 0 ) {
			result = list.get(0);
		}
		
		return result;	
						
	}
	
	public long count(int versionId) throws SQLException  {
		long count = 0 ;
				
		
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT count(*) as count FROM class where version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		if (rs.next())
			count = rs.getLong("count");
		
		return count;
		
	}
	
	public long individualCount(int versionId) throws SQLException  {
		long sum = 0 ;
			
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT SUM(`count`) as sum FROM class WHERE version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		if (rs.next())
			sum = rs.getLong("sum");
		
		return sum;
		
	}
	
	public List<Class> listAll(int versionId) throws SQLException{
		List<Class> results = new ArrayList<Class>();
		
		Statement stmtSys = connection.createStatement();	
		String query = "SELECT * FROM class where version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		VersionService versionService = new VersionService(connection);
		while (rs.next()) {
			Version version = versionService.get(versionId);
			Class myClass = new Class(rs.getInt("ID"), rs.getString("url"), rs.getString("label"),rs.getString("comment"), rs.getLong("count"), version);
			
			int classId = rs.getInt("ID");
			List<Class> parents = new ArrayList<Class>();
			Statement stmtParent = connection.createStatement();			
			String queryParent = "SELECT * FROM class_parent where class_id =" + classId ;
			ResultSet rsP = stmtParent.executeQuery(queryParent); 
			while(rsP.next()) {
				int parentId = rsP.getInt("ID");
				Class parent = getByID(parentId);
				parents.add(parent);
				
			}
			
			myClass.setParents(parents);
			results.add(myClass);
		}
		
		return results;
			
	}
	
	public boolean checkExist(int versionId) throws SQLException{
				
		Statement stmtSys = connection.createStatement();	
		String query = "SELECT * FROM class where version_id=" + versionId;
		ResultSet rs = stmtSys.executeQuery(query); 
		if (rs.next() == true)
			return true;
		
		return false;
		
			
	}

}
