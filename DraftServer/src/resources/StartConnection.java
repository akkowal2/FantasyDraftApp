package resources;

import java.util.ArrayList;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import privateResources.DraftInfo;
import privateResources.Connect;
import privateResources.Player;
import privateResources.Team;

@Singleton
@Path ("/StartConnection")
public class StartConnection {
	
	static ArrayList<DraftInfo> currDraftInfos = new ArrayList<DraftInfo>();
	
	/**
	 * Clients make a GET request to /Connect.
	 * Clients need the correct league name and password and also pass in the team name they want.
	 * If the league exists and is currently open the team will be created and added to the league.
	 * Upon success, this method will return a json string of the BPA list back to the client.
	 *
	 * @param leagueName
	 * @param leaguePass
	 * @param teamName
	 * @return Response 200 or 500 depending on success
	 */
	
	@GET
	@Path ("/Connect/{leagueName}/{leaguePass}/{teamName}")
	@Produces("application/json")
	public Response connect(@PathParam("leagueName") String leagueName, @PathParam("leaguePass") String leaguePass, @PathParam("teamName") String teamName){
		Connect con = new Connect();
		if (con.checkAuthorization(leagueName, leaguePass)){
			
		    boolean result = con.addConnection(leagueName, teamName);
		    ArrayList<Player> players = con.getPlayerData();
			Gson gson = new Gson();
			String json = gson.toJson(players);
		    
			if (result == false){
				return Response.serverError().entity("League has been started already!").build();
			}
			
			
			return Response.ok("GET TO DA CHOPPA", MediaType.APPLICATION_JSON).build();
		}
		
		return Response.serverError().entity("League Authorization failed, please check you have the right league name and password").build();
	}
	
	/**
	 * Game manager makes GET request to OpenConnections to create the league and this function initializes everything the league needs.
	 * Creates a table for the new league and stores the league password and allows new teams to be added to the league
	 *
	 * @param leagueName
	 * @param leaguePass
	 * @return Response 200 or 500 depending on success
	 */
	
	@GET
	@Path("/OpenConnections/{leagueName}/{leaguePass}")
	@Produces("application/json")
	public Response openConnections(@PathParam("leagueName") String leagueName, @PathParam("leaguePass") String leaguePass){
	    
	    Connect con = new Connect();
	    boolean result = con.addLeague(leagueName, leaguePass);
	    
	    if (result){
	    	DraftInfo newDraftInfo = new DraftInfo(leagueName, leaguePass);
			currDraftInfos.add(newDraftInfo);
			
			//Establish connection for DraftInfo here
	    	return Response.ok("League Added").build();
	    }
	    else{
	    	return Response.serverError().entity("League Name already exists").build();
	    }
	   
	}
	
	/**
	 * Game manager makes a GET request to Close Connections to stop adding new teams to league.
	 * Also returns the BPA list as a json string.
	 * 
	 * @param req
	 * @param leagueName
	 * @param leaguePass
	 * @return Response 200 or 500 depending on success
	 */
	
	@GET
	@Path("/CloseConnections/{leagueName}/{leaguePass}")
	@Produces("application/json")
	public Response closeConnections(@PathParam("leagueName") String leagueName, @PathParam("leaguePass") String leaguePass){
		
		Connect con = new Connect();
		
		if (con.checkAuthorization(leagueName, leaguePass)){
			ArrayList<Team> teams = con.getTeams(leagueName);
			//System.out.println(teams.toString());
			Gson gson = new Gson();
			String json = gson.toJson(teams);
			
			con.closeConnection(leagueName);
			
			for (DraftInfo curr : currDraftInfos){
				if (curr.compareDraftInfo(leagueName, leaguePass)){
					curr.startDraft();
					break;
				}
			}

            Sync.closeCon(leagueName, leaguePass);
			
			return Response.ok(json, MediaType.APPLICATION_JSON).build(); 
		}
		
		return Response.serverError().entity("You are either not the game manager or have incorrect league information").build();
	}
	
	/**
	 * Returns a json string of an ArrayList of Team objects associated with the league name
	 * @param leagueName
	 * @return Response 200 or 500 depending on success
	 */
	
	
	@GET
	@Path("/GetTeams/{leagueName}")
	@Produces("application/json")
	public Response getTeams(@PathParam("leagueName") String leagueName){
		Connect con = new Connect();
		ArrayList<Team> teams = con.getTeams(leagueName);
		if (teams == null){
			return Response.serverError().entity("Incorrect League Name").build();
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(teams);
		
		return Response.ok(json, MediaType.APPLICATION_JSON).build(); 
	}
	
	@GET
	@Path("/Wait/{leagueName}/{leaguePass}")
	@Produces("application/json")
	public Response waitForDraftInfo(@PathParam("leagueName") String leagueName, @PathParam("leaguePass") String leaguePass){
		System.out.println("There are " + currDraftInfos.size() + " drafts");
		for (DraftInfo curr : currDraftInfos){
			if (curr.compareDraftInfo(leagueName, leaguePass) && curr.draftReady()){
				Connect con = new Connect();
				ArrayList<Team> teams = con.getTeams(leagueName);
				if (teams == null){
					return Response.serverError().entity("Incorrect League Name").build();
				}
				
				Gson gson = new Gson();
				String json = gson.toJson(teams);
				
				return Response.ok(json, MediaType.APPLICATION_JSON).build();
			}
			else if (curr.compareDraftInfo(leagueName, leaguePass) && !curr.draftReady()){
				return Response.serverError().entity("Draft is not ready yet.").build();
			}	
		}
		
		return Response.serverError().entity("Draft does not exist yet or you have incorrect league information").build();
	}

    public static ArrayList<DraftInfo> getDraftInfos(){
        return currDraftInfos;
    }
	
}
