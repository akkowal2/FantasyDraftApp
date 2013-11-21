package application;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntroController implements Initializable{
	@FXML private Button startButton;
    @FXML private TableView<Team> teamTable;
    @FXML private TableColumn<Team,String> teamColumn;
	@FXML private BorderPane background;
	@FXML private BorderPane dialogue;
    @FXML public BorderPane warningPane;
	@FXML public TextField name;
	@FXML public PasswordField password;


    private TextField title;
	private String leagueName;
	private String leaguePassword;
    ArrayList<Team> teams;
    private Boolean draftStarted;


	@FXML
	public void startDraft(ActionEvent event){
        draftStarted=true;
		Stage stage = (Stage) startButton.getScene().getWindow();
		stage.close();

		DraftView view = new DraftView(leagueName,leaguePassword);

		view.start(new Stage());
        //view.sendName();
        //view.sendPass();
		//Main.server.changeFlag();
	}
    @FXML
    public void refreshTeams(MouseEvent event){
        Client client = ClientBuilder.newClient();
        WebTarget base = client.target("http://draft.elasticbeanstalk.com/rest");
        WebTarget con=base.path("/StartConnection/GetTeams/"+leagueName);
        Response playerResponse = con.request("application/json").get();

        if(playerResponse.getStatus()==200){
            String jsonString =playerResponse.readEntity(String.class);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Team>>() {}.getType();
            ArrayList<Team> newTeams =gson.fromJson(jsonString,type);
            System.out.println("size of newteams"+ newTeams.size());
            if(teams == null && newTeams.size()!=0){
                teams = newTeams;

            }
            if(teams!=null && !newTeams.containsAll(teams)){
                System.out.println("updated Team names");
                teams = newTeams;

            }
        }
        else{
            System.out.println("Brian's a genius");
            refreshTeams(event);
        }
        teamColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
        ObservableList<Team> teamObservableList;
        teamObservableList = FXCollections.observableArrayList();
        teamObservableList.addAll(teams);
        teamTable.setItems(teamObservableList);
        teamTable.setId("connectionTable");
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        draftStarted=false;
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

		
	}
	
	@FXML
	public void sendLeagueInfo(MouseEvent e) {
		leagueName = name.getText();
		leaguePassword = password.getText();

        System.out.println("leagueName:"+ leagueName);
		System.out.println("leaguePass:"+ leaguePassword);
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://draft.elasticbeanstalk.com/rest");
		WebTarget con=base.path("/StartConnection/OpenConnections/"+leagueName+"/"+leaguePassword);
		Response res = con.request("application/json").get();

        if(res.getStatus()==500){
            dialogue.setVisible(false);
            warningPane.setVisible(true);
            warningPane.requestFocus();
            return;
        }
        else{

            System.out.println(res.toString());
            background.setVisible(true);
            dialogue.setVisible(false);
            background.getScene().getWindow().setWidth(900);
            background.getScene().getWindow().setHeight(600);
        }

        /*
        new Thread() {
            // runnable for that thread

            public void run() {
                //poll for new teams

                while(!draftStarted && dialogue.getScene().getWindow().isShowing() ){

                    Client client = ClientBuilder.newClient();
                    WebTarget base = client.target("http://draft.elasticbeanstalk.com/rest");
                    WebTarget con=base.path("/StartConnection/GetTeams/"+leagueName);
                    Response playerResponse = con.request("application/json").get();
                    boolean updateThread = false;
                    if(playerResponse.getStatus()==200){
                        String jsonString =playerResponse.readEntity(String.class);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Team>>() {}.getType();
                        ArrayList<Team> newTeams =gson.fromJson(jsonString,type);
                        System.out.println("size of newteams"+ newTeams.size());
                        if(teams == null && newTeams.size()!=0){
                            teams = newTeams;

                        }
                        if(teams!=null && !newTeams.containsAll(teams)){
                            System.out.println("updated Team names");
                            teams = newTeams;
                            updateThread=true;
                        }
                    }
                    else{
                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        continue;
                    }



                    if(updateThread){
                        Platform.runLater(new Runnable(){
                            public void run() {
                                // what will be ran in gui thread

                                if(teams!=null){
                                    System.out.println("sizeof teams " +  teams.size());
                                    teamColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
                                    ObservableList<Team> teamObservableList;
                                    teamObservableList = FXCollections.observableArrayList();
                                    teamObservableList.addAll(teams);
                                    teamTable.setItems(teamObservableList);
                                    teamTable.setId("connectionTable");
                                }




                            }
                        });
                    }

                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
                System.out.println("I don't think so");


            }
        }.start();
        */
    }

   @FXML
   public void pickNewName(MouseEvent e){
       warningPane.setVisible(false);
       dialogue.setVisible(true);
       dialogue.requestFocus();





       name.clear();
        password.clear();

   }

	

	
}
