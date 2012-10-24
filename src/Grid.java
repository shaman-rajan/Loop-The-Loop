import java.util.ArrayList;

import java.util.Stack;

public class Grid {

	// r X c no. of tiles

	public int row_size = 0;
	public int col_size = 0;
	public Tile[][] cells;
	public Point[][] dots;
	public Stack<Point> pointsToCheck;
	public ArrayList<Point> activePoints; 		// "Open" list
	public ArrayList<Tile> complete; 			// "Closed" list
	public ArrayList<Tile> incomplete;

	public Grid(int rows, int cols) {
		this.row_size = rows;
		this.col_size = cols;
		this.cells = new Tile[rows][cols];
		this.dots = new Point[rows + 1][cols + 1];
		this.pointsToCheck = new Stack<Point>();
		this.activePoints = new ArrayList<Point>(); 		// "Open" list
		this.complete = new ArrayList<Tile>(); 				// "Closed" list
		this.incomplete = new ArrayList<Tile>();
	}

	public void solve() {
		// Cross out edges of all tiles having 0 as data
		this.check0();

		// if 3 cross to 3
		this.check3diagnol();

		// 3 beside 3: adjacent edge and edges opposite to that should
		// definitely be active
		this.checkAdjacent3s();

		// 3 cross to 0: both edges from common point active
		this.check3Diagonal0();

		// 3 side to 0: all edges of 3 except common edge active
		this.check3Adjacent0();

		// 3 in corner: corner edges activated
		this.check3or1corner();

		// Start stack search
		this.stackSearch();
		//After this stack is empty so refill it with elements from the list
		
		MyGUI.solveThread.suspend();
		
		for(Point p : this.activePoints)
			this.pointsToCheck.add(p);
		
		if(!activePoints.isEmpty()) this.backTrack(this);
		else this.addAndBacktrack();
		
		System.out.println(this.complete);
		System.out.println(incomplete);
		return;
	}

	private void check0() {
		for (int i = 0; i < row_size; i++)
			for (int j = 0; j < col_size; j++)
				if (this.cells[i][j].data == 0) {
					this.cells[i][j].top.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i][j].bot.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i][j].right.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i][j].left.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.complete.add(this.cells[i][j]);
					incomplete.remove(this.cells[i][j]);

				}
	}

	private void check3diagnol() {
		for (int i = 0; i < row_size - 1; ++i)
			for (int j = 0; j < col_size - 1; ++j)
				if (this.cells[i][j].data == 3 && this.cells[i + 1][j + 1].data == 3) {
					this.cells[i][j].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i][j].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i+1][j+1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i+1][j+1].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				}

		for (int i = 1; i < row_size; ++i)
			for (int j = 0; j < col_size - 1; ++j)
				if (this.cells[i][j].data == 3 && this.cells[i - 1][j + 1].data == 3) {
					this.cells[i][j].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i][j].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i-1][j+1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i-1][j+1].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				}
	}

	private void checkAdjacent3s() {
		int i, j;

		for (i = 0; i < row_size - 1; ++i)
			for (j = 0; j < col_size - 1; ++j)
				if (this.cells[i][j].data == 3) {
					if (this.cells[i + 1][j].data == 3) {
						this.cells[i][j].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i + 1][j].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					}
					if (this.cells[i][j + 1].data == 3) {
						this.cells[i][j].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j + 1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					}
				}

		for (i = 0; i < col_size - 1; ++i)
			if (this.cells[row_size - 1][i].data == 3 && this.cells[row_size - 1][i + 1].data == 3) {
				this.cells[row_size - 1][i].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[row_size - 1][i].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[row_size - 1][i + 1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			}

		for (i = 0; i < row_size - 1; ++i)
			if (this.cells[i][col_size - 1].data == 3 && this.cells[i + 1][col_size - 1].data == 3) {
				this.cells[i][col_size - 1].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[i][col_size - 1].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[i + 1][col_size - 1].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			}

	}

	private void check3Adjacent0() {
		int i, j;

		for (i = 0; i < row_size - 1; ++i)
			for (j = 0; j < col_size - 1; ++j) {
				if (this.cells[i][j].data == 3) {
					if (this.cells[i + 1][j].data == 0) {
						this.cells[i][j].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					}
					if (this.cells[i][j + 1].data == 0) {
						this.cells[i][j].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					}
				}

				else if (this.cells[i][j].data == 0) {
					if (this.cells[i + 1][j].data == 3) {
						this.cells[i + 1][j].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i + 1][j].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i + 1][j].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					}
					if (this.cells[i][j + 1].data == 3) {
						this.cells[i][j + 1].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j + 1].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
						this.cells[i][j + 1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					}
				}
			}

		for (i = 0; i < col_size - 1; ++i) {
			if (this.cells[row_size - 1][i].data == 0
					&& this.cells[row_size - 1][i + 1].data == 3) {
				this.cells[row_size - 1][i + 1].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[row_size - 1][i + 1].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[row_size - 1][i + 1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			}

			else if (this.cells[row_size - 1][i].data == 3
					&& this.cells[row_size - 1][i + 1].data == 0) {
				this.cells[row_size - 1][i].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[row_size - 1][i].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[row_size - 1][i].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			}
		}

		for (i = 0; i < row_size - 1; ++i) {
			if (this.cells[i][col_size - 1].data == 3
					&& this.cells[i + 1][col_size - 1].data == 0) {
				this.cells[i][col_size - 1].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[i][col_size - 1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[i][col_size - 1].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			}

			else if (this.cells[i][col_size - 1].data == 0
					&& this.cells[i + 1][col_size - 1].data == 3) {
				this.cells[i + 1][col_size - 1].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[i + 1][col_size - 1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				this.cells[i + 1][col_size - 1].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			}

		}
	}

	private void check3Diagonal0() {
		for (int i = 0; i < row_size - 1; ++i)
			for (int j = 0; j < col_size - 1; ++j) {
				if (this.cells[i][j].data == 3
						&& this.cells[i + 1][j + 1].data == 0) {
					this.cells[i][j].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i][j].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				} else if (this.cells[i][j].data == 0
						&& this.cells[i + 1][j + 1].data == 3) {
					this.cells[i + 1][j + 1].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i + 1][j + 1].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				}
			}

		for (int i = 1; i < row_size; ++i)
			for (int j = 0; j < col_size - 1; ++j) {
				if (this.cells[i][j].data == 3
						&& this.cells[i - 1][j + 1].data == 0) {
					this.cells[i][j].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i][j].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				} else if (this.cells[i][j].data == 0
						&& this.cells[i - 1][j + 1].data == 3) {
					this.cells[i - 1][j + 1].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
					this.cells[i - 1][j + 1].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				}
			}
	}

	private void check3or1corner() {
		if (cells[0][0].data == 3) {
			cells[0][0].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			cells[0][0].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		} else if (cells[0][0].data == 1) {
			cells[0][0].left.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			cells[0][0].top.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		}

		if (cells[0][col_size - 1].data == 3) {
			cells[0][col_size - 1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			cells[0][col_size - 1].top.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		} else if (cells[0][0].data == 1) {
			cells[0][0].left.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			cells[0][0].top.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		}

		if (cells[row_size - 1][0].data == 3) {
			cells[row_size - 1][0].left.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			cells[row_size - 1][0].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		} else if (cells[0][0].data == 1) {
			cells[0][0].left.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			cells[0][0].top.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		}

		if (cells[row_size - 1][col_size - 1].data == 3) {
			cells[row_size - 1][col_size - 1].right.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			cells[row_size - 1][col_size - 1].bot.activate(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		} else if (cells[0][0].data == 1) {
			cells[0][0].left.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
			cells[0][0].top.crossOut(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		}
	}

	/*
	 * Add from list to stack pop from top of stack - if edge from this is
	 * activated if stack is empty add all contents of list to stack again
	 */
	private void stackSearch() {
		int changed = 0;
		int i = 0;
		while (!this.pointsToCheck.isEmpty()) {
			Point p = this.pointsToCheck.pop();
			if (p.numUndecided() == 1)
				p.activateUndecided(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
		}
		
		this.GoalTest();

		while (i < this.incomplete.size()) {
			Tile t = this.incomplete.get(i);

			if (t.data - t.numActive() == t.numUndecided()) {
				t.activateUndecided(this.pointsToCheck,this.activePoints,this.complete,this.incomplete);
				changed = 1;
			}

			i++;
		}

		if (changed != 0)
			stackSearch();

		return;
	}
	
	
	public void backTrack(Grid gridNode)
	{
		if(!checkValidity(gridNode))
			return;
		gridNode.GoalTest();
		Grid gridChild = new Grid(gridNode.row_size, gridNode.col_size);
		
		gridChild.CopyAll(gridNode);
		MyGUI.grid = gridChild;

		Point p = gridChild.pointsToCheck.pop();
		ArrayList<Edge> undecidedList=p.getUndecidedList();
		
		while( !undecidedList.isEmpty() ) {
			
			Edge e = undecidedList.get(0);
			
			try {
				e.activate(gridChild.pointsToCheck, gridChild.activePoints, gridChild.complete, gridChild.incomplete);
				backTrack(gridChild);
			} catch (NullPointerException e2) {
				MyGUI.grid = gridNode;
				MyGUI.drawGrid();
				MyGUI.sleep();
			}
			
			if(e.p1.row == gridNode.row_size)
				gridNode.cells[e.p1.row-1][e.p1.col].bot.crossOut(gridNode.pointsToCheck, gridNode.activePoints, gridNode.complete, gridNode.incomplete);
			else if(e.p1.col == gridNode.col_size)
				gridNode.cells[e.p1.row][e.p1.col-1].right.crossOut(gridNode.pointsToCheck, gridNode.activePoints, gridNode.complete, gridNode.incomplete);
			else {
				if(e.p1.row == e.p2.row)
					gridNode.cells[e.p1.row][e.p1.col].top.crossOut(gridNode.pointsToCheck, gridNode.activePoints, gridNode.complete, gridNode.incomplete);
				else if(e.p1.col == e.p2.col)
					gridNode.cells[e.p1.row][e.p1.col].left.crossOut(gridNode.pointsToCheck, gridNode.activePoints, gridNode.complete, gridNode.incomplete);
				else 
					System.out.println("WTF!!");
			}
			gridNode.stackSearch();
			
			gridChild = new Grid(gridNode.row_size, gridNode.col_size);
			gridChild.CopyAll(gridNode);
			
			gridChild.pointsToCheck.addAll(gridChild.activePoints);
			int flag = 0;
			while(!gridChild.pointsToCheck.isEmpty()) {
				if(!gridChild.pointsToCheck.peek().getUndecidedList().isEmpty()) {
					undecidedList = gridChild.pointsToCheck.peek().getUndecidedList();
					flag= 1;
					break;
				} else gridChild.pointsToCheck.pop();
			}
			if(flag==0) break;
		}
		
		return;
	}
	
	//to check if the node is a valid node or not
	public boolean checkValidity(Grid gridNode)
	{
		if(gridNode.activePoints.isEmpty() && !(gridNode.incomplete.isEmpty()) )
			return false;
		else
			for(Point p : gridNode.activePoints ) {
				if(p.numUndecided()==0)
					return false;	
			}
				
		return true;
	}
	
	public void GoalTest(){
		//print something here
		if(this.activePoints.isEmpty() && this.incomplete.isEmpty())
		{
			System.out.println("Found sol");
			this.finish();
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	//copies data of parent grid into this object
	public void CopyAll(Grid parentGrid){
		
		int rownum = parentGrid.row_size, colnum = parentGrid.col_size;
		
		int i, j;
		
		for(i=0; i<rownum+1; ++i)
			for(j=0; j<colnum+1; ++j) {
				this.dots[i][j] = new Point(i, j);
				if(parentGrid.activePoints.contains(parentGrid.dots[i][j]))
					this.activePoints.add(this.dots[i][j]);
				if(parentGrid.pointsToCheck.contains(parentGrid.dots[i][j]))
					this.pointsToCheck.push(this.dots[i][j]);
			}
		
		for(i=0; i<parentGrid.row_size; ++i)
			for(j=0; j<parentGrid.col_size; ++j) {
				this.cells[i][j] = new Tile(i, j, parentGrid.cells[i][j].data);
				if(parentGrid.complete.contains(parentGrid.cells[i][j]))
					this.complete.add(this.cells[i][j]);
				if(parentGrid.incomplete.contains(parentGrid.cells[i][j]))
					this.incomplete.add(this.cells[i][j]);
			}
		
		for(i=0; i<rownum; ++i)
			for(j=0; j<colnum; ++j) {
				this.cells[i][j].left = new Edge(this.dots[i][j],this.dots[i+1][j], this.cells);
				this.cells[i][j].left.state = parentGrid.cells[i][j].left.state;
				this.cells[i][j].top = new Edge(this.dots[i][j],this.dots[i][j+1], this.cells);
				this.cells[i][j].top.state = parentGrid.cells[i][j].top.state;
			}
		
		for(i=0; i<rownum; ++i)
			for(j=0; j<colnum-1; ++j)
				this.cells[i][j].right = this.cells[i][j+1].left;
	
		for(i=0; i<rownum-1; ++i)
			for(j=0; j<colnum; ++j)
				this.cells[i][j].bot = this.cells[i+1][j].top; 
		
		for(i=0; i<rownum; ++i) { 
			this.cells[i][colnum-1].right = new Edge(this.dots[i][colnum], this.dots[i+1][colnum], this.cells);
			this.cells[i][colnum-1].right.state = parentGrid.cells[i][colnum-1].right.state;
		}
		
		for(i=0; i<colnum; ++i) {
			this.cells[rownum-1][i].bot = new Edge(this.dots[rownum][i], this.dots[rownum][i+1], this.cells);
			this.cells[rownum-1][i].bot.state = parentGrid.cells[rownum-1][i].bot.state;
		}
		
	}
	
	private void addAndBacktrack() {
		int max = 0, flag = 0, count = 0;
		int row = 0, col = 0;
		
		for(int i=0; i<this.row_size && flag == 0; ++i)
			for(int j=0; j<this.col_size && flag == 0; ++j)
				if(this.cells[i][j].data > max) {
					max = this.cells[i][j].data;
					row = i;
					col = j;
					if(max == 3)
						flag = 1;
				}
		if(max==3)
			while(count<4) {
				Grid backup = new Grid(this.row_size, this.col_size);
				backup.CopyAll(this);
				MyGUI.grid = backup;
				
				Tile x = backup.cells[row][col];
				Edge e = x.getEdges().get(count);
				e.crossOut(backup.pointsToCheck, backup.activePoints, backup.complete, backup.incomplete);

				try {
					backup.stackSearch();
					backup.backTrack(backup);
				} catch (NullPointerException e2) {
					MyGUI.grid = this;
					MyGUI.drawGrid();
					MyGUI.sleep();
				}

				count++;
			}
		
		else if(max == 1 || max == 2)
			while(count<4) {
				Grid backup = new Grid(this.row_size, this.col_size);
				backup.CopyAll(this);
				MyGUI.grid = backup;

				Tile x = backup.cells[row][col];
				Edge e = x.getEdges().get(count);
				e.activate(backup.pointsToCheck, backup.activePoints, backup.complete, backup.incomplete);

				try {
					backup.stackSearch();
					backup.backTrack(backup);
				} catch (NullPointerException e2) {
					MyGUI.grid = this;
					MyGUI.drawGrid();
					MyGUI.sleep();
				}
				
				count++;
			}
			
	}
	
	public void finish() {
		int i;
		
		for (i = 0; i < row_size; i++)
			for (int j = 0; j < col_size; j++) {
				if(this.cells[i][j].left.state==1) System.out.println(this.cells[i][j].left);
				if(this.cells[i][j].top.state==1) System.out.println(this.cells[i][j].top);
			}
		
		for(i=0; i<row_size; ++i)
			if(this.cells[i][col_size-1].right.state==1) System.out.println(this.cells[i][col_size-1].right);
		
		for(i=0; i<col_size; ++i)
			if(this.cells[row_size-1][i].bot.state==1) System.out.println(this.cells[row_size-1][i].bot);
		
		MyGUI.drawGrid();
	}

}
