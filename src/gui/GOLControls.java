package gui;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import event.GOLControlsEvent;
import event.GOLControlsListener;
import shape.Shape;
import shape.ShapeCollection;

public class GOLControls extends Panel {

	private static final long serialVersionUID = 1L;
	
	private final String genLabelText = "Generations : ";
	private final String nextLabelText = "Next";
	private final String startLabelText = "Start";
	private final String stopLabelText = "Stop";
	private final String clearLabelText = "Clear";
	private static final Dimension DIM = new Dimension(130, 20);
	
	public static final String SLOW = "Slow";
	public static final String FAST = "Fast";
	public static final String HYPER = "Hyper";
	public static final String BIG = "Big";
	public static final String MEDIUM = "Medium";
	public static final String SMALL = "Small";
	public static final int SIZE_BIG = 11;
	public static final int SIZE_MEDIUM = 7;
	public static final int SIZE_SMALL = 3;
	
	private Vector<GOLControlsListener> listeners 	= new Vector<GOLControlsListener>();
	private Label genLabel 			= new Label(genLabelText);
	private Button startstopButton 	= new Button(startLabelText);
	private Button nextButton 		= new Button(nextLabelText);
	private Button clearButton 		= new Button(clearLabelText);
	private Choice shapesChoice		= new Choice();
	private Choice speedChoice 		= new Choice();
	private Choice zoomChoice 		= new Choice();
	
	public GOLControls(GOLControlsListener listener) {
		listeners.addElement(listener);
		
		genLabel.setPreferredSize(DIM);
		zoomChoice.setPreferredSize(DIM);
		shapesChoice.setPreferredSize(DIM);
		speedChoice.setPreferredSize(DIM);
		
		Shape[] shapes = ShapeCollection.getShapes();
		for (int i = 0 ; i < shapes.length ; i++) {
			shapesChoice.addItem(shapes[i].getName());
		}
		
		shapesChoice.addItemListener(
			new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					shapeSelected((String) e.getItem());
				}
			}
		);
		
		speedChoice.addItem(SLOW);
		speedChoice.addItem(FAST);
		speedChoice.addItem(HYPER);
		speedChoice.addItemListener(
			new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					String arg = (String) e.getItem();
					if (SLOW.equals(arg))
						speedChanged(1000);
					else if (FAST.equals(arg))
						speedChanged(100);
					else if (HYPER.equals(arg))
						speedChanged(10);
				}
			}
		);
		
		zoomChoice.addItem(BIG);
		zoomChoice.addItem(MEDIUM);
		zoomChoice.addItem(SMALL);
		zoomChoice.addItemListener(
			new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					String arg = (String) e.getItem();
					if (BIG.equals(arg))
						zoomChanged(SIZE_BIG);
					else if (MEDIUM.equals(arg))
						zoomChanged(SIZE_MEDIUM);
					else if (SMALL.equals(arg))
						zoomChanged(SIZE_SMALL);
				}
			}
		);
		
		startstopButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					startStopButtonClicked();
				}
			}
		);
		
		nextButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					nextButtonClicked();
				}
			}
		);
		
		clearButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearButtonClicked();
				}
			}
		);
		
		GridBagLayout gb = new GridBagLayout();
		setLayout(gb);
		GridBagConstraints gbc = new GridBagConstraints();	
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(startstopButton, gbc);
	
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(nextButton, gbc);
		
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(clearButton, gbc);
		
		gbc.insets = new Insets(5, 20, 5, 0);
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(genLabel, gbc);
		
		gbc.insets = new Insets(0, 0, 8, 0);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 2;
		gbc.weighty = 1;
		this.add(shapesChoice, gbc);
		
		gbc.insets = new Insets(0, 0, 8, 0);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(speedChoice, gbc);
		
		gbc.insets = new Insets(0, 0, 8, 0);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		this.add(zoomChoice, gbc);
		
		this.validate();
	}


	/**
	 * Set the number of generations in the control bar.
	 * @param generations number of generations
	 */
	public void setGeneration(int generations) {
		genLabel.setText(genLabelText + generations);
	}
	
	/**
	 * Called when a new cell size from the zoom pull down is selected.
	 * Notify event-listeners.
	 */
	public void setZoom(String n) {
		zoomChoice.select(n);
	}
	
	/**
	 * Start-button is activated.
	 */
	public void start() {
		startstopButton.setLabel(stopLabelText);
		nextButton.setEnabled(false);
	}
	
	/**
	 * Stop-button is activated.
	 */
	public void stop() {
		startstopButton.setLabel(startLabelText);
		nextButton.setEnabled(true);
	}
	
	/**
	 * Called when the start/stop-button is clicked.
	 * Notify event-listeners.
	 */
	public void startStopButtonClicked() {
		GOLControlsEvent event = new GOLControlsEvent(this);
		for (GOLControlsListener l : listeners) {
			l.startStopButtonClicked(event);
		}
	}
	
	/**
	 * Called when the next-button is clicked.
	 * Notify event-listeners.
	 */
	public void nextButtonClicked() {
		GOLControlsEvent event = new GOLControlsEvent(this);
		for (GOLControlsListener l : listeners) {
			l.nextButtonClicked(event);
		}
	}
	
	/**
	 * Called when the clear-button is clicked.
	 * Notify event-listeners.
	 */
	public void clearButtonClicked() {
		GOLControlsEvent event = new GOLControlsEvent(this);
		for (GOLControlsListener l : listeners) {
			l.clearButtonClicked(event);
		}
	}
	
	/**
	 * Called when a new speed from the speed pull down is selected.
	 * Notify event-listeners.
	 */
	public void speedChanged(int speed) {
		GOLControlsEvent event = GOLControlsEvent.getSpeedChangedEvent(this, speed);
		for (GOLControlsListener l : listeners) {
			l.speedChanged(event);
		}
	}
	
	/**
	 * Called when a new zoom from the zoom pull down is selected.
	 * Notify event-listeners.
	 */
	public void zoomChanged( int zoom ) {
		GOLControlsEvent event = GOLControlsEvent.getZoomChangedEvent(this, zoom);
		for (GOLControlsListener l : listeners) {
			l.zoomChanged(event);
		}
	}
	
	/**
	 * Called when a new zoom from the zoom pull down is selected.
	 * Notify event-listeners.
	 */
	public void shapeSelected(String shapeName) {
		GOLControlsEvent event = GOLControlsEvent.getShapeSelectedEvent( this, shapeName );
		for (GOLControlsListener l : listeners) {
			l.shapeSelected(event);
		}
	}
}
