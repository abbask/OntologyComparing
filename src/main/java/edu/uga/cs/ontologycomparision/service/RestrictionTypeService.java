package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.RestrictionType;

public class RestrictionTypeService {
	
final static Logger logger = Logger.getLogger(RestrictionTypeService.class);
	
	private Connection connection;	
	
	public RestrictionTypeService(Connection connection) {
		this.connection = connection;
	}
	
	public RestrictionType get(int ID) throws SQLException {
		
		List<RestrictionType> list = new LinkedList<RestrictionType>();	
		
		connection.setAutoCommit(false);
		Statement stmtSys = connection.createStatement();			
		String query = "SELECT * FROM restriction_type WHERE ID=" + ID;
		ResultSet rs = stmtSys.executeQuery(query); 
		
		while(rs.next()) {
			list.add(new RestrictionType(rs.getInt("ID"), rs.getString("type")));
		}
		
		return list.get(0);				
	}

}
