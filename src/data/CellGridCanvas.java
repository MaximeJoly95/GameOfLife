package data;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

import shape.Shape;
import shape.ShapeCollection;

public class CellGridCanvas extends Canvas implements CellGridDrawer {

	private static final long serialVersionUID = 1L;
	
	private static final Color BACKGROUND_COLOR = new Color(0x999999);
	private static final Color GRID_COLOR = Color.GRAY;
	private static final Color LIVING_CELLS_COLOR = Color.YELLOW;
	
	private Image offScreenImage;
	private Graphics offScreenGraphics;
	
	private CellGrid cellGrid;
	private int cellSize;
	private int newCellSize;
	private boolean cellUnderMouse;
	
	private Shape shape = ShapeCollection.POINT;
	
	/**
	 * Constructs a CellGridCanvas.
	 * @param cellGrid the Game of life cellgrid
	 * @param cellSize size of cell in pixels
	 */
	public CellGridCanvas(CellGrid cellGrid, int cellSize) {
		this.cellGrid = cellGrid;
		this.cellSize = cellSize;
		
		setBackground(BACKGROUND_COLOR);
		
		addMouseListener(
			new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					draw(e.getX(), e.getY());
				}
				public void mousePressed(MouseEvent e) {
					saveCellUnderMouse(e.getX(), e.getY());
				}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				draw(e.getX(), e.getY());
			}
		});
		
		addComponentListener( 
			new ComponentListener() {
				public void componentResized(ComponentEvent e) {
					resized();
					repaint();
				}
				public void componentMoved(ComponentEvent e) {}
				public void componentHidden(ComponentEvent e) {}
				public void componentShown(ComponentEvent e) {}
			}
		);
	}

	/**
	 * Set cell size (zoom factor)
	 * @param cellSize Size of cell in pixels
	 */
	@Override
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
		resized();
		repaint();
	}

	/**
	 * The grid is resized (by window resize or zooming).
	 * Also apply post-resize properties when necessary
	 */
	@Override
	public void resized() {
		if (newCellSize != 0) {
			cellSize = newCellSize;
			newCellSize = 0;
		}
		Dimension canvasDim = this.getSize();
		offScreenImage = null;
		cellGrid.resize(canvasDim.width / cellSize, canvasDim.height / cellSize);
	}

	/**
	 * Draw in this cell.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	@Override
	public void draw(int x, int y) {
		try {
			int col = x / cellSize;
			int row = y / cellSize;
			if (shape.equals(ShapeCollection.POINT)) {
				cellGrid.setCell(x / cellSize, y / cellSize, !cellUnderMouse);
			}
			else {
				Enumeration<int[]> cells = shape.getCells();
				while (cells.hasMoreElements()) {
					int[] cell = cells.nextElement();
					cellGrid.setCell(col + cell[0], row + cell[1], true);
				}
			}
			repaint();
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
	}
	
	/**
	 * Remember state of cell for drawing.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void saveCellUnderMouse(int x, int y) {
		try {
			cellUnderMouse = cellGrid.getCell(x / cellSize, y / cellSize);
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			// ignore
		}
	}
	
	/**
	 * Draw this generation.
	 */
	public void paint(Graphics g) {
		// Draw grid on background image, which is faster
		if (offScreenImage == null) {
			Dimension gridDim = cellGrid.getDimension();
			Dimension panelDim = getSize();
			offScreenImage = createImage(panelDim.width, panelDim.height);
			offScreenGraphics = offScreenImage.getGraphics();
			offScreenGraphics.setColor(BACKGROUND_COLOR);
			offScreenGraphics.fillRect(0, 0, panelDim.width, panelDim.height);
			offScreenGraphics.setColor(GRID_COLOR);
			offScreenGraphics.fillRect(0, 0, cellSize * gridDim.width, cellSize * gridDim.height);
			offScreenGraphics.setColor(BACKGROUND_COLOR);
			for (int x = 1; x < gridDim.width; x++) {
				offScreenGraphics.drawLine(x * cellSize, 0, x * cellSize, cellSize * gridDim.height);
			}
			for (int y = 1; y < gridDim.height; y++) {
				offScreenGraphics.drawLine( 0, y * cellSize, cellSize * gridDim.width, y * cellSize);
			}
		}
		// Draw grid lines
		g.drawImage(offScreenImage, 0, 0, null);
		// Draw living cells
		g.setColor(LIVING_CELLS_COLOR);
		ConcurrentLinkedQueue<Cell> current = cellGrid.getEnum();
		for (Cell c : current) {
			g.fillRect(c.col * cellSize, c.row * cellSize, cellSize, cellSize);
		}
	}

	/**
	 * To draw shape in grid.
	 * @param shape name of shape
	 */
	public synchronized void setShape(Shape shape) {
		this.shape = shape;
	}
	
}
