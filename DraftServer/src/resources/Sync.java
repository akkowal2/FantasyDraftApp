package resources;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ChunkedOutput;
import org.glassfish.jersey.server.ResourceConfig;

@Singleton
@Path("Sync")
public class Sync {

	private SseBroadcaster broadcaster = new SseBroadcaster();
	//private ArrayList<EventOutput> connections = new ArrayList<EventOutput>();
	private SseFeature feature = new SseFeature();
	
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String broadcastMessage(String message) {
    	System.out.println("I broadcasssst guuud");
    	
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        
       /* OutboundEvent event = eventBuilder.name("message")
            .mediaType(MediaType.TEXT_PLAIN_TYPE)
            .data(String.class, message)
            .build();*/
 
        broadcaster.broadcast(eventBuilder.data(String.class, "testt").build());
        
        /*System.out.println("Number of Connections: " + connections.size());
        for (EventOutput connection : connections){
        	try {
				connection.write(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Choke a bitch");
				e.printStackTrace();
			}
        }*/
 
        return "Message was '" + message + "' broadcast.";
    }
 
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput listenToBroadcast() {
    	System.out.println("I get in here");
        final EventOutput eventOutput = new EventOutput();
        //connections.add(eventOutput);
        //System.out.println("Number of cons: " + connections.size());
        this.broadcaster.add(eventOutput);
        
        return eventOutput;
    }
	
    
    /*@GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getServerSentEvents() {
    	
        final EventOutput eventOutput = new EventOutput();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        // ... code that waits 1 second
                        final OutboundEvent.Builder eventBuilder
                        = new OutboundEvent.Builder();
                        eventBuilder.name("message-to-client");
                        eventBuilder.data(String.class,
                            "Hello world " + i + "!");
                        final OutboundEvent event = eventBuilder.build();
                        eventOutput.write(event);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(
                        "Error when writing the event.", e);
                } finally {
                    try {
                        eventOutput.close();
                    } catch (IOException ioClose) {
                        throw new RuntimeException(
                            "Error when closing the event output.", ioClose);
                    }
                }
            }
        }).start();
        return eventOutput;
    }
    */
}
