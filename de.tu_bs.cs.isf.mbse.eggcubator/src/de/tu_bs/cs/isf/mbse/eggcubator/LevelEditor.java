package de.tu_bs.cs.isf.mbse.eggcubator;

import org.eclipse.graphiti.ui.editor.DiagramBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.IDiagramEditorInput;
import org.eclipse.ui.IEditorInput;

public class LevelEditor extends DiagramEditor {

	private String currentProject = null;
	
	public LevelEditor() {
		super();
	}
	
	@Override
	protected void setInput(IEditorInput input) {
		if ((input instanceof IDiagramEditorInput) && ((IDiagramEditorInput) input).getUri().segmentsList().get(0).equals("resource")) {
			currentProject = ((IDiagramEditorInput) input).getUri().segmentsList().get(1);
			EggScriptionLoader.init(currentProject);
		} else {
			currentProject = null;
			EggScriptionLoader.init();
		}
		super.setInput(input);
	}

	public String getCurrentProject() {
		return currentProject;
	}
	
	@Override
	protected DiagramBehavior createDiagramBehavior() {
		return new LevelDiagramBehavior(this);
	}
	
}
