package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Version;

public class VersionService {
	
	public List<Version> getListAll() throws SQLException {
		
		List<Version> list = new LinkedList<Version>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		c.setAutoCommit(false);
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM version ORDER BY ID";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new Version(rs.getInt("ID"), rs.getString("name"), rs.getString("number")));
		}
		
		return list;						
				
	}

}
