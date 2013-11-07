package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntroController{
	@FXML private Button startButton;
	
	
	
	@FXML
	public void startDraft(ActionEvent Event){
		Stage stage = (Stage) startButton.getScene().getWindow();
		stage.close();
		DraftView view = new DraftView();
		view.start(new Stage());
	}

	
}
