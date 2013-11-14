package core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import resources.Server;
import server.ClientConnection;
import dbConnection.Connect;

public class Draft extends Thread{
	
	ArrayList<ClientConnection> clients;
	String leagueName;
	String leaguePass;
	boolean startDraft;
	
	public Draft(String leagueName, String leaguePass){
		this.leagueName = leagueName.replaceAll(" ", "_");
		this.leaguePass = leaguePass;
		startDraft = false;
	}
	
	public void startDraft(){
		startDraft = true;
		start();
	}
	
	public boolean compareDraftInfo(String name, String pass){
		if (leagueName.equals(name) && leaguePass.equals(pass)) return true;
		
		return false;
	}
	
	public boolean draftReady(){
		return startDraft;
	}
	
	public void run(){
		//This is where the draft logic will go
	}
}
