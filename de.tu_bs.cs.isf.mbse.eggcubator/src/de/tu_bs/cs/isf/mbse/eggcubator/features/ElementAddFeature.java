package de.tu_bs.cs.isf.mbse.eggcubator.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedBlock;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedEnemy;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedItem;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper;

// TODO missing EggBlock
public class ElementAddFeature extends AbstractAddFeature implements IElementFeature {

	public ElementAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		Object o = context.getNewObject();
		return (o instanceof PlacedBlock || o instanceof PlacedEnemy || o instanceof PlacedItem) &&
				context.getTargetContainer().getContainer() != null &&
				context.getTargetContainer().getContainer().getLink() != null &&
				context.getTargetContainer().getContainer().getLink().getBusinessObjects().size() == 1 &&
				context.getTargetContainer().getContainer().getLink().getBusinessObjects().get(0) instanceof Level;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		PlacedElement element = (PlacedElement) context.getNewObject();
		Level level = (Level) context.getTargetContainer().getContainer().getLink().getBusinessObjects().get(0);
		ContainerShape elementContainer = LevelPictogramHelper.addElementShape(context.getTargetContainer(), element, level, getDiagram());
		link(elementContainer, element); // Linking can't be done in util class
		return elementContainer;
	}

}
