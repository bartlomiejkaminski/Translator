package com.pezal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);	
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			primaryStage.setTitle("Pezal");
			Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


