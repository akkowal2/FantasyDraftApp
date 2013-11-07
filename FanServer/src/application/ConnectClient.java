package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectClient extends Thread {
	
	Socket clientSocket;
	Server server;
	boolean flag = false;
	
	public ConnectClient(Socket client, Server server){
		clientSocket = client;
		this.server = server;
		start();
	}
	
	public void closeClient(){
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
	    System.out.println ("New Client Connected at " + clientSocket.getInetAddress().getHostAddress());
	    server.getTracker().clients.add(this);
	
	    while (true){
	    	
	    }
	    
	    
	    /*try{ 
	         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); 
	         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
	
	         //String inputLine; 
	
	       	 while (flag == false){
	       		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	       	 }
	
	         out.close(); 
	         in.close(); 
	         clientSocket.close(); 
	    } 
	    catch (IOException e){ 
	        System.err.println("Problem with Server");
	        System.exit(1); 
        } */
    }
}
