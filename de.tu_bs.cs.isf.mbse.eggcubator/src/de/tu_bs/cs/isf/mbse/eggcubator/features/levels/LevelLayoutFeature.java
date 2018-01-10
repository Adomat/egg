package de.tu_bs.cs.isf.mbse.eggcubator.features.levels;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;

import de.tu_bs.isf.cs.mbse.egg.level.Level;
import de.tu_bs.isf.cs.mbse.egg.level.PlacedElement;

public class LevelLayoutFeature extends AbstractLayoutFeature {
	static final int DEFAULT_BLOCK_SIZE = 75; // TODO model
	static final int BLOCK_FRAME_WIDTH = 1;
	static final int LEVEL_FRAME_WIDTH = 2;

	public LevelLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		if (!(context.getPictogramElement() instanceof ContainerShape))
			return false;
		// Check that this container is linked to the level
		EList<EObject> bos = ((ContainerShape) context.getPictogramElement()).getLink().getBusinessObjects();
		return bos.size() == 1 && bos.get(0) instanceof Level;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape levelShape = (ContainerShape) context.getPictogramElement();
		
		// get old sizes from level shape properties
		int oldWidth = -1, oldHeight = -1;
		for (Property prop : levelShape.getProperties()) {
			if (prop.getKey().equals("width"))
				oldWidth = Integer.parseInt(prop.getValue());
			else if (prop.getKey().equals("height"))
				oldHeight = Integer.parseInt(prop.getValue());
		}
		if (oldWidth < 0 || oldHeight < 0)
			throw new IllegalStateException("Width and height of level container shape must be set and bigger than zero.");
		
		// get new (already applied) sizes
		Integer newWidth = (levelShape.getGraphicsAlgorithm().getWidth() - LEVEL_FRAME_WIDTH * 2) / DEFAULT_BLOCK_SIZE;
		Integer newHeight = (levelShape.getGraphicsAlgorithm().getHeight() - LEVEL_FRAME_WIDTH * 2) / DEFAULT_BLOCK_SIZE;
		if (newWidth == oldWidth && newHeight == oldHeight)
			return false; // nothing changed
		
		Level level = (Level) levelShape.getLink().getBusinessObjects().get(0);
		IPeService pe = Graphiti.getPeService();
		IGaService ga = Graphiti.getGaService();
		
		if (newWidth < oldWidth || newHeight < oldHeight) {
			// remove shapes and elements that are now outside the level
			ArrayList<Shape> removeShapes = new ArrayList<>();
			for (Shape shape : levelShape.getChildren()) {
				int x = -1, y = -1;
				for (Property prop : shape.getProperties()) {
					if (prop.getKey().equals("positionX"))
						x = Integer.parseInt(prop.getValue());
					else if (prop.getKey().equals("positionY"))
						y = Integer.parseInt(prop.getValue());
				}
				if (x >= newWidth || y >= newHeight) {
					EList<Shape> children = ((ContainerShape) shape).getChildren();
					if (!children.isEmpty() && children.size() == 1 && children.get(0).getLink().getBusinessObjects().size() == 1 &&
							children.get(0).getLink().getBusinessObjects().get(0) instanceof PlacedElement) {
						PlacedElement element = (PlacedElement) ((ContainerShape) shape).getChildren().get(0).getLink().getBusinessObjects().get(0);
						// level.removeElement(element)
						// TODO model
					} else if (!children.isEmpty())
						System.out.println("WARNING: Cannot remove possibly existing business object. Invalid format.");
					removeShapes.add(shape);
				}
			}
			for (Shape shape : removeShapes)
				pe.deletePictogramElement(shape);
		}
		
		// add new empty shapes
		for (Integer x = 0; x < newWidth; x++) {
			if (oldHeight == newHeight && x < oldWidth)
				continue;
			for (Integer y = 0; y < newHeight; y++) {
				if (x < oldWidth && y < oldHeight)
					continue;
				// TODO util
				// the container shape gets properties, so we can search through them
				ContainerShape blockContainerShape = pe.createContainerShape(levelShape, false);
				pe.setPropertyValue(blockContainerShape, "positionX", x.toString());
				pe.setPropertyValue(blockContainerShape, "positionY", y.toString());
				
				// add visible rectangle for each empty block
				// This will be always below real blocks once added
				Rectangle blockRectangle = ga.createRectangle(blockContainerShape);
				blockRectangle.setLineWidth(BLOCK_FRAME_WIDTH);
				blockRectangle.setForeground(manageColor(IColorConstant.LIGHT_BLUE));
				blockRectangle.setBackground(manageColor(IColorConstant.WHITE));
				blockRectangle.setLineStyle(LineStyle.DASH);
				ga.setLocationAndSize(blockRectangle,
						DEFAULT_BLOCK_SIZE * x + LEVEL_FRAME_WIDTH, DEFAULT_BLOCK_SIZE * y + LEVEL_FRAME_WIDTH,
						DEFAULT_BLOCK_SIZE, DEFAULT_BLOCK_SIZE);
				// TODO model
			}
		}
		
		pe.setPropertyValue(levelShape, "width", newWidth.toString());
		pe.setPropertyValue(levelShape, "height", newHeight.toString());
		
		return true; // size and layout changed
	}

}
