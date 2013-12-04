package resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.*;
import org.glassfish.jersey.server.spi.Container;
import privateResources.*;

@Singleton
@Path("Sync")
public class Sync{

    private static ArrayList<IDBroadcaster> broadcasters = new ArrayList<IDBroadcaster>();
    private ArrayList<IDEventOutput> gameManagersIntro = new ArrayList<IDEventOutput>();
    private ArrayList<IDEventOutput> gameManagersDraft = new ArrayList<IDEventOutput>();

    /**
     * When the draft starts and the picked player needs to be broadcasted to every client and the game manager
     * for the particular league.
     *
     * @param player
     * @param leagueName
     * @return
     */

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{leagueName}")
    public String broadcastMessage(String player, @PathParam("leagueName") String leagueName) {
        System.out.println("A POST request came in!");
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder.name("Player Picked")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(String.class, player)
                .build();

        IDBroadcaster clients = findBroadcaster(leagueName);
        IDEventOutput manager = findEvent(leagueName, false);

        clients.broadcast(event);
        try {
            manager.write(event);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return "Message was '" + player + "' broadcast.";
    }

    /**
     * Used to setup communication for the draft process.  Separates manager from clients.
     *
     * @param leagueName
     * @param isManager
     * @return
     */

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @Path("{leagueName}/{isManager}")
    public EventOutput listenToBroadcast(@PathParam("leagueName") final String leagueName, @PathParam("isManager") String isManager) {
        System.out.println("A GET request came in!");
        final IDEventOutput eventOutput;
        Connect con = null;
        ArrayList<Team> teams = null;

        if (isManager.equals("false")){
            eventOutput = new IDEventOutput();
            IDBroadcaster leagueBroadcaster = findBroadcaster(leagueName);
            leagueBroadcaster.add(eventOutput);

            con = new Connect();
            teams = con.getTeams(leagueName);
            
        }
        else{
            eventOutput = new IDEventOutput();
            eventOutput.setLeagueName(leagueName);
            gameManagersDraft.add(eventOutput);
        }

        OutboundEvent event = null;
        if (con != null && teams.size() != 0){
            Gson gson = new Gson();
            String json = gson.toJson(teams.get(teams.size()-1));

            OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
            event = eventBuilder.name("Incoming Team MAYDAY!").mediaType(MediaType.APPLICATION_JSON_TYPE).data(String.class, json).build();
        }


        final IDEventOutput gameManagerIntro = findEvent(leagueName, true);

        if (gameManagerIntro != null && con != null){
            try {
                gameManagerIntro.write(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("here");
                e.printStackTrace();
            }
            
        }

        if (con != null){
        	con.killItWithFire();
            con = null;
        }

        System.out.println("Returned");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                workaround(leagueName, eventOutput, false);
            }
        }).start();


        return eventOutput;
    }

    public static void closeCon(String leagueName, String leaguePass){
        ArrayList<DraftInfo> drafts = StartConnection.getDraftInfos();

        for (DraftInfo draft : drafts){
            if (draft.compareDraftInfo(leagueName, leaguePass)){
                if (draft.draftReady()){
                    IDBroadcaster broadcaster = findBroadcaster(leagueName);
                    OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
                    OutboundEvent event = eventBuilder.name("Draft Started")
                            .mediaType(MediaType.APPLICATION_JSON_TYPE)
                            .data(String.class, "Draft Started")
                            .build();
                    broadcaster.broadcast(event);
                }
            }
        }
    }

    private void workaround(String leagueName, IDEventOutput eventOutput, boolean isManager){
        if (isManager){
            eventOutput.getInit();
        }

        if (eventOutput != null){
            OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
            OutboundEvent event = eventBuilder.name("Workaround").mediaType(MediaType.APPLICATION_JSON_TYPE).data(String.class, "BOOM").build();
            try {
                eventOutput.write(event);
                eventOutput.write(event);
                eventOutput.write(event);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }


    /**
     * Used by the manager in order to obtain new clients of the draft.
     *
     * @param leagueName
     * @return
     */

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @Path("GetTeams/{leagueName}")
    public EventOutput teamListener(@PathParam("leagueName") final String leagueName){
        final IDEventOutput gameManagerIntro = new IDEventOutput();
        gameManagerIntro.setLeagueName(leagueName);
        gameManagersIntro.add(gameManagerIntro);

        IDBroadcaster caster = new IDBroadcaster();
        caster.setLeagueName(leagueName);
        broadcasters.add(caster);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                workaround(leagueName, gameManagerIntro, true);
            }
        }).start();

        return gameManagerIntro;
    }

    /**
     * Helper function to find the correct broadcaster for the league.
     *
     * @param leagueName
     * @return
     */

    private static IDBroadcaster findBroadcaster(String leagueName){
        for (IDBroadcaster broadcaster : broadcasters){
            if (broadcaster.getLeagueName().equals(leagueName)) return broadcaster;
        }

        return null;
    }

    /**
     * Helper function to find the correct event output associated with the manager for the league.
     *
     * @param leagueName
     * @param isIntro
     * @return
     */

    private IDEventOutput findEvent(String leagueName, boolean isIntro){
        ArrayList<IDEventOutput> list;
        if (isIntro) list = gameManagersIntro;
        else list = gameManagersDraft;

        for (IDEventOutput event : list){
            if (event.getLeagueName().equals(leagueName)) return event;
        }

        return null;
    }
}
