package application;

import java.util.List;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GUIController implements Initializable{

	@FXML private TableView<Song> musicTable;
	@FXML private TableColumn<Song, String> NameCol;
	@FXML private TableColumn<Song, String> ArtistCol;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//musicTable = new TableView<Song>();
		//NameCol = new TableColumn<Song, String>("Name");
		//ArtistCol = new TableColumn<Song, String>("Artist");
		
		NameCol.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));
        ArtistCol.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));

        musicTable.setItems(importSongs());
        //musicTable.getColumns().addAll(NameCol, ArtistCol);
	}

	private ObservableList<Song> importSongs() {
		ObservableList<Song> songs = FXCollections.observableArrayList();
		Song testSong = new Song("Eye of the Tiger", "Survivor");
		Song song2 = new Song("Song Name", "Song Artist");
		songs.add(testSong);
		songs.add(song2);
		
		return songs;
	}
	
	@FXML
	private void functionName(ActionEvent event){
		//System.out.println("sadfsad");
		System.exit(0);
	}
	
	
	
}
