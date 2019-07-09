package edu.uga.cs.ontologycomparision.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class MySQLConnection {
	final static Logger logger = Logger.getLogger(MySQLConnection.class);
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String JDBC_DB_URL = "jdbc:mysql://localhost:3306/ontology_comparison"; 
	
	static final String JDBC_USER = "root";
	static final String JDBC_PASS = "coolerthanever";//coolerthanever
	
	Connection connObj;
	Statement statement;
	PreparedStatement preparedStatement;
	
	public MySQLConnection() {
		connObj = null;
		statement = null;
		preparedStatement =null;
		openConnection();
	}

	public Connection openConnection() {
		
		try {
			Class.forName(JDBC_DRIVER);  

			connObj = DriverManager.getConnection(JDBC_DB_URL + "?user=" + JDBC_USER + "&password=" + JDBC_PASS + "&serverTimezone=UTC&useSSL=false");
		} catch (Exception sqlException) {
			sqlException.printStackTrace();
			logger.error("MySQLConnection: sqlException error - Connection openning error");			
		}
		return connObj;
	}
	
	public PreparedStatement createPreparedStatement(String sql) {
		
		try {
			
			preparedStatement = connObj.prepareStatement(sql);
			
		} catch (SQLException e) {
			logger.error("MySQLConnection: sqlException error - Connection openning error");	
			closeConnection();
		}
		return preparedStatement ;
	}
	
	public Statement createStatement() {
		
		try {
			
			statement = connObj.createStatement();
			
		} catch (SQLException e) {
			logger.error("MySQLConnection: sqlException error - Connection openning error");	
			closeConnection();
		}
		return statement ;
	}
	
	public void closeConnection() {
		try {
			connObj.close();
		} catch (SQLException e) {
			logger.error("MySQLConnection: sqlException error - Connection closing error");	
			e.printStackTrace();
		}
	}

}
