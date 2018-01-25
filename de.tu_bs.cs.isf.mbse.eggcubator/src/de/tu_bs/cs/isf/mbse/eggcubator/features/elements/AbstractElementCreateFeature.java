package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

import de.tu_bs.cs.isf.mbse.egg.descriptions.Description;
import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper;

public abstract class AbstractElementCreateFeature extends AbstractCreateFeature {
	
	protected AbstractElementCreateFeature(IFeatureProvider fp, Description desc) {
		super(fp, "Create " + desc.getName(), "Create a new " + desc.getName() + " element.");
	}
	
	protected AbstractElementCreateFeature(IFeatureProvider fp, String name) {
		super(fp, "Create " + name, "Create a new " + name + " element.");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		ContainerShape container = context.getTargetContainer(); // target must be an empty element (i.e. level container)
		if (container != null && container.getContainer() != null &&
				!(container.getContainer() instanceof Diagram)) // or none-empty element (in case of replace)
			container = container.getContainer().getContainer();
		return container != null && container.getLink() != null && 
				container.getLink().getBusinessObjects().size() == 1 &&
				container.getLink().getBusinessObjects().get(0) instanceof Level;
	}

	@Override
	public Object[] create(ICreateContext context) {
		ContainerShape levelContainer = context.getTargetContainer();
		Level level;
		PlacedElement element;
		ContainerShape elementContainer = null;
		int posX, posY;
		if (!(levelContainer.getContainer() instanceof Diagram)) {
			// replace -> remove first
			// levelContainer is currently set to PlacedBlockContainer
			element = (PlacedElement) levelContainer.getLink().getBusinessObjects().get(0);
			posX = element.getPositionX();
			posY = element.getPositionY();
			
			IDeleteContext delContext = new DeleteContext(levelContainer);
			elementContainer = levelContainer.getContainer();
			levelContainer = elementContainer.getContainer();
			IDeleteFeature delFeature = getFeatureProvider().getDeleteFeature(delContext);
			delFeature.delete(delContext);
			
			// just to be sure that the EmptyElementContainer is empty now and element will be removed
			Shape[] remainingShapes = elementContainer.getChildren().toArray(new Shape[elementContainer.getChildren().size()]);
			for (Shape s : remainingShapes)
				Graphiti.getPeService().deletePictogramElement(s);
			level = (Level) levelContainer.getLink().getBusinessObjects().get(0);
			level.removeElement(element);
		} else {
			level = (Level) levelContainer.getLink().getBusinessObjects().get(0);
			posX = context.getX() / level.getElementSize();
			posY = context.getY() / level.getElementSize();
		}
		
		element = createInstanceWithDescription();
		element.setPositionX(posX);
		element.setPositionY(posY);
		level.addElement(element);

		// Delegate to the add feature
		// addGraphicalRepresentation(context, element); // not possible, need other target container
		if (elementContainer == null)
			elementContainer = LevelPictogramHelper.getEmptyElementShape(element.getPositionX(), element.getPositionY(), levelContainer);
		CreateContext tmpContext = new CreateContext();
		tmpContext.setTargetContainer(elementContainer);
		addGraphicalRepresentation(tmpContext, element);
		return new Object[] { element };
	}

	protected abstract PlacedElement createInstanceWithDescription();
	
	/**
	 * Check if this CreateFeature is responsible for creating elements like this.
	 * @return true if this feature creates elements like the given element
	 */
	public abstract boolean createsElement(PlacedElement element);
	
}
