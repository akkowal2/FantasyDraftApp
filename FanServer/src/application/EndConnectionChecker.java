package application;

import java.io.IOException;
import java.net.ServerSocket;

public class EndConnectionChecker extends Thread {
	
	private boolean flag = false;
	private ServerSocket killSocket;
	
	
	public EndConnectionChecker(ServerSocket killSocket){
		this.killSocket = killSocket;
		start();
	}
	
	public void run(){
		while (flag == false){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			killSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void EndConnections(){
		flag = true;
	}
	
}
