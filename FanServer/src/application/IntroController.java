package application;

import java.net.URL;
import java.util.ResourceBundle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;



import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntroController implements Initializable{
	@FXML private Button startButton;
	//@FXML private BorderPane instructionBox;
	@FXML private BorderPane background;
	@FXML private BorderPane dialogue;
    @FXML public BorderPane warningPane;
	@FXML public TextField name;
	@FXML public PasswordField password;


	private TextField title;
	private String leagueName;
	private String leaguePassword;
	
	@FXML
	public void startDraft(ActionEvent Event){
		Stage stage = (Stage) startButton.getScene().getWindow();
		stage.close();
		DraftView view = new DraftView(leagueName,leaguePassword);

		view.start(new Stage());
        //view.sendName();
        //view.sendPass();
		//Main.server.changeFlag();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		title = new TextField("Fantasy Draft");
		title.setId("programTitle");
		title.setStyle("-fx-text-fill: black;"+
			    "-fx-background-color: white;"+
			    "-fx-font-family: Razer;"+
			    "-fx-font-weight: bold;"+
			    
			    "-fx-font-size: 45;");
		title.setEditable(false);
		title.selectAll();
		title.setPrefHeight(100);
		title.setAlignment(Pos.CENTER);
		//BorderPane border =  new BorderPane();
		//border.setCenter(title);
		//instructionBox.setTop(border);
		
	}
	
	@FXML
	public void sendLeagueInfo(MouseEvent e) {
		leagueName = name.getText();
		leaguePassword = password.getText();
		
		
		System.out.println("here");
		System.out.println("leagueName:"+ leagueName);
		System.out.println("leaguePass:"+ leaguePassword);
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://draft-env.elasticbeanstalk.com/rest");
		WebTarget con=base.path("/StartConnection/OpenConnections/"+leagueName+"/"+leaguePassword);
		Response res = con.request("application/json").get();

        if(res.getStatus()==500){
            dialogue.setVisible(false);
            warningPane.setVisible(true);
            warningPane.requestFocus();
        }
        else{

            System.out.println(res.toString());
            background.setVisible(true);
            dialogue.setVisible(false);
            background.getScene().getWindow().setWidth(900);
            background.getScene().getWindow().setHeight(600);
        }
    }

    @FXML
    public void pickNewName(MouseEvent e){
        warningPane.setVisible(false);
        dialogue.setVisible(true);
        dialogue.requestFocus();;
        name.clear();
        password.clear();

    }

	

	
}
