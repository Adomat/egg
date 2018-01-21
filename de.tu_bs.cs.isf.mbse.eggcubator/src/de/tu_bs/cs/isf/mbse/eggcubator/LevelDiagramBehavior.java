package de.tu_bs.cs.isf.mbse.eggcubator;

import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.IDiagramContainerUI;
import org.eclipse.graphiti.ui.editor.IDiagramEditorInput;

public class LevelDiagramBehavior extends DiagramBehavior {

	private boolean startup = false;
	
	public LevelDiagramBehavior(IDiagramContainerUI diagramContainer) {
		super(diagramContainer);
	}
	
	@Override
	protected void setInput(IDiagramEditorInput input) {
		startup = true;
		super.setInput(input);
		startup = false;
	}
	
	public boolean isStartingUp() {
		return startup;
	}

}
