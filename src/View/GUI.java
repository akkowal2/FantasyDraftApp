package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter; 
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.*;

import java.awt.*;
import java.util.Iterator;
import java.util.regex.PatternSyntaxException;




import javax.swing.JMenu;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import DBSetup.Connect;
import Data.ExcelParser;
import Data.Player;

public class GUI implements ActionListener {
	
	JPanel backgroundPanel;
	JPanel tablePanel;
	JPanel searchPanel;
	JFrame frame;
	JTable playerTable;
	ArrayList<Player> players;
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
		openSelection();
		
		playerTableInit();
		searchPanelInit();
		backgroundPanel.add(searchPanel,BorderLayout.PAGE_START);
		backgroundPanel.add(tablePanel,BorderLayout.CENTER);
		backgroundPanel.setVisible(true);
	}
	
	private void searchPanelInit() {
		searchPanel = new JPanel (new BorderLayout());
		final JTextField filterCpText = new JTextField();
        filterCpText.setFont(new Font("Serif", Font.BOLD, 28));
        filterCpText.setForeground(Color.BLUE);
        filterCpText.setBackground(Color.LIGHT_GRAY);
        TableModel myTableModel = playerTable.getModel();
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(myTableModel);
        playerTable.setRowSorter(sorter);
        filterCpText.getDocument().addDocumentListener(new DocumentListener() {

            private void searchFieldChangedUpdate(DocumentEvent evt) {
                String text = filterCpText.getText();
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                    playerTable.clearSelection();
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
                        playerTable.clearSelection();
                    } catch (PatternSyntaxException pse) {
                        JOptionPane.showMessageDialog(null, "Bad regex pattern",
                                "Bad regex pattern", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            @Override
            public void insertUpdate(DocumentEvent evt) {
                searchFieldChangedUpdate(evt);
            }

            @Override
            public void removeUpdate(DocumentEvent evt) {
                searchFieldChangedUpdate(evt);
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                searchFieldChangedUpdate(evt);
            }
        });
    
        
        searchPanel.add(filterCpText,BorderLayout.CENTER);
        
		
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
		
		DefaultTableModel model = new DefaultTableModel(columnNames,players.size()) {
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
		
		for(int col=0;col<5;col++){
			for(int row=0; row < players.size(); row++ ){
				if(col==0){
					int ranking = new Integer(players.get(row).rank);
					playerTable.setValueAt(ranking, row, col);
				}
				else if(col==1){
					String playerName=players.get(row).name;
					playerTable.setValueAt(playerName, row, col); 
				}
				else if(col==2){
					String position = players.get(row).position;
					playerTable.setValueAt(position,row,col);
					
				}
				else if (col==3){
					String cityName = players.get(row).team;
					playerTable.setValueAt(cityName,row,col);
					
				}
				else if(col == 5){
					String positionalRank = players.get(row).positionalRank;
					playerTable.setValueAt(positionalRank, row, col);
				}
			}
		}
		playerTable.setAutoCreateRowSorter(true);
		
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(new JScrollPane(playerTable));
		
		
		
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
		
		Connect con = new Connect();
		players=con.getPlayerData();
		
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
