package de.tu_bs.cs.isf.mbse.eggcubator.tabbedProperties;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

import de.tu_bs.cs.isf.mbse.egg.level.Level;

public class LevelFilter extends AbstractPropertySectionFilter {

	public LevelFilter() {
	}

	@Override
	protected boolean accept(PictogramElement pictogramElement) {
		if (pictogramElement == null || pictogramElement.getLink() == null)
			return false;
		EList<EObject> bos = pictogramElement.getLink().getBusinessObjects();
		return bos != null && bos.size() == 1 && bos.get(0) instanceof Level;
	}

}
