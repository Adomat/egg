package de.tu_bs.cs.isf.mbse.eggcubator.features.levels;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper;
import de.tu_bs.cs.isf.mbse.eggcubator.features.ILevelFeature;

public class LevelUpdateFeature extends AbstractUpdateFeature implements ILevelFeature {

	public LevelUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return context.getPictogramElement().getLink() != null &&
				context.getPictogramElement().getLink().getBusinessObjects().size() == 1 &&
				context.getPictogramElement().getLink().getBusinessObjects().get(0) instanceof Level;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		ContainerShape levelShape = (ContainerShape) context.getPictogramElement();
		Level level = (Level) levelShape.getLink().getBusinessObjects().get(0);
		int oldWidth = -1, oldHeight = -1;
		for (Property prop : levelShape.getProperties()) {
			if (prop.getKey().equals(LevelPictogramHelper.LEVEL_WIDTH_PROPERTY))
				oldWidth = Integer.parseInt(prop.getValue());
			else if (prop.getKey().equals(LevelPictogramHelper.LEVEL_HEIGHT_PROPERTY))
				oldHeight = Integer.parseInt(prop.getValue());
		}
		if (level.getWidth() != oldWidth || level.getHeight() != oldHeight // trivial resize
				|| levelShape.getGraphicsAlgorithm().getWidth() != level.getWidth() * level.getElementSize() + 2 * LevelPictogramHelper.LEVEL_BORDER_WIDTH) // element size changed
			return Reason.createTrueReason("Level was resized");
		else
			return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		ContainerShape levelShape = (ContainerShape) context.getPictogramElement();
		Level level = (Level) levelShape.getLink().getBusinessObjects().get(0);
		LevelPictogramHelper.updateLevelShape(level, levelShape, getDiagram());
		return true;
	}

}
