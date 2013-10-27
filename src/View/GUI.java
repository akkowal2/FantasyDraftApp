package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter; 
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;


import javax.swing.JMenu;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import javax.swing.JPanel;
import javax.swing.UIManager;

import Data.ExcelParser;
import Data.Player;

public class GUI implements ActionListener {
	
	JPanel backgroundPanel;
	JFrame frame;
	JTable playerTable;
	ArrayList<Player> players;
	ExcelParser data;
	boolean fileOpened;
	
	public GUI(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			
		}
		fileOpened=false;
		initializeBackgroundPanel();
		initializeBackgroundFrame(backgroundPanel);
		initializeMenubar();
		while(fileOpened!=true){
			
		}
		players = new ArrayList<Player>();
		//listFilterPanelInit();
		playerTableInit();
		
	}
	private void teamSelectionInit(){
		JPanel teamNames  = new JPanel(new BorderLayout());
		
	}
	
	private void listFilterPanelInit(){
		JPanel listFilter = new JPanel(new BorderLayout());
		String[] positions = { "QB" , "RB" , "WR" , "TE", "DEF"}; 
		JComboBox positionList = new JComboBox(positions);
		positionList.setSelectedIndex(4);
		positionList.addActionListener(this);
		listFilter.add(positionList, BorderLayout.CENTER );
		backgroundPanel.add(listFilter, BorderLayout.PAGE_END);
		
		
	}
	
	
	private void playerTableInit() {
		
		String columnNames[] = {"Ranking", "Name","Position", "Team"};
		
		DefaultTableModel model = new DefaultTableModel(columnNames,data.getPlayers().size()) {
		    @Override
		    public Class<?> getColumnClass(int column) {
		        if (column == 0) {
		            return Integer.class;
		        }
		        return super.getColumnClass(column);
		    }
		    
		};
		
		playerTable=new JTable(model){@Override
	        public boolean isCellEditable(int arg0, int arg1) {
	         
	            return false;
	    }};
	    
	    centerColumns();
		
		for(int col=0;col<4;col++){
			for(int row=0; row < data.getPlayers().size(); row++ ){
				if(col==0){
					int ranking = new Integer(data.getPlayers().get(row).ranking);
					playerTable.setValueAt(ranking, row, col);
				}
				else if(col==1){
					String playerName=data.getPlayers().get(row).name;
					playerTable.setValueAt(playerName, row, col); 
				}
				else if(col==2){
					String position = data.getPlayers().get(row).position;
					playerTable.setValueAt(position,row,col);
					
				}
				else if (col==3){
					String cityName = data.getPlayers().get(row).team;
					playerTable.setValueAt(cityName,row,col);
					
				}
			}
		}
		playerTable.setAutoCreateRowSorter(true);
		backgroundPanel.add(playerTable.getTableHeader(), BorderLayout.PAGE_START);
		playerTable.getTableHeader().addMouseListener(new MouseAdapter() {
		      @Override
		      public void mouseClicked(MouseEvent mouseEvent) {
		        //this is where the sort algorithm will go
		    	  JTableHeader target = (JTableHeader) mouseEvent.getSource();
		    	  
	              String headerSelected=target.getName();
	              
	              
	              
	              
		    	  mouseEvent.getPoint();
		      };
		    });
		      
		backgroundPanel.add(playerTable,BorderLayout.CENTER);
		backgroundPanel.setVisible(true);
		
	}



	/**
	 * 
	 */
	private void centerColumns() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		playerTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		playerTable.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		playerTable.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
		playerTable.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
	    JScrollPane pane = new JScrollPane(playerTable);
	}



	/**
	 *Initializes MenuBar on the member variable frame
	 */
	private void initializeMenubar() {
		JMenuBar menubar = new JMenuBar();
        
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        
        
        open.addActionListener(this);
        file.add(open);
        menubar.add(file);
        frame.setJMenuBar(menubar);
		
	}



	private void initializeBackgroundFrame(JPanel backgroundPanel) {
		frame = new JFrame();
		frame.setContentPane(backgroundPanel);
		frame.setSize(500,500);
		frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	private void initializeBackgroundPanel(){
		backgroundPanel = new JPanel();
		backgroundPanel.setPreferredSize(new Dimension(500,500));
	    backgroundPanel.setLayout(new BorderLayout(0,0));
	    backgroundPanel.setVisible(false);
	}


	private void openSelection() {
		
	
		final JFileChooser open = new JFileChooser(new File("/home/drew"));
	    open.setFileFilter(new FileFilter() {
	        @Override
	        public boolean accept(File f) {
	            if (f.isDirectory()) {
	                return true;
	            }
	            final String name = f.getName();
	            //note: only accepts files with .txt 
	            return name.endsWith(".xls");
	        }

	        public String getDescription() {
	            return "*.xls";
	        }
	    });
	    open.showOpenDialog(null);
	    
	    File selected = open.getSelectedFile();
	    selected.setWritable(true);
	    selected.setReadable(true);
	    FileInputStream file=null;
		try {
			file= new FileInputStream(selected);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data=new ExcelParser(file);
		
		//variable whether to go ahead and get the table data (after succesful excel file)
		fileOpened=true;
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if("Open".equals(event.getActionCommand())){
			openSelection();
		}
		
	}
	
	public static void main(String [] args){
		GUI view = new GUI();
	}


	
	
	
}
