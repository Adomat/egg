package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

import de.tu_bs.cs.isf.mbse.egg.level.Elements.WarpPoint;
import de.tu_bs.cs.isf.mbse.eggcubator.EggImageProvider;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IEggBlockFeature;

public class WarpPointUpdateFeature extends AbstractUpdateFeature implements IEggBlockFeature {

	public WarpPointUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return context.getPictogramElement() instanceof ContainerShape && context.getPictogramElement().getLink() != null &&
				context.getPictogramElement().getLink().getBusinessObjects() != null &&
				context.getPictogramElement().getLink().getBusinessObjects().size() == 1 &&
				context.getPictogramElement().getLink().getBusinessObjects().get(0) instanceof WarpPoint;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		WarpPoint warpPoint = (WarpPoint) context.getPictogramElement().getLink().getBusinessObjects().get(0);
		for(Shape shape : ((ContainerShape) context.getPictogramElement()).getChildren()) { // check if image is up to date
			if (!(shape.getGraphicsAlgorithm() instanceof Image))
				continue;
			Image image = (Image) shape.getGraphicsAlgorithm();
			String id = EggImageProvider.getImageId(warpPoint);
			if (!image.getId().equals(id))
				return Reason.createTrueReason("Image changed");
			break;
		}
		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		WarpPoint warpPoint = (WarpPoint) context.getPictogramElement().getLink().getBusinessObjects().get(0);
		for(Shape shape : ((ContainerShape) context.getPictogramElement()).getChildren()) { // check if image is up to date
			if (!(shape.getGraphicsAlgorithm() instanceof Image))
				continue;
			Image image = (Image) shape.getGraphicsAlgorithm();
			String id = EggImageProvider.getImageId(warpPoint);
			image.setId(id);
			return true;
		}
		return false;
	}

}
