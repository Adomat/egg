package de.tu_bs.cs.isf.mbse.eggcubator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICopyFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IPasteFeature;
import org.eclipse.graphiti.features.IPrintFeature;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.ISaveImageFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICopyContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IPasteContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.descriptions.Description;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.BlockDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.EnemyDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.ItemDescription;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.ElementAddFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.ElementDeleteFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.ElementInitialUpdateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.ElementMoveFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.ElementRemoveFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.EndPointCreateFeaute;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.EnemyCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.ItemCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.UserBlockCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.WarpPointCreateFeaute;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelAddFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelLayoutFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelResizeFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelUpdateFeature;

public class LevelFeatureProvider extends AbstractFeatureProvider {

	private boolean createFeaturesInitialized = false;
	private ICreateFeature[] createFeatures;
	
	private LevelAddFeature levelAddFeature = new LevelAddFeature(this);
	private LevelResizeFeature levelResizeFeature = new LevelResizeFeature(this);
	private LevelLayoutFeature levelLayoutFeature = new LevelLayoutFeature(this);
	private LevelUpdateFeature levelUpdateFeature = new LevelUpdateFeature(this);
	
	private ElementAddFeature elementAddFeature = new ElementAddFeature(this);
	private ElementDeleteFeature elementDeleteFeature = new ElementDeleteFeature(this);
	private ElementRemoveFeature elementRemoveFeature = new ElementRemoveFeature(this);
	private ElementMoveFeature elementMoveFeature = new ElementMoveFeature(this);
	private ElementInitialUpdateFeature elementInitialUpdateFeature = new ElementInitialUpdateFeature(this);
	
	public LevelFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	protected void initializeCreateFeatures() {
		Collection<Description> descriptions = EggScriptionLoader.getDescriptions();
		List<ICreateFeature> features = new ArrayList<>();
		if (descriptions != null) { // Descriptions maybe not ready yet
			for (Description desc : descriptions) {
				if (desc instanceof BlockDescription)
					features.add(new UserBlockCreateFeature(this, (BlockDescription) desc));
				else if (desc instanceof ItemDescription)
					features.add(new ItemCreateFeature(this, (ItemDescription) desc));
				else if (desc instanceof EnemyDescription)
					features.add(new EnemyCreateFeature(this, (EnemyDescription) desc));
				else
					System.err.println("[WARN] Unknown description type: " + desc.getClass().getName());
			}
			createFeaturesInitialized = true;
		}

		features.add(new LevelCreateFeature(this));
		features.add(new WarpPointCreateFeaute(this));
		features.add(new EndPointCreateFeaute(this));
		
		createFeatures = features.toArray(new ICreateFeature[features.size()]);
	}
	
	@Override
	public ICreateFeature[] getCreateFeatures() {
		if (!createFeaturesInitialized)
			initializeCreateFeatures();
		return createFeatures;
	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		if (elementAddFeature.canAdd(context))
			return elementAddFeature;
		else if (levelAddFeature.canAdd(context))
			return levelAddFeature;
		return null;
	}
	
	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		if (levelResizeFeature.canResizeShape(context))
			return levelResizeFeature;
		return null;
	}
	
	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		if (levelLayoutFeature.canLayout(context))
			return levelLayoutFeature;
		return null;
	}
	
	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		if (levelUpdateFeature.canUpdate(context) && levelUpdateFeature.updateNeeded(context).toBoolean())
			return levelUpdateFeature;
		else if (elementInitialUpdateFeature.canUpdate(context) && elementInitialUpdateFeature.updateNeeded(context).toBoolean())
			return elementInitialUpdateFeature;
		return null;
	}
	
	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		if (elementDeleteFeature.canDelete(context))
			return elementDeleteFeature;
		return null;
	}
	
	@Override
	public IRemoveFeature getRemoveFeature(IRemoveContext context) {
		if (elementRemoveFeature.canRemove(context))
			return elementRemoveFeature;
		return null;
	}
	
	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		if (elementMoveFeature.canMoveShape(context))
			return elementMoveFeature;
		return null;
	}
	
	@Override
	public ICopyFeature getCopyFeature(ICopyContext context) {
		return null;
	}

	@Override
	public IPasteFeature getPasteFeature(IPasteContext context) {
		return null;
	}

	@Override
	public IPrintFeature getPrintFeature() {
		return null;
	}

	@Override
	public ISaveImageFeature getSaveImageFeature() {
		return null;
	}

}
