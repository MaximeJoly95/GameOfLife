package event;

import java.util.EventListener;

public interface GOLControlsListener extends EventListener {
	
	/**
	 * The Start/Stop button is clicked.
	 * @param e event object
	 */
	public void startStopButtonClicked(GOLControlsEvent e);

	/**
	 * The Next button is clicked.
	 * @param e event object
	 */
	public void nextButtonClicked(GOLControlsEvent e);
	
	/**
	 * The Clear button is clicked.
	 * @param e event object
	 */
	public void clearButtonClicked(GOLControlsEvent e);
	
	/**
	 * A new shape is selected.
	 * @param e event object
	 */
	public void shapeSelected(GOLControlsEvent e);

	/**
	 * A new speed is selected.
	 * @param e event object
	 */
	public void speedChanged(GOLControlsEvent e);

	/**
	 * A new cell size is selected.
	 * @param e event object
	 */
	public void zoomChanged(GOLControlsEvent e);

}
