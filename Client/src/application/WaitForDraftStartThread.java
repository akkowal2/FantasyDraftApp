package application;

public class WaitForDraftStartThread extends Thread {
	
	
	
	boolean finished = false;
	String leagueName;
	String password;
	Model currModel;
	
	public WaitForDraftStartThread(String leagueName, String password, Model currModel){
		super();
		this.leagueName = leagueName;
		this.password = password;
		this.currModel = currModel;
	}
	public void stopThread()
	{
		finished = true;
	}
	
	
	
	
	public void run()
	{
		//Client client = ClientBuilder.newClient();
		//WebTarget base = client.target("http://draft-env.elasticbeanstalk.com/rest");
		
		
		while(!finished){
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// update ProgressIndicator on FX thread
			
	   	}
		
	}
	
}
