
public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i, j;
		int rows = 5, cols = 5;
		
		/*
		 * Making the grid
		 */
		Grid solveGrid = MyGUI.createAndShowGUI();
		rows = solveGrid.row_size;
		cols = solveGrid.col_size;

		/*
		 * Initializing the points and tiles
		 */
		for(i=0; i<rows+1; ++i)
			for(j=0; j<cols+1; ++j)
				solveGrid.dots[i][j] = new Point(i, j);				
		
		for(i=0; i<rows; ++i)
			for(j=0; j<cols; ++j)
				solveGrid.cells[i][j] = new Tile(i, j);

		/*
		 * Making edges here? Venkat is an idiot.
		 */
		
		// Basic grid making (left and top edges for each block)
		for(i=0; i<rows; ++i)
			for(j=0; j<cols; ++j) {
				solveGrid.cells[i][j].left = new Edge(solveGrid.dots[i][j],solveGrid.dots[i+1][j], solveGrid.cells);
				solveGrid.cells[i][j].top = new Edge(solveGrid.dots[i][j],solveGrid.dots[i][j+1], solveGrid.cells);
			}
		
		// Right edge for each is left edge of next block
		for(i=0; i<rows; ++i)
			for(j=0; j<cols-1; ++j)
				solveGrid.cells[i][j].right = solveGrid.cells[i][j+1].left;
	
		// Bottom edge for each is top edge of block below
		for(i=0; i<rows-1; ++i)
			for(j=0; j<cols; ++j)
				solveGrid.cells[i][j].bot = solveGrid.cells[i+1][j].top; 
		
		// Rightmost column
		for(i=0; i<rows; ++i) 
			solveGrid.cells[i][cols-1].right = new Edge(solveGrid.dots[i][cols], solveGrid.dots[i+1][cols], solveGrid.cells);
		
		// Bottom row
		for(i=0; i<cols; ++i) 
			solveGrid.cells[rows-1][i].bot = new Edge(solveGrid.dots[rows][i], solveGrid.dots[rows][i+1], solveGrid.cells);
		
	}
	
	public static void error(String s) {
		System.out.println(s);
		System.exit(1);
	}

}
