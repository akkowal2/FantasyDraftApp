package Data;

import java.util.ArrayList;

public class Team {

	private String name;
	private ArrayList<Player> players;
	private int pick;
	//private ClientConnection connect;
	
	public Team(String name, int pick){
		this.name = name;
		players = new ArrayList<Player>();
		this.setPick(pick);
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

	public int getPick() {
		return pick;
	}

	public void setPick(int pick) {
		this.pick = pick;
	}

	


	
}
