package DBSetup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import Data.ExcelParser;
import Data.Player;
//import View.GUI;

public class PopulateDB implements ActionListener{

	JPanel backgroundPanel;
	JFrame frame;
	ArrayList<Player> players;
	ExcelParser data;
	boolean fileOpened;
	
	public PopulateDB(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			
		}
		fileOpened=false;
		initializeBackgroundPanel();
		initializeBackgroundFrame(backgroundPanel);
		
		initializeMenubar();
		while(fileOpened!=true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		players = data.getPlayers();

		Connect connect = new Connect();
		for (Player player : players){
			//connect.addData(player);
		}
	
		connect.printData();
		
		backgroundPanel.setVisible(true);
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
		PopulateDB view = new PopulateDB();
	}

}
