package DBSetup;

import java.net.UnknownHostException;

import Data.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connect {
	
	String dbUrl;
	String user;
	String pass;
	String driver;
	//String db;
	Connection dbCon = null;
	
	public Connect(){
		dbUrl = "jdbc:mysql://engr-cpanel-mysql.engr.illinois.edu/akkowal2_players";
		user = "akkowal2_admin";
		pass = "bears54";
		driver = "com.mysql.jdbc.Driver";
		//db = "akkowal2_players";
		
        
       
        try {
        	Class.forName("com.mysql.jdbc.Driver");
            //getting database connection to MySQL server
            dbCon = DriverManager.getConnection(dbUrl, user, pass);
           
        } catch (SQLException ex) {
        	System.out.println(ex.getStackTrace());
        	System.exit(1);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
           //close connection ,stmt and resultset here
        }
		
	}
	
	public void addData(Player data){
		try {
			Statement statement = dbCon.createStatement();
			String query ="insert into BPATable values (\"" + data.name + "\",\"" + data.team + "\",\"" + data.position + "\"," + data.rank + ",\"" + data.positionalRank + "\"," + data.byeWeek + ")";
			int res = statement.executeUpdate(query);
			System.out.println("Player added to Database");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Player> getPlayerData(){
		ArrayList<Player> players = new ArrayList<Player>();
		
		try {
			Statement statement = dbCon.createStatement();
			String query ="select * from BPATable";
			ResultSet res = statement.executeQuery(query);
			while (res.next()){
				String name = res.getString("Name");
				String team = res.getString("Team");
				String position = res.getString("Position");
				int rank = res.getInt("Rank");
				String positionalRank = res.getString("Positional Rank");
				int byeWeek = res.getInt("Bye Week");
				
				Player newPlayer = new Player(name, rank, position, positionalRank, team, byeWeek);
				players.add(newPlayer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return players;
	}
	
	public void printData(){
		try {
			Statement statement = dbCon.createStatement();
			String query ="select * from BPATable";
			ResultSet res = statement.executeQuery(query);
			while (res.next()){
				System.out.println("Name: " + res.getString("Name"));
				System.out.println("Team: " + res.getString("Team"));
				System.out.println("Position: " + res.getString("Position"));
				System.out.println("Rank: " + res.getInt("Rank"));
				System.out.println("Positional Rank: " + res.getString("Positional Rank"));
				System.out.println("Bye Week: " + res.getInt("Bye Week"));
				System.out.println("");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
