package cell;

public class Cell {
	
	public final short col;
	public final short row;
	
	// Number of neighbors of this cell.
	public int neighbors;
	
	public Cell(int col, int row) {
		this.col = (short) col;
		this.row = (short) row;
		neighbors = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Cell))
			return false;
		return col == ((Cell) o).col && row == ((Cell) o).row;
	}
}
