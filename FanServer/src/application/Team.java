package application;

import java.util.ArrayList;

public class Team {

	private String name;
	private ArrayList<Player> players;
	private ClientConnection connect;
	
	public Team(String name, ClientConnection connect){
		this.name = name;
		this.connect = connect;
		players = new ArrayList<Player>();
	}
	
	public void addPlayer(Player newGuy){
		players.add(newGuy);
	}
	
	//Getters and Setters
	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ClientConnection getConnect() {
		return connect;
	}

	public void setConnect(ClientConnection connect) {
		this.connect = connect;
	}


	
}
