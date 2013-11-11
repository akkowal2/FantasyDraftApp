package application;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DraftView extends Application{
	FXMLLoader loader;
	int x;
	@Override
	public void start(Stage primaryStage) {
		try {
			loader = new FXMLLoader(getClass().getResource("../fxml/Draft.fxml"));
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root,960,610);
			scene.getStylesheets().add(getClass().getResource("../css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setResizable(false);
			
			new Thread() {

                // runnable for that thread
                public void run() {
                		x=0;
                    	while(true){
                			try {
                				Thread.sleep(1000);
                			} catch (InterruptedException e) {
                				// TODO Auto-generated catch block
                				e.printStackTrace();
                			}
                			// update ProgressIndicator on FX thread
	                        Platform.runLater(new Runnable() {
	                            public void run() {
	                            		DraftController controller = (DraftController)loader.getController();
	                            		
	                            		controller.rotateDraftOrder();
	                            		controller.positionalBreakdown(controller.getDraftQ().peek());
	                            		Random r = new Random();
	                            		int random=r.nextInt(300);
	                            		
	                            		System.out.println("random::" + random);
	                            		Player draftedPlayer= controller.getPlayersList().get(random);
	                            		controller.getDraftQ().peek().addPlayer(draftedPlayer);
	                            		System.out.println("removed?" + controller.removePlayer(draftedPlayer.getName()));
	                            		x++;
	                        	}
	                        });
                		}
                    }
                
            }.start();
			
			
			primaryStage.show();
			System.out.println("Hello");
			
			
			
			/*
			while(true){
				Thread.sleep(10000);
				System.out.println("update");
				
				controller.rotateDraftOrder();
				
			}
			*/
			//startScheduledExecutorService(loader);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void startScheduledExecutorService(final FXMLLoader loader){
		 
        final ScheduledExecutorService scheduler 
            = Executors.newScheduledThreadPool(1);
 
        scheduler.scheduleAtFixedRate(
                new Runnable(){
                      
                    
                      
                    @Override
                    public void run() {

                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                            	DraftController controller = (DraftController)loader.getController();
                            	while(true){
                            		System.out.println("herro");
                            		controller.rotateDraftOrder();
                            	}
                            	
                            }
                        });
                             
                             
                      
                          
                    }
                }, 
                1, 
                1, 
                TimeUnit.SECONDS);      
    }
}
