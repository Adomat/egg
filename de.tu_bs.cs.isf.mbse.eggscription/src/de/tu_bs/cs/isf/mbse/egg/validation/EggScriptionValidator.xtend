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
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.InventoryItemsTypes
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.HeroDescription
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
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.InventoryItemsCounts
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.UniqueAttribute
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.EnemyDescription
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.EnemyAttribute
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.IdleAnimation
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.RunAnimation
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.JumpAnimation
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.MeleeAttackAnimation
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.RangeAttackAnimation
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.RangeAttackEnabled
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.IntelligenceLevel
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Pictures

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class EggScriptionValidator extends AbstractEggScriptionValidator {
	
	/**
	 * Max Life must be larger than 0
	 */
	@Check
	def checkMinimumMaxLife(MaxLife object) {
		if(object.value <= 0) {
			error('MaxLife should be larger than 0',
				CharacterPackage.Literals.MAX_LIFE__VALUE)
		}
	}
	
	/**
	 * Speed must be larger than 0
	 */
	@Check
	def checkSpeed(Speed object) {
		if(object.value <= 0) {
			error('Speed should be larger than 0',
				CharacterPackage.Literals.SPEED__VALUE)
		}
	}
	
	/**
	 * Strength must be larger than 0
	 */
	@Check
	def checkStrength(Strength object) {
		if(object.value <= 0) {
			error('Strength should be larger than 0',
				CharacterPackage.Literals.STRENGTH__VALUE)
		}
	}
	
	/**
	 * JumpPower must be larger than or equal to 0
	 */
	@Check
	def checkJump(JumpPower object) {
		if(object.value < 0) {
			error('Jump power should be larger than or equal to 0',
				CharacterPackage.Literals.JUMP_POWER__VALUE)
		}
	}
	
	/**
	 * Inventory size must be 0 or larger
	 */
	@Check
	def checkInventorySize(InventorySize object) {
		if(object.value < 0) {
			error('Inventory cannot be smaller than 0',
				CharacterPackage.Literals.INVENTORY_SIZE__VALUE)
		}
	}
	
	/**
	 * Animation duration must be longer than 0
	 */
	@Check
	def checkAnimationDuration(Duration object) {
		if(object.value <= 0) {
			error('Duration must be larger than 0',
				AuxiliaryPackage.Literals.DURATION__VALUE)
		}
	}
	
	/**
	 * Items cannot be usable and consumable at the same time
	 */
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
	
	/**
	 * Blocks can not be movable and have no collision at the same time
	 */
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
	
	/**
	 * Blocks cannot be destroyable and have no collision at the same time
	 */
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
	
	/**
	 * Check required attributes in ItemDescription (scorePoints, consumable/usable, Animation)
	 */
	@Check
	def checkExistenceOfItemAttributes(ItemDescription desc) {
		var foundConsumable = false;
		var foundUsable = false;
		var foundScorePoints = false;
		var foundAnimation = false;
		for(ItemAttribute attribute : desc.properties) {
			if(attribute instanceof Consumable) foundConsumable = true;
			if(attribute instanceof Usable) foundUsable = true;
			if(attribute instanceof ScorePoints) foundScorePoints = true;
			if(attribute instanceof AnimationDescription) foundAnimation = true;
		}
		if(!foundAnimation) {
			warning('Could not find attribute Animation in item  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundUsable && !foundConsumable) {
			warning('Could neither find attribute usable not consumable in item  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundScorePoints) {
			warning('Could not find attribute scorePoints in item  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
	}
	
	/**
	 * Check required attributes in ItemDescription (scorePoints, consumable/usable, Animation)
	 */
	@Check
	def checkExistenceOfBlockAttributes(BlockDescription desc) {
		var foundAnimation = false;
		for(BlockAttribute attribute : desc.properties) {
			if(attribute instanceof AnimationDescription) foundAnimation = true;
		}
		if(!foundAnimation) {
			warning('Could not find attribute Animation in block  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
	}
	
	/**
	 * Check required attributes in ItemDescription (scorePoints, consumable/usable, Animation)
	 */
	@Check
	def checkExistenceOfAnimationAttributes(AnimationDescription desc) {
		var foundDuration = false;
		var foundPictures = false;
		for(AnimationAttribute attribute : desc.properties) {
			if(attribute instanceof Duration) foundDuration = true;
			if(attribute instanceof Pictures) foundPictures = true;
		}
		if(!foundDuration) {
			warning('Could not find attribute duration in  of the Animation', 
				desc.eClass.getEStructuralFeature(desc.toString));
		}
		if(!foundPictures) {
			warning('Could not find attribute pictures in  of the Animation', 
				desc.eClass.getEStructuralFeature(desc.toString));
		}
	}
	
	/**
	 * Check required attributes in HeroDescription (speed, jumpPower, maxLife, strength, idle-, run-, jumpAnimation,
	 * inventorySize, inventoryItemsTypes, inventoryItemsCounts, CloseAttackAnimation, canKillInDistance => DistanceAttackAnimation)
	 */
	@Check
	def checkExistenceOfHeroAttributes(HeroDescription desc) {
		var foundSpeed = false;
		var foundJumpPower = false;
		var foundMaxLife = false;
		var foundStrength = false;
		var foundIdleAni = false;
		var foundRunAni = false;
		var foundJumpAni = false;
		var foundCloseAni = false;
		var foundDisAni = false;
		var foundInvSize = false;
		var foundInvTypes = false;
		var foundInvCounts = false;
		var foundDistanceKiller = false;
		
		for(HeroAttribute attribute : desc.properties) {
			if(attribute instanceof Speed) foundSpeed = true;
			if(attribute instanceof JumpPower) foundJumpPower = true;
			if(attribute instanceof MaxLife) foundMaxLife = true;
			if(attribute instanceof Strength) foundStrength = true;
			if(attribute instanceof IdleAnimation) foundIdleAni = true;
			if(attribute instanceof RunAnimation) foundRunAni = true;
			if(attribute instanceof JumpAnimation) foundJumpAni = true;
			if(attribute instanceof MeleeAttackAnimation) foundCloseAni = true;
			if(attribute instanceof RangeAttackAnimation) foundDisAni = true;
			if(attribute instanceof InventorySize) foundInvSize = true;
			if(attribute instanceof InventoryItemsTypes) foundInvTypes = true;
			if(attribute instanceof InventoryItemsCounts) foundInvCounts = true;
			if(attribute instanceof RangeAttackEnabled) foundDistanceKiller = true;
		}
		if(!foundInvSize) {
			warning('Could not find attribute inventorySize in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if((foundInvTypes && !foundInvCounts) ||(!foundInvTypes && foundInvCounts)) {
			error('Found incomplete specification of inventory (types and counts) in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundSpeed) {
			warning('Could not find attribute speed in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundJumpPower) {
			warning('Could not find attribute jumpPower in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundMaxLife) {
			warning('Could not find attribute maxLife in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundStrength) {
			warning('Could not find attribute strength in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundIdleAni) {
			warning('Could not find attribute idle (Animation) in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundRunAni) {
			warning('Could not find attribute run (Animation) in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundJumpAni) {
			warning('Could not find attribute jump (Animation) in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundCloseAni) {
			warning('Could not find attribute melee (Animation) in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if((foundDisAni && !foundDistanceKiller) || (!foundDisAni && foundDistanceKiller)) {
			error('Range attack attributes incomplete in hero  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
	}
	
	/**
	 * Check required attributes in HeroDescription (speed, jumpPower, maxLife, strength, idle-, run-, jumpAnimation,
	 * inventorySize, inventoryItemsTypes, inventoryItemsCounts, CloseAttackAnimation, canKillInDistance => RangeAttackAnimation)
	 */
	@Check
	def checkExistenceOfEnemyAttributes(EnemyDescription desc) {
		var foundSpeed = false;
		var foundJumpPower = false;
		var foundMaxLife = false;
		var foundStrength = false;
		var foundIdleAni = false;
		var foundRunAni = false;
		var foundJumpAni = false;
		var foundCloseAni = false;
		var foundDisAni = false;
		var foundIQ = false;
		var foundInvTypes = false;
		var foundInvCounts = false;
		var foundDistanceKiller = false;
		
		for(EnemyAttribute attribute : desc.properties) {
			if(attribute instanceof Speed) foundSpeed = true;
			if(attribute instanceof JumpPower) foundJumpPower = true;
			if(attribute instanceof MaxLife) foundMaxLife = true;
			if(attribute instanceof Strength) foundStrength = true;
			if(attribute instanceof IdleAnimation) foundIdleAni = true;
			if(attribute instanceof RunAnimation) foundRunAni = true;
			if(attribute instanceof JumpAnimation) foundJumpAni = true;
			if(attribute instanceof MeleeAttackAnimation) foundCloseAni = true;
			if(attribute instanceof RangeAttackAnimation) foundDisAni = true;
			if(attribute instanceof IntelligenceLevel) foundIQ = true;
			if(attribute instanceof InventoryItemsTypes) foundInvTypes = true;
			if(attribute instanceof InventoryItemsCounts) foundInvCounts = true;
			if(attribute instanceof RangeAttackEnabled) foundDistanceKiller = true;
		}
		if(!foundIQ) {
			warning('Could not find attribute iq in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if((foundInvTypes && !foundInvCounts) ||(!foundInvTypes && foundInvCounts)) {
			error('Found incomplete specification of inventory (types and counts) in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundSpeed) {
			warning('Could not find attribute speed in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundJumpPower) {
			warning('Could not find attribute jumpPower in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundMaxLife) {
			warning('Could not find attribute maxLife in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundStrength) {
			warning('Could not find attribute strength in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundIdleAni) {
			warning('Could not find attribute idle (Animation) in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundRunAni) {
			warning('Could not find attribute run (Animation) in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundJumpAni) {
			warning('Could not find attribute jump (Animation) in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if(!foundCloseAni) {
			warning('Could not find attribute melee (Animation) in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
		if((foundDisAni && !foundDistanceKiller) || (!foundDisAni && foundDistanceKiller)) {
			error('Range attack attributes incomplete in enemy  \"' + desc.name.toString + '\"', 
				DescriptionsPackage.Literals.DESCRIPTION__NAME);
		}
	}
		
	/**
	 * Unique attributes must be existing exactly once
	 */
	@Check
	def checkUniquenessOfAttributes(UniqueAttribute inspectedAttribute) {
		var errorstr = 'Attribute ' + inspectedAttribute.eClass.name.toString + ' should appear only once per description.'
		if(inspectedAttribute.eContainer instanceof ItemDescription) {
			for(ItemAttribute compareAttribute : (inspectedAttribute.eContainer as ItemDescription).properties) {
				if(compareAttribute.eClass.equals(inspectedAttribute.eClass) && compareAttribute != inspectedAttribute) {
					error(errorstr, inspectedAttribute.eClass.getEStructuralFeature(inspectedAttribute.toString));
				}
			}
		}
		if(inspectedAttribute.eContainer instanceof BlockDescription) {
			for(BlockAttribute compareAttribute : (inspectedAttribute.eContainer as BlockDescription).properties) {
				if(compareAttribute.eClass.equals(inspectedAttribute.eClass) && compareAttribute != inspectedAttribute) {
					error(errorstr, inspectedAttribute.eClass.getEStructuralFeature(inspectedAttribute.toString));
				}
			}
		}
		if(inspectedAttribute.eContainer instanceof HeroDescription) {
			for(HeroAttribute compareAttribute : (inspectedAttribute.eContainer as HeroDescription).properties) {
				if(compareAttribute.eClass.equals(inspectedAttribute.eClass) && compareAttribute != inspectedAttribute) {
					error(errorstr, inspectedAttribute.eClass.getEStructuralFeature(inspectedAttribute.toString));
				}
			}
		}
		if(inspectedAttribute.eContainer instanceof EnemyDescription) {
			for(EnemyAttribute compareAttribute : (inspectedAttribute.eContainer as EnemyDescription).properties) {
				if(compareAttribute.eClass.equals(inspectedAttribute.eClass) && compareAttribute != inspectedAttribute) {
					error(errorstr, inspectedAttribute.eClass.getEStructuralFeature(inspectedAttribute.toString));
				}
			}
		}
		if(inspectedAttribute.eContainer instanceof AnimationDescription) {
			for(AnimationAttribute compareAttribute : (inspectedAttribute.eContainer as AnimationDescription).properties) {
				if(compareAttribute.eClass.equals(inspectedAttribute.eClass) && compareAttribute != inspectedAttribute) {
					error(errorstr, inspectedAttribute.eClass.getEStructuralFeature(inspectedAttribute.toString));
				}
			}
		}
	}

	/**
	 * Identifiers of the descriptions should be unique
	 */
	@Check
	def checkUniqueIdentifiers(Description object) {
		var root = (object.eContainer as DescriptionRoot)
		for(Description other : root.descriptions) {
			if(other.name == object.name && object != other) {
				error('There are multiple objects named '+ object.name, DescriptionsPackage.Literals.DESCRIPTION__NAME)
			}
		}
	}
	
	/**
	 * There must be as many types in the inventory as there are counts
	 */
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
	
	/**
	 * There must be as many types in the inventory as there are counts
	 */
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