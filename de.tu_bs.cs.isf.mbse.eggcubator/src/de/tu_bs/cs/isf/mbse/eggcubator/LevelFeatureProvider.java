package de.tu_bs.cs.isf.mbse.eggcubator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICopyFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IPasteFeature;
import org.eclipse.graphiti.features.IPrintFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.ISaveImageFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICopyContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IPasteContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.AbstractFeatureProvider;

import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelAddFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelLayoutFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.levels.LevelResizeFeature;
import de.tu_bs.isf.cs.mbse.egg.level.Level;

public class LevelFeatureProvider extends AbstractFeatureProvider {

	private ICreateFeature[] createFeatures = new ICreateFeature[] { new LevelCreateFeature(this) };
	private LevelAddFeature levelAddFeature = new LevelAddFeature(this);
	private LevelResizeFeature levelResizeFeature = new LevelResizeFeature(this);
	private LevelLayoutFeature levelLayoutFeature = new LevelLayoutFeature(this);
	
	public LevelFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return createFeatures;
	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		if (context.getNewObject() instanceof Level)
			return levelAddFeature;
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
