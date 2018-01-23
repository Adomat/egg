package de.tu_bs.cs.isf.mbse.eggcubator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.StackEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;

import de.tu_bs.cs.isf.mbse.eggcubator.features.IEggBlockFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IEnemyFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IItemFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.ILevelFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IUserBlockFeature;

public class LevelToolBehaviorProvider extends DefaultToolBehaviorProvider {

	public LevelToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	public IPaletteCompartmentEntry[] getPalette() {
		List<IPaletteCompartmentEntry> compartments = new ArrayList<IPaletteCompartmentEntry>();

		// Create palette structure
		PaletteCompartmentEntry blocksEntry = new PaletteCompartmentEntry("Blocks", null);
		PaletteCompartmentEntry itemsEntry = new PaletteCompartmentEntry("Items", null);
		PaletteCompartmentEntry enemiesEntry = new PaletteCompartmentEntry("Enemies", null);
		PaletteCompartmentEntry levelEntry = new PaletteCompartmentEntry("Level", null);
		compartments.add(blocksEntry);
		compartments.add(itemsEntry);
		compartments.add(enemiesEntry);
		compartments.add(levelEntry);
		StackEntry eggBlocks = new StackEntry("Egg blocks", "Pre defined blocks", null);
		blocksEntry.addToolEntry(eggBlocks);

		// add all creation features based on their marker interface
		IFeatureProvider featureProvider = getFeatureProvider();		
		ICreateFeature[] createFeatures = featureProvider.getCreateFeatures();
		if (createFeatures.length > 0) {
			for (ICreateFeature createFeature : createFeatures) {
				ObjectCreationToolEntry ocTool = new ObjectCreationToolEntry(
						createFeature.getCreateName(), createFeature.getCreateDescription(),
						createFeature.getCreateImageId(), createFeature.getCreateLargeImageId(), createFeature);
				if (createFeature instanceof IEggBlockFeature)
					eggBlocks.addCreationToolEntry(ocTool);
				else if (createFeature instanceof IUserBlockFeature)
					blocksEntry.addToolEntry(ocTool);
				else if (createFeature instanceof IItemFeature)
					itemsEntry.addToolEntry(ocTool);
				else if (createFeature instanceof IEnemyFeature)
					enemiesEntry.addToolEntry(ocTool);
				else if (createFeature instanceof ILevelFeature)
					levelEntry.addToolEntry(ocTool);
			}
		}
		
		IPaletteCompartmentEntry[] res = compartments.toArray(new IPaletteCompartmentEntry[compartments.size()]);
		return res;
	}
	
	@Override
	public boolean isConnectionSelectionEnabled() {
		return false; // we don't use this
	}
	
}
