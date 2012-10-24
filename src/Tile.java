import java.util.ArrayList;
import java.util.Stack;
public class Tile {

	public Edge top;
	public Edge bot;
	public Edge right;
	public Edge left;
	public int data;
	public int row;
	public int col;

	public Tile(int row_val, int col_val, int val){
		this.row = row_val;
		this.col = col_val;
		this.data = val;
	}

	public Tile(int row_val, int col_val){
		this.row = row_val;
		this.col = col_val;
		this.data = -1;
	}
	
	public int numUndecided() {
		int count = 0;
		if(this.top.state==0) count++;
		if(this.bot.state==0) count++;
		if(this.left.state==0) count++;
		if(this.right.state==0) count++;
		return count;
	}
	
	public int numActive() {
		int count = 0;
		if(this.top.state==1) count++;
		if(this.bot.state==1) count++;
		if(this.left.state==1) count++;
		if(this.right.state==1) count++;
		return count;
	}
	
	public void crossOutUndecided(Stack<Point> pointsToCheck,ArrayList<Point> activePoints, ArrayList<Tile> complete,ArrayList<Tile> incomplete) {
		if(this.top.state==0) this.top.crossOut(pointsToCheck,activePoints,complete,incomplete);
		if(this.bot.state==0) this.bot.crossOut(pointsToCheck,activePoints,complete,incomplete);
		if(this.left.state==0) this.left.crossOut(pointsToCheck,activePoints,complete,incomplete);
		if(this.right.state==0) this.right.crossOut(pointsToCheck,activePoints,complete,incomplete);
	}
	
	public void activateUndecided(Stack<Point> pointsToCheck,ArrayList<Point> activePoints,ArrayList<Tile> complete,ArrayList<Tile> incomplete) {
		if(this.top.state==0) this.top.activate(pointsToCheck,activePoints,complete,incomplete);
		if(this.bot.state==0) this.bot.activate(pointsToCheck,activePoints,complete,incomplete);
		if(this.left.state==0) this.left.activate(pointsToCheck,activePoints,complete,incomplete);
		if(this.right.state==0) this.right.activate(pointsToCheck,activePoints,complete,incomplete);
	}
	
	public void checkAndCross(Stack<Point> pointsToCheck,ArrayList<Point> activePoints,ArrayList<Tile> complete,ArrayList<Tile> incomplete) {
		Edge e;
		if(this.top.state==0) e = this.top;
		else if(this.bot.state==0) e = this.bot;
		else if(this.left.state==0) e = this.left;
		else e = this.right;
		
		Point p = null;
		if(!activePoints.contains(e.p1)) p = e.p1;
		else if(!activePoints.contains(e.p2)) p = e.p2;
		
		if(p!=null)
			if(p.numUndecided()==1) e.crossOut(pointsToCheck,activePoints,complete,incomplete);
	}
	
	public ArrayList<Edge> getEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		edges.add(this.left);
		edges.add(this.top);
		edges.add(this.right);
		edges.add(this.bot);
		return edges;
	}
	
	public String toString() {
		return "(" + this.row + "," + this.col + ")";
	}
	
}
