package gui;

import java.awt.Dimension;
import java.util.concurrent.ConcurrentLinkedQueue;

import data.Cell;
import data.CellGrid;

public class GameOfLifeGrid implements CellGrid {
	
	private int cellRows;
	private int cellCols;
	private int generations;
	
	// Contains the current, living cell
	private ConcurrentLinkedQueue<Cell> current = new ConcurrentLinkedQueue<Cell>();
	// Contains the neighbor cells of the living cells
	private ConcurrentLinkedQueue<Cell> next = new ConcurrentLinkedQueue<Cell>();
	
	private Cell[][] grid;
	
	public GameOfLifeGrid(int cellCols, int cellRows) {
		this.cellCols = cellCols;
		this.cellRows = cellRows;
		this.grid = new Cell[cellCols][cellRows];
		for (int c = 0 ; c < cellCols ; c++)
			for (int r = 0 ; r < cellRows ; r++)
				grid[c][r] = new Cell(c, r);
	}
	
	/**
	 * Clears grid.
	 */
	public synchronized void clear() {
		generations = 0;
		current.clear();
		next.clear();
	}
	
	/**
	 * Create next generation.
	 */
	public synchronized void next() {
		generations++;
		next.clear();
		
		for (Cell c : current)
			c.neighbors = 0;
		
		for (Cell c : current) {
			addNeighbor(c.col - 1, c.row - 1);
			addNeighbor(c.col, c.row - 1);
			addNeighbor(c.col + 1, c.row - 1);
			addNeighbor(c.col - 1, c.row);
			addNeighbor(c.col + 1, c.row);
			addNeighbor(c.col - 1, c.row + 1);
			addNeighbor(c.col, c.row + 1);
			addNeighbor(c.col + 1, c.row + 1);
		}
		
		for (Cell c : current) {
			// Here is the Game Of Life rule (1):
			if (c.neighbors != 3 && c.neighbors != 2)
				current.remove(c);
		}
		
		for (Cell c : next) {
			// Here is the Game Of Life rule (2):
			if (c.neighbors == 3)
				setCell(c.col, c.row, true);
		}
		
	}
	
	/**
	 * Adds a new neighbor to a cell.
	 * 
	 * @param col Cell-column
	 * @param row Cell-row
	 */
	public synchronized void addNeighbor(int col, int row) {
		try {
			Cell c = grid[col][row];
			if (!next.contains(c)) {
				c.neighbors = 1;
				next.add(c);
			}
			else {
				c.neighbors++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
	}

	/**
	 * Get value of cell.
	 * @param col x-coordinate of cell
	 * @param row y-coordinate of cell
	 * @return value of cell
	 */
	@Override
	public boolean getCell(int col, int row) {
		return current.contains(grid[col][row]);
	}

	/**
	 * Set value of cell.
	 * @param col x-coordinate of cell
	 * @param row y-coordinate of cell
	 * @param c value of cell
	 */
	@Override
	public void setCell(int col, int row, boolean cell) {
		Cell c = grid[col][row];
		if (cell) {
			if (!current.contains(c)) {
				current.add(c);
			}
		}
		else {
			current.remove(c);
		}
	}

	/**
	 * Get dimension of grid.
	 * @return dimension of grid
	 */
	@Override
	public Dimension getDimension() {
		return new Dimension(cellCols, cellRows);
	}

	/**
	 * Resize grid. Reuse existing cells.
	 */
	@Override
	public void resize(int cellColsNew, int cellRowsNew) {
		if (cellCols == cellColsNew && cellRows == cellRowsNew)
			return;
		
		// Create a new grid, reusing existing Cell's
		Cell[][] gridNew = new Cell[cellColsNew][cellRowsNew];
		for (int c = 0 ; c < cellColsNew ; c++) {
			for (int r = 0 ; r < cellRowsNew ; r++) {
				if (c < cellCols && r < cellRows)
					gridNew[c][r] = grid[c][r];
				else
					gridNew[c][r] = new Cell(c, r);
			}
		}
		
		// Copy existing shape to center of new shape
		int colOffset = (cellColsNew - cellCols) / 2;
		int rowOffset = (cellRowsNew - cellRows) / 2;
		next.clear();
		for (Cell c : current) {
			int colNew = c.col + colOffset;
			int rowNew = c.row + rowOffset;
			try {
				next.add(gridNew[colNew][rowNew]);
			} catch (ArrayIndexOutOfBoundsException e) {
				// ignore
			}
		}
		
		grid = gridNew;
		current.clear();
		for (Cell c : next) {
			current.add(c);
		}
		cellCols = cellColsNew;
		cellRows = cellRowsNew;
	}

	/**
	 * Get list of Cell's
	 */
	@Override
	public ConcurrentLinkedQueue<Cell> getEnum() {
		return current;
	}
	
	/**
	 * Get number of generations.
	 * @return number of generations
	 */
	public int getGenerations() {
		return generations;
	}

}
