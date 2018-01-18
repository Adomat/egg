package de.tu_bs.cs.isf.mbse.eggcubator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICopyFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
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
import org.eclipse.graphiti.features.context.IPasteContext;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.descriptions.Description;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.BlockDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.EnemyDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.ItemDescription;
import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.eggcubator.features.ElementAddFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.ElementRemoveFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.EggBlockCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.ElementDeleteFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.EnemyCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.elements.ItemCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelAddFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelLayoutFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelResizeFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelUpdateFeature;

public class LevelFeatureProvider extends AbstractFeatureProvider {

	private boolean createFeaturesInitialized = false;
	private ICreateFeature[] createFeatures;
	private LevelAddFeature levelAddFeature = new LevelAddFeature(this);
	private ElementAddFeature elementAddFeature = new ElementAddFeature(this);
	private LevelResizeFeature levelResizeFeature = new LevelResizeFeature(this);
	private LevelLayoutFeature levelLayoutFeature = new LevelLayoutFeature(this);
	private LevelUpdateFeature levelUpdateFeature = new LevelUpdateFeature(this);
	private IDeleteFeature elementDeleteFeature = new ElementDeleteFeature(this);
	private IRemoveFeature elementRemoveFeature = new ElementRemoveFeature(this);
	
	public LevelFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	protected void initializeCreateFeatures() {
		Collection<Description> descriptions = EggScriptionLoader.getDescriptions();
		List<ICreateFeature> features = new ArrayList<>();
		if (descriptions != null) { // Descriptions maybe not ready yet
			for (Description desc : descriptions) {
				if (desc instanceof BlockDescription)
					features.add(new EggBlockCreateFeature(this, (BlockDescription) desc));
				else if (desc instanceof ItemDescription)
					features.add(new ItemCreateFeature(this, (ItemDescription) desc));
				else if (desc instanceof EnemyDescription)
					features.add(new EnemyCreateFeature(this, (EnemyDescription) desc));
				else
					System.err.println("[WARN] Unknown description type: " + desc.getClass().getName());
			}
			createFeaturesInitialized = true;
		}
		// TODO pre defined blocks
		features.add(new LevelCreateFeature(this));
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
		if (levelAddFeature.canAdd(context))
			return levelAddFeature;
		else if (elementAddFeature.canAdd(context))
			return elementAddFeature;
		return null;
	}
	
	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		EList<EObject> bos = context.getShape().getLink().getBusinessObjects();
		if (bos.size() == 1 && bos.get(0) instanceof Level)
			return levelResizeFeature;
		return null;
	}
	
	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		EList<EObject> bos = context.getPictogramElement().getLink().getBusinessObjects();
		if (bos.size() == 1 && bos.get(0) instanceof Level)
			return levelLayoutFeature;
		return null;
	}
	
	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		if (context.getPictogramElement() == null || context.getPictogramElement().getLink() == null)
			return null;
		EList<EObject> bos = context.getPictogramElement().getLink().getBusinessObjects();
		if (bos.size() == 1 && bos.get(0) instanceof Level)
			return levelUpdateFeature;
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
