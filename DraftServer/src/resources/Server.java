package resources;

import java.util.ArrayList;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;

import com.google.gson.Gson;

import server.ClientConnection;
import core.Draft;
import core.InitData;
import dbConnection.Connect;
import dbConnection.Player;
import dbConnection.Team;

@Path ("/StartConnection")
public class Server {
	
	private SseBroadcaster broadcaster = new SseBroadcaster();
	ArrayList<Draft> currDrafts = new ArrayList<Draft>();
	
	@GET
	@Path ("/Connect/{leagueName}/{leaguePass}/{teamName}")
	@Produces("application/json")
	public Response connect(@Context HttpServletRequest req, @PathParam("leagueName") String leagueName, @PathParam("leaguePass") String leaguePass, @PathParam("teamName") String teamName){
		Connect con = new Connect();
		if (con.checkAuthorization(leagueName, leaguePass)){
			String remoteHost = req.getRemoteHost();
		    String remoteAddr = req.getRemoteAddr();
		    int remotePort = req.getRemotePort();
		    ClientConnection client = new ClientConnection(remoteHost, remoteAddr, remotePort);
		    con.addConnection(leagueName, teamName, client);
		    ArrayList<Player> players = con.getPlayerData();
			Gson gson = new Gson();
			String json = gson.toJson(players);
			
		    
		    
			return Response.ok(json, MediaType.APPLICATION_JSON).build();
		}
		
		return Response.serverError().entity("League Authorization failed, please check you have the right league name and password").build();
	}
	
	@GET
	@Path("/OpenConnections/{leagueName}/{leaguePass}")
	@Produces("application/json")
	public Response openConnections(@Context HttpServletRequest req, @PathParam("leagueName") String leagueName, @PathParam("leaguePass") String leaguePass){
		System.out.println("Request made successfully!");
		String remoteHost = req.getRemoteHost();
	    String remoteAddr = req.getRemoteAddr();
	    int remotePort = req.getRemotePort();
	    System.out.println("<<OPEN CONNECTIONS>>");
	    System.out.println("Remote Host: " + remoteHost);
	    System.out.println("Remote Address: " + remoteAddr);
	    System.out.println("Remote Port: " + remotePort);
	    System.out.println("<<OPEN CONNECTIONS>>");
	    ClientConnection manager = new ClientConnection(remoteHost, remoteAddr, remotePort);
	    
	    Connect con = new Connect();
	    boolean result = con.addLeague(leagueName, manager, leaguePass);
	    
	    if (result){
	    	Draft newDraft = new Draft(leagueName, leaguePass);
			currDrafts.add(newDraft);
	    	return Response.ok("League Added").build();
	    }
	    else{
	    	return Response.serverError().entity("League Name already exists").build();
	    }
	   
	}
	
	@GET
	@Path("/CloseConnections/{leagueName}/{leaguePass}")
	@Produces("application/json")
	public Response closeConnections(@Context HttpServletRequest req, @PathParam("leagueName") String leagueName, @PathParam("leaguePass") String leaguePass){
		
		Connect con = new Connect();
		String remoteHost = req.getRemoteHost();
	    String remoteAddr = req.getRemoteAddr();
	    int remotePort = req.getRemotePort();
	    System.out.println("<<CLOSE CONNECTIONS>>");
	    System.out.println("Remote Host: " + remoteHost);
	    System.out.println("Remote Address: " + remoteAddr);
	    System.out.println("Remote Port: " + remotePort);
	    ClientConnection manager = new ClientConnection(remoteHost, remoteAddr, remotePort);
		if (con.closeConnections(leagueName, leaguePass, manager)){
			ArrayList<Player> players = con.getPlayerData();
			//System.out.println(teams.toString());
			Gson gson = new Gson();
			String json = gson.toJson(players);
			System.out.println("<<CLOSE CONNECTIONS>>");
			
			return Response.ok(json, MediaType.APPLICATION_JSON).build(); 
		}
		System.out.println("<<CLOSE CONNECTIONS>>");
		return Response.serverError().entity("You are either not the game manager or have incorrect league information").build();
	}
	
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
	@Singleton
	@Path("/Wait/{leagueName}/{leaguePass}")
	@Produces(MediaType.TEXT_PLAIN)
	public String waitForDraftStart(@PathParam("leagueName") final String leagueName, @PathParam("leaguePass") final String leaguePass){
		System.out.println("Waiting for Manager to start Draft");
		new Thread(new Runnable(){

			@Override
			public void run() {
				OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
				while (true){
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					boolean leagueStart = false;
					boolean draftExists = false;
					for (Draft index : currDrafts){
						if (index.compareDraftInfo(leagueName, leaguePass)){
							draftExists = true;
							if (index.draftReady()){
								leagueStart = true;
								break;
							}
						}
					}
					
					if (leagueStart) break;
					else if (!draftExists){
						OutboundEvent event = eventBuilder.name("Message").mediaType(MediaType.TEXT_PLAIN_TYPE).data(String.class, "Failure").build();
						broadcaster.broadcast(event);
						return;
					}
				}
				
				System.out.println("Start Draft Triggered");
				OutboundEvent event = eventBuilder.name("Message").mediaType(MediaType.TEXT_PLAIN_TYPE).data(String.class, "Success").build();
				broadcaster.broadcast(event);
				
			}
			
		});
			
		
		
		
		return "Waiting for League to Start";
	}
	
}
