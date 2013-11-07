package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import Data.*;
import DBSetup.*;

public class Controller implements Initializable {

	ArrayList<Player> dbplayers;
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
	@FXML private ToggleGroup x1;
	@FXML private Label playerInfo;
	@FXML private Button draftPlayer;
	
	ObservableList<Player> players;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		players = FXCollections.observableArrayList();
		
		Player testPlayer = new Player("johnny doughnuts", 301, "RB", "RB99", "Doughnuts", 80);
		ObservableList<String> team1 = FXCollections.observableArrayList();
		team1.add("1. "+ testPlayer.getName());
		
		Connect con = new Connect();
		dbplayers=con.getPlayerData();
		
		players.addAll(dbplayers);
		
		rankColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("rank"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("position"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("team"));
        
        playerTable.setItems(players);
        
        draftHistory.setItems(team1);
       
	}
	
	@FXML protected void searchPlayers(KeyEvent event) {
		
		updateTable(search.getText().toLowerCase(), findToggle());
        
	}
	
	
	@FXML protected void filterPlayers(MouseEvent event){
		
		updateTable(search.getText().toLowerCase(), findToggle());
			
	}
	
	
	@FXML protected void playerClicked(MouseEvent event){
		
		Player selected = playerTable.getSelectionModel().getSelectedItem();
		playerInfo.setText(selected.getName()+ " " + selected.getPosition() + " Rank: " + selected.getRank() );
		playerInfo.setFont(new Font("System", 25));
		draftPlayer.setDisable(false);
		
	}
	
	public void updateTable(String searchText, String pos){
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
	
	public String findToggle(){
		Toggle selected = x1.getSelectedToggle();
		if(selected == allFilter)
			return "ALL";
		else if (selected == qbFilter)
			return "QB";
		else if (selected == wrFilter)
			return "WR";
		else if (selected == rbFilter)
			return "RB";
		else if (selected == teFilter)
			return "TE";
		else if (selected == defFilter)
			return "DEF";
		else if (selected == kFilter)
			return "K";
		else{
			System.out.println("Radio button not found!");
			return null;
		}
		
	}
}
