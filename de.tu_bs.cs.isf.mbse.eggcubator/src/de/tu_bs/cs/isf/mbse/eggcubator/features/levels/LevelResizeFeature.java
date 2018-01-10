package de.tu_bs.cs.isf.mbse.eggcubator.features.levels;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;

import de.tu_bs.cs.isf.mbse.egg.level.Level;

public class LevelResizeFeature extends DefaultResizeShapeFeature {
	static final int DEFAULT_BLOCK_SIZE = 75; // TODO model
	static final int MIN_BLOCKS_X = 3;
	static final int MIN_BLOCKS_Y = 3;
	static final int LEVEL_FRAME_WIDTH = 2;
	
	public LevelResizeFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		if (!(context.getShape() instanceof ContainerShape))
			return false;
		// Check that this container is linked to the level
		EList<EObject> bos = ((ContainerShape) context.getShape()).getLink().getBusinessObjects();
		return bos.size() == 1 && bos.get(0) instanceof Level;
	}
	
	@Override
	public void resizeShape(IResizeShapeContext context) {
		ContainerShape shape = (ContainerShape) context.getShape();
		Level level = (Level) shape.getLink().getBusinessObjects().get(0);
		// TODO model: change level
		
		// only whole blocks
		int width = context.getWidth() - (context.getWidth() - LEVEL_FRAME_WIDTH * 2) % DEFAULT_BLOCK_SIZE;
		if (width < MIN_BLOCKS_X * DEFAULT_BLOCK_SIZE + LEVEL_FRAME_WIDTH * 2)
			width = MIN_BLOCKS_X * DEFAULT_BLOCK_SIZE + LEVEL_FRAME_WIDTH * 2;
		int height = context.getHeight() - (context.getHeight() - LEVEL_FRAME_WIDTH * 2) % DEFAULT_BLOCK_SIZE;
		if (height < MIN_BLOCKS_Y * DEFAULT_BLOCK_SIZE + LEVEL_FRAME_WIDTH * 2)
			height = MIN_BLOCKS_Y * DEFAULT_BLOCK_SIZE + LEVEL_FRAME_WIDTH * 2;

		GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
		if (ga.getWidth() != width || ga.getHeight() != height) {
			Graphiti.getGaService().setLocationAndSize(ga, ga.getX(), ga.getY(), width, height);
	
			// pass inner shape layouting to layout feature
			layoutPictogramElement(shape);
		}
	}

}
