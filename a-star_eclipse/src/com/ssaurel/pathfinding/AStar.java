package com.ssaurel.pathfinding;

import java.util.PriorityQueue;
import java.util.Scanner;

public class AStar {

	// costs for diagonal and vertical / horizontal moves
	public static final int DIAGONAL_COST = 14;
	public static final int V_H_COST = 10;
	// Cells of our grid
	private Cell[][] grid;
	// We define a priority queue for open cells
	// Open Cells : the set of nodes to be evaluated
	// we put cells with lowest cost in first
	private PriorityQueue<Cell> openCells;
	// Closed Cells : the set of nodes already evaluated
	private boolean[][] closedCells;
	// Start cell
	private int startI, startJ;
	// End cell
	private int endI, endJ;

	public AStar(int width, int height, int si, int sj, int ei, int ej, int[][] blocks) {
		grid = new Cell[width][height];
		closedCells = new boolean[width][height];
		openCells = new PriorityQueue<Cell>((Cell c1, Cell c2) -> {
			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});

		startCell(si, sj);
		endCell(ei, ej);

		// init heuristic and cells
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
				grid[i][j].solution = false;
			}
		}

		grid[startI][startJ].finalCost = 0;

		// we put the blocks on the grid
		for (int i = 0; i < blocks.length; i++) {
			addBlockOnCell(blocks[i][0], blocks[i][1]);
		}
	}

	public void addBlockOnCell(int i, int j) {
		grid[i][j] = null;
	}

	public void startCell(int i, int j) {
		startI = i;
		startJ = j;
	}

	public void endCell(int i, int j) {
		endI = i;
		endJ = j;
	}

	public void updateCostIfNeeded(Cell current, Cell t, int cost) {
		if (t == null || closedCells[t.i][t.j])
			return;

		int tFinalCost = t.heuristicCost + cost;
		boolean isOpen = openCells.contains(t);

		if (!isOpen || tFinalCost < t.finalCost) {
			t.finalCost = tFinalCost;
			t.parent = current;

			if (!isOpen)
				openCells.add(t);
		}
		
	}

	public void process() {
		// we add the start location to open list
		openCells.add(grid[startI][startJ]);
		Cell current;

		while (true) {
			current = openCells.poll();

			if (current == null)
				break;

			closedCells[current.i][current.j] = true;

			if (current.equals(grid[endI][endJ]))
				return;

			Cell t;

			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);

				if (current.j - 1 >= 0) {
					t = grid[current.i - 1][current.j - 1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				}

				if (current.j + 1 < grid[0].length) {
					t = grid[current.i - 1][current.j + 1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				}
			}

			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}

			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
			}

			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				updateCostIfNeeded(current, t, current.finalCost + V_H_COST);

				if (current.j - 1 >= 0) {
					t = grid[current.i + 1][current.j - 1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				}

				if (current.j + 1 < grid[0].length) {
					t = grid[current.i + 1][current.j + 1];
					updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
				}
			}
		}
	}

	public void display() {
		System.out.println("Grid :");
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (i == startI && j == startJ)
					System.out.print("SO  "); // Source cell
		        else if (i == endI && j == endJ)
		            System.out.print("DE  "); // Destination cell
		        else if (grid[i][j] != null)
		        	System.out.printf("%-3d ", 0);
		        else
		            System.out.print("BL  "); // Block Cell
		    }
			
			System.out.println();
		}
		   
		System.out.println();
	}

	public void displayScores() {
		System.out.println("\nScores for cells :");
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
		          if (grid[i][j] != null)
		        	  System.out.printf("%-3d ", grid[i][j].finalCost);
		          else
		        	  System.out.print("BL  ");
			}
			
			System.out.println();
		}
		
		System.out.println();
		
	}
	
	public void displaySolution() {
		if (closedCells[endI][endJ]) {
			//we track back the path
			System.out.print("Path: ");
			Cell current = grid[endI][endJ];
			System.out.println(current);
			grid[current.i][current.j].solution = true;
			
			while(current.parent != null) {
				System.out.print(" -> " + current.parent);
				grid[current.parent.i][current.parent.j].solution = true;
				current = current.parent;
			}
			
			System.out.println("\n");
			
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					if (i == startI && j == startJ)
						System.out.print("SO  "); // Source cell
			        else if (i == endI && j == endJ)
			            System.out.print("DE  "); // Destination cell
			        else if (grid[i][j] != null)
			        	System.out.printf("%-3s ", grid[i][j].solution ? "X" : "0");
			        else
			            System.out.print("BL  "); // Block Cell
			    }
				
				System.out.println();
			}
			   
			System.out.println();
		} else
			System.out.println("No possible path");
	}
	 
	
	public static void main(String[] args) {
		//67,93,0,0,66,92,
		Scanner scan = new Scanner(System.in);
		System.out.println("Choose Office Number: \n205, 206, 207, 208, 209\n");
		int DE_x = 0;
		int DE_y = 0;
		
		String destination = scan.nextLine();
		if (destination.equals("205")) {
			DE_x = 22;
			DE_y = 8;
		}
		else if (destination.equals("206") || destination.equals("207")) {
			DE_x = 59;
			DE_y = 8;
		}
		else if (destination.equals("208")) {
			DE_x = 59;
			DE_y = 46;
		}
		else if (destination.equals("209")) {
			DE_x = 59;
			DE_y = 83;
		}
		else {
			System.out.print("Not a destination. Choose another.");
			System.exit(0);
		}
			
		
		AStar aStar = new AStar(67,93,0,8,DE_x,DE_y, Blocks.block);
		//aStar.display();
		aStar.process(); //apply A* algorithm
		//aStar.displayScores(); //display scores on grid
		aStar.displaySolution(); // display solution path
	}
		
}
