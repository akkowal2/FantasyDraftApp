package application;

import java.util.Timer;
import java.util.TimerTask;

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
			// update ProgressIndicator on FX thread
			Platform.runLater(new Runnable() {
				public void run() {
					Controller server = (Controller)loader.getController();
					server.setClockLabel(server.interval);
					
					//server.testLabelChanges();	
					//server.rotateDraftOrder();
				}
			});
	   	}
		
	}
	
	
}
