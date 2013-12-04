package privateResources;

import org.glassfish.jersey.media.sse.EventOutput;

/**
 * Created with IntelliJ IDEA.
 * User: Drew
 * Date: 12/3/13
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class IDEventOutput extends EventOutput {
    private String leagueName;
    private boolean initFlag = true;

    public String getLeagueName(){
        return leagueName;
    }

    public void setLeagueName(String name){
        leagueName = name;
    }

    public boolean getInit(){
        if (initFlag){
            initFlag = false;
            return true;
        }
        else return false;
    }
}
