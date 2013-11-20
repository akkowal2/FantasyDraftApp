package application;

import java.awt.List;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import Data.*;
import DBSetup.*;

public class Controller implements Initializable {

	
	@FXML private TableView<Player> playerTable;
	@FXML private TableColumn<Player, Integer> rankColumn;
	@FXML private TableColumn<Player, String> nameColumn;
	@FXML private TableColumn<Player, String> positionColumn;
	@FXML private TableColumn<Player, String> teamColumn;
	@FXML private MenuBar teamBar;
	@FXML private Menu chooseTeam;
	@FXML private ListView<String> draftHistory;
	@FXML private TextField search;
	@FXML private RadioButton allFilter;
	@FXML private RadioButton qbFilter;
	@FXML private RadioButton rbFilter;
	@FXML private RadioButton wrFilter;
	@FXML private RadioButton teFilter;
	@FXML private RadioButton kFilter;
	@FXML private RadioButton defFilter;
	@FXML private ToggleGroup filterGroup;
	@FXML private Label playerInfo;
	@FXML private Button draftPlayer;
	
	@FXML private Label clockLabel;
	@FXML private Label onTheClock;
	@FXML private Label secondPickNumber;
	@FXML private Label thirdPickNumber;
	@FXML private Label fourthPickNumber;
	@FXML private Label fifthPickNumber;
	@FXML private Label sixthPickNumber;
	@FXML private Label seventhPickNumber;
	@FXML private Label eigthPickNumber;
	
	@FXML private Label secondPickName;
	@FXML private Label thirdPickName;
	@FXML private Label fourthPickName;
	@FXML private Label fifthPickName;
	@FXML private Label sixthPickName;
	@FXML private Label seventhPickName;
	@FXML private Label eigthPickName;
	
	@FXML private TextField teamNameField;
	@FXML private TextField leagueNameField;
	@FXML private PasswordField passwordField;
	@FXML private Button draftConnect;
	@FXML private Label teamNamePrompt;
	@FXML private Label passwordPrompt;
	@FXML private Label leagueNamePrompt;
	@FXML private Label loginTitle;
	@FXML private Rectangle loginRectangle;
	
	@FXML private Label errorText;
	
	@FXML private ListView<String> teamViews;
	
	Model model = new Model();
	Hashtable<String, RadioButton> filters = new Hashtable<String, RadioButton>();
	Hashtable<Integer, Label> pickNumbers = new Hashtable<Integer, Label>();
	Hashtable<Integer, Label> pickNames = new Hashtable<Integer, Label>();
	
	ArrayList<MenuItem> teamMenus = new ArrayList<MenuItem>();
	ArrayList<String> draftedPlayers = new ArrayList<String>();
	ObservableList<String> draftedPlayerNames = FXCollections.observableArrayList();
	
	Timer clock;
	public int interval= 20;

	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		populateHashes();
		rankColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("rank"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("position"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("team"));
	}

	
	
	@FXML protected void searchPlayers(KeyEvent event) {
		
		String toggle = model.findToggle(filterGroup, filters);
		model.updateTable(search.getText().toLowerCase(), toggle, playerTable);
        
	}
	
	
	@FXML private void filterPlayers(MouseEvent event){
		
		String toggle = model.findToggle(filterGroup, filters);
		model.updateTable(search.getText().toLowerCase(), toggle, playerTable);
			
	}
	
	
	@FXML private void playerClicked(MouseEvent event){
		
		Player selected = playerTable.getSelectionModel().getSelectedItem();
		playerInfo.setText(selected.getName()+ " " + selected.getPosition() + " Rank: " + selected.getRank() );
		playerInfo.setFont(new Font("System", 25));
		draftPlayer.setDisable(false);
		
	}
	
	@FXML private void connectToDraft(MouseEvent event){
		
		model.setLeagueName(leagueNameField.getText());
		model.setTeamName(teamNameField.getText());
		model.setPassword(passwordField.getText());
		
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://draft-env.elasticbeanstalk.com/rest/StartConnection/Connect/"+leagueNameField.getText()+"/"+passwordField.getText()+"/"+teamNameField.getText());
		
		Response res = base.request("application/json").get();
		
		String serverResponse = (res.readEntity(String.class));
		//System.out.println(serverResponse);
		if(serverResponse.equals("League Authorization failed, please check you have the right league name and password"))
			errorText.setVisible(true);
		else{
			
			Gson notJson = new Gson();
			Type type = new TypeToken<ArrayList<Player>>() {}.getType();
			ArrayList<Player> bpaPlayerList = notJson.fromJson(serverResponse, type);
		
			model.initialize(playerTable, bpaPlayerList);
			
			EventHandler<MouseEvent> menuClicks = new EventHandler<MouseEvent>(){
				public void handle(MouseEvent e){
					System.out.println("event");
					e.consume();
				}
			};
			
			model.populateTeams(pickNumbers, pickNames, chooseTeam, teamViews, teamMenus, menuClicks);
			
			loginTitle.setVisible(false);
			teamNameField.setVisible(false);
			leagueNameField.setVisible(false);
			passwordField.setVisible(false);
			draftConnect.setVisible(false);
			teamNamePrompt.setVisible(false);
			passwordPrompt.setVisible(false);
			leagueNamePrompt.setVisible(false);
			loginRectangle.setVisible(false);
			errorText.setVisible(false);
			startClock();
		}
		
	}
	
	
	
	public void startClock() {
		
		int delay = 1000;
		int period = 1000;
		clock = new Timer();
		interval = 20;
		clock.scheduleAtFixedRate(new TimerTask() {
		public void run() {
			 setInterval();
		   }
		 }, delay, period);
		  
		
		
	}
	
	private void setInterval(){
	    if( interval== 1){ 
	    	clock.cancel();
	    	rotateDraftOrder();
	    	startClock();
	    }
	    --interval;
	}
	
	public void setClockLabel(int time){
		clockLabel.setText(time+"");
	}



	@FXML private void draftPlayer(){
		Player selected = playerTable.getSelectionModel().getSelectedItem();
		if(selected == null){
			playerInfo.setText("No player selected, please try again");
			playerInfo.setFont(new Font("System", 25));
		}
		else{
			int currTeam = (model.getPick())%8;
			Team clientTeam = model.getTeams().get(currTeam);
			
			clientTeam.addPlayer(selected);
			rotateDraftOrder();
			
			model.addPlayerToTeam(selected);
			//draftedPlayers.add(selected.getName());
			draftedPlayerNames.add(selected.getName());
			draftHistory.setItems(draftedPlayerNames);
			
			model.removePlayer(selected, playerTable);
			String toggle = model.findToggle(filterGroup, filters);
			model.updateTable(search.getText().toLowerCase(), toggle, playerTable);
			clock.cancel();
			startClock();
			
		}
			
	}
	
	public void rotateDraftOrder(){
		ArrayList<Team> teams = model.getTeams();
		
		model.updatePick();
		int pick = model.getPick();
		//System.out.println(pick);
		for(int i = 0; i<pickNames.size(); i++){
			if(i == 0)
				pickNames.get(i).setText(pick+". "+teams.get(pick%8).getName());
			else
				pickNames.get(i).setText(teams.get((pick+i)%8).getName() + " ");
			if(i < 7)
				pickNumbers.get(i).setText((pick+1+i)+".");
		}
		
		
	}
	
	private void populateHashes() {
		filters.put("ALL", allFilter);
		filters.put("QB", qbFilter);
		filters.put("RB", rbFilter);
		filters.put("WR", wrFilter);
		filters.put("TE", teFilter);
		filters.put("K", kFilter);
		filters.put("DEF", defFilter);
		
		
		pickNumbers.put(0, secondPickNumber);
		pickNumbers.put(1, thirdPickNumber);
		pickNumbers.put(2, fourthPickNumber);
		pickNumbers.put(3, fifthPickNumber);
		pickNumbers.put(4, sixthPickNumber);
		pickNumbers.put(5, seventhPickNumber);
		pickNumbers.put(6, eigthPickNumber);
		
		pickNames.put(0, onTheClock);
		pickNames.put(1, secondPickName);
		pickNames.put(2, thirdPickName);
		pickNames.put(3, fourthPickName);
		pickNames.put(4, fifthPickName);
		pickNames.put(5, sixthPickName);
		pickNames.put(6, seventhPickName);
		pickNames.put(7, eigthPickName);
	}
	
	
}
