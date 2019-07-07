package event;

import java.util.EventListener;

public interface GameControlsListener extends EventListener {
	
	/**
	 * The Start/Stop button is clicked.
	 * @param e event object
	 */
	public void startStopButtonClicked(GameControlsEvent e);

	/**
	 * The Next button is clicked.
	 * @param e event object
	 */
	public void nextButtonClicked(GameControlsEvent e);
	
	/**
	 * The Clear button is clicked.
	 * @param e event object
	 */
	public void clearButtonClicked(GameControlsEvent e);
	
	/**
	 * A new shape is selected.
	 * @param e event object
	 */
	public void shapeSelected(GameControlsEvent e);

	/**
	 * A new speed is selected.
	 * @param e event object
	 */
	public void speedChanged(GameControlsEvent e);

	/**
	 * A new cell size is selected.
	 * @param e event object
	 */
	public void zoomChanged(GameControlsEvent e);

}
