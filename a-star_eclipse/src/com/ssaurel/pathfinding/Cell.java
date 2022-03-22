package com.ssaurel.pathfinding;

public class Cell {
	
	// coordinates
	public int i, j;
    // parent cell for path
	public Cell parent;
	// Heuristic cost of the current cell
    public int heuristicCost;
    // Final cost
    public int finalCost; // G + H with
    // G(n) the cost of the path from the start node to n
    // and H(n) the heuristic that estimates the cost of the cheapest path fromn to the goal
	public boolean solution; // if cell is part of the solution path
   
    public Cell(int i, int j) {
    	this.i = i;
        this.j = j;
    }

    @Override
    public String toString() {
        return "[" + i + "," + j + "]";
    }
}
