package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.BlockDescription;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.ElementsFactory;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.PlacedBlock;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IUserBlockFeature;

public class UserBlockCreateFeature extends AbstractElementCreateFeature implements IUserBlockFeature {
	
	protected BlockDescription blockDescription;

	public UserBlockCreateFeature(IFeatureProvider fp, BlockDescription blockDesc) {
		super(fp, blockDesc);
		blockDescription = blockDesc;
	}
	
	@Override
	protected PlacedBlock createInstanceWithDescription() {
		PlacedBlock block = ElementsFactory.eINSTANCE.createPlacedBlock();
		block.setProperties(blockDescription);
		return block;
	}
	
	@Override
	public boolean createsElement(PlacedElement element) {
		return element instanceof PlacedBlock && ((PlacedBlock) element).getProperties() != null &&
				((PlacedBlock) element).getProperties().getName() != null &&
				((PlacedBlock) element).getProperties().getName().equals(blockDescription.getName());
	}
	
	// TODO temporary file as icon?
	/*@Override
	public String getCreateImageId() {
		return EggImageProvider.getImageId(blockDescription);
	}*/
}
