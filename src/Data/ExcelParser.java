package Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelParser {
	
	HSSFSheet sheet;
	ArrayList<Player> players;


	public ExcelParser(FileInputStream bpaList){
		HSSFWorkbook workbook=null;
		try {
			workbook = new HSSFWorkbook(bpaList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sheet=workbook.getSheetAt(0);
		players = new ArrayList<Player>();
		
		parseSheet();
		
	}
	
	
	private void parseSheet(){
		Iterator<org.apache.poi.ss.usermodel.Row> rowIterator= sheet.iterator();
		//skip the first row
		rowIterator.next();
		//iterate through the rows
		while(rowIterator.hasNext()){
			org.apache.poi.ss.usermodel.Row thisRow=rowIterator.next();
			java.util.Iterator<Cell> cellIterator=thisRow.iterator();
			String name = null;
			String team = null;
			String position = null;
			int rank = 0;
			String positionalRank = null;
			int byeWeek = 0;
			
			for(int i=0;i<5;i++){
				Cell thisCell=cellIterator.next();
				if(i==0){
					rank=(int) thisCell.getNumericCellValue();
				}
				if(i==1){
					String result = thisCell.getStringCellValue();
					//this is where the string parsee should happen
					name = getPlayerName(result);
					team = getPlayerTeam(result);
				}
				if(i==2){
					//String bye =(String) thisCell.getStringCellValue();
					//byeWeek = Integer.parseInt(bye);
					//byeWeek = (int) thisCell.getNumericCellValue();
					if(thisCell.getCellType()==0){
						byeWeek = (int) thisCell.getNumericCellValue();
					}
					else{
						
						String bye =(String) thisCell.getStringCellValue();
						if(bye.contains("--")){
							byeWeek = 0;
						}
						else byeWeek = Integer.parseInt(bye);
					}
					
				}
				if(i==3){
					positionalRank = thisCell.getStringCellValue();
					
					
					if(positionalRank.contains("DEF")){
						position = "DEF";
					}
					else{
						position = positionalRank.substring(0,2);
						if(position.contains("K"))position="K";
					}
				}
			}
			Player thisPlayer=new Player(name, rank, position, positionalRank, team, byeWeek);
			players.add(thisPlayer);
		}
	}

	
	
	private String getPlayerTeam(String input) {
		String team = null;
		if(input.contains(",")){
			//normal player
			team = input.substring(input.indexOf(',')+1, input.length());
		}
		else{
			if(input.contains("Seattle"))team ="SEA";
			if(input.contains("San Fran"))team ="SF";
			if(input.contains("Baltimore"))team ="BAL";
			if(input.contains("Chicago"))team ="CHI";
			if(input.contains("Green Bay"))team = "GB";
			if(input.contains("Cincinnati"))team ="CIN";
			if(input.contains("Detroit"))team ="DET";
			if(input.contains("Minnesota"))team ="MIN";
			if(input.contains("Atlanta"))team ="ATL";
			if(input.contains("Carolina"))team ="CAR";
			if(input.contains("New Orleans"))team ="NO";
			if(input.contains("Tampa Bay"))team ="TB";
			if(input.contains("Cleveland"))team ="CLE";
			if(input.contains("Pittsburgh"))team ="PIT";
			if(input.contains("Houston"))team ="HOU";
			if(input.contains("Indianapolis"))team ="IND";
			if(input.contains("Jacksonville"))team ="JAC";
			if(input.contains("Tennessee"))team ="TEN";
			if(input.contains("Buffalo"))team ="BUF";
			if(input.contains("Miami"))team ="MIA";
			if(input.contains("New England"))team ="NE";
			if(input.contains("New York Jets"))team ="NYJ";
			if(input.contains("New York Giants"))team ="NYG";
			if(input.contains("Denver"))team ="DEN";
			if(input.contains("Kansas"))team ="KC";
			if(input.contains("Oakland"))team ="OAK";
			if(input.contains("San Diego"))team ="SD";
			if(input.contains("Dallas"))team ="DAL";
			if(input.contains("Philadelphia"))team ="PHI";
			if(input.contains("Washington"))team ="WAS";
			if(input.contains("Arizona"))team ="ARI";
			if(input.contains("Seattle"))team ="SEA";
			if(input.contains("Louis"))team ="STL";
		}
		return team;
	}


	private String getPlayerName(String input) {
		String name;
		if(input.contains(",")){
			//normal player
			name = input.substring(0, input.indexOf(','));
		}
		else{
			//defense player
			name = input;
			
		}
		return name;
		
		
		
		
	}


	public HSSFSheet getSheet() {
		return sheet;
	}


	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}


	public ArrayList<Player> getPlayers() {
		return players;
	}


	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
}
