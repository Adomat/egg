package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IElementFeature;

public class ElementDeleteFeature extends DefaultDeleteFeature implements IElementFeature {

	public ElementDeleteFeature(IFeatureProvider fp) {
		super(fp);
		setDoneChanges(true);
	}

	@Override
	public boolean canDelete(IDeleteContext context) {
		if (!(context.getPictogramElement() instanceof Shape))
			return false;
		Shape shape = (Shape) context.getPictogramElement();
		return super.canDelete(context) && shape.getLink() != null &&
						shape.getLink().getBusinessObjects().size() == 1 &&
						shape.getLink().getBusinessObjects().get(0) instanceof PlacedElement &&
						shape.getContainer().getContainer() != null &&
						shape.getContainer().getContainer().getLink() != null &&
						shape.getContainer().getContainer().getLink().getBusinessObjects().size() == 1 &&
						shape.getContainer().getContainer().getLink().getBusinessObjects().get(0) instanceof Level;
	}
	
	@Override
	public void delete(IDeleteContext context) {
		Shape shape = (Shape) context.getPictogramElement();
		PlacedElement element = (PlacedElement) shape.getLink().getBusinessObjects().get(0);
		Level level = (Level) shape.getContainer().getContainer().getLink().getBusinessObjects().get(0);
		
		// pass to remove feature
		IRemoveContext rc = new RemoveContext(shape);
		IRemoveFeature removeFeature = getFeatureProvider().getRemoveFeature(rc);
		removeFeature.remove(rc);
		
		level.removeElement(element);
	}
	
}
