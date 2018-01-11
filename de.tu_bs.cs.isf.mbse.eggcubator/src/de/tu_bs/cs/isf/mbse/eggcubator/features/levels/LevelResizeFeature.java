package de.tu_bs.cs.isf.mbse.eggcubator.features.levels;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper;
import de.tu_bs.cs.isf.mbse.eggcubator.features.ILevelFeature;

public class LevelResizeFeature extends DefaultResizeShapeFeature implements ILevelFeature {
	
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
		ContainerShape levelShape = (ContainerShape) context.getShape();
		Level level = (Level) levelShape.getLink().getBusinessObjects().get(0);
		
		if (LevelPictogramHelper.resizeLevelShape(level, levelShape, context.getWidth(), context.getHeight(), getDiagram())) {
			// get new values
			int width = -1, height = -1;
			for (Property prop : levelShape.getProperties()) {
				if (prop.getKey().equals(LevelPictogramHelper.LEVEL_WIDTH_PROPERTY))
					width = Integer.parseInt(prop.getValue());
				else if (prop.getKey().equals(LevelPictogramHelper.LEVEL_HEIGHT_PROPERTY))
					height = Integer.parseInt(prop.getValue());
			}
			// if changed: pass inner shape layouting to layout feature
			layoutPictogramElement(levelShape);
			// update model afterwards, so detection of changes within layout feature is possible (this automatically deletes elements)
			level.setWidth(width);
			level.setHeight(height);
		}
	}

}
