package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.ItemDescription;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.ElementsFactory;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.PlacedItem;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IItemFeature;

public class ItemCreateFeature extends AbstractElementCreateFeature implements IItemFeature {
	
	protected ItemDescription itemDescription;


	public ItemCreateFeature(IFeatureProvider fp, ItemDescription itemDesc) {
		super(fp, itemDesc);
		itemDescription = itemDesc;
	}

	@Override
	protected PlacedItem createInstanceWithDescription() {
		PlacedItem item = ElementsFactory.eINSTANCE.createPlacedItem();
		item.setProperties(itemDescription);
		return item;
	}
	
	@Override
	public boolean createsElement(PlacedElement element) {
		return element instanceof PlacedItem && ((PlacedItem) element).getProperties() != null &&
				((PlacedItem) element).getProperties().getName() != null &&
				((PlacedItem) element).getProperties().getName().equals(itemDescription.getName());
	}
	
	// TODO temporary file as icon?
	/*@Override
	public String getCreateImageId() {
		return EggImageProvider.getImageId(itemDescription);
	}*/

}
