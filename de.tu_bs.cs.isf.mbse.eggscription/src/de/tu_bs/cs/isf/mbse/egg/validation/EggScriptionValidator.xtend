/*
 * generated by Xtext 2.10.0
 */
package de.tu_bs.cs.isf.mbse.egg.validation

import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.CharacterPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.InventorySize
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.JumpPower
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.MaxLife
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.Speed
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.Strength
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.Consumable
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.ItemAttribute
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.ItemPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.ScorePoints
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.Usable
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AuxiliaryPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Duration
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.ItemDescription
import org.eclipse.xtext.validation.Check
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationAttribute
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationDescription
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Pictures
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.InventoryItemsTypes
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.HeroDescription
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.EnemyDescription
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.HeroAttribute
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.BlockAttribute
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.Movable
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.NoCollision
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.BlockDescription
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.BlockPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.Destroyable
import de.tu_bs.cs.isf.mbse.egg.descriptions.DescriptionRoot
import de.tu_bs.cs.isf.mbse.egg.descriptions.Description
import de.tu_bs.cs.isf.mbse.egg.descriptions.DescriptionsPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.GameelementsPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.InventoryItemsCounts

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class EggScriptionValidator extends AbstractEggScriptionValidator {
	
	@Check
	def checkMinimumMaxLife(MaxLife object) {
		if(object.value <= 0) {
			error('MaxLife should be larger than 0',
				CharacterPackage.Literals.MAX_LIFE__VALUE)
		}
	}
	
	@Check
	def checkSpeed(Speed object) {
		if(object.value <= 0) {
			error('Speed should be larger than 0',
				CharacterPackage.Literals.SPEED__VALUE)
		}
	}
	
	@Check
	def checkStrength(Strength object) {
		if(object.value <= 0) {
			error('Strength should be larger than 0',
				CharacterPackage.Literals.STRENGTH__VALUE)
		}
	}
	
	@Check
	def checkJump(JumpPower object) {
		if(object.value <= 0) {
			error('Jump power should be larger than 0',
				CharacterPackage.Literals.JUMP_POWER__VALUE)
		}
	}
	
	@Check
	def checkInventorySize(InventorySize object) {
		if(object.value < 0) {
			error('Inventory cannot be smaller than 0',
				CharacterPackage.Literals.INVENTORY_SIZE__VALUE)
		}
	}
	
	@Check
	def checkAnimationDuration(Duration object) {
		if(object.value <= 0) {
			error('Duration must be larger than 0',
				AuxiliaryPackage.Literals.DURATION__VALUE)
		}
	}
	
	@Check
	def checkUsableVsConsumable(ItemAttribute object) {
		if(object instanceof Usable || object instanceof Consumable) {
			for(ItemAttribute other : (object.eContainer as ItemDescription).properties) {
				if(other != object) {
					if((other instanceof Consumable && object instanceof Usable)
						|| (object instanceof Consumable && other instanceof Usable)) {
						if(object instanceof Consumable) {
							error('Items cannot be usable and consumable at the same time',
								ItemPackage.Literals.CONSUMABLE__VALUE)							
						} else {
							error('Items cannot be usable and consumable at the same time',
								ItemPackage.Literals.USABLE__VALUE)
						}
					}
				}
			}
		}
	}
	
	@Check
	def checkMovableVsNoCollision(BlockAttribute object) {
		if(object instanceof Movable || object instanceof NoCollision) {
			for(BlockAttribute other : (object.eContainer as BlockDescription).properties) {
				if(other != object) {
					if((other instanceof Movable && object instanceof NoCollision)
						|| (object instanceof Movable && other instanceof NoCollision)) {
						if(object instanceof Movable) {
							error('Blocks cannot be movable and have no collision at the same time',
								BlockPackage.Literals.MOVABLE__VALUE)							
						} else {
							error('Blocks cannot be movable and have no collision at the same time',
								BlockPackage.Literals.NO_COLLISION__VALUE)
						}
					}
				}
			}
		}
	}
	
	@Check
	def checkDestoryableVsNoCollision(BlockAttribute object) {
		if(object instanceof Destroyable || object instanceof NoCollision) {
			for(BlockAttribute other : (object.eContainer as BlockDescription).properties) {
				if(other != object) {
					if((other instanceof Destroyable && object instanceof NoCollision)
						|| (object instanceof Destroyable && other instanceof NoCollision)) {
						if(object instanceof Destroyable) {
							error('Blocks cannot be destroyable and have no collision at the same time',
								BlockPackage.Literals.DESTROYABLE__VALUE)							
						} else {
							error('Blocks cannot be destroyable and have no collision at the same time',
								BlockPackage.Literals.NO_COLLISION__VALUE)
						}
					}
				}
			}
		}
	}
	
	@Check
	def checkNumberOfItemAttributes(ItemAttribute object) {
		if(object.eContainer instanceof ItemDescription) {
			for(ItemAttribute other : (object.eContainer as ItemDescription).properties) {
				if(other != object) {
					if(other.eClass == object.eClass) {
						var errortext = 'Attribute ' 
						var errortext2 = ''
						var errortext3 = ' should appear only once per Item object'
						var literal = ItemPackage.Literals.CONSUMABLE__VALUE
						var founderror = false
						if(object instanceof Consumable) {
							literal = ItemPackage.Literals.CONSUMABLE__VALUE
							errortext2 = 'consumable'
							founderror = true
						} else if (object instanceof Usable) {
							literal = ItemPackage.Literals.USABLE__VALUE
							errortext2 = 'usable'
							founderror = true
						} else if (object instanceof ScorePoints) {
							literal = ItemPackage.Literals.SCORE_POINTS__VALUE
							errortext2 = 'scorePoints'
							founderror = true
						} 
						if(founderror){
							error(errortext+errortext2+errortext3, object, literal)
						}				
					}
				}
			}
		}
	}
	
	@Check
	def checkNumberOfAnimationAttributes(AnimationAttribute object) {
		if(object.eContainer instanceof AnimationDescription) {
			for(AnimationAttribute other : (object.eContainer as AnimationDescription).properties) {
				if(other != object && other.eClass == object.eClass) {
					var errortext = 'Attribute ' 
					var errortext2 = ''
					var errortext3 = ' should appear only once per Animation object'
					var literal = AuxiliaryPackage.Literals.DURATION__VALUE
					var founderror = false
					if(object instanceof Duration) {
						literal = AuxiliaryPackage.Literals.DURATION__VALUE
						errortext2 = 'duration'
						founderror = true
					} else if (object instanceof Pictures) {
						literal = AuxiliaryPackage.Literals.PICTURES__VALUE
						errortext2 = 'pictures'
						founderror = true
					}
					if(founderror){
							error(errortext+errortext2+errortext3, object, literal)
					}	
				}
			}
		}	
	}

	@Check
	def checkNumberOfHerosMin(DescriptionRoot root) {
		var count = 0
		for(Description object : root.descriptions) {
			if(object instanceof HeroDescription) {
				count++
			}
		}
		if(count < 1) {
			error('There must be exactly one hero per game', DescriptionsPackage.Literals.DESCRIPTION_ROOT__DESCRIPTIONS)
		}
	}
	
	@Check
	def checkNumberOfHerosMax(HeroDescription object) {
		var root = (object.eContainer as DescriptionRoot)
		var count = 0
		for(Description other : root.descriptions) {
			if(other instanceof HeroDescription) {
				count++
			}
		}
		if(count > 1) {
			error('There must not be more than one hero', DescriptionsPackage.Literals.DESCRIPTION__NAME, 1)
		}
	}

	@Check
	def checkUniqueIdentifiers(Description object) {
		var root = (object.eContainer as DescriptionRoot)
		for(Description other : root.descriptions) {
			if(other.name == object.name && object != other) {
				error('There are multiple objects named '+ object.name, DescriptionsPackage.Literals.DESCRIPTION__NAME)
			}
		}
	}
	
	@Check
	def checkInventorySizesHero(InventoryItemsTypes object) {
		if(!(object.eContainer instanceof HeroDescription)) return
		
		var character = object.eContainer as HeroDescription
		
		var foundTypes = false
		var foundCounts = false
		var attribute_types = character.properties.get(0);
		var attribute_counts = character.properties.get(0);
		for(HeroAttribute attribute : character.properties) {
			if(attribute instanceof InventoryItemsTypes) {
				foundTypes = true;
				attribute_types = attribute
			}
			if(attribute instanceof InventoryItemsCounts) {
				foundCounts = true;
				attribute_counts = attribute
			}
		}
		if(foundTypes && foundCounts) {
			if((attribute_types as InventoryItemsTypes).value.size() != (attribute_counts as InventoryItemsCounts).value.size()) {
				error('There must be the same number of types and counts', CharacterPackage.Literals.INVENTORY_ITEMS_TYPES__VALUE)
			}
		}
	}
	
	@Check
	def checkInventorySizesHero(InventoryItemsCounts object) {
		if(!(object.eContainer instanceof HeroDescription)) return
		
		var character = object.eContainer as HeroDescription
		
		var foundTypes = false
		var foundCounts = false
		var attribute_types = character.properties.get(0);
		var attribute_counts = character.properties.get(0);
		for(HeroAttribute attribute : character.properties) {
			if(attribute instanceof InventoryItemsTypes) {
				foundTypes = true;
				attribute_types = attribute
			}
			if(attribute instanceof InventoryItemsCounts) {
				foundCounts = true;
				attribute_counts = attribute
			}
		}
		if(foundTypes && foundCounts) {
			if((attribute_types as InventoryItemsTypes).value.size() != (attribute_counts as InventoryItemsCounts).value.size()) {
				error('There must be the same number of types and counts', CharacterPackage.Literals.INVENTORY_ITEMS_COUNTS__VALUE)
			}
		}
	}
}
