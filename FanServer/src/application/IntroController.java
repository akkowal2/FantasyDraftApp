package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntroController implements Initializable{
	@FXML private Button startButton;
	@FXML private BorderPane textPane;
	private TextField title;
	
	@FXML
	public void startDraft(ActionEvent Event){
		Stage stage = (Stage) startButton.getScene().getWindow();
		stage.close();
		DraftView view = new DraftView();
		view.start(new Stage());
		Main.server.changeFlag();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		title = new TextField("Fantasy Draft");
		title.setStyle("-fx-text-fill: black;"+
			    "-fx-background-color: white;"+
			    "-fx-font-family: GungsuhChe;"+
			    "-fx-font-weight: bold;"+
			    
			    "-fx-font-size: 45;");
		title.setEditable(false);
		title.setPrefHeight(100);
		title.setAlignment(Pos.CENTER);
		textPane.setTop(title);
		
	}

	
}
