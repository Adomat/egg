package de.tu_bs.cs.isf.mbse.eggcubator;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

public class LevelDiagramProvider extends AbstractDiagramTypeProvider {

	private IToolBehaviorProvider[] toolBehaviorProviders;
	
	public LevelDiagramProvider() {
		super();
		setFeatureProvider(new LevelFeatureProvider(this));
	}

	@Override
	public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
		if (toolBehaviorProviders == null) {
			toolBehaviorProviders = new IToolBehaviorProvider[] { new LevelToolBehaviorProvider(this) };
		}
		return toolBehaviorProviders;
	}
	
	@Override
	public boolean isAutoUpdateAtStartup() {
		return true; // to reload images
	}
}
