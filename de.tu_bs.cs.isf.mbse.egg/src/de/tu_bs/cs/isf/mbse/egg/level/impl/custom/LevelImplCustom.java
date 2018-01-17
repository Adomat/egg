package de.tu_bs.cs.isf.mbse.egg.level.impl.custom;

import java.util.Map;

import org.eclipse.emf.common.util.EMap;

import de.tu_bs.cs.isf.mbse.egg.level.LevelFactory;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.impl.ColumnEntryImpl;
import de.tu_bs.cs.isf.mbse.egg.level.impl.LevelFactoryImpl;
import de.tu_bs.cs.isf.mbse.egg.level.impl.LevelImpl;
import de.tu_bs.cs.isf.mbse.egg.level.impl.RowEntryImpl;

/**
 * This custom map implementation adds all operations to {@link de.tu_bs.cs.isf.mbse.egg.level.Level}.
 * It manages the used maps that contain the {@link de.tu_bs.cs.isf.mbse.egg.level.PlacedElement}s. Therefore
 * it shrinks the maps on resize, but doesn't add map entries as long as they are unused.
 */
public class LevelImplCustom extends LevelImpl {
	
	public LevelImplCustom() {
		super();
	}
	
	@Override
	public void setWidth(int newWidth) {
		int oldWidth = getWidth();
		super.setWidth(newWidth);
		if (oldWidth > newWidth) {
			// make map smaller and remove unused elements
			EMap<Integer, EMap<Integer, PlacedElement>> elements = getElements();
			for (int x = oldWidth - 1; x >= newWidth; x--) {
				if (elements.containsKey(x))
					elements.removeKey(x);
			}
		}
	}

	@Override
	public void setHeight(int newHeight) {
		int oldHeight = getHeight();
		super.setHeight(newHeight);
		if (oldHeight > newHeight) {
			// make map smaller and remove unused elements and maps
			for (int x = 0; x < getWidth(); x++) {
				if (!elements.containsKey(x))
					continue;
				for (int y = oldHeight - 1; y >= newHeight; y--) {
					if (elements.get((Integer) x).containsKey(y))
						elements.get((Integer) x).removeKey(y);
				}
				if (elements.get((Integer) x).isEmpty())
					elements.removeKey(x);
			}
		}
	}

	protected ColumnEntryImpl getColumnEntry(EMap<Integer, EMap<Integer, PlacedElement>> elements, Integer key) {
		for (Map.Entry<Integer, EMap<Integer, PlacedElement>> columnEntry : elements)
			if (columnEntry.getKey() == key)
				return (ColumnEntryImpl) columnEntry;
		return null;
	}

	protected RowEntryImpl getRowEntry(EMap<Integer, PlacedElement> elements, Integer key) {
		for (Map.Entry<Integer, PlacedElement> rowEntry : elements)
			if (rowEntry.getKey() == key)
				return (RowEntryImpl) rowEntry;
		return null;
	}
	
	/**
	 * Get the {@link PlacedElement} at the given position or null if not set.
	 */
	@Override
	public PlacedElement getElement(int positionX, int positionY) {
		EMap<Integer, EMap<Integer, PlacedElement>> elements = getElements();
		return elements.containsKey(positionX) && elements.get((Integer) positionX).containsKey(positionY) ?
				elements.get((Integer) positionX).get((Integer) positionY) : null;
	}

	/**
	 * Adds the {@link PlacedElement} to the level at the position saved inside the element.
	 * @return true if existing element was replaced
	 */
	@Override
	public boolean addElement(PlacedElement element) {
		if (!(LevelFactory.eINSTANCE instanceof LevelFactoryImpl))
			throw new UnsupportedOperationException("LevelFactory must be subtype of LevelFactoryImpl to be compatible.");
		EMap<Integer, EMap<Integer, PlacedElement>> elements = getElements();
		LevelFactoryImpl factory = (LevelFactoryImpl) LevelFactory.eINSTANCE;
		
		// add entrys to map
		ColumnEntryImpl columnEntry;
		if (!elements.containsKey(element.getPositionX())) {
			columnEntry = (ColumnEntryImpl) factory.createColumnEntry(); // TODO error checking needed? Cannot use interface because none exists
			columnEntry.setKey(element.getPositionX());
			elements.put(columnEntry.getKey(), columnEntry.getValue());
		} else
			columnEntry = getColumnEntry(elements, element.getPositionX());
		
		RowEntryImpl rowEntry;
		if (!columnEntry.getValue().containsKey(element.getPositionY())) {
			rowEntry = (RowEntryImpl) factory.createRowEntry(); // TODO error checking needed? Cannot use interface because none exists
			rowEntry.setKey(element.getPositionY());
			columnEntry.getValue().put(rowEntry.getKey(), rowEntry.getValue());
		} else
			rowEntry = getRowEntry(columnEntry.getValue(), element.getPositionY());
		
		boolean override = rowEntry.getValue() != null;
		rowEntry.setValue(element);
		return override;
	}

	/**
	 * Removes the {@link PlacedElement} from the level.
	 * @return true if the element was successfully removed from the level
	 */
	@Override
	public boolean removeElement(PlacedElement element) {
		EMap<Integer, EMap<Integer, PlacedElement>> elements = getElements();
		if (!elements.containsKey(element.getPositionX()))
			return false;
		ColumnEntryImpl columnEntry = getColumnEntry(elements, element.getPositionX());
		
		if (!columnEntry.getValue().containsKey(element.getPositionY()))
			return false;
		RowEntryImpl rowEntry = getRowEntry(columnEntry.getValue(), element.getPositionY());
		
		// remove if matching entry found
		boolean remove = rowEntry.getValue() == element;
		if (remove) {
			columnEntry.getValue().removeKey(element.getPositionY());
			if (columnEntry.getValue().isEmpty())
				elements.removeKey(element.getPositionX());
		}
		return remove;
	}

	/**
	 * Removes a {@link PlacedElement} from the level based on the coordinates.
	 * @return true if an element was successfully removed from the level at the given position
	 */
	@Override
	public boolean removeElement(int positionX, int positionY) {
		EMap<Integer, EMap<Integer, PlacedElement>> elements = getElements();
		if (!elements.containsKey(positionX))
			return false;
		ColumnEntryImpl columnEntry = getColumnEntry(elements, positionX);
		
		if (!columnEntry.getValue().containsKey(positionY))
			return false;
		
		// remove
		columnEntry.getValue().removeKey(positionY);
		if (columnEntry.getValue().isEmpty())
			elements.removeKey(positionX);
		return true;
	}
	
}
