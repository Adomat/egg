package de.tu_bs.cs.isf.mbse.eggcubator.features.elements;

import org.eclipse.graphiti.features.IFeatureProvider;

import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.EnemyDescription;
import de.tu_bs.cs.isf.mbse.egg.level.LevelFactory;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedEnemy;
import de.tu_bs.cs.isf.mbse.eggcubator.features.AbstractElementCreateFeature;
import de.tu_bs.cs.isf.mbse.eggcubator.features.IEnemyFeature;

public class EnemyCreateFeature extends AbstractElementCreateFeature implements IEnemyFeature {
	
	protected EnemyDescription enemyDescription;

	public EnemyCreateFeature(IFeatureProvider fp, EnemyDescription enemyDesc) {
		super(fp, enemyDesc);
		enemyDescription = enemyDesc;
	}

	@Override
	protected PlacedEnemy createInstanceWithDescription() {
		PlacedEnemy enemy = LevelFactory.eINSTANCE.createPlacedEnemy();
		enemy.setProperties(enemyDescription);
		return enemy;
	}
}
