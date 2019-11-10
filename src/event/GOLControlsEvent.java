package event;

import java.awt.Event;

public class GOLControlsEvent extends Event {

	private static final long serialVersionUID = 1L;
	
	private int speed;
	private int zoom;
	private String shapeName;
	
	public GOLControlsEvent(Object source) {
		super(source, 0, null);
	}
	
	/**
	 * Constructs a event due to the speed changed.
	 * @param source source of the event
	 * @param speed new speed
	 * @return new event object
	 */
	public static GOLControlsEvent getSpeedChangedEvent(Object source, int speed) {
		GOLControlsEvent event = new GOLControlsEvent(source);
		event.setSpeed(speed);
		return event;
	}
	
	/**
	 * Constructs a event due to the zoom changed.
	 * @param source source of the event
	 * @param zoom new zoom (cell size in pixels)
	 * @return new event object
	 */
	public static GOLControlsEvent getZoomChangedEvent(Object source, int zoom) {
		GOLControlsEvent event = new GOLControlsEvent(source);
		event.setZoom(zoom);
		return event;
	}
	
	/**
	 * Constructs a event due to the shape changed.
	 * @param source source of the event
	 * @param shapeName name of selected shape
	 * @return new event object
	 */
	public static GOLControlsEvent getShapeSelectedEvent(Object source, String shapeName) {
		GOLControlsEvent event = new GOLControlsEvent(source);
		event.setShapeName(shapeName);
		return event;
	}
	
	/**
	 * Gets speed of Game
	 * @return speed (10 is fast, 1000 is slow)
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets speed of Game
	 * @param speed (10 is fast, 1000 is slow)
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Gets size of cell
	 * @return speed (10 is big, 2 is small)
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * Sets zoom of Game
	 * @param zoom size of cell in pixels
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	/**
	 * Gets name of shape
	 * @return name of selected shape
	 */
	public String getShapeName() {
		return shapeName;
	}

	/**
	 * Sets name of shape
	 * @param shapeName name of shape
	 */
	public void setShapeName( String shapeName ) {
		this.shapeName = shapeName;
	}
}