
package privateResources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

public class Connect {
	
	String dbUrl;
	String user;
	String pass;
	String driver;
	
	Connection dbCon = null;
	
	public Connect(){
		dbUrl = "jdbc:mysql://engr-cpanel-mysql.engr.illinois.edu/akkowal2_players";
		user = "akkowal2_admin";
		pass = "bears54";
		driver = "com.mysql.jdbc.Driver";
		
        try {
        	Class.forName("com.mysql.jdbc.Driver");
            //getting database connection to MySQL server
            dbCon = DriverManager.getConnection(dbUrl, user, pass);
           
        } catch (SQLException ex) {
            System.out.println("1");
        	System.out.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
        	System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("2");
			e.printStackTrace();
		} finally{
           //close connection ,stmt and resultset here
        }
		
	}
	
	/**
	 * Adds the new league to the data base by creating a new table for the league.
	 * The first entry in the table stores the league password for future reference.
	 * 
	 * @param leagueName
	 * @param leaguePass
	 * @return true or false based on whether the table was successfully created
	 */
	
	public boolean addLeague(String leagueName, String leaguePass){
		leagueName = leagueName.replaceAll(" ", "_");
		
		try {
			Statement statement = dbCon.createStatement();
			statement.executeUpdate("CREATE TABLE " + leagueName + " ("
					+ "TeamName TEXT,"
					+ "Players LONGBLOB,"
					+ "LeaguePass TEXT,"
					+ "Open INT)");
			statement.executeUpdate("insert into " + leagueName + " values (\"LEAGUE PROPERTY\", null, \"" + leaguePass + "\", 0)");
		} catch (SQLException e) {
			if (e.getMessage().contains("Table") && e.getMessage().contains("already exists")){
				System.out.println("League Name already taken!");
				return false;
			}
            System.out.println("ruh roh");
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * Obtains an arraylist of Team objects from a particular league.
	 * 
	 * @param leagueName
	 * @return ArrayList<Team> upon success, null otherwise
	 */
	
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
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * Used when creating the BPA list in the database.  Only needs to be called once every season.
	 * 
	 * @param data
	 */
	
	public void addPlayerData(Player data){
		try {
			Statement statement = dbCon.createStatement();
			String query ="insert into BPATable values (\"" + data.getName() + "\",\"" + data.getTeam() + "\",\"" + data.getPosition() + "\"," + data.getRank() + ",\"" + data.getPositionalRank() + "\"," + data.getByeWeek() + ")";
			statement.executeUpdate(query);
			System.out.println("Player added to Database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Function to obtain an ArrayList<Player> for the game manager and clients to use.
	 * 
	 * @return ArrayList<Player>
	 */
	
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
			e.printStackTrace();
		}
		
		return players;
	}
	
	/**
	 * Debugging function for fetching players from DB.
	 */
	
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
			e.printStackTrace();
		}
		
	}

	/**
	 * Function that finds the table associated with the leagueName and verifies that the password stored there matches the given password.
	 * 
	 * @param leagueName
	 * @param leaguePass
	 * @return true or false depending on success of verification
	 */
	
	public boolean checkAuthorization(String leagueName, String leaguePass) {
		leagueName = leagueName.replaceAll(" ", "_");
		try {
			Statement statement = dbCon.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM " + leagueName + " WHERE LeaguePass='" + leaguePass + "'");
			
			if (res.first()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public boolean addPlayerToTeam(String leagueName, Player player, String teamName){
		//Add player to team's list
		return false;
	}
	
	/**
	 * Adds a team to an existing league.
	 * 
	 * @param leagueName
	 * @param teamName
	 */
	
	public boolean addConnection(String leagueName, String teamName){
		
		try {
			Statement statement1 = dbCon.createStatement();
			String query = "SELECT * FROM " + leagueName + " WHERE Open=0";
			ResultSet res = statement1.executeQuery(query);
			
			
			
			if (!res.first()) return false;
		} catch (SQLException e1) {
			e1.printStackTrace();
			
			
			return false;
		}
		
		try {
			Statement statement2 = dbCon.createStatement();
			String query ="insert into " + leagueName + " values (\"" + teamName + "\", null" + "," + "\"n/a\", " + " -1)";
			System.out.println(query);
			statement2.executeUpdate(query);
			System.out.println("Player added to League");
			
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public void closeConnection(String leagueName) {
		Statement statement2;
		try {
			statement2 = dbCon.createStatement();
			String query ="UPDATE " + leagueName.replaceAll(" ", "_") + " SET Open=-1";
			System.out.println(query);
			statement2.executeUpdate(query);
			System.out.println(leagueName + " is not accepting anymore clients");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
	}
	
	public void killItWithFire(){
		try {
			dbCon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}