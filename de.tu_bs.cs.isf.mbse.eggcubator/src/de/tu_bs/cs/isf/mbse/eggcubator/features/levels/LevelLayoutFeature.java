package de.tu_bs.cs.isf.mbse.eggcubator.features.levels;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper;
import de.tu_bs.cs.isf.mbse.eggcubator.features.ILevelFeature;

public class LevelLayoutFeature extends AbstractLayoutFeature implements ILevelFeature {

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
		Level level = (Level) levelShape.getLink().getBusinessObjects().get(0);
		
		int newWidth = -1, newHeight = -1;
		for (Property prop : levelShape.getProperties()) {
			if (prop.getKey().equals(LevelPictogramHelper.LEVEL_WIDTH_PROPERTY))
				newWidth = Integer.parseInt(prop.getValue());
			else if (prop.getKey().equals(LevelPictogramHelper.LEVEL_HEIGHT_PROPERTY))
				newHeight = Integer.parseInt(prop.getValue());
		}
		
		if (level.getWidth() == newWidth && level.getHeight() == newHeight)
			return false; // nothing changed
		
		LevelPictogramHelper.layoutElementShapes(level.getWidth(), level.getHeight(), newWidth, newHeight, levelShape, level, getDiagram());
		
		return true; // layout changed
	}

}
