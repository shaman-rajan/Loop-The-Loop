import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class MyGUI {
	
	static Grid grid;
	static int row, col;
	
	static JFrame mainFrame;
	static JPanel gridPanel;
	static JLabel statusBar;
	static JButton[][] gridCells;
	
	static Thread solveThread;
	
	private static Border defaultBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
	private static int sleepTime = 1;
	
	public static Grid createAndShowGUI() {
		
		// Create the frame
		mainFrame = new JFrame("Loop-The-Loop");
		
		// Change function of close button to terminate the program 
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		getDetails();
		grid = new Grid(row,col);
		
		// Get the main container of the window to add stuff to
		Container mainPane = mainFrame.getContentPane();
		
		// Create the menu bar to add to the main container
		JMenuBar menuBar = new JMenuBar();
		
		// Create the menu to add the the menu bar
		JMenu fileMenu = new JMenu("File");
		
		// Create new game option in the file menu and add functionality to it
		JMenuItem newGame = fileMenu.add("Make Grid");
		newGame.addActionListener( new ActionListener() {			 
			@Override												
			public void actionPerformed(ActionEvent arg0) {
				makeNewGrid();
			}
		});
		
		// Create new game option in the file menu and add functionality to it
		JMenuItem start = fileMenu.add("Start solving");
		start.addActionListener( new ActionListener() {			 
			@Override	
			public void actionPerformed(ActionEvent arg0) {
				solveThread = new Thread() {
					public void run() {
						try {
							grid.solve();
						} catch (ArrayIndexOutOfBoundsException e) {
						}
					}
				};
				solveThread.start();
			}
		});
		
		JMenuItem pause = fileMenu.add("Pause");
		pause.addActionListener( new ActionListener() {			 
			@Override	
			public void actionPerformed(ActionEvent arg0) {
				solveThread.suspend();
				statusBar.setText("Paused");
			}
		});
		
		JMenuItem resume = fileMenu.add("Resume");
		resume.addActionListener( new ActionListener() {			 
			@Override	
			public void actionPerformed(ActionEvent arg0) {
				solveThread.resume();
				statusBar.setText("Running");
			}
		});
		
		// Add quit option
		JMenuItem quit = fileMenu.add("Quit");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		
		// Add the file menu to the menu bar
		menuBar.add(fileMenu);
		
		// add the menu bar to the main container
		mainPane.add(menuBar, BorderLayout.PAGE_START);
		
		// Create a NxN grid layout and add it to the main container
		gridPanel = new JPanel( new GridLayout(grid.row_size, grid.col_size) );
		mainPane.add(gridPanel, BorderLayout.CENTER);
		
		// Create status bar
		statusBar = new JLabel("Not started yet");
		mainPane.add(statusBar, BorderLayout.PAGE_END);
				
		mainFrame.pack();
		
		mainFrame.setVisible(true);
		
		return grid;
	}
	
	private static void getDetails() {
		
		// prompt to get size
		String s = JOptionPane.showInputDialog(null, "number of rows?", "Board size", 3);
		if( s != null && s.length() > 0 ) row = Integer.parseInt(s);
		
		s = JOptionPane.showInputDialog(null, "number of columns?", "Board size", 3);
		if( s != null && s.length() > 0 ) col = Integer.parseInt(s);
				
		// Set minimum size of the game window
		mainFrame.setMinimumSize(new Dimension(row*60, col*60) );
		
	}
	
	private static void makeNewGrid() {
		
		// Clear the area!!
		gridPanel.removeAll();
		
		gridCells = new JButton[grid.row_size][grid.col_size];
		
		// Set status message
		statusBar.setText( "Grid made" );
		
		for( int i=0; i<grid.row_size; i++ )
			for( int j=0; j<grid.col_size; j++ ) {
				gridCells[i][j] = new JButton("");
				gridCells[i][j].setBackground( Color.white );
				gridCells[i][j].setFont( new Font( "Comic Sans MS" , Font.BOLD, 26 ) );
				gridCells[i][j].setBorder(defaultBorder);
				
				final int ii = i;
				final int jj = j;
				
				gridCells[i][j].addActionListener( new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						String s = JOptionPane.showInputDialog(null, "Enter digit between 0 and 3", "Program input", 3);
						if(s!=null) {
							gridCells[ii][jj].setText(s);
							grid.cells[ii][jj].data = Integer.parseInt(s);
							grid.incomplete.add(grid.cells[ii][jj]);
						}
					}
				});
				
				gridPanel.add(gridCells[i][j]);
			}
		
		mainFrame.pack();
	}
	
	public static void drawGrid() {
		int i, j;
		for(i=0; i<grid.row_size; ++i)
			for(j=0; j<grid.col_size; ++j) {
				gridCells[i][j].setBorder(BorderFactory.createMatteBorder(
						grid.cells[i][j].top.state*3, 
						grid.cells[i][j].left.state*3, 
						grid.cells[i][j].bot.state*3, 
						grid.cells[i][j].right.state*3, 
						Color.GREEN
					));
			}
		
		mainFrame.pack();
	}

	public static void sleep() {
		try {
			Thread.currentThread().sleep(MyGUI.sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
