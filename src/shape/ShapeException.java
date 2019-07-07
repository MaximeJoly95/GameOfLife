package shape;

public class ShapeException extends Exception {

	private static final long serialVersionUID = 1L;

	public ShapeException() {
		super();
	}
	
	/**
	* Constructs a ShapeException with a description.
	*/
	public ShapeException(String s) {
		super(s);
	}
}
