package de.tu_bs.cs.isf.mbse.eggcubator.features.levels;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper;
import de.tu_bs.cs.isf.mbse.eggcubator.features.ILevelFeature;

public class LevelAddFeature extends AbstractAddFeature implements ILevelFeature {

	public LevelAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getNewObject() instanceof Level &&
				context.getTargetContainer() instanceof Diagram &&
				(context.getTargetContainer().getChildren() == null ||
				context.getTargetContainer().getChildren().isEmpty());
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Level level = (Level) context.getNewObject();
		ContainerShape levelShape = LevelPictogramHelper.addLevelShape(level, context.getTargetContainer(), getDiagram());
		link(levelShape, level); // Linking can't be done in util class
		return levelShape;
	}

}
