package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.features.impl.AbstractMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.ui.PlatformUI;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelEditor;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper;
import de.tu_bs.cs.isf.mbse.eggcubator.LevelPictogramHelper.ElementPosition;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IElementFeature;

public class ElementMoveFeature extends AbstractMoveShapeFeature implements IElementFeature {

	public ElementMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		PictogramElement[] currentSelection = getDiagramBehavior().getDiagramContainer().getSelectedPictogramElements();
		if (currentSelection == null || currentSelection.length > 1) // only one element can be moved at a time
			return false;
		
		if (!(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof LevelEditor)) // only our editor is usable
			return false;

		PictogramLink sourceLink = context.getPictogramElement().getLink();
		if (sourceLink == null || sourceLink.getBusinessObjects().size() != 1 || // only PlacedElements
				!(sourceLink.getBusinessObjects().get(0) instanceof PlacedElement))
			return false;
		
		if (context.getSourceContainer().getContainer().getLink() == null ||
				context.getSourceContainer().getContainer().getLink().getBusinessObjects().size() != 1 ||
				!(context.getSourceContainer().getContainer().getLink().getBusinessObjects().get(0) instanceof Level))
			return false;

		// sadly I don't understand the coords given by the context. They also seem to ignore zoom and scroll bars
		// so here is my own mouse position relative to the diagram
		Point p = ((DiagramBehavior) getDiagramBehavior()).getMouseLocation();
		LevelEditor editor = (LevelEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Viewport viewport = ((FigureCanvas) editor.getGraphicalViewer().getControl()).getViewport();
		double zoom = ((DiagramBehavior) getDiagramBehavior()).getZoomLevel();
		int mouseX = (int) ((p.x + viewport.getHorizontalRangeModel().getValue()) / zoom);
		int mouseY = (int) ((p.y + viewport.getVerticalRangeModel().getValue()) / zoom);

		Level level = (Level) context.getSourceContainer().getContainer().getLink().getBusinessObjects().get(0);
		
		int newXPos = (mouseX - LevelPictogramHelper.LEVEL_OFFSET_X - LevelPictogramHelper.LEVEL_BORDER_WIDTH) / level.getElementSize();
		int newYPos = (mouseY - LevelPictogramHelper.LEVEL_OFFSET_Y - LevelPictogramHelper.LEVEL_BORDER_WIDTH) / level.getElementSize();
		ElementPosition oldPos = new ElementPosition(context.getSourceContainer());
		
		if (context instanceof MoveShapeContext) { // ship these values to move function (faster than recalculation)
			//((MoveShapeContext) context).setX(xCoord); // doesn't effect the visual temporary shape :-(
			//((MoveShapeContext) context).setY(yCoord);
			
			((MoveShapeContext) context).putProperty("newXPos", newXPos);
			((MoveShapeContext) context).putProperty("newYPos", newYPos);
		} else
			return false; // not supported
		
		if (newXPos == oldPos.getX() && newYPos == oldPos.getY())
			return false;
		
		return context.getSourceContainer() == context.getTargetContainer() || // moving to empty element (wrongly recognized as same container)
				context.getTargetContainer().getLink() != null && // other element (replace)
				context.getTargetContainer().getLink().getBusinessObjects().size() == 1 &&
				context.getTargetContainer().getLink().getBusinessObjects().get(0) instanceof PlacedElement &&
				context.getTargetContainer().getContainer().getContainer() == context.getSourceContainer().getContainer();
	}

	@Override
	public void moveShape(IMoveShapeContext context) {
		// get new pos from canMove function
		int newXPos = -1, newYPos = -1;
		for (Object key : context.getPropertyKeys()) {
			if (key.equals("newXPos"))
				newXPos = (int) context.getProperty(key);
			else if (key.equals("newYPos"))
				newYPos = (int) context.getProperty(key);
		}
		if (newXPos == -1 || newYPos == -1)
			throw new IllegalStateException("Position not found");
		
		ContainerShape targetContainer = null;
		if (context.getSourceContainer() != context.getTargetContainer()) { // TODO replace or switch?
			targetContainer = context.getTargetContainer().getContainer();
			// replace -> remove old element
			IDeleteContext deleteContext = new DeleteContext(context.getTargetContainer());
			IDeleteFeature delFeature = getFeatureProvider().getDeleteFeature(deleteContext);
			if (delFeature == null)
				throw new IllegalStateException("There is no appropriate DeleteFeature available");
			delFeature.delete(deleteContext);
		} else {
			// search for target shape
			for (Shape shape : context.getSourceContainer().getContainer().getChildren()) {
				if (!(shape instanceof ContainerShape))
					continue;
				ElementPosition pos = new ElementPosition(shape);
				if (pos.getX() == newXPos && pos.getY() == newYPos) {
					targetContainer = (ContainerShape) shape;
					break;
				}
			}
		}
		if (targetContainer == null || targetContainer instanceof Diagram)
			throw new IllegalStateException("Invalid target container");
		
		Level level = (Level) context.getSourceContainer().getContainer().getLink().getBusinessObjects().get(0);
		PlacedElement element = (PlacedElement) context.getPictogramElement().getLink().getBusinessObjects().get(0);
	
		// remember selection
		PictogramElement[] currentSelection = getDiagramBehavior().getDiagramContainer().getSelectedPictogramElements();	
		// move
		LevelPictogramHelper.moveElementShape((Shape) context.getPictogramElement(), targetContainer, level);
		// restore selection
		getDiagramBehavior().getDiagramContainer().setPictogramElementsForSelection(currentSelection);
		
		// move business object
		level.removeElement(element);
		element.setPositionX(newXPos);
		element.setPositionY(newYPos);
		level.addElement(element);
	}

}
