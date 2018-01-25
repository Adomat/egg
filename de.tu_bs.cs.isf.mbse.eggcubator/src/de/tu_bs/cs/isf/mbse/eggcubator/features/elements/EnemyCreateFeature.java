package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.EnemyDescription;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.ElementsFactory;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.PlacedEnemy;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IEnemyFeature;

public class EnemyCreateFeature extends AbstractElementCreateFeature implements IEnemyFeature {
	
	protected EnemyDescription enemyDescription;

	public EnemyCreateFeature(IFeatureProvider fp, EnemyDescription enemyDesc) {
		super(fp, enemyDesc);
		enemyDescription = enemyDesc;
	}

	@Override
	protected PlacedEnemy createInstanceWithDescription() {
		PlacedEnemy enemy = ElementsFactory.eINSTANCE.createPlacedEnemy();
		enemy.setProperties(enemyDescription);
		return enemy;
	}
	
	@Override
	public boolean createsElement(PlacedElement element) {
		return element instanceof PlacedEnemy && ((PlacedEnemy) element).getProperties() != null &&
				((PlacedEnemy) element).getProperties().getName() != null &&
				((PlacedEnemy) element).getProperties().getName().equals(enemyDescription.getName());
	}

	// TODO temporary file as icon?
	/*@Override
	public String getCreateImageId() {
		return EggImageProvider.getImageId(enemyDescription);
	}*/
}
