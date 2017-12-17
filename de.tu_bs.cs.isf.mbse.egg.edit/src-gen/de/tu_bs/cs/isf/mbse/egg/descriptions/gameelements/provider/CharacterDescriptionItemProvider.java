/**
 */
package de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.provider;


import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AuxiliaryFactory;

import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.CharacterDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.GameelementsPackage;

import de.tu_bs.cs.isf.mbse.egg.descriptions.provider.DescriptionItemProvider;
import de.tu_bs.cs.isf.mbse.egg.descriptions.provider.DescriptionsEditPlugin;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.CharacterDescription} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CharacterDescriptionItemProvider extends DescriptionItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharacterDescriptionItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addInventoryItemsKindPropertyDescriptor(object);
			addInventoryItemsCountPropertyDescriptor(object);
			addSpeedPropertyDescriptor(object);
			addJumpPowerPropertyDescriptor(object);
			addMaxLifePropertyDescriptor(object);
			addStrengthPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Inventory Items Kind feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInventoryItemsKindPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharacterDescription_inventoryItemsKind_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharacterDescription_inventoryItemsKind_feature", "_UI_CharacterDescription_type"),
				 GameelementsPackage.Literals.CHARACTER_DESCRIPTION__INVENTORY_ITEMS_KIND,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Inventory Items Count feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInventoryItemsCountPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharacterDescription_inventoryItemsCount_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharacterDescription_inventoryItemsCount_feature", "_UI_CharacterDescription_type"),
				 GameelementsPackage.Literals.CHARACTER_DESCRIPTION__INVENTORY_ITEMS_COUNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharacterDescription_speed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharacterDescription_speed_feature", "_UI_CharacterDescription_type"),
				 GameelementsPackage.Literals.CHARACTER_DESCRIPTION__SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Jump Power feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addJumpPowerPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharacterDescription_jumpPower_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharacterDescription_jumpPower_feature", "_UI_CharacterDescription_type"),
				 GameelementsPackage.Literals.CHARACTER_DESCRIPTION__JUMP_POWER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Life feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxLifePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharacterDescription_maxLife_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharacterDescription_maxLife_feature", "_UI_CharacterDescription_type"),
				 GameelementsPackage.Literals.CHARACTER_DESCRIPTION__MAX_LIFE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Strength feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStrengthPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharacterDescription_strength_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharacterDescription_strength_feature", "_UI_CharacterDescription_type"),
				 GameelementsPackage.Literals.CHARACTER_DESCRIPTION__STRENGTH,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__IDLE_ANIMATION);
			childrenFeatures.add(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__RUN_ANIMATION);
			childrenFeatures.add(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__JUMP_ANIMATION);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns CharacterDescription.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CharacterDescription"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((CharacterDescription)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_CharacterDescription_type") :
			getString("_UI_CharacterDescription_type") + " " + label;
	}
	

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(CharacterDescription.class)) {
			case GameelementsPackage.CHARACTER_DESCRIPTION__INVENTORY_ITEMS_COUNT:
			case GameelementsPackage.CHARACTER_DESCRIPTION__SPEED:
			case GameelementsPackage.CHARACTER_DESCRIPTION__JUMP_POWER:
			case GameelementsPackage.CHARACTER_DESCRIPTION__MAX_LIFE:
			case GameelementsPackage.CHARACTER_DESCRIPTION__STRENGTH:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case GameelementsPackage.CHARACTER_DESCRIPTION__IDLE_ANIMATION:
			case GameelementsPackage.CHARACTER_DESCRIPTION__RUN_ANIMATION:
			case GameelementsPackage.CHARACTER_DESCRIPTION__JUMP_ANIMATION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__IDLE_ANIMATION,
				 AuxiliaryFactory.eINSTANCE.createAnimationDescription()));

		newChildDescriptors.add
			(createChildParameter
				(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__IDLE_ANIMATION,
				 AuxiliaryFactory.eINSTANCE.createTextPageAnimationDescription()));

		newChildDescriptors.add
			(createChildParameter
				(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__RUN_ANIMATION,
				 AuxiliaryFactory.eINSTANCE.createAnimationDescription()));

		newChildDescriptors.add
			(createChildParameter
				(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__RUN_ANIMATION,
				 AuxiliaryFactory.eINSTANCE.createTextPageAnimationDescription()));

		newChildDescriptors.add
			(createChildParameter
				(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__JUMP_ANIMATION,
				 AuxiliaryFactory.eINSTANCE.createAnimationDescription()));

		newChildDescriptors.add
			(createChildParameter
				(GameelementsPackage.Literals.CHARACTER_DESCRIPTION__JUMP_ANIMATION,
				 AuxiliaryFactory.eINSTANCE.createTextPageAnimationDescription()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == GameelementsPackage.Literals.CHARACTER_DESCRIPTION__IDLE_ANIMATION ||
			childFeature == GameelementsPackage.Literals.CHARACTER_DESCRIPTION__RUN_ANIMATION ||
			childFeature == GameelementsPackage.Literals.CHARACTER_DESCRIPTION__JUMP_ANIMATION;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return DescriptionsEditPlugin.INSTANCE;
	}

}
