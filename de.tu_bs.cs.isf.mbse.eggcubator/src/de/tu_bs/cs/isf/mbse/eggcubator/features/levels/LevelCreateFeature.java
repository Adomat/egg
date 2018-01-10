package de.tu_bs.cs.isf.mbse.eggcubator.features.levels;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;

import de.tu_bs.cs.isf.mbse.eggcubator.features.ILevelFeature;
import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.egg.level.LevelFactory;

// TODO this should be done on diagram creation instead, but I have no idea how. Wizards seem to complex...
public class LevelCreateFeature extends AbstractCreateFeature implements ILevelFeature {

	public LevelCreateFeature(IFeatureProvider fp) {
		super(fp, "Create Level", "Create a new and empty level (only in empty diagrams).");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram &&
				(context.getTargetContainer().getChildren() == null ||
				context.getTargetContainer().getChildren().isEmpty());
	}

	@Override
	public Object[] create(ICreateContext context) {
		Level level = LevelFactory.eINSTANCE.createLevel();

		Resource resource = context.getTargetContainer().eResource();
		resource.getContents().add(level);

		// Delegate to the add feature
		addGraphicalRepresentation(context, level);
		return new Object[] { level };
	}

}
