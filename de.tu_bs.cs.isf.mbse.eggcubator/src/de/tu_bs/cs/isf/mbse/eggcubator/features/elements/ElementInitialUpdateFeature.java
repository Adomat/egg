package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.platform.IDiagramBehavior;

import de.tu_bs.cs.isf.mbse.egg.level.PlacedBlock;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedEnemy;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedItem;
import de.tu_bs.cs.isf.mbse.eggcubator.EggImageProvider;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelDiagramBehavior;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IBlockFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IEnemyFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IItemFeature;

public class ElementInitialUpdateFeature extends AbstractUpdateFeature
		implements IBlockFeature, IEnemyFeature, IItemFeature {

	public ElementInitialUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		// update PlacedElements in startup phase
		// needs auto update at startup!
		// auto update is applied to the diagram
		IDiagramBehavior behavior = getDiagramBehavior();
		return behavior instanceof LevelDiagramBehavior && ((LevelDiagramBehavior) behavior).isStartingUp() &&
				context.getPictogramElement() instanceof Diagram &&
				((Diagram) context.getPictogramElement()).getChildren().size() == 1;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		// Quite a waste of time, but without this the editor is dirty everytime
		Diagram diagram = (Diagram) context.getPictogramElement();
		ContainerShape levelShape = (ContainerShape) diagram.getChildren().get(0);
		
		for (Shape eeShape : levelShape.getChildren()) {
			if (!(eeShape instanceof ContainerShape))
				continue;
			for (Shape eShape : ((ContainerShape) eeShape).getChildren()) {
				if (!(eShape instanceof ContainerShape))
					continue;
				if (eShape.getLink() != null && eShape.getLink().getBusinessObjects().size() == 1 &&
						updateElement((ContainerShape) eShape, true))
					return Reason.createTrueReason("Image changed");
			}
		}
		return Reason.createFalseReason();
	}

	/**
	 * Update if image id of the given element shape changed.
	 * @return true if something changed
	 */
	protected boolean updateElement(ContainerShape elShape) {
		return updateElement(elShape, false);
	}

	/**
	 * Update if image id of the given element shape changed.
	 * @return true if something changed
	 */
	protected boolean updateElement(ContainerShape elShape, boolean checkOnly) {
		PlacedElement element = (PlacedElement) elShape.getLink().getBusinessObjects().get(0);
		boolean hasDescription;
		if (element instanceof PlacedBlock) {
			hasDescription = ((PlacedBlock) element).getProperties().getName() != null;
		} else if (element instanceof PlacedItem) {
			hasDescription = ((PlacedItem) element).getProperties().getName() != null;
		} else if (element instanceof PlacedEnemy) {
			hasDescription = ((PlacedEnemy) element).getProperties().getName() != null;
		} else
			return false; // ignore unknown element type
		
		for(Shape shape : elShape.getChildren()) {
			if (!(shape.getGraphicsAlgorithm() instanceof Image))
				continue;
			Image image = (Image) shape.getGraphicsAlgorithm();
			String id = hasDescription ? EggImageProvider.getImageId(element) : EggImageProvider.IMG_NOT_FOUND_ID;
			if (!image.getId().equals(id)) {
				if (!checkOnly)
					image.setId(id);
				return true;
			}
			break;
		}
		return false;
	}
	
	@Override
	public boolean update(IUpdateContext context) {
		Diagram diagram = (Diagram) context.getPictogramElement();
		ContainerShape levelShape = (ContainerShape) diagram.getChildren().get(0);
		
		boolean changed = false;
		for (Shape eeShape : levelShape.getChildren()) {
			if (!(eeShape instanceof ContainerShape))
				continue;
			for (Shape eShape : ((ContainerShape) eeShape).getChildren()) {
				if (!(eShape instanceof ContainerShape))
					continue;
				if (eShape.getLink() != null && eShape.getLink().getBusinessObjects().size() == 1)
					changed = updateElement((ContainerShape) eShape) || changed;
			}
		}
		return changed;
	}

}
