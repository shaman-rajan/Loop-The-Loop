import java.util.ArrayList;
import java.util.Stack;
public class Point {

	public int row;
	public int col;
	public Edge top_Edge;
	public Edge bot_Edge;
	public Edge right_Edge;
	public Edge left_Edge;
		
	public Point(int x_val, int y_val){
		this.row = x_val;
		this.col = y_val;
	}
		
	public int numActive() {
		return this.numCount(1);
	}
	
	public int numUndecided() {
		return this.numCount(0);
	}
	
	public int numCrossedOut() {
		return this.numCount(-1);
	}
	
	/*
	 * Counts nummber of edges with "val" as state
	 */
	private int numCount(int val) {
		int count = 0;
		if(this.top_Edge!=null && this.top_Edge.state==val) count++;
		if(this.bot_Edge!=null && this.bot_Edge.state==val) count++;
		if(this.left_Edge!=null && this.left_Edge.state==val) count++;
		if(this.right_Edge!=null && this.right_Edge.state==val) count++;
		return count;
	}
	
	public void checkPoint(Stack<Point> pointsToCheck,ArrayList<Point> activePoints,ArrayList<Tile> complete,ArrayList<Tile> incomplete) {
		if(this.numActive()==2) {
			if(pointsToCheck.contains(this)) pointsToCheck.remove(this);
			if(activePoints.contains(this)) activePoints.remove(this);
			if(this.top_Edge!=null && this.top_Edge.state!=1) this.top_Edge.crossOut(pointsToCheck,activePoints,complete,incomplete);
			if(this.bot_Edge!=null && this.bot_Edge.state!=1) this.bot_Edge.crossOut(pointsToCheck,activePoints,complete,incomplete);
			if(this.left_Edge!=null && this.left_Edge.state!=1) this.left_Edge.crossOut(pointsToCheck,activePoints,complete,incomplete);
			if(this.right_Edge!=null && this.right_Edge.state!=1) this.right_Edge.crossOut(pointsToCheck,activePoints,complete,incomplete);
		} else if(this.numActive()==1) {
			if(!pointsToCheck.contains(this)) pointsToCheck.push(this);
			if(!activePoints.contains(this)) activePoints.add(this);
		} else if(this.numActive()==0 && this.numUndecided()==1) {
			if(this.top_Edge!=null ) this.top_Edge.crossOut(pointsToCheck,activePoints,complete,incomplete);
			if(this.bot_Edge!=null ) this.bot_Edge.crossOut(pointsToCheck,activePoints,complete,incomplete);
			if(this.left_Edge!=null ) this.left_Edge.crossOut(pointsToCheck,activePoints,complete,incomplete);
			if(this.right_Edge!=null ) this.right_Edge.crossOut(pointsToCheck,activePoints,complete,incomplete);
		}
	}
	
	public void activateUndecided(Stack<Point> pointsToCheck,ArrayList<Point> activePoints,ArrayList<Tile> complete,ArrayList<Tile> incomplete) {
		if(this.top_Edge != null && this.top_Edge.state==0) this.top_Edge.activate(pointsToCheck,activePoints,complete,incomplete);
		if(this.bot_Edge != null && this.bot_Edge.state==0) this.bot_Edge.activate(pointsToCheck,activePoints,complete,incomplete);
		if(this.left_Edge != null && this.left_Edge.state==0) this.left_Edge.activate(pointsToCheck,activePoints,complete,incomplete);
		if(this.right_Edge != null && this.right_Edge.state==0) this.right_Edge.activate(pointsToCheck,activePoints,complete,incomplete);
	}
	
	public ArrayList<Edge> getUndecidedList()
	{
		ArrayList<Edge> undecidedList = new ArrayList<Edge>();
		if(this.top_Edge != null && this.top_Edge.state==0) undecidedList.add(this.top_Edge);
		if(this.bot_Edge != null && this.bot_Edge.state==0) undecidedList.add(this.bot_Edge);
		if(this.right_Edge != null && this.right_Edge.state==0) undecidedList.add(this.right_Edge);
		if(this.left_Edge != null && this.left_Edge.state==0) undecidedList.add(this.left_Edge);
		return undecidedList;
	}
	
	public String toString() {
		return "(Row: " + this.row + " Col: " + this.col + ")";
	}

	public ArrayList<Edge> getActivated() {
		ArrayList<Edge> active = new ArrayList<Edge>();
		if(this.top_Edge != null && this.top_Edge.state==1) active.add(this.top_Edge);
		if(this.bot_Edge != null && this.bot_Edge.state==1) active.add(this.bot_Edge);
		if(this.right_Edge != null && this.right_Edge.state==1) active.add(this.right_Edge);
		if(this.left_Edge != null && this.left_Edge.state==1) active.add(this.left_Edge);
		return active;
	}

<<<<<<< HEAD
	@Override
	public int compareTo(Point p) {
		if(this.numUndecided() > p.numUndecided()) return 1;
		if(this.numUndecided() < p.numUndecided()) return -1;
		return 0;
	}

=======
>>>>>>> parent of 7171294... Added heuristic that sorts activepoints according to undecided edges before choosing an edge in backtrack
}
