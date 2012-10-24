import java.util.ArrayList;
import java.util.Stack;
public class Edge {

	public Point p1;
	public Point p2;
	public Tile t1;
	public Tile t2;
	
	//	-1 	->  surely no edge
	// 	0 	->	not decided
	//	1	->	edge present
	public int state;
	
	public Edge(Point p1_val,Point p2_val, Tile[][] tiles)
	{
		this.p1=p1_val;
		this.p2=p2_val;
		this.assignEdgesAndTiles(tiles);
	}
	public void assignEdgesAndTiles(Tile[][] tiles)
	{
		int row_diff = this.p1.row - this.p2.row;
		int col_diff = this.p1.col - this.p2.col;
		
		if(col_diff == 0){
			switch(row_diff){
			case 1:
				this.p2.bot_Edge=this;
				this.p1.top_Edge=this;
				if(this.p2.col!=0) this.t1 = tiles[this.p2.row][this.p2.col-1];
				if(this.p2.col!=tiles[0].length) this.t2 = tiles[this.p2.row][this.p2.col];
				break;
			
			case -1:
				this.p2.top_Edge=this;
				this.p1.bot_Edge=this;
				if(this.p1.col!=0) this.t1 = tiles[this.p1.row][this.p1.col-1];
				if(this.p1.col!=tiles[0].length) this.t2 = tiles[this.p1.row][this.p1.col];
				break;

			default:
				System.out.println("Improper assignment");
				System.exit(0);
			}
			
		}
		
		if(row_diff == 0){
			switch(col_diff){
			case 1:
				this.p1.left_Edge=this;
				this.p2.right_Edge=this;
				if(this.p2.row!=0) this.t1 = tiles[this.p2.row-1][this.p2.col];
				if(this.p2.row!=tiles.length) this.t2 = tiles[this.p2.row][this.p2.col];
				break;
			
			case -1:
				this.p1.right_Edge=this;
				this.p2.left_Edge=this;
				if(this.p1.row!=0) this.t1 = tiles[this.p1.row-1][this.p1.col];
				if(this.p1.row!=tiles.length) this.t2 = tiles[this.p1.row][this.p1.col];
				break;

			default:
				System.out.println("Improper assignment");
				System.exit(0);
			}
		}
	}
	
	/*
	 * 	1. check both points 
	 *		if 2 edges coming out of point, then cross out rest and remove point from list
	 *		else add the point to list and stack
	 *	2. check in tiles adjacent to activated edge:
	 *		1) if data -1 then if 3 == active+crossed out surrounding edges then cross out 4th edge
	 *		2) else if data == number of active edges then cross out others and add tile to complete
	 *		3) else if data-active == undecided, activate undecided edges
	 */
	public void activate(Stack<Point> pointsToCheck,ArrayList<Point> activePoints,ArrayList<Tile> complete,ArrayList<Tile> incomplete) {
		
		if (this.state == 0)
			this.state = 1;
		else if (this.state == 1)
			return;
		else
			Start.error("Activating wrong edge " + this);
		
		MyGUI.drawGrid();
		MyGUI.sleep();
		
		// do stuff to scalars and then check if loop is already formed 
		if(this.p1.numActive()==2 && activePoints.contains(this.p1)) activePoints.remove(this.p1);
		if(this.p2.numActive()==2 && activePoints.contains(this.p2)) activePoints.remove(this.p2);
		if(this.t1 != null && this.t1.data == this.t1.numActive()) {
			complete.add(this.t1);
			incomplete.remove(this.t1);
		}
		if(this.t2 != null && this.t2.data == this.t2.numActive()) {
			complete.add(this.t2);
			incomplete.remove(this.t2);
		}
		
		if(this.p1.numActive()==2 && this.p2.numActive()==2) 
			this.checkLoop(incomplete, activePoints);
		
		this.p1.checkPoint(pointsToCheck,activePoints,complete,incomplete);
		this.p2.checkPoint(pointsToCheck,activePoints,complete,incomplete);
		
		if( this.t1 != null && this.t1.data == -1 && this.t1.numUndecided() == 1 ) this.t1.checkAndCross(pointsToCheck,activePoints,complete,incomplete);
		if( this.t2 != null && this.t2.data == -1 && this.t2.numUndecided() == 1 ) this.t2.checkAndCross(pointsToCheck,activePoints,complete,incomplete);
		
		if( this.t1 != null && this.t1.data == this.t1.numActive() )
			this.t1.crossOutUndecided(pointsToCheck,activePoints,complete,incomplete);
		if( this.t2 != null && this.t2.data == this.t2.numActive() )
			this.t2.crossOutUndecided(pointsToCheck,activePoints,complete,incomplete);
		
		if( this.t1 != null && this.t1.data - this.t1.numActive() == this.t1.numUndecided() ) this.t1.activateUndecided(pointsToCheck,activePoints,complete,incomplete);
		if( this.t2 != null && this.t2.data - this.t2.numActive() == this.t2.numUndecided() ) this.t2.activateUndecided(pointsToCheck,activePoints,complete,incomplete);
		
	}


	/*
	 * while crossing out edges, check adjacent tiles for completion
	 */
	public void crossOut(Stack<Point> pointsToCheck,ArrayList<Point> activePoints,ArrayList<Tile> complete,ArrayList<Tile> incomplete) {
		
		if (this.state == 0)
			this.state = -1;
		else if (this.state == -1)
			return;
		else
			Start.error("Activating wrong edge " + this);
			
		this.p1.checkPoint(pointsToCheck,activePoints,complete,incomplete);
		this.p2.checkPoint(pointsToCheck,activePoints,complete,incomplete);
		
		if( this.t1 != null && this.t1.data == -1 && this.t1.numUndecided() == 1 ) this.t1.checkAndCross(pointsToCheck,activePoints,complete,incomplete);
		if( this.t2 != null && this.t2.data == -1 && this.t2.numUndecided() == 1 ) this.t2.checkAndCross(pointsToCheck,activePoints,complete,incomplete);
		
		if( this.t1 != null && this.t1.data == this.t1.numActive() ) this.t1.crossOutUndecided(pointsToCheck,activePoints,complete,incomplete);
		if( this.t2 != null && this.t2.data == this.t2.numActive() ) this.t2.crossOutUndecided(pointsToCheck,activePoints,complete,incomplete);
		
		if( this.t1 != null && this.t1.data - this.t1.numActive() == this.t1.numUndecided() ) this.t1.activateUndecided(pointsToCheck,activePoints,complete,incomplete);
		if( this.t2 != null && this.t2.data - this.t2.numActive() == this.t2.numUndecided() ) this.t2.activateUndecided(pointsToCheck,activePoints,complete,incomplete);
		
	}
	
	private void checkLoop(ArrayList<Tile> incomplete, ArrayList<Point> activePoints) {
		if(incomplete.isEmpty() && activePoints.isEmpty()) return;
		Point nextPoint = this.p2;
		Edge currentEdge = this;
		
		while(true) {
			if(nextPoint.numActive()<2) return;
			else if(nextPoint==this.p1) throw new NullPointerException();
			ArrayList<Edge> temp = nextPoint.getActivated();
			currentEdge = currentEdge == temp.get(0) ? temp.get(1) : temp.get(0);
			nextPoint =  nextPoint == currentEdge.p1 ? currentEdge.p2 : currentEdge.p1;
		}
	}
	
	public String toString() {
		String s = this.p1.toString() + " to " + this.p2.toString() + " " + this.state;
		return s;
	}
	
}
