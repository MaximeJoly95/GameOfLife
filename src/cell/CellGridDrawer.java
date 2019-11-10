package cell;

public interface CellGridDrawer {
	void setCellSize(int cellSize);
    void resized();
    void draw(int x, int y);
}
