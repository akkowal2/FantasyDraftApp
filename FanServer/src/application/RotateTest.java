package application;

import javafx.concurrent.Task;

public class RotateTest {
	DraftController controller;
	public RotateTest(final DraftController controller){
		this.controller=controller;
		
		Task<Void> task = new Task<Void>() {
	         @Override protected Void call() throws Exception {
	        	 while(true){
	        		 
	        		 System.out.println("before");
	        		 System.out.println(controller.numTeams);
	        		 controller.rotateDraftOrder();
	        		 
	        		 
	        		 System.out.println("here nigger");
	        		 
	        	 }
	             
	         }
	     };
	     Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
	}
}
