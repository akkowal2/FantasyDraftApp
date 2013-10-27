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
			String nam=null;
			String pos= null;
			String city=null;
			int rank=0;
			for(int i=0;i<4;i++){
				Cell thisCell=cellIterator.next();
				if(i==0){
					rank=(int) thisCell.getNumericCellValue();
				}
				if(i==1){
					nam = thisCell.getStringCellValue();
				}
				if(i==2){
					pos = thisCell.getStringCellValue();
				}
				if(i==3){
					city = thisCell.getStringCellValue();
				}
			}
			Player thisPlayer=new Player(nam,rank,pos,city);
			players.add(thisPlayer);
		}
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
