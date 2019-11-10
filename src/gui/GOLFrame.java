package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cell.CellGridCanvas;
import event.GOLControlsEvent;
import event.GOLControlsListener;
import shape.Shape;
import shape.ShapeCollection;
import shape.ShapeException;

public class GOLFrame extends JFrame implements GOL, Runnable, GOLControlsListener {

	private static final long serialVersionUID = 1L;
	
	private static final Dimension DEFAULT_FRAME_DIM = new Dimension(700, 550);
	
	private static final Color BACKGROUND_COLOR = new Color(0x999999);
	
	private static final int DEFAULT_CELL_SIZE = 11;
	private static final int DEFAULT_CELL_COLS = 50;
	private static final int DEFAULT_CELL_ROWS = 30;
	private static final int DEFAULT_SPEED = 1000;
	
	private int cellSize = DEFAULT_CELL_SIZE;
	private int cellCols = DEFAULT_CELL_COLS;
	private int cellRows = DEFAULT_CELL_ROWS;
	private int speed = DEFAULT_SPEED;
	
	private GOLControls controls;
	
	private CellGridCanvas gameOfLifeCanvas;
	private GOLGrid gameOfLifeGrid;
	private static Thread gameThread = null;
	
	public GOLFrame() {
		super("Game Of Life");
		setSize(DEFAULT_FRAME_DIM);
		setResizable(true);
		setMinimumSize(DEFAULT_FRAME_DIM);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(BACKGROUND_COLOR);
		init();
		setVisible(true);
		validate();
	}
	
	public void init() {
		gameOfLifeGrid = new GOLGrid(cellCols, cellRows);
		gameOfLifeCanvas = new CellGridCanvas(gameOfLifeGrid, cellSize);
		controls = new GOLControls(this);
		
		GridBagLayout gb = new GridBagLayout();
		setLayout(gb);
        
        GridBagConstraints canvasConstraints = new GridBagConstraints();    
        canvasConstraints.fill = GridBagConstraints.BOTH;
        canvasConstraints.gridx = GridBagConstraints.REMAINDER;
        canvasConstraints.gridy = 0;
        canvasConstraints.weightx = 1;
        canvasConstraints.weighty = 1;
        canvasConstraints.anchor = GridBagConstraints.CENTER;
        add(gameOfLifeCanvas, canvasConstraints);
        
        GridBagConstraints controlsConstraints = new GridBagConstraints();   
        controlsConstraints.gridy = 1;
        controlsConstraints.gridx = 0;
        controlsConstraints.gridx = GridBagConstraints.REMAINDER;
        add(controls, controlsConstraints);
	}
	
	/**
	 * Resets
	 */
	public void reset() {
		stop();
		gameOfLifeCanvas.repaint();
		showGenerations();
	}
	
	/**
	 * Starts creating new generations.
	 * No start() to prevent starting immediately.
	 */
	public synchronized void startNewGeneration() {
		controls.start();
		if (gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();
		}
	}
	
	@Override
	public void stop() {
		controls.stop();
		gameThread = null;
	}
	

	@Override
	public void run() {
		while (gameThread != null) {
			nextGeneration();
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Is the application running?
	 * @return true: application is running
	 */
	@Override
	public boolean isRunning() {
		return gameThread != null;
	}

	/**
	 * Go to the next generation.
	 */
	@Override
	public void nextGeneration() {
		gameOfLifeGrid.next();
		gameOfLifeCanvas.repaint();
		showGenerations();
	}
	
	/**
	 * Show number of generations.
	 */
	private void showGenerations() {
		controls.setGeneration(gameOfLifeGrid.getGenerations());
	}
	
	/**
	 * Set the new shape
	 * @param shape name of shape
	 */
	public void setShape(Shape shape) {
		if ( shape == null )
			return;
		
		gameOfLifeCanvas.setShape(shape);
		reset();
	}
	
	/**
	 * Set speed of new generations.
	 * @param fps generations per second
	 */
	@Override
	public void setSpeed(int fps) {
		speed = fps;
	}

	/**
	 * Sets cell size.
	 * @param size of cell in pixels
	 */
	@Override
	public void setCellSize(int size) {
		cellSize = size;
		gameOfLifeCanvas.setCellSize(cellSize);
	}

	/**
	 * Gets cell size.
	 * @return size of cell
	 */
	@Override
	public int getCellSize() {
		return cellSize;
	}
	
	// ======================================================= //

	@Override
	public void startStopButtonClicked(GOLControlsEvent e) {
		if (isRunning())
			stop();
		else
			startNewGeneration();
	}

	@Override
	public void nextButtonClicked(GOLControlsEvent e) {
		nextGeneration();
	}

	@Override
	public void clearButtonClicked(GOLControlsEvent e) {
		reset();
		gameOfLifeGrid.clear();
	}

	@Override
	public void speedChanged(GOLControlsEvent e) {
		setSpeed(e.getSpeed());
	}

	@Override
	public void zoomChanged(GOLControlsEvent e) {
		setCellSize(e.getZoom());
	}
	
	@Override
	public void shapeSelected(GOLControlsEvent e) {
		try {
			String shapeName = (String) e.getShapeName();
			Shape shape = ShapeCollection.getShapeByName(shapeName);
			setShape(shape);
		} catch (ShapeException ex) {
			JOptionPane.showMessageDialog(null, "Error when setting the shape", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void start() {
		
	}
}
