package com.pezal;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableDB {
	
	private IntegerProperty 	id;
	private StringProperty 		namePL;
	private StringProperty 		nameEN;
	
	// CONSTRUCTOR
	public TableDB(int id, String namePL, String nameEN){
		setId(id);
		setNamePL(namePL);
		setNameEN(nameEN);
	}
	
	// CONSTRUCTOR EMPTY
	public TableDB(){
	}
	
	// METHODS FOR ID
	public void setId(int id) {
		idProperty().set(id);
	}
	
	public int getId() {
		return idProperty().get();
	}
	
	public IntegerProperty idProperty(){
		 if (id == null) id = new SimpleIntegerProperty(this, "id");
         return id; 
	}

	
	// METHODS FOR namePL
	public void setNamePL(String namePL) {
		namePLProperty().set(namePL);
	}
	
	public String getNamePL() {
		return namePLProperty().get();
	}

	public StringProperty namePLProperty(){
		 if (namePL == null) namePL = new SimpleStringProperty(this, "namePL");
        return namePL; 
	}
	
	// METHODS FOR nameEN
	public void setNameEN(String nameEN) {
		nameENProperty().set(nameEN);
	}
	
	public String getNameEN() {
		return nameENProperty().get();
	}

	public StringProperty nameENProperty(){
		 if (nameEN == null) nameEN = new SimpleStringProperty(this, "nameEN");
        return nameEN; 
	}


}
