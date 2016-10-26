package com.pezal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;

public abstract class Query extends Application{
	
	private 	static final String DRIVER_CLASSNAME 	= "org.sqlite.JDBC";
	private 	static final String PASSWORD			= "";
	private 	static final String USERNAME 			= "";
	
	protected   static 	 	 String base 				= "data/pezalDB";
	private 	static 		 String JDBC_URL			="jdbc:sqlite:"+base+".db";
		
	
	protected 	static 		 String sql;
	
	protected   static 	 Connection connection;
	protected	static 	 Statement statement;	
	protected 	static 	 ResultSet resultSet;

	

	public static void connectToDatabase() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER_CLASSNAME);
		connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
	}
	
	
	public static void executeSql(String sql) throws SQLException {
		
		statement = connection.createStatement();
		statement.executeUpdate(sql);
	}
	
	public static void executeSql2(String sql) throws SQLException {
		
		statement = connection.createStatement();
		resultSet = statement.executeQuery(sql);
	}
	
	
	public static void close() throws SQLException {
		statement.close();
		connection.close();
		resultSet.close();
	}



	
}
