package de.tu_bs.cs.isf.mbse.eggcubator.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.impl.DefaultRemoveFeature;

import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;

public class ElementRemoveFeature extends DefaultRemoveFeature implements IElementFeature {

	public ElementRemoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canRemove(IRemoveContext context) {
		return context instanceof InternalRemoveContext && super.canRemove(context) && 
				context.getPictogramElement().getLink() != null &&
				context.getPictogramElement().getLink().getBusinessObjects().size() == 1 &&
				context.getPictogramElement().getLink().getBusinessObjects().get(0) instanceof PlacedElement;
	}
	
}
