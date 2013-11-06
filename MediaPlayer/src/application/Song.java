package application;

import javafx.beans.property.SimpleStringProperty;

public class Song {
	private SimpleStringProperty name;
	private SimpleStringProperty artist;
	
	public Song(String name, String artist){
		this.name = new SimpleStringProperty(name);
		this.artist = new SimpleStringProperty(artist);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getArtist() {
		return artist.get();
	}

	public void setArtist(String artist) {
		this.artist.set(artist);
	}
	
	
	
}
