package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.BlockDescription;
import de.tu_bs.cs.isf.mbse.egg.level.LevelFactory;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedBlock;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IEggBlockFeature;

public class EggBlockCreateFeature extends AbstractElementCreateFeature implements IEggBlockFeature {
	
	protected BlockDescription blockDescription;

	public EggBlockCreateFeature(IFeatureProvider fp, BlockDescription blockDesc) {
		super(fp, blockDesc);
		blockDescription = blockDesc;
	}
	
	@Override
	protected PlacedBlock createInstanceWithDescription() {
		PlacedBlock block = LevelFactory.eINSTANCE.createPlacedBlock();
		block.setProperties(blockDescription);
		return block;
	}
}
