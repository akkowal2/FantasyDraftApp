package Data;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Player {
	private SimpleStringProperty name;
	private SimpleStringProperty team;
	private SimpleStringProperty position;
	private SimpleIntegerProperty rank;
	private SimpleStringProperty positionalRank;
	private SimpleIntegerProperty byeWeek;
	
	public Player(String name, int rank, String position, String positionalRank, String team, int byeWeek){
		this.name=new SimpleStringProperty(name);
		this.rank = new SimpleIntegerProperty(rank);
		this.position=new SimpleStringProperty(position);
		this.positionalRank= new SimpleStringProperty(positionalRank);
		this.team = new SimpleStringProperty(team);
		this.byeWeek= new SimpleIntegerProperty(byeWeek);
		
	}
	
	public String getName(){
		return name.getValue();
	}
	
	public void setName(String name){
		this.name.setValue(name);
	}
	
	public String getPosition(){
		return position.getValue();
	}
	
	public void setPosition(String pos){
		this.position.setValue(pos);
	}
	
	public int getRank(){
		return rank.getValue();
	}
	
	public void setRank(int rank){
		this.rank.setValue(rank);
	}
	
	public String getPositionalRank(){
		return positionalRank.getValue();
	}
	
	public void setPositionalRank(String pos){
		this.positionalRank.setValue(pos);
	}
	
	public String getTeam(){
		return team.getValue();
	}
	
	public void setTeam(String team){
		this.team.setValue(team);
	}
	
	public int getByeWeek(){
		return byeWeek.getValue();
	}
	
	public void setByeWeek(int bye){
		this.byeWeek.setValue(bye);
	}
	
}



