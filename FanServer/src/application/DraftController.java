package application;

import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DraftController implements Initializable{
	
	@FXML private BorderPane draftBorderPane;
	private GridPane draftOrder;
	@FXML private TableView<Player> top10Table;
	@FXML private TableColumn<Player, Integer> rankCol;
	@FXML private TableColumn<Player, String> nameCol;
	@FXML private TableColumn<Player, String> positionCol;
	@FXML private TableColumn<Player, String> prCol;
	private ArrayList<TableColumn<Player, String>> teams;
	@FXML private TableView<Player> teamTable;
	
	Connect dbCon;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Database Connection
		dbCon = new Connect();
		ObservableList<Player> players = FXCollections.observableArrayList();
		players.addAll(dbCon.getPlayerData());
		
		
		//Top 10 Table Initialization
		rankCol.setCellValueFactory(new PropertyValueFactory<Player, Integer>("rank"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
		positionCol.setCellValueFactory(new PropertyValueFactory<Player, String>("position"));
		prCol.setCellValueFactory(new PropertyValueFactory<Player, String>("positionalRank"));
		top10Table.setItems(players);
		
		//Team View Table Initialization Mock
		teams = new ArrayList<TableColumn<Player, String>>();
		for (int x = 0; x < 8; x++){
			TableColumn<Player, String> newCol = new TableColumn<Player, String>("Team " + x);
			newCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
			teams.add(newCol);
			teamTable.getColumns().add(newCol);
		}
		
		ObservableList<Player> testData = FXCollections.observableArrayList();
		testData.add(new Player("Adrian Peterson", -1, null, null, null, -1));
		testData.add(new Player("Bobby Hill", -1, null, null, null, -1));
		testData.add(new Player("OBAMA", -1, null, null, null, -1));
		testData.add(new Player("Brian Dahmen", -1, null, null, null, -1));
		teamTable.setItems(testData);
		
	}
}
