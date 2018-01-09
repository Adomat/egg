package de.tu_bs.cs.isf.mbse.eggcubator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.palette.IObjectCreationToolEntry;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.IStackToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.palette.impl.PaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.StackEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;

public class LevelToolBehaviorProvider extends DefaultToolBehaviorProvider {

	public LevelToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	public IPaletteCompartmentEntry[] getPalette() {
		List<IPaletteCompartmentEntry> compartments = new ArrayList<IPaletteCompartmentEntry>();

		// Create palette structure
		// TODO icons?
		PaletteCompartmentEntry blocksEntry = new PaletteCompartmentEntry("Blocks", null);
		PaletteCompartmentEntry itemsEntry = new PaletteCompartmentEntry("Items", null);
		PaletteCompartmentEntry enemiesEntry = new PaletteCompartmentEntry("Enemies", null);
		PaletteCompartmentEntry levelEntry = new PaletteCompartmentEntry("Level", null);
		compartments.add(blocksEntry);
		compartments.add(itemsEntry);
		compartments.add(enemiesEntry);
		compartments.add(levelEntry);

		IFeatureProvider featureProvider = getFeatureProvider();		
		ICreateFeature[] createFeatures = featureProvider.getCreateFeatures();
		if (createFeatures.length > 0) {
			for (ICreateFeature createFeature : createFeatures) {
				// TODO class hierarchy for create features
				/*ObjectCreationToolEntry ocTool = new ObjectCreationToolEntry(
						createFeature.getCreateName(), createFeature.getCreateDescription(),
						createFeature.getCreateImageId(), createFeature.getCreateLargeImageId(), createFeature);
				entry.addToolEntry(ocTool);*/
			}
		}
		
		IPaletteCompartmentEntry[] res = compartments.toArray(new IPaletteCompartmentEntry[compartments.size()]);
		return res;
	}
	
}
