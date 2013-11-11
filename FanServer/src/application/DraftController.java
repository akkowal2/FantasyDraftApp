package application;

import java.awt.GridLayout;
import java.beans.EventHandler;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DraftController implements Initializable{
	
	
	@FXML private TableView<Player> top10Table;
	@FXML private TableView<Player> teamTable;
	@FXML private TableView<Player> qbTable;
	@FXML private TableView<Player> rbTable;
	@FXML private TableView<Player> wrTable;
	@FXML private TableView<Player> teTable;
	@FXML private TableView<Player> defTable;
	@FXML private TableView<Player> kickTable;
	@FXML private TableColumn<Player, Integer> rankCol;
	@FXML private TableColumn<Player, String> nameCol;
	@FXML private TableColumn<Player, String> positionCol;
	@FXML private TableColumn<Player, String> prCol;
	@FXML private TableColumn<Player, Integer> byeWeek;
	@FXML private TableColumn<Player, Integer> qbRankCol;
	@FXML private TableColumn<Player, Integer> rbRankCol;
	@FXML private TableColumn<Player, Integer> wrRankCol;
	@FXML private TableColumn<Player, Integer> teRankCol;
	@FXML private TableColumn<Player, Integer> defRankCol;
	@FXML private TableColumn<Player, Integer> kickRankCol;
	@FXML private TableColumn<Player, String> qbNameCol;
	@FXML private TableColumn<Player, String> rbNameCol;
	@FXML private TableColumn<Player, String> wrNameCol;
	@FXML private TableColumn<Player, String> teNameCol;
	@FXML private TableColumn<Player, String> defNameCol;
	@FXML private TableColumn<Player, String> kickNameCol;
	@FXML private TableColumn<Player, String> qbTeamCol;
	@FXML private TableColumn<Player, String> rbTeamCol;
	@FXML private TableColumn<Player, String> wrTeamCol;
	@FXML private TableColumn<Player, String> teTeamCol;
	@FXML private TableColumn<Player, String> defTeamCol;
	@FXML private TableColumn<Player, String> kickTeamCol;
	
	@FXML private AnchorPane currentTeamAnchor;
	@FXML private BorderPane rightDraft;
	@FXML private BorderPane leftDraft;
	@FXML private BorderPane bpaTablePane;
	@FXML private BorderPane draftBorderPane;
	
	private GridPane draftOrderGrid;
	
	public TextField clockTextArea;
	private ObservableList<Player> players;
	private ObservableList<Team> teams;
	private Queue<Team> draftQ;
	
	int numTeams;
	Connect dbCon;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		numTeams=8;
		//Database Connection
		players = databaseConnect();
		
		//teams Array
		teams = FXCollections.observableArrayList();
		//testing
		ArrayList<Team> teamList= new ArrayList<Team>();
		for(int i=0; i<numTeams;i++){
			teamList.add(new Team("Team "+ i,null));
		}
		teams.addAll(teamList);
		
		
		//Top 10 Table Initialization
		bpaTableInit();
		
		//Team View Table Initialization Mock
		teamTableTab();
		
		
		
		//Clock Area
		clockTextArea = new TextField("00:00");
		clockTextArea.setId("clockText");
		clockTextArea.setEditable(false);
		clockTextArea.setAlignment(Pos.CENTER);
		BorderPane clockPane = new BorderPane();
		clockPane.setCenter(clockTextArea);
		clockPane.setId("clockPane");
		leftDraft.setTop(clockPane);
		//Top 10 text
		TextField top = new TextField("Top 10");
		top.setId("topText");
		top.setAlignment(Pos.CENTER);
		bpaTablePane.setTop(top);
		
		//Mock Draft Order Based off 8 team numbers and arbitrary names
		draftOrderGridInit();
		
		//Current Team Name for Positional Tables
		
		
		positionalColumnInit();
		
		
		positionalBreakdown(draftQ.peek());
		
		
		
		
		rotateDraftOrder();
		rotateDraftOrder();
		
		
		 
		
	}
	public ObservableList<Player> getPlayersList(){
		return players;
	}
	
	public Queue<Team> getDraftQ(){
		return draftQ;
	}

	private Player findPlayer(String name){
		
		for(int i=0; i<players.size();i++){
			if(players.get(i).getName().equals(name)){
				return players.get(i);
			}
		}
	
		return null;
			
	}
	/**
	 * Displays the tables relating to the current team. 
	 * This info is on the left side of the Draft Tab
	 * 
	 */
	public void positionalBreakdown(Team currentTeam) {
		TextField currentTeamField = new TextField();
		currentTeamField.setText(currentTeam.getName());
		currentTeamField.setId("currentTeamText");
		currentTeamField.setAlignment(Pos.CENTER);
		currentTeamField.setEditable(false);
		rightDraft.setTop(currentTeamField);

		wrTable.getItems().clear();
		qbTable.getItems().clear();
		rbTable.getItems().clear();
		defTable.getItems().clear();
		kickTable.getItems().clear();
		teTable.getItems().clear();
		System.out.println("Current Team size"+ currentTeam.getPlayers().size());
		for(int i=0;i<currentTeam.getPlayers().size();i++){
			
			Player currentPlayer = currentTeam.getPlayers().get(i);
			System.out.println("position"+currentTeam.getPlayers().get(i));
			if(currentTeam.getPlayers().get(i)==null)continue;
			System.out.println("position"+currentPlayer.getPosition());
			switch(currentPlayer.getPosition()){
				case "WR":
					wrTable.getItems().add(currentPlayer);
					break;
				case "QB":
					qbTable.getItems().add(currentPlayer);
					break;
				case "RB":
					rbTable.getItems().add(currentPlayer);
					break;
					
				case "DEF":
					defTable.getItems().add(currentPlayer);
					break;
				case "K":
					kickTable.getItems().add(currentPlayer);
					break;
				case "TE":
					teTable.getItems().add(currentPlayer);
					break;
			}
			wrTable.setPlaceholder(new Text(" "));
			qbTable.setPlaceholder(new Text(" "));
			rbTable.setPlaceholder(new Text(" "));
			teTable.setPlaceholder(new Text(" "));
			defTable.setPlaceholder(new Text(" "));
			kickTable.setPlaceholder(new Text(" "));
		}

	}
	
	
	/**
	 *  Tab to handle the table of drafted players by each team
	 */
	private void teamTableTab() {
		for (int x = 0; x < numTeams; x++){
			TableColumn<Player, String> newCol = new TableColumn<Player, String>("Team " + x);
			newCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
			teamTable.getColumns().add(newCol);
		}
		
		ObservableList<Player> testData = FXCollections.observableArrayList();
		testData.add(new Player("Adrian Peterson", -1, null, null, null, -1));
		testData.add(new Player("Bobby Hill", -1, null, null, null, -1));
		testData.add(new Player("OBAMA", -1, null, null, null, -1));
		testData.add(new Player("Brian Dahmen", -1, null, null, null, -1));
		teamTable.setItems(testData);
	}
	
	/**
	 * Initializes the BPA table with the columns based on the Player class.
	 */
	private void bpaTableInit() {
		rankCol.setCellValueFactory(new PropertyValueFactory<Player, Integer>("rank"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
		positionCol.setCellValueFactory(new PropertyValueFactory<Player, String>("position"));
		prCol.setCellValueFactory(new PropertyValueFactory<Player, String>("positionalRank"));
		byeWeek.setCellValueFactory(new PropertyValueFactory<Player,Integer> ("byeWeek"));
		top10Table.setItems(players);
	}
	
	/**
	 * Initializes the left draft order panel where the order of the teams is determined 
	 */
	private void draftOrderGridInit() {
		draftOrderGrid = new GridPane();
		draftOrderGrid.setId("draftOrderGrid");
		
		draftQ = new ArrayBlockingQueue<Team>(numTeams);
		for(int i=0;i<numTeams;i++){
			draftQ.add(teams.get(i));
			
		}
		
		for(int i=0; i<numTeams; i++){
			TextField currentTeam = new TextField(teams.get(i).getName());
			currentTeam.setId("teamNames");
			currentTeam.setMinWidth(169);
			currentTeam.setPrefWidth(169);
			currentTeam.setAlignment(Pos.CENTER);
			
			BorderPane gridPane = new BorderPane();
			gridPane.setCenter(currentTeam);
			
			draftOrderGrid.getRowConstraints().add(new RowConstraints(600/(numTeams+1)));
			draftOrderGrid.add(gridPane, 0, i);
		}
		draftBorderPane.setLeft(draftOrderGrid);
	}
	public void rotateDraftOrder(){
		draftOrderGrid = new GridPane();
		draftOrderGrid.setId("draftOrderGrid");
		draftQ.add(draftQ.poll());
		
		Object[] draftQueue = draftQ.toArray();
		
		for(int i=0; i<draftQ.size(); i++){
			TextField currentTeam = new TextField( ((Team)draftQueue[i]).getName() );
			currentTeam.setId("teamNames");
			currentTeam.setMinWidth(169);
			currentTeam.setPrefWidth(169);
			currentTeam.setAlignment(Pos.CENTER);
			
			BorderPane gridPane = new BorderPane();
			gridPane.setCenter(currentTeam);
			
			draftOrderGrid.getRowConstraints().add(new RowConstraints(600/(numTeams+1)));
			draftOrderGrid.add(gridPane, 0, i);
		}
		draftBorderPane.setLeft(draftOrderGrid);
	}
	/**
	 * Connects to the CPANEL database and adds the top 300 Players from the database to the players ObservableList
	 * @return
	 */
	private ObservableList<Player> databaseConnect() {
		dbCon = new Connect();
		ObservableList<Player> players = FXCollections.observableArrayList();
		players.addAll(dbCon.getPlayerData());
		return players;
	}
	
	public boolean removePlayer(String name){
		
		for(int i=0; i<players.size();i++){
			if(players.get(i).getName().equals(name)){
				players.remove(i);
				top10Table.setItems(players);
				return true;
			}
		}
		
		return false;
		
		
	}

	/**
	 * 
	 */
	private void positionalColumnInit() {
		qbRankCol.setCellValueFactory(new PropertyValueFactory<Player,Integer>("rank"));
		rbRankCol.setCellValueFactory(new PropertyValueFactory<Player,Integer>("rank"));
		wrRankCol.setCellValueFactory(new PropertyValueFactory<Player,Integer>("rank"));
		teRankCol.setCellValueFactory(new PropertyValueFactory<Player,Integer>("rank"));
		defRankCol.setCellValueFactory(new PropertyValueFactory<Player,Integer>("rank"));
		kickRankCol.setCellValueFactory(new PropertyValueFactory<Player,Integer>("rank"));
		qbNameCol.setCellValueFactory(new PropertyValueFactory<Player,String>("name"));
		rbNameCol.setCellValueFactory(new PropertyValueFactory<Player,String>("name"));
		wrNameCol.setCellValueFactory(new PropertyValueFactory<Player,String>("name"));
		teNameCol.setCellValueFactory(new PropertyValueFactory<Player,String>("name"));
		defNameCol.setCellValueFactory(new PropertyValueFactory<Player,String>("name"));
		kickNameCol.setCellValueFactory(new PropertyValueFactory<Player,String>("name"));
		qbTeamCol.setCellValueFactory(new PropertyValueFactory<Player,String>("team"));
		rbTeamCol.setCellValueFactory(new PropertyValueFactory<Player,String>("team"));
		wrTeamCol.setCellValueFactory(new PropertyValueFactory<Player,String>("team"));
		teTeamCol.setCellValueFactory(new PropertyValueFactory<Player,String>("team"));
		defTeamCol.setCellValueFactory(new PropertyValueFactory<Player,String>("team"));
		kickTeamCol.setCellValueFactory(new PropertyValueFactory<Player,String>("team"));
	}

}
