package shape;

import java.awt.Dimension;
import java.util.Enumeration;

public class Shape {
	
	private final String name;
	private final int[][] shape;
	
	/**
	 * Construct a a Shape.
	 * @param name name of shape
	 * @param shape shape data
	 */
	public Shape(String name, int[][] shape) {
		this.name = name;
		this.shape = shape;
	}
	
	/**
	 * Get dimension of shape.
	 * @return dimension of the shape in cells
	 */
	public Dimension getDimension() {
		int shapeWidth = 0;
		int shapeHeight = 0;
		for (int cell = 0; cell < shape.length; cell++) {
			if (shape[cell][0] > shapeWidth)
				shapeWidth = shape[cell][0];
			if (shape[cell][1] > shapeHeight)
				shapeHeight = shape[cell][1];
		}
		shapeWidth++;
		shapeHeight++;
		return new Dimension(shapeWidth, shapeHeight);
	}
	
	/**
	 * Get name of shape.
	 * @return name of shape
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get shape data.
	 * Hide the shape implementation. Returns a anonymous Enumerator object.
	 * @return enumerated shape data
	 */
	public Enumeration<int[]> getCells() {
		return new Enumeration<int[]>() {
			private int index = 0;
			public boolean hasMoreElements() {
				return index < shape.length;
			}
			public int[] nextElement() {
				return shape[index++];
			}
		};
	}
}
