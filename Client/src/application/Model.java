package application;

import java.util.ArrayList;
import java.util.Hashtable;













import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import Data.Player;
import Data.Team;


public class Model {
	
	private ObservableList<Player> players = FXCollections.observableArrayList();
	private ArrayList<Team> teams = new ArrayList<Team>();
	private ArrayList<ObservableList<String>> teamPlayerLists = new ArrayList<ObservableList<String>>();
	private Team clientTeam;
	private int currentPick;
	private int clientPick;
	private String teamName;
	private String leagueName;
	private String password;
	
	private Player lastPicked;
	
	public void initialize(TableView<Player> playerTable, ArrayList<Player> dbplayers){
		
		players.addAll(dbplayers);
		playerTable.setItems(players);
		currentPick = 0;
		
	}
	
	public void setUpTeams(){
		for(int i=0; i<teams.size();i++){
			
			ObservableList<String> willthiswork = FXCollections.observableArrayList();
			teamPlayerLists.add(willthiswork);
			if(teams.get(i).getName().equals(teamName)){
				clientTeam = (teams.get(i));
				clientPick = i;
			}
		}
	}
	
	public void updateTable(String searchText, String pos, TableView<Player> playerTable){
		ObservableList<Player> filtered = FXCollections.observableArrayList();
		
		if (searchText.length() == 0) {
            filtered.addAll(players);
        } else {
        	for(Player player : players){
        		if (player.getName().toLowerCase().contains(searchText)){
        			filtered.add(player);
        		}
        			
        	}
        }
		if (!pos.equals("ALL") && !pos.equals(null)){
			for(Player player : players){
				if (filtered.contains(player) && !player.getPosition().equals(pos)){
					filtered.remove(player);
				}	
			}
		}
		
		
        playerTable.setItems(filtered);
	}
	
	public String findToggle(ToggleGroup filterGroup, Hashtable<String, RadioButton> filters){
		Toggle selected = filterGroup.getSelectedToggle();
		if(selected == filters.get("ALL"))
			return "ALL";
		else if (selected == filters.get("QB"))
			return "QB";
		else if (selected == filters.get("WR"))
			return "WR";
		else if (selected == filters.get("RB"))
			return "RB";
		else if (selected == filters.get("TE"))
			return "TE";
		else if (selected == filters.get("DEF"))
			return "DEF";
		else if (selected == filters.get("K"))
			return "K";
		else{
			System.out.println("Radio button not found!");
			return null;
		}
		
	}
	
	public void populateTeams(Hashtable<Integer, Label> pickNumbers, Hashtable<Integer, Label> pickNames, Menu chooseTeam, final ListView<String> teamList, ArrayList<MenuItem> teamMenuItems){
		
		System.out.println("in here1");
		pickNames.get(0).setText(currentPick + ". " + teams.get(currentPick).getName());
		System.out.println("in here2");
		for(int i = 0; i < pickNumbers.size(); i++){
			
			pickNumbers.get(i).setText((i+1)+".");
			pickNames.get(i+1).setText(teams.get((i+1)%teams.size()).getName());
			
		}
		for(int i = 0; i < teams.size(); i++){
			MenuItem team = new MenuItem(teams.get(i).getName());
			final Team currTeam = teams.get(i);
			team.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	ObservableList<String> teamPlayers = FXCollections.observableArrayList();
			    	System.out.println(currTeam.getName());
			    	for(int j = 0; j <currTeam.getPlayers().size(); j++){
			    		System.out.println(currTeam.getPlayers().get(j).getName());
			    		teamPlayers.add(currTeam.getPlayers().get(j).getName());
			    	}
			    	teamList.setItems(teamPlayers);
			    }

				
			});
			teamMenuItems.add(team);
			chooseTeam.getItems().add(team);
		}
		System.out.println("done");
	}
	

	public void addPlayerToTeam(Player picked){
		this.teamPlayerLists.get(currentPick%teams.size()).add(picked.getName());
		this.teams.get(currentPick%teams.size()).addPlayer(picked);
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setTeamName(String name){
		this.teamName = name;
	}
	
	public void setLeagueName(String name){
		this.leagueName = name;
	}
	
	public ArrayList<Team> getTeams(){
		return this.teams;
	}
	
	public int getPick(){
		return this.currentPick;
	}
	
	public void updatePick(){
		this.currentPick++;
	}

	public void removePlayer(Player selected, TableView<Player> playerTable) {
		for(int i = 0; i < players.size(); i++)
			if(players.get(i).getName().equals(selected.getName()))
				players.remove(i);
	}

	public void waitForDraftStart() {
		
	}

	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
		
	}

	public String getLeagueName() {
		return this.leagueName;
	}
	
	public String getPassword(){
		return this.password;
	}

	public int getClientPick() {
		return this.clientPick;
	}
	
	public String getLastPicked(){
		if(this.lastPicked == null)
			return "";
		return this.lastPicked.getName();
	}
	
	public void setLastPicked(Player player){
		this.lastPicked = player;
	}

	public Team getClientTeam() {
		return clientTeam;
	}

	public void setClientTeam(Team clientTeam) {
		this.clientTeam = clientTeam;
	}
}
