package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.level.Elements.ElementsFactory;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.EndPoint;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IEggBlockFeature;

public class EndPointCreateFeaute extends AbstractElementCreateFeature implements IEggBlockFeature {

	public EndPointCreateFeaute(IFeatureProvider fp) {
		super(fp, "End Point");
	}
	
	@Override
	protected EndPoint createInstanceWithDescription() {
		return ElementsFactory.eINSTANCE.createEndPoint();
	}
	
	// TODO temporary file as icon?
	/*@Override
	public String getCreateImageId() {
		return EggImageProvider.IMG_END_POINT_ID;
	}*/

}
