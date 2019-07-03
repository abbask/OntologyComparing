package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.EndPoint;


public class EndPointService {
	
	public List<EndPoint> getListAll() throws SQLException {
		
		List<EndPoint> list = new LinkedList<EndPoint>();
				
		MySQLConnection mySQLConnection = new MySQLConnection();
		Connection c = mySQLConnection.openConnection();			
		
		c.setAutoCommit(false);
		Statement stmtSys = c.createStatement();			
		String query = "SELECT * FROM endpoint ORDER BY ID";
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new EndPoint(rs.getString("name"), rs.getString("url")));
		}
		
		return list;						
		
		
	}
	
	


}
