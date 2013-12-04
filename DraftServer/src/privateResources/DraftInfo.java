package privateResources;

import java.util.ArrayList;

import privateResources.Connect;
import privateResources.Player;
import privateResources.Team;

public class DraftInfo{
	
	ArrayList<Team> clients;
	String leagueName;
	String leaguePass;
	boolean startDraft;
	ArrayList<Player> lastPlayersPicked;
	
	public DraftInfo(String leagueName, String leaguePass){
		this.leagueName = leagueName.replaceAll(" ", "_");
		this.leaguePass = leaguePass;
		startDraft = false;
		lastPlayersPicked = new ArrayList<Player>();
	}
	
	public void startDraft(){
		startDraft = true;
		Connect con = new Connect();
		clients = con.getTeams(leagueName);
	}
	
	public boolean compareDraftInfo(String name, String pass){
		if (leagueName.equals(name) && leaguePass.equals(pass)) return true;
		
		return false;
	}
	
	public String getLeagueName(){
		return leagueName;
	}
	
	public boolean draftReady(){
		return startDraft;
	}
	
	public void addPlayer(Player player){
		
		lastPlayersPicked.clear();
		lastPlayersPicked.add(player);
		
		/*if (lastPlayersPicked.size() < 3) lastPlayersPicked.add(player);
		else{
			lastPlayersPicked.remove(0);
			lastPlayersPicked.add(player);
		}*/
	}
	
	public ArrayList<Player> getLastPlayers(){
		return lastPlayersPicked;
	}
	
}
