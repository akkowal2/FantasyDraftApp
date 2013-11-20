package application;

import java.util.ArrayList;
import java.util.Hashtable;





import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import DBSetup.Connect;
import Data.Player;
import Data.Team;


public class Model {
	
	private ObservableList<Player> players = FXCollections.observableArrayList();
	private ArrayList<Team> teams = new ArrayList<Team>();
	private ArrayList<ObservableList<String>> teamPlayerLists = new ArrayList<ObservableList<String>>();
	private Team clientTeam;
	
	
	private int currentPick;
	private int totalPicks;
	
	//make a new one?
	private String teamName;
	private String leagueName;
	private String password;
	
	
	
	
	
	
	public void initialize(TableView<Player> playerTable, ArrayList<Player> dbplayers){
		
		players.addAll(dbplayers);
		playerTable.setItems(players);
		
		for(int i=0; i<8;i++){
			teams.add(new Team("Team "+ i, i));
			ObservableList<String> willthiswork = FXCollections.observableArrayList();
			teamPlayerLists.add(willthiswork);
		}
		clientTeam = teams.get(0);
		currentPick = 0;
		
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
	
	public void populateTeams(Hashtable<Integer, Label> pickNumbers, Hashtable<Integer, Label> pickNames, Menu chooseTeam, ListView<String> teamList, ArrayList<MenuItem> teamMenuItems, EventHandler<MouseEvent> clicks){
		
		pickNames.get(0).setText(currentPick + ". " + teams.get(currentPick).getName());
		
		
		for(int i = 0; i < pickNumbers.size(); i++){
			pickNumbers.get(i).setText((i+1)+".");
			pickNames.get(i+1).setText(teams.get(i+1).getName());
			MenuItem team = new MenuItem(teams.get(i).getName());
			team.addEventHandler(MouseEvent.MOUSE_CLICKED, clicks);
			teamMenuItems.add(team);
			chooseTeam.getItems().add(team);
		}
		
	}
	
	public void addPlayerToTeam(Player picked){
		this.teamPlayerLists.get(currentPick%8).add(picked.getName());
		this.teams.get(currentPick%8).addPlayer(picked);
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
		this.players.remove(selected);
	}
	
	
}
