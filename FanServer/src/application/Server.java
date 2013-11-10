package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server extends Thread {
	
	public boolean flag = false;
	private EndConnectionChecker ender;
	ClientTracker tracker;
	
	public Server(){
		start();
	}
	
	private Server(Socket client){
		new ConnectClient(client, this);
	}
	
	public void changeFlag(){
		flag = true;
		ender.EndConnections();
	}
	
	public ClientTracker getTracker(){
		return tracker;
	}
	
	public void run(){
		ServerSocket serverSocket = null; 

	    try { 
	         serverSocket = new ServerSocket(10005, 20, InetAddress.getByName("0.0.0.0")); 
	         String host = serverSocket.getInetAddress().getHostAddress();
	         System.out.println(host);
	         System.out.println ("Connection Socket Created");
	         try { 
	              while (flag == false){
	                  System.out.println ("Waiting for Connection");
	                  ender = new EndConnectionChecker(serverSocket);
	                  new Server (serverSocket.accept()); 
	              }
	         } 
	         catch (IOException e){ 
	              //System.err.println("Accept failed."); 
	              System.out.println("Not accepting more connections");
	         } 
	    } 
	    catch (IOException e){ 
	         System.err.println("Could not listen on port: 10008."); 
	         System.exit(1); 
	    } 
	    finally{
	         try {
	              serverSocket.close(); 
	         }
	         catch (IOException e){ 
	              System.err.println("Could not close port: 10008."); 
	              System.exit(1); 
	          } 
	     }
	 }

	 
}
