package server;

import org.glassfish.jersey.media.sse.SseFeature;
import resources.Draft;
import resources.StartConnection;
import resources.Sync;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Drew
 * Date: 11/24/13
 * Time: 12:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class App extends Application {
     public Set<Class<?>> getClasses(){
         Set<Class<?>> s = new HashSet<Class<?>>();
         s.add(StartConnection.class);
         s.add(Sync.class);
         s.add(Draft.class);
         s.add(SseFeature.class);
         return s;
     }


}
