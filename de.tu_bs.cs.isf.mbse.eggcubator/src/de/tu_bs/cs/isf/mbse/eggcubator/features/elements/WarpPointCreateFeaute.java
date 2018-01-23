package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.level.Elements.ElementsFactory;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.WarpPoint;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IEggBlockFeature;

public class WarpPointCreateFeaute extends AbstractElementCreateFeature implements IEggBlockFeature {

	public WarpPointCreateFeaute(IFeatureProvider fp) {
		super(fp, "Warp Point");
	}
	
	@Override
	protected WarpPoint createInstanceWithDescription() {
		return ElementsFactory.eINSTANCE.createWarpPoint();
	}
	
	// TODO temporary file as icon?
	/*@Override
	public String getCreateImageId() {
		return EggImageProvider.IMG_WARP_POINT_ID;
	}*/

}
