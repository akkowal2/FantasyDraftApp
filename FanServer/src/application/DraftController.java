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
import java.text.DecimalFormat;
import java.util.*;
import java.util.EventListener;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
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
import org.glassfish.jersey.media.sse.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.sse.*;

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
    boolean draftStarted;
    boolean countRunning;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        virgin = true;
		//Database Connection
		//teams Array

        Client client = ClientBuilder.newClient();
        WebTarget base = client.target("http://finalproject54.servehttp.com:8080");
        WebTarget con=base.path("/StartConnection/CloseConnections/"+leagueName+"/"+leaguePassword+"/");
        con.request().get();
        Connect connection = new Connect();
        ArrayList<Player> playerList = connection.getPlayerData();
        players = FXCollections.observableArrayList();
        players.addAll(playerList);


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
            System.out.println("Teams array: "+teams);

        }
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
        final int minutes = 5;
        final double seconds = 0;

        startCountdown();


        //Top 10 text
		TextField top = new TextField("Top 10");
		top.setId("topText");
		top.setAlignment(Pos.CENTER);
		bpaTablePane.setTop(top);
		
		//Mock Draft Order Based off 8 team numbers and arbitrary names
		draftOrderGridInit(true);
		//Current Team Name for Positional Tables
		positionalColumnInit();
		positionalBreakdown(draftQ.peek());

        //Start the connection with the server
        //open the connection with the server


        Client clientSSE = ClientBuilder.newBuilder().register(SseFeature.class).build();
        WebTarget target = clientSSE.target("http://finalproject54.servehttp.com:8080/Sync/"+leagueName+"/true");
        EventSource eventSource = EventSource.target(target).build();
        org.glassfish.jersey.media.sse.EventListener listener = new org.glassfish.jersey.media.sse.EventListener() {
            @Override
            //This is the listener to collect team information
            public void onEvent(InboundEvent inboundEvent) {
                System.out.println(inboundEvent.getName() + "; "+ inboundEvent.readData(String.class));
                if(inboundEvent.getName().equals("Workaround")){
                    return;
                }
                String jsonString =inboundEvent.readData(String.class);
                Gson gson = new Gson();
                Type type = new TypeToken<Player>() {}.getType();
                Player newPlayer =gson.fromJson(jsonString,type);
                final Team currentTeam = draftQ.peek();
                currentTeam.addPlayer(newPlayer);
                draftQ.add(draftQ.poll());
                final Team lastTeam = draftQ.peek();
                removePlayer(newPlayer.getName());
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {


                        positionalBreakdown(lastTeam);
                        teamTableTab();
                        countRunning=false;
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        bpaTableInit();
                        //rotateDraftOrder();
                        draftOrderGridInit(false);
                        startCountdown();
                    }
                });


            }
        };
        eventSource.register(listener);
        eventSource.open();



	}

    private void startCountdown() {
        countRunning = true;
        new Thread(){

            @Override
            public void run() {

                for(int i=300;i>0;i--){
                    if(countRunning==false){
                        break;
                    }
                    final int counter = i;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            int minutes = counter / 60;
                            int seconds = counter % 60;
                            System.out.println("seconds: " + seconds);
                            if (seconds < 10) {
                                clockTextArea.setText(minutes + ":0" + seconds);
                            }
                            else if (seconds ==0) {
                                clockTextArea.setText(minutes + ":00");
                            }
                            else clockTextArea.setText(minutes + ":" + seconds);

                        }
                    });

                }
            }
        }.start();
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

        }
        for (int x = 0; x < numTeams; x++){
            TableView<Player> teamTable = new TableView<Player>();
            TableColumn<Player, String> nameCol = new TableColumn<Player, String>("name");
            TableColumn<Player, String> posCol = new TableColumn<Player, String>("position");
            TableColumn<Player, String> teamCol = new TableColumn<Player, String>("team");
            if(virgin){
                nameCol.setPrefWidth(width/numTeams);
                teamCol.setPrefWidth(width/numTeams);
                posCol.setPrefWidth(width/numTeams);
                virgin=false;
            }
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
	private void draftOrderGridInit(boolean init) {
		draftOrderGrid = new GridPane();
		draftOrderGrid.setId("draftOrderGrid");
        if(init==true){
            draftQ = new ArrayBlockingQueue<Team>(numTeams);
            for(int i=0;i<numTeams;i++){
                draftQ.add(teams.get(i));

            }
        }
        ArrayList<Team> tempList = new ArrayList<Team>();
        for(int i=0;i<numTeams;i++){
            tempList.add(draftQ.peek());
            draftQ.add(draftQ.poll());
        }


		for(int i=0; i<numTeams; i++){
			TextField currentTeam = new TextField(tempList.get(i).getName());
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


}
