package Data;

public class Player {
	public String name;
	public String team;
	public String position;
	public int rank;
	public String positionalRank;
	public int byeWeek;
	
	public Player(String name, int rank, String position, String positionalRank, String team, int byeWeek){
		this.name=name;
		this.rank = rank;
		this.position=position;
		this.positionalRank=positionalRank;
		this.team = team;
		this.byeWeek=byeWeek;
		
		
		
		
	}
	
}
