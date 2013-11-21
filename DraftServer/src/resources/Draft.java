package resources;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import core.DraftInfo;
import dbConnection.Connect;
import dbConnection.Player;
import dbConnection.Team;

@Path("Draft")
public class Draft {

	@POST
	@Path("Pick/{leagueName}/{teamName}")
	public Response addPlayer(String player, @PathParam("leagueName") String leagueName, @PathParam("teamName") String teamName){
		DraftInfo draft = null;
		for (DraftInfo curr : Server.getDraftInfos()){
			if (curr.getLeagueName().equals(leagueName)){
				draft = curr;
				break;
			}
		}
		
		if (draft == null) return Response.serverError().entity("Incorrect League Name").build();
		
		
		
		Gson gson = new Gson();
		
		Type type = new TypeToken<Player>() {}.getType();
		
		
		Player tempPlayer = (gson.fromJson(player, type));
		
		Connect con = new Connect();
		boolean result = con.addPlayerToTeam(leagueName, tempPlayer, teamName);
		
		//if (result == false) return Response.serverError().entity("Incorrect Team Name").build();
		
		draft.addPlayer(tempPlayer);
		
		return Response.ok("Drafted Player Received").build();
	}
	
	@GET
	@Path("Update/{leagueName}")
	@Produces("application/json")
	public Response getUpdates(@PathParam("leagueName") String leagueName){
		DraftInfo draft = null;
		for (DraftInfo curr : Server.getDraftInfos()){
			if (curr.getLeagueName().equals(leagueName)){
				draft = curr;
				break;
			}
		}
		
		if (draft == null) return Response.serverError().entity("Incorrect League Name").build();
		
		Gson gson = new Gson();
		ArrayList<Player> players = draft.getLastPlayers();
		String json = gson.toJson(players);
		
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
}
