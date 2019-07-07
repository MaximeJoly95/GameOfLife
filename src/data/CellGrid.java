package data;

import java.awt.Dimension;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface CellGrid {
	
	/**
	 * Get status of cell (alive or dead).
	 * @param col x-position
	 * @param row y-position
	 * @return living or not
	 */
	public boolean getCell(int col, int row);

	/**
	 * Set status of cell (alive or dead).
	 * @param col x-position
	 * @param row y-position
	 * @param cell living or not
	 */
	public void setCell(int col, int row, boolean cell);

	/**
	 * Get dimension of cellgrid.
	 * @return dimension
	 */
	public Dimension getDimension();
	
	/**
	 * Resize the cell grid.
	 * @param col new number of columns.
	 * @param row new number of rows.
	 */
	public void resize(int col, int row);

	/**
	 * Enumerates over all living cells (type Cell).
	 * @return Enumerator over Cell.
	 * @see Cell
	 */
	public ConcurrentLinkedQueue<Cell> getEnum();

	/**
	 * Clears grid.
	 */
	public void clear();
}
