package de.tu_bs.cs.isf.mbse.eggcubator;

import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;

import de.tu_bs.cs.isf.mbse.egg.level.Level;

/**
 * Utility class for drawing with pictograms.
 */
public final class LevelPictogramHelper {

	private LevelPictogramHelper() { }

	//======================
	//========LEVEL=========
	//======================
	/**
	 * Border size of a level shape (outwards).
	 */
	public static final int LEVEL_BORDER_WIDTH = 2;
	
	/**
	 * Left offset for the level shape.
	 */
	public static final int LEVEL_OFFSET_X = 20;
	
	/**
	 * Top offset for the level shape.
	 */
	public static final int LEVEL_OFFSET_Y = 20;
	
	/**
	 * Shapes property name that is used to save the current width (in elements) of the level.
	 */
	public static final String LEVEL_WIDTH_PROPERTY = "width";
	
	/**
	 * Shapes property name that is used to save the current height (in elements) of the level.
	 */
	public static final String LEVEL_HEIGHT_PROPERTY = "height";
	
	/**
	 * Foreground color of the level shape.
	 */
	public static final IColorConstant LEVEL_FOREGROUND_COLOR = IColorConstant.BLACK;
	
	/**
	 * Background color of the level shape.
	 */
	public static final IColorConstant LEVEL_BACKGROUND_COLOR = IColorConstant.WHITE;
	
	/**
	 * Border style of the level shape.
	 */
	public static final LineStyle LEVEL_BORDER_STYLE = LineStyle.SOLID;
	
	/**
	 * Add a level shape with empty elements to the given ContainerShape.
	 * @return The new ContainerShape for the level
	 */
	public static final ContainerShape addLevelShape(Level level, ContainerShape containerShape, Diagram diagram) {
		IPeService pe = Graphiti.getPeService();
		IGaService ga = Graphiti.getGaService();
		
		// outer shape
		ContainerShape levelShape = pe.createContainerShape(containerShape, true);
		pe.setPropertyValue(levelShape, LEVEL_WIDTH_PROPERTY, String.valueOf(level.getWidth()));
		pe.setPropertyValue(levelShape, LEVEL_HEIGHT_PROPERTY, String.valueOf(level.getHeight()));
		
		// add visible rectangle
		Rectangle levelRectangle = ga.createRectangle(levelShape);
		levelRectangle.setForeground(ga.manageColor(diagram, LEVEL_FOREGROUND_COLOR));
		levelRectangle.setBackground(ga.manageColor(diagram, LEVEL_BACKGROUND_COLOR));
		levelRectangle.setLineWidth(LEVEL_BORDER_WIDTH);
		levelRectangle.setLineStyle(LEVEL_BORDER_STYLE);
		ga.setLocationAndSize(levelRectangle, LEVEL_OFFSET_X, LEVEL_OFFSET_Y,
				level.getElementSize() * level.getWidth() + LEVEL_BORDER_WIDTH * 2,
				level.getElementSize() * level.getHeight() + LEVEL_BORDER_WIDTH * 2);
		
		// add visible empty elements
		for (Integer x = 0; x < level.getWidth(); x++) {
			for (Integer y = 0; y < level.getHeight(); y++)
				addEmptyElementShape(x, y, level, levelShape, diagram);
		}

		return levelShape;
	}
	
	/**
	 * Resize the level shape without modifying the elements within.
	 * (Exception: width and height in pixels!)
	 * @return true if something changed and the elements must be reworked
	 */
	public static final boolean resizeLevelShape(Level level, ContainerShape levelShape, int width, int height, Diagram diagram) {
		// resizing whole elements only
		int truncatedWidth = width - (width - LEVEL_BORDER_WIDTH * 2) % level.getElementSize();
		if (truncatedWidth < MIN_ELEMENTS_X * level.getElementSize() + LEVEL_BORDER_WIDTH * 2)
			truncatedWidth = MIN_ELEMENTS_X * level.getElementSize() + LEVEL_BORDER_WIDTH * 2;
		int truncatedHeight = height - (height - LEVEL_BORDER_WIDTH * 2) % level.getElementSize();
		if (truncatedHeight < MIN_ELEMENTS_Y * level.getElementSize() + LEVEL_BORDER_WIDTH * 2)
			truncatedHeight = MIN_ELEMENTS_Y * level.getElementSize() + LEVEL_BORDER_WIDTH * 2;
		
		// adding/removing elements is not part of this operation

		GraphicsAlgorithm ga = levelShape.getGraphicsAlgorithm();
		boolean changed = ga.getWidth() != truncatedWidth || ga.getHeight() != truncatedHeight;
		if (changed) {
			// update level shape and properties
			Graphiti.getGaService().setLocationAndSize(ga, ga.getX(), ga.getY(), truncatedWidth, truncatedHeight);

			Graphiti.getPeService().setPropertyValue(levelShape, LEVEL_WIDTH_PROPERTY, String.valueOf(truncatedWidth / level.getElementSize()));
			Graphiti.getPeService().setPropertyValue(levelShape, LEVEL_HEIGHT_PROPERTY, String.valueOf(truncatedHeight / level.getElementSize()));
		}
		
		return changed;
	}
	
	/**
	 * Update the level shape and all contained element shapes.
	 */
	public static final void updateLevelShape(Level level, ContainerShape levelShape, Diagram diagram) {
		if (level.getWidth() < MIN_ELEMENTS_X)
			level.setWidth(MIN_ELEMENTS_X);
		if (level.getWidth() < MIN_ELEMENTS_Y)
			level.setWidth(MIN_ELEMENTS_Y);
		
		int oldWidth = -1, oldHeight = -1;
		for (Property prop : levelShape.getProperties()) {
			if (prop.getKey().equals(LEVEL_WIDTH_PROPERTY))
				oldWidth = Integer.parseInt(prop.getValue());
			else if (prop.getKey().equals(LEVEL_HEIGHT_PROPERTY))
				oldHeight = Integer.parseInt(prop.getValue());
		}
		
		resizeLevelShape(level, levelShape, level.getElementSize() * level.getWidth() + LEVEL_BORDER_WIDTH * 2,
				level.getElementSize() * level.getHeight() + LEVEL_BORDER_WIDTH * 2, diagram);
		
		updateElementShapes(oldWidth, oldHeight, level.getWidth(), level.getHeight(), levelShape, level, diagram);
	}
	
	//======================
	//========ELEMENT=======
	//======================

	/**
	 * Minimum count of horizontal elements.
	 * TODO this is bad and should be part of level itself.
	 */
	public static final int MIN_ELEMENTS_X = 3;

	/**
	 * Minimum count of vertical elements.
	 * TODO this is bad and should be part of level itself.
	 */
	public static final int MIN_ELEMENTS_Y = 3;
	
	/**
	 * Border size of an empty element shape (inwards).
	 */
	public static final int EMPTY_ELEMENT_BORDER_WIDTH = 1;
	
	/**
	 * Shapes property name that is used to save the x position of a element container.
	 */
	public static final String ELEMENT_X_PROPERTY = "posX";
	
	/**
	 * Shapes property name that is used to save the y position of a element container.
	 */
	public static final String ELEMENT_Y_PROPERTY = "posY";
	
	/**
	 * Foreground color of the empty element shape.
	 */
	public static final IColorConstant EMPTY_ELEMENT_FOREGROUND_COLOR = IColorConstant.LIGHT_BLUE;
	
	/**
	 * Background color of the empty element shape.
	 */
	public static final IColorConstant EMPTY_ELEMENT_BACKGROUND_COLOR = IColorConstant.WHITE;
	
	/**
	 * Border style of the empty element shape.
	 */
	public static final LineStyle EMPTY_ELEMENT_BORDER_STYLE = LineStyle.DASH;
	
	/**
	 * Add a single empty element shape into the ContainerShape.
	 * @return the new ContainerShape of the empty element
	 */
	public static final ContainerShape addEmptyElementShape(int x, int y, Level level, ContainerShape levelShape, Diagram diagram) {
		IPeService pe = Graphiti.getPeService();
		IGaService ga = Graphiti.getGaService();
		
		// the container shape gets properties, so we can search through them
		ContainerShape elContainerShape = pe.createContainerShape(levelShape, false);
		pe.setPropertyValue(elContainerShape, ELEMENT_X_PROPERTY, String.valueOf(x));
		pe.setPropertyValue(elContainerShape, ELEMENT_Y_PROPERTY, String.valueOf(y));
		
		// add visible rectangle for each empty element
		// This will be always below real elements once added
		Rectangle elRectangle = ga.createRectangle(elContainerShape);
		elRectangle.setForeground(ga.manageColor(diagram, EMPTY_ELEMENT_FOREGROUND_COLOR));
		elRectangle.setBackground(ga.manageColor(diagram, EMPTY_ELEMENT_BACKGROUND_COLOR));
		elRectangle.setLineWidth(EMPTY_ELEMENT_BORDER_WIDTH);
		elRectangle.setLineStyle(EMPTY_ELEMENT_BORDER_STYLE);
		ga.setLocationAndSize(elRectangle,
				level.getElementSize() * x + LEVEL_BORDER_WIDTH, level.getElementSize() * y + LEVEL_BORDER_WIDTH, // mind the left and top border
				level.getElementSize(), level.getElementSize());
		
		return elContainerShape;
	}
	
	/**
	 * Change the elements within the ContainerShape based on old and new sizes.
	 */
	public static final void layoutElementShapes(int oldWidth, int oldHeight, int newWidth, int newHeight,
			ContainerShape levelShape, Level level, Diagram diagram) {
		if (newWidth < oldWidth || newHeight < oldHeight) {
			IPeService pe = Graphiti.getPeService();
			// remove shapes and elements that are now outside the level
			final Shape[] shapes = levelShape.getChildren().toArray(new Shape[levelShape.getChildren().size()]);
			for (Shape shape : shapes) {
				int x = -1, y = -1;
				for (Property prop : shape.getProperties()) {
					if (prop.getKey().equals(ELEMENT_X_PROPERTY))
						x = Integer.parseInt(prop.getValue());
					else if (prop.getKey().equals(ELEMENT_Y_PROPERTY))
						y = Integer.parseInt(prop.getValue());
				}
				if (x >= newWidth || y >= newHeight)
					pe.deletePictogramElement(shape);
			}
		}
		
		// add new empty shapes
		final int xStart = oldHeight >= newHeight ? oldWidth : 0;
		final int yStart = oldWidth >= newWidth ? oldHeight : 0;
		for (int x = xStart; x < newWidth; x++) {
			for (int y = yStart; y < newHeight; y++) {
				if (x < oldWidth && y < oldHeight)
					continue;
				addEmptyElementShape(x, y, level, levelShape, diagram);
			}
		}
	}
	
	/**
	 * Change and resize the elements within the ContainerShape based on old and new sizes.
	 */
	public static final void updateElementShapes(int oldWidth, int oldHeight, int newWidth, int newHeight,
			ContainerShape levelShape, Level level, Diagram diagram) {
		IPeService pe = Graphiti.getPeService();
		IGaService ga = Graphiti.getGaService();
		
		// modify shapes we want to keep
		final int xKeep = Math.min(oldWidth, newWidth);
		final int yKeep = Math.min(oldHeight, newHeight);
		final Shape[] shapes = levelShape.getChildren().toArray(new Shape[levelShape.getChildren().size()]);
		for (Shape elCShape : shapes) {
			if (!(elCShape instanceof ContainerShape))
				continue;
			ContainerShape elContainerShape = (ContainerShape) elCShape;
			int x = -1, y = -1;
			for (Property prop : elContainerShape.getProperties()) {
				if (prop.getKey().equals(ELEMENT_X_PROPERTY))
					x = Integer.parseInt(prop.getValue());
				else if (prop.getKey().equals(ELEMENT_Y_PROPERTY))
					y = Integer.parseInt(prop.getValue());
			}
			if (x >= 0 && x < xKeep && y >= 0 && y < yKeep) {
				GraphicsAlgorithm elRectangle = elContainerShape.getGraphicsAlgorithm();
				// change size and position
				ga.setLocationAndSize(elRectangle,
						level.getElementSize() * x + LEVEL_BORDER_WIDTH, level.getElementSize() * y + LEVEL_BORDER_WIDTH, // mind the left and top border
						level.getElementSize(), level.getElementSize());
				
				// also for possible element within
				// TODO change element itself (if exists)
			} else {
				// since we already iterate over all children, remove this one (faster)
				pe.deletePictogramElement(elContainerShape);
			}
		}
		
		// add new ones
		layoutElementShapes(oldWidth, oldHeight, newWidth, newHeight, levelShape, level, diagram);
	}
	
}
