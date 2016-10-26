package com.pezal;

import java.sql.SQLException;

import javafx.stage.Stage;

public class QueryMethods extends Query {
	
	
	public static boolean ifExist(String nameEN) throws SQLException{
		try {
			connectToDatabase();
			
			sql = "SELECT CASE WHEN EXISTS (SELECT * FROM dictionary WHERE nameEN = '" + nameEN + "') THEN 1 ELSE 0 END as bit";

			executeSql2(sql);
				
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet.getBoolean(1);		
	}
	
	public static String getTranslation(String nameEN) throws SQLException{
		try {
			connectToDatabase();
				
			sql = "SELECT namePL FROM dictionary WHERE nameEN = '" + nameEN + "'" ;
			
			executeSql2(sql);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet.getString(1);
	}
	
	
	
	public static void getFromDictionary(){
		try {
			connectToDatabase();
				
			sql = "UPDATE wordsToTranslation SET namePL = ( SELECT namePL FROM dictionary WHERE nameEN = wordsToTranslation.nameEN )";
			
			executeSql(sql);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	

	public static void getWordsToTranslate(){
		try {
			connectToDatabase();
				
			sql = "SELECT * FROM wordsToTranslation" ;
			
			executeSql2(sql);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	
	public static void getWordsFromDictionary(){
		try {
			connectToDatabase();
				
			sql = "SELECT * FROM dictionary" ;
			
			executeSql2(sql);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	
	public static void updateNamePL(String nameEN, String namePL){
		try {
			connectToDatabase();
				
			sql = "UPDATE wordsToTranslation " + " SET " + " 'namePL' " + " = " + "'" + namePL + "'" + " WHERE " 
					+ " wordsToTranslation " + " 'nameEN' " + " = " + "'" +  nameEN + "'" ;
			
			executeSql(sql);
			close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public static void updateNameEN(String namePL, String nameEN){
		try {
			connectToDatabase();
				
			sql = "UPDATE " + "'dictionary'" + " SET " + " 'nameEN' " + " = " + "'" + nameEN + "'" + " WHERE " 
					+ "'dictionary'." + " 'namePL' " + " = " + "'" +  namePL + "'" ;
			
			executeSql(sql);
			close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	
	public static void addPosition(String nameEN, String namePL){
		try {
			connectToDatabase();
			
			sql = "INSERT INTO dictionary (namePL, nameEN) " + "VALUES ('" + namePL + "'," + "'" + nameEN + "'" + ")";

			executeSql(sql);
			close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void clearWordsInDatabase(){
		try {
			connectToDatabase();
			
			sql = "DELETE FROM wordsToTranslation";

			executeSql(sql);
			close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public static void addPositionToWords(String nameEN, String namePL){
		try {
			connectToDatabase();
			
			sql = "INSERT INTO wordsToTranslation (namePL, nameEN) " + "VALUES ('" + namePL + "'," + "'" + nameEN + "'" + ")";

			executeSql(sql);
			close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	
	@Override
	public void start(Stage arg0) throws Exception {		
	}
	


}
