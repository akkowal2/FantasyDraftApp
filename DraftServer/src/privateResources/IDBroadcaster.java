package privateResources;

import org.glassfish.jersey.media.sse.SseBroadcaster;

/**
 * Created with IntelliJ IDEA.
 * User: Drew
 * Date: 12/3/13
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class IDBroadcaster extends SseBroadcaster {
    private String leagueName;

    public String getLeagueName(){
        return leagueName;
    }

    public void setLeagueName(String name){
        leagueName = name;
    }
}
