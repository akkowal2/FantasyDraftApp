package application;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static com.google.common.base.Charsets.*;

public class DraftController implements Initializable{
	
	@FXML private TableView<Player> top10Table;

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
	@FXML private AnchorPane anchorPane;
	@FXML private BorderPane rightDraft;
	@FXML private BorderPane leftDraft;
	@FXML private BorderPane bpaTablePane;
	@FXML private BorderPane draftBorderPane;
    @FXML private BorderPane researchPane;
    @FXML private BorderPane bottomPaneButton;
	
	private GridPane draftOrderGrid;
	
	public TextField clockTextArea;
    private ArrayList<TableView<Player>> teamTables;
	private ObservableList<Player> players;
	private ObservableList<Team> teams;
	private Queue<Team> draftQ;
    private boolean received = false;
    String leagueName;
    String leaguePassword;
	int numTeams;
    boolean virgin;
    public DraftController(String leagueName, String leaguePassword){
        this.leagueName = leagueName;
        this.leaguePassword = leaguePassword;
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        virgin = true;
		//Database Connection

		
		//players = databaseConnect();


		//teams Array

        Client client = ClientBuilder.newClient();
        WebTarget base = client.target("http://draft.elasticbeanstalk.com/rest");
        WebTarget con=base.path("/StartConnection/CloseConnections/"+leagueName+"/"+leaguePassword+"/");
        Response playerResponse = con.request("application/json").get();

        System.out.println("playerResponse:" + playerResponse);

        if(playerResponse.getStatus()==200){
            String jsonString =playerResponse.readEntity(String.class);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Player>>() {}.getType();
            ArrayList<Player> playerList =gson.fromJson(jsonString,type);
            players = FXCollections.observableArrayList();
            players.addAll(playerList);
        }
        System.out.println("2"+ leagueName);
        WebTarget teamReq = base.path("/StartConnection/GetTeams/"+leagueName+"/");
        Response teamResponse = teamReq.request("application/json").get();
        System.out.println(teamResponse);
        if(teamResponse.getStatus()==200){
            System.out.println("is it here");
            String jsonString =teamResponse.readEntity(String.class);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Team>>() {}.getType();
            ArrayList<Team> teamList =gson.fromJson(jsonString,type);






            teams = FXCollections.observableArrayList();
            teams.addAll(teamList);
            numTeams = teams.size();
            System.out.println(teams);

        }


        /*
        //Fake Practice
        Client client = ClientBuilder.newClient();
        WebTarget base = client.target("http://draft.elasticbeanstalk.com/rest");
        WebTarget con=base.path("/StartConnection/CloseConnections/conkay/kanye/");
        Response playerResponse = con.request("application/json").get();

        System.out.println("playerResponse:" + playerResponse);

        if(playerResponse.getStatus()==200){
            String jsonString =playerResponse.readEntity(String.class);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Player>>() {}.getType();
            ArrayList<Player> playerList =gson.fromJson(jsonString,type);
            players = FXCollections.observableArrayList();
            players.addAll(playerList);
        }

        ArrayList<Team> teamListExample = new ArrayList<Team>();
        teamListExample.add(new Team("Team1"));
        teamListExample.add(new Team("Team2"));
        teamListExample.add(new Team("Team3"));
        teamListExample.add(new Team("Team4"));
        teams = FXCollections.observableArrayList();
        teams.addAll(teamListExample);
        numTeams = teams.size();


        */
		
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


	}
    public void setLeagueName(String leagueName){
        System.out.println("I got " + leagueName);
        this.leagueName=leagueName;

    }
    public void setLeaguePassword(String leaguePassword){
        System.out.println("I got " + leaguePassword);
        this.leaguePassword=leaguePassword;
        received = true;
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
            String position = currentPlayer.getPosition();
            if(position.equals("WR")){
                wrTable.getItems().add(currentPlayer);
            }
            else if(position.equals("RB")){
                rbTable.getItems().add(currentPlayer);
            }
            else if(position.equals("QB")){
                qbTable.getItems().add(currentPlayer);
            }
            else if(position.equals("DEF")){
                defTable.getItems().add(currentPlayer);
            }
            else if(position.equals("K")){
                kickTable.getItems().add(currentPlayer);
            }
            else if(position.equals("TE")){
                teTable.getItems().add(currentPlayer);
            }
			wrTable.setPlaceholder(new Text(" "));
			qbTable.setPlaceholder(new Text(" "));
			rbTable.setPlaceholder(new Text(" "));
			teTable.setPlaceholder(new Text(" "));
			defTable.setPlaceholder(new Text(" "));
			kickTable.setPlaceholder(new Text(" "));
		}

	}
	public BorderPane getBpaBorderPane(){
		return bpaTablePane;
	}
	public AnchorPane getAnchorPane(){
		return anchorPane;
	}
	/**
	 *  Tab to handle the table of drafted players by each team
	 */
	public void teamTableTab() {
        GridPane teamGrid = new GridPane();
        teamTables = new ArrayList<TableView<Player>>();
        System.out.println("Width: " + researchPane.getWidth()+researchPane.getWidth()*.25);

        Double width=researchPane.getWidth();
        if(virgin){
            width = 945.0;
            virgin = false;
        }
        for (int x = 0; x < numTeams; x++){
            TableView<Player> teamTable = new TableView<Player>();
            TableColumn<Player, String> nameCol = new TableColumn<Player, String>("name");
            TableColumn<Player, String> posCol = new TableColumn<Player, String>("position");
            TableColumn<Player, String> teamCol = new TableColumn<Player, String>("team");
            nameCol.setPrefWidth((width/numTeams)/3);
            teamCol.setPrefWidth((width/numTeams)/3);
            posCol.setPrefWidth((width/numTeams)/3);

            nameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
            teamCol.setCellValueFactory(new PropertyValueFactory<Player, String>("team"));
            posCol.setCellValueFactory(new PropertyValueFactory<Player, String>("position"));
            ObservableList<Player> playerListTeam = FXCollections.observableArrayList(teams.get(x).getPlayers());
            Label teamName = new Label();
            teamName.setText(teams.get(x).getName());
            teamName.setAlignment(Pos.CENTER);
            System.out.println("Alignment: " + teamName.getAlignment().toString());

            teamTable.setId("teamTables");
            teamName.setId("researchNames");
            GridPane.setConstraints(teamName,x+1,1);

            GridPane.setHalignment(teamName, HPos.CENTER);
            teamTable.setItems(playerListTeam);

            teamTable.setPrefSize(9000, 9000);

            teamTable.getColumns().addAll(nameCol,posCol, teamCol);

            GridPane.setConstraints(teamTable, x + 1, 2);
            teamGrid.getChildren().addAll(teamTable, teamName);
            teamTables.add(teamTable);
            teamTable.requestFocus();

        }

        teamGrid.setAlignment(Pos.CENTER);
        teamGrid.requestFocus();

        teamGrid.setPrefHeight(7000);
        teamGrid.setPrefWidth(7000);

        //Double width =960.0;
        /*
        //ObservableList<TableColumn<Player, ?>> columnList = teamTable.getColumns();
        for (int i=0 ; i<columnList.size(); i++){

            columnList.get(i).setPrefWidth((width)/numTeams);
            columnList.get(i).setText(teams.get(i).getName());



        }
        */
        //teamTable.requestFocus();
        teamGrid.gridLinesVisibleProperty();
        researchPane.setCenter(teamGrid);

    }

   public ArrayList<TableView<Player>> getTeamTables(){
       return teamTables;
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


		ObservableList<TableColumn<Player, ?>> columnList = top10Table.getColumns();
		for (int i=0 ; i<columnList.size(); i++){
			columnList.get(i).setPrefWidth((340.0/top10Table.getColumns().size()));
		}
	}

	/**
	 * Initializes the left draft order panel where the order of the teams is determined
	 */
	private void draftOrderGridInit() {
        Button update = new Button();
        update.setPrefSize(75,10);
        update.setText("Update");
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.out.println("Here");


                Client client = ClientBuilder.newClient();
                WebTarget base = client.target("http://draft.elasticbeanstalk.com/rest");
                WebTarget con=base.path("/Draft/Update/"+leagueName+"/");
                Response playerResponse = con.request("application/json").get();

                System.out.println("playerResponse:" + playerResponse);
                ArrayList<Player> lastPlayer = null;
                if(playerResponse.getStatus()==200){
                    String jsonString =playerResponse.readEntity(String.class);
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Player>>() {}.getType();
                    lastPlayer =gson.fromJson(jsonString,type);

                }
                else{
                    return;
                }
                if(lastPlayer==null || lastPlayer.size()==0){
                    return;
                }
                else if(lastPlayer.size()==1){
                    Team currentTeam = draftQ.peek();
                    if(currentTeam.getPlayers()==null || !currentTeam.getPlayers().contains(lastPlayer.get(0))){
                        currentTeam.addPlayer(lastPlayer.get(0));
                    }
                    else if(currentTeam.getPlayers().contains(lastPlayer.get(0))){
                        return;
                    }
                }
                



                positionalBreakdown(draftQ.peek());
                teamTableTab();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                rotateDraftOrder();


            }
        });
        bottomPaneButton.setCenter(update);
        update.setAlignment(Pos.CENTER);

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







	public TableView<Player> getTop10Table() {
		return top10Table;
	}
	public void setTop10Table(TableView<Player> top10Table) {
		this.top10Table = top10Table;
	}
	public TableView<Player> getQbTable() {
		return qbTable;
	}
	public void setQbTable(TableView<Player> qbTable) {
		this.qbTable = qbTable;
	}
	public TableView<Player> getRbTable() {
		return rbTable;
	}
	public void setRbTable(TableView<Player> rbTable) {
		this.rbTable = rbTable;
	}
	public TableView<Player> getWrTable() {
		return wrTable;
	}
	public void setWrTable(TableView<Player> wrTable) {
		this.wrTable = wrTable;
	}
	public TableView<Player> getTeTable() {
		return teTable;
	}
	public void setTeTable(TableView<Player> teTable) {
		this.teTable = teTable;
	}
	public TableView<Player> getDefTable() {
		return defTable;
	}
	public void setDefTable(TableView<Player> defTable) {
		this.defTable = defTable;
	}
	public TableView<Player> getKickTable() {
		return kickTable;
	}
	public void setKickTable(TableView<Player> kickTable) {
		this.kickTable = kickTable;
	}


}
