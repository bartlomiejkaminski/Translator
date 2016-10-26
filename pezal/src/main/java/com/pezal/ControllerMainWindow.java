package com.pezal;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class ControllerMainWindow extends QueryMethods implements Initializable {
	
	@FXML
	private TableView<TableDB> 			 tablePezal;
	@FXML
	private TableColumn<TableDB, String> columnID;
	@FXML
	private TableColumn<TableDB, String> columnNameEN;
	@FXML
	private TableColumn<TableDB, String> columnNamePL;
		
	private ObservableList<TableDB> 	 data                        = FXCollections.observableArrayList();
	
	private TreeMap<String, String> 	 dictionary  				 = new TreeMap<String, String>();
	private TreeMap<String, String> 	 dictionaryToUpdate 		 = new TreeMap<String, String>();
	
	private Service<Void>				 backgroundThread;
	private Service<Void> 				 backgroundThreadUpdateWords;
	
	
	
	// LOAD DATA TO TABLEVIEW AND SHOW
	void displayValuesWithTranslate(){		
			backgroundThread = new Service<Void>() {
				@Override
				protected Task<Void> createTask() {
					return new Task<Void>(){
						@Override
						protected Void call() throws Exception {
							for(int i = 0; i < data.size(); i++ ){
								data.get(i).setNamePL(dictionary.get(data.get(i).getNameEN()));		   					
							}					
							return null;
						}
					};
				}
			};			
			backgroundThread.restart(); 
	tablePezal.setItems(FXCollections.observableArrayList(data));
	}

	
	@FXML
	public void exportCSV(ActionEvent event) throws IOException {		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName(null);
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV File", "*.csv*"));
		File savedFile = fileChooser.showSaveDialog(null);
		String csv = savedFile.toString();
		CSVWriter writer = new CSVWriter(new FileWriter(csv));
		List<String[]> base = new ArrayList<String[]>();
		
		for(int i = 0; i < data.size(); i++){	
			base.add(new String[] {data.get(i).getNameEN(), data.get(i).getNamePL()});
		}
		
		writer.writeAll(base);
		writer.close();	
	}
	
	@FXML
	public void importCSV() throws IOException {
	  FileChooser fileChooser = new FileChooser();
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
      fileChooser.getExtensionFilters().add(extFilter);
      File file = fileChooser.showOpenDialog(null);
      String csv = file.toString();
      
      clearWordsInDatabase();
      CSVReader reader = null;
      try {
          reader = new CSVReader(new FileReader(csv), ';');
          String[] line;
          int i =0;
          while ((line = reader.readNext()) != null) {
        	  data.add(new TableDB(i, line[1], line[0]));	
        	  i++;
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
      
      displayValuesWithTranslate();     
	}
	
	
	// Close window
	@FXML
	public void exitApplication(ActionEvent event) {
	   Platform.exit();
	}
	
	
	
	@FXML
	void openAboutWinow() throws IOException{
		Stage stage = new Stage();
		stage.setTitle("About Pezal Translator 1.0");
		Parent root = FXMLLoader.load(getClass().getResource("/com/pezal/AboutWindow.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().addAll(this.getClass().getResource("application2.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
	
	
	 
	// After pressing the SAVE, sending new words to the dictionary database
	@FXML
	public void addToDictionary(ActionEvent event) {		
		for(Entry<String, String> entry : dictionaryToUpdate.entrySet()) {
			  String nameEN = entry.getKey();
			  String namePL = entry.getValue();

			  addPosition(nameEN, namePL);
			}
	}
	
	
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		tablePezal.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnNameEN.setCellValueFactory(new PropertyValueFactory<>("nameEN"));
		columnNamePL.setCellValueFactory(new PropertyValueFactory<>("namePL"));
		
		
		
		//LOAD DICTIONARY TO MAP dictionary
		getWordsFromDictionary();
		try {
			while(resultSet.next()){
			dictionary.put(resultSet.getString("nameEN"), resultSet.getString("namePL"));				    
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			}
		
		try {
		close();
		} catch (SQLException e1) {
		e1.printStackTrace();
		}	
			
		
		
		//GET AND ADD 'WORDSTOTRANSLATE' TO data
		getWordsToTranslate();	
		try {
				while(resultSet.next()){
				data.add(new TableDB(resultSet.getInt("ID"), resultSet.getString("namePL"), resultSet.getString("nameEN")));					    
				}			
			} catch (SQLException e) {
				e.printStackTrace();
				}
		try {
			close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
		
		
		
		displayValuesWithTranslate();
		
		
		// TURN ON EDITABLE AND ACTION
		tablePezal.setEditable(true);		
		columnNamePL.setCellFactory(TextFieldTableCell.forTableColumn());
		columnNamePL.setOnEditCommit(
			   new EventHandler<CellEditEvent<TableDB, String>>() {
				   @Override
			       public void handle(CellEditEvent<TableDB, String> t) {
 					((TableDB) t.getTableView().getItems().get(
	   			                t.getTablePosition().getRow())
	   			                ).setNamePL(t.getNewValue());
 					
			   							dictionaryToUpdate.put(columnNameEN.getCellData(t.getTablePosition().getRow()), t.getNewValue());
			   							dictionary.put(columnNameEN.getCellData(t.getTablePosition().getRow()), t.getNewValue());
											   					
					   					backgroundThreadUpdateWords = new Service<Void>() {
					   						@Override
					   						protected Task<Void> createTask() {
					   							return new Task<Void>(){
					   								@Override
					   								protected Void call() throws Exception {
					   					
					   									
					   									if( t.getTablePosition().getRow() < 20)	{
					   										for(int i = 0; i < t.getTablePosition().getRow() + 20; i++ ){
					   											data.get(i).setNamePL(dictionary.get(data.get(i).getNameEN()));
					   											tablePezal.setItems(FXCollections.observableArrayList(data));		
					   										}	
					   									}
					   									else{
					   										for(int i = t.getTablePosition().getRow()-20; i < t.getTablePosition().getRow() + 20; i++ ){
					   											data.get(i).setNamePL(dictionary.get(data.get(i).getNameEN()));
					   											tablePezal.setItems(FXCollections.observableArrayList(data));		
					   										}	
					   									}
					   					
					   									return null;
					   								}
					   							};
					   						}
					   						
					   					};			
						   				backgroundThreadUpdateWords.restart(); 	   
			        }
			   }
		);    			
	}
}