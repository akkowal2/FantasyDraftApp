package unitTests;

/*import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRequests {

	@Test
	public void AcreateLeague() {
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://localhost:8080/DraftServer/rest");
		
		//WebTarget con = base.path("StartConnection").path("OpenConnections/league2/test");
		//WebTarget con2 = con.path("OpenConnections/league2/test");
		
		WebTarget con = base.path("StartConnection/OpenConnections/leagueTest8/pass");
		
		
		//WebTarget con = base.path("StartConnection/Wait");
		Response res = con.request().get();
		assertEquals(res.getStatus(), 200);
		//System.out.println(res.readEntity(String.class));
		
		
		System.out.println(res.toString());
	}
	
	@Test
	public void BaddConnection(){
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://localhost:8080/DraftServer/rest");
		
		WebTarget con = base.path("StartConnection/Connect/leagueTest8/pass/Team1");
		
		Response res = con.request("application/json").get();
		assertEquals(res.getStatus(), 200);
		System.out.println(res.readEntity(String.class));
		System.out.println(res.toString());
	}
	
	@Test
	public void DgetTeams(){
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://localhost:8080/DraftServer/rest");
		
		WebTarget con = base.path("StartConnection/GetTeams/leagueTest8");
		
		Response res = con.request("application/json").get();
		assertEquals(res.getStatus(), 200);
		System.out.println(res.readEntity(String.class));
		System.out.println(res.toString());
	}
	
	@Test
	public void CcloseConnections(){
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://localhost:8080/DraftServer/rest");
		
		WebTarget con = base.path("StartConnection/CloseConnections/leagueTest8/pass");
		
		Response res = con.request("application/json").get();
		assertEquals(res.getStatus(), 200);
		System.out.println(res.readEntity(String.class));
		System.out.println(res.toString());
	}

}                                                   */
