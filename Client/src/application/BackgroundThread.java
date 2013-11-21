package application;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

public class BackgroundThread extends Thread {
	volatile boolean finished = false;
	volatile boolean clockin = false;
	FXMLLoader loader;
	
	
	
	public BackgroundThread(FXMLLoader loader) {
		super();
		this.loader = loader;
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
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Platform.runLater(new Runnable() {
				public void run() {
					Controller server = (Controller)loader.getController();
				
					if(!(server.getTeams().size() == 0) && server.getPick()%(server.getTeams().size()) == server.getClientPick()){
						server.setDraftButtonVisible(false);
					}
					else{
						server.setDraftButtonVisible(true);
					}
					
				}
			});
	   	}
		
	}
	
	
}
