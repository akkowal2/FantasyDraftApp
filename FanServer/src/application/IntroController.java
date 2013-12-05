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
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

public class IntroController implements Initializable{
	@FXML private Button startButton;
    @FXML private TableView<Team> teamTable;
    @FXML private TableColumn<Team,String> teamColumn;
	@FXML private BorderPane background;
	@FXML private BorderPane dialogue;
    @FXML private BorderPane teamInfoPane;
    @FXML public BorderPane warningPane;
	@FXML public TextField name;
	@FXML public PasswordField password;



    private TextField title;
	private String leagueName;
	private String leaguePassword;
    ArrayList<Team> teams;
    private Boolean draftStarted;
    public EventSource eventSource;


	@FXML
	public void startDraft(ActionEvent event){
        draftStarted=true;
		Stage stage = (Stage) startButton.getScene().getWindow();
		stage.close();

		DraftView view = new DraftView(leagueName,leaguePassword);

		view.start(new Stage());


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
        teams = new ArrayList<Team>();

	}
	
	@FXML
    /**
     * The "Enter" button on the intro screen's handler.
     * This will create a  table for the league name and password
     */
	public void sendLeagueInfo(MouseEvent e) {
		leagueName = name.getText();
		leaguePassword = password.getText();

        System.out.println("leagueName:"+ leagueName);
		System.out.println("leaguePass:"+ leaguePassword);
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://finalproject54.servehttp.com:8080");
		WebTarget con=base.path("/StartConnection/OpenConnections/"+leagueName+"/"+leaguePassword);
		Response res = con.request("application/json").get();

        if(res.getStatus()==500){
            System.out.println("500 error");
            dialogue.setVisible(false);
            warningPane.setVisible(true);
            warningPane.requestFocus();
            return;
        }
        else{


            background.setVisible(true);
            dialogue.setVisible(false);
            background.getScene().getWindow().setWidth(900);
            background.getScene().getWindow().setHeight(600);
        }

        //open the connection with the server


        Client clientSSE = ClientBuilder.newBuilder().register(SseFeature.class).build();
        WebTarget target = clientSSE.target("http://finalproject54.servehttp.com:8080/Sync/GetTeams/"+leagueName);
        EventSource eventSource = EventSource.target(target).build();
        EventListener listener = new EventListener() {
            @Override
            //This is the listener to collect team information
            public void onEvent(InboundEvent inboundEvent) {
                System.out.println(inboundEvent.getName() + "; "+ inboundEvent.readData(String.class));
                if(inboundEvent.getName().equals("Workaround")){
                    return;
                }

                String jsonString =inboundEvent.readData(String.class);
                Gson gson = new Gson();
                Type type = new TypeToken<Team>() {}.getType();
                Team newTeam =gson.fromJson(jsonString,type);

                teams.add(newTeam);

                teamColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
                ObservableList<Team> teamObservableList;
                teamObservableList = FXCollections.observableArrayList();
                teamObservableList.addAll(teams);
                teamTable.setItems(teamObservableList);
                teamTable.setId("connectionTable");

            }
        };
        eventSource.register(listener);
        eventSource.open();

        TextArea lname = new TextArea(leagueName);
        lname.setId("passwordText");
        TextArea lpass = new TextArea(leaguePassword);
        lpass.setId("passwordText");
        lname.setEditable(false);
        lpass.setEditable(false);
        teamInfoPane.setTop(lname);
        teamInfoPane.setBottom(lpass);
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
