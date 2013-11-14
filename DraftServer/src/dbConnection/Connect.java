
package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import server.ClientConnection;

import com.thoughtworks.xstream.XStream;

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
	
	public boolean addLeague(String leagueName, ClientConnection manager, String leaguePass){
		leagueName = leagueName.replaceAll(" ", "_");
		XStream xstream = new XStream();
		String xml = xstream.toXML(manager);
		System.out.println(xml);
		try {
			Statement statement = dbCon.createStatement();
			statement.executeUpdate("CREATE TABLE " + leagueName + " ("
					+ "TeamName TEXT,"
					+ "ConnectionInfo LONGBLOB,"
					+ "Players LONGBLOB,"
					+ "LeaguePass TEXT,"
					+ "OpenConnections INT)");
			statement.executeUpdate("insert into " + leagueName + " values (\"LEAGUE PROPERTY\", \""+ xml +"\", null, \"" + leaguePass + "\", 1)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (e.getMessage().contains("Table") && e.getMessage().contains("already exists")){
				System.out.println("League Name already taken!");
				return false;
			}
			e.printStackTrace();
		}
		
		return true;
	}
	
	public ArrayList<Team> getTeams(String leagueName){
		leagueName = leagueName.replaceAll(" ", "_");
		ArrayList<Team> teams = new ArrayList<Team>();
		XStream xstream = new XStream();
		try {
			Statement statement = dbCon.createStatement();
			String query ="select * from " + leagueName;
			ResultSet res = statement.executeQuery(query);
			while (res.next()){
				String name = res.getString("TeamName");
				if (name.equals("LEAGUE PROPERTY")) continue;
				//ArrayList<Player> players = null;
				teams.add(new Team(name));
				
			}
			return teams;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void addPlayerData(Player data){
		try {
			Statement statement = dbCon.createStatement();
			String query ="insert into BPATable values (\"" + data.getName() + "\",\"" + data.getTeam() + "\",\"" + data.getPosition() + "\"," + data.getRank() + ",\"" + data.getPositionalRank() + "\"," + data.getByeWeek() + ")";
			statement.executeUpdate(query);
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

	public boolean checkAuthorization(String leagueName, String leaguePass) {
		leagueName = leagueName.replaceAll(" ", "_");
		try {
			Statement statement = dbCon.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM " + leagueName + " WHERE LeaguePass='" + leaguePass + "'");
			if (res.first()) return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean closeConnections(String leagueName, String leaguePass, ClientConnection info){//Need to fix this
		XStream xstream = new XStream();
		
		if (checkAuthorization(leagueName, leaguePass)){
			Statement statement = null;
			try {
				statement = dbCon.createStatement();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			ResultSet res = null;
			try {
				res = statement.executeQuery("SELECT * FROM " + leagueName + " WHERE LeaguePass='" + leaguePass + "'");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String managerXML = null;
			try {
				res.first();
				managerXML = (String) res.getString("ConnectionInfo");
				System.out.println("Connection Info XML: " + managerXML);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ClientConnection manager = (ClientConnection) xstream.fromXML(managerXML);
			System.out.println("Manager Host: " + manager.host);
			System.out.println("Manager Address: " + manager.address);
			System.out.println("Manager Port: " + manager.port);
			if (manager == info){
				//Update openConnections to 0 here
				System.out.println("Successful match with game manager");
				return true;
			}
			
		}
		System.out.println("Unsuccessful match with game manager");
		return true;
	}
	
	public void addConnection(String leagueName, String teamName, ClientConnection info){
		XStream xstream = new XStream();
		String xml = xstream.toXML(info);
		try {
			Statement statement = dbCon.createStatement();
			String query ="insert into " + leagueName + " values (\"" + teamName + "\",\""+ xml +"\", null" + "," + "\"n/a\", " + "-1" + ")";
			System.out.println(query);
			statement.executeUpdate(query);
			System.out.println("Player added to League");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}