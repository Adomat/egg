package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.EndPoint;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.WarpPoint;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper.ElementPosition;
import de.tu_bs.cs.isf.mbse.eggcubator.features.ILevelFeature;

public class ElementMultiplyFeature extends DefaultResizeShapeFeature implements ILevelFeature {

	public ElementMultiplyFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		if (!(context.getShape() instanceof ContainerShape) || context.getShape().getContainer() == null || context.getShape().getLink() == null)
			return false;
		// Check that this container is linked to an element and there is a level available
		ContainerShape shape = ((ContainerShape) context.getShape());
		ContainerShape levelContainer = shape.getContainer().getContainer();
		EList<EObject> bos = shape.getLink().getBusinessObjects();
		PictogramLink levelLink = levelContainer.getLink();
		if (bos.size() != 1 || !(bos.get(0) instanceof PlacedElement) || shape.getChildren().size() != 1 ||
				!(shape.getChildren().get(0).getGraphicsAlgorithm() instanceof Image) ||
				levelLink == null || levelLink.getBusinessObjects().size() != 1 ||
				!(levelLink.getBusinessObjects().get(0) instanceof Level))
			return false;
		
		ICreateFeature createFeature = null;
		for (ICreateFeature cf : getFeatureProvider().getCreateFeatures()) {
			if (cf instanceof AbstractElementCreateFeature && ((AbstractElementCreateFeature) cf).createsElement((PlacedElement) bos.get(0))) {
				createFeature = cf;
				break;
			}
		}
		return createFeature != null;
	}
	
	@Override
	public void resizeShape(IResizeShapeContext context) {
		ContainerShape elementShape = ((ContainerShape) context.getShape());
		ContainerShape levelContainer = elementShape.getContainer().getContainer();
		Level level = (Level) levelContainer.getLink().getBusinessObjects().get(0);
		
		final int newElementsY = (context.getHeight() / level.getElementSize()) - 1;
		final int newElementsX = (context.getWidth() / level.getElementSize()) - 1;
		
		if (newElementsX < 0 || newElementsY < 0 || (newElementsX == 0 && newElementsY == 0))
			return;
		
		PlacedElement element = (PlacedElement) elementShape.getLink().getBusinessObjects().get(0);

		ICreateFeature createFeature = null;
		for (ICreateFeature cf : getFeatureProvider().getCreateFeatures()) {
			if (cf instanceof AbstractElementCreateFeature &&
					((AbstractElementCreateFeature) cf).createsElement((PlacedElement) elementShape.getLink().getBusinessObjects().get(0))) {
				createFeature = cf;
				break;
			}
		}

		// top left (NORTH_WEST) to bottom right (SOUTH_EAST)
		final int xStart = element.getPositionX() - ((context.getDirection() & IResizeShapeContext.DIRECTION_WEST) > 0 ? newElementsX : 0);
		final int yStart = element.getPositionY() - ((context.getDirection() & IResizeShapeContext.DIRECTION_NORTH) > 0 ? newElementsY : 0);
		final int xEnd = xStart + newElementsX;
		final int yEnd = yStart + newElementsY;

		// remember selection
		PictogramElement[] currentSelection = getDiagramBehavior().getDiagramContainer().getSelectedPictogramElements();
		
		for (Shape shape : levelContainer.getChildren()) {
			// check if this element isn't the current one and we are within the selected range
			if (shape == elementShape.getContainer() || !(shape instanceof ContainerShape))
				continue;
			ElementPosition pos = new ElementPosition(shape);
			if (pos.getX() < xStart || pos.getX() > xEnd || pos.getY() < yStart || pos.getY() > yEnd)
				continue;
			
			// find correct container
			ContainerShape contextContainer = levelContainer;
			if (((ContainerShape) shape).getChildren().size() > 0) {
				for (Shape cShape : ((ContainerShape) shape).getChildren()) {
					if (cShape instanceof ContainerShape && cShape.getLink() != null &&
							cShape.getLink().getBusinessObjects().size() == 1 &&
							cShape.getLink().getBusinessObjects().get(0) instanceof PlacedElement) {
						contextContainer = (ContainerShape) cShape;
						break;
					}
				}
			}
			
			// pass further actions to CreateFeature
			CreateContext createContext = new CreateContext();
			createContext.setLocation(pos.getX() * level.getElementSize(), pos.getY() * level.getElementSize());
			createContext.setTargetContainer(contextContainer);
			if (createFeature.canCreate(createContext)) {
				Object[] res = createFeature.create(createContext); // this does replacing by itself, no need to worry
				// special case: WarpPoint and EndPoint need copy of properties
				for (Object o : res) {
					if (element instanceof WarpPoint && o instanceof WarpPoint) {
						((WarpPoint) o).setWarpTo(((WarpPoint) element).getWarpTo());
						((WarpPoint) o).setHeroOnEntry(((WarpPoint) element).getHeroOnEntry());
						IUpdateContext updateContext = new UpdateContext(((ContainerShape) shape).getChildren().get(0)); // should be set now
						IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
						if (updateFeature != null)
							updateFeature.update(updateContext); // change image
					} else if (element instanceof EndPoint && o instanceof EndPoint) {
						((EndPoint) o).setWinScreenTitle(((EndPoint) element).getWinScreenTitle());
						((EndPoint) o).setWinScreenText(((EndPoint) element).getWinScreenText());
					}
				}
			}
		}
		
		// restore selection
		getDiagramBehavior().getDiagramContainer().setPictogramElementsForSelection(currentSelection);
	}
	
	@Override
	public String getName() {
		return "Multiply element";
	}

}
