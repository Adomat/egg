/*
 * generated by Xtext 2.10.0
 */
package de.tu_bs.cs.isf.mbse.egg.validation

import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.MaxLife
import org.eclipse.xtext.validation.Check
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.CharacterPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.Speed
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.Strength
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.JumpPower
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.InventorySize
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Duration
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AuxiliaryPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.ItemDescription
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.ItemAttribute
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.Consumable
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.Usable
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.GameelementsPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.ItemPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.ScorePoints
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationDescription
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.AttributesPackage
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.HeroAttribute
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.HeroDescription

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
	def checkNumberOfItemAttributes(ItemAttribute object) {
		if(object.eContainer instanceof ItemDescription) {
			for(ItemAttribute other : (object.eContainer as ItemDescription).properties) {
				if(other != object) {
					if(other.eClass == object.eClass) {
						var errortext = 'Attribute ' 
						var errortext2 = ''
						var errortext3 = ' should appear only once per object'
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
	
//	@Check
//	def checkNumberOfHeroAttributes(HeroAttribute object) {
//		if(object.eContainer instanceof HeroDescription) {
//			for(HeroAttribute other : (object.eContainer as HeroDescription).properties) {
//				if(other != object) {
//					if(other.eClass == object.eClass) {
//						var errortext = 'Attribute ' 
//						var errortext2 = ''
//						var errortext3 = ' should appear only once per object'
//						var literal = ItemPackage.Literals.CONSUMABLE__VALUE
//						var founderror = false
//						if(object instanceof Consumable) {
//							literal = ItemPackage.Literals.CONSUMABLE__VALUE
//							errortext2 = 'consumable'
//							founderror = true
//						} else if (object instanceof Usable) {
//							literal = ItemPackage.Literals.USABLE__VALUE
//							errortext2 = 'usable'
//							founderror = true
//						} else if (object instanceof ScorePoints) {
//							literal = ItemPackage.Literals.SCORE_POINTS__VALUE
//							errortext2 = 'scorePoints'
//							founderror = true
//						} 
//						if(founderror){
//							error(errortext+errortext2+errortext3, object, literal)
//						}				
//					}
//				}
//			}
//		}
//	}

}
