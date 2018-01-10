package de.tu_bs.cs.isf.mbse.egg.modeltranformation.actions;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import de.tu_bs.cs.isf.mbse.egg.modeltranformation.JavaScriptGenerator;

public class GenerateAction implements IObjectActionDelegate {

	private Shell shell;
	private File selectedFile;
	
	/**
	 * Constructor for Action1.
	 */
	public GenerateAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if(MessageDialog.openQuestion(shell, "Generate JavaScript Code?",
				"Do you want EGG to generate your game now?\n\nJavaScript Code will be created somewhere in this workspace"
				+ " - at least we're trying to!")) {
			try {
				//IFile targetFile = action.;
				JavaScriptGenerator.generateCode(selectedFile);
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openError(shell, "Generating went wrong!", "Something went wrong while generating your Code:\n\n"
						+ e.getMessage().substring(0, 1000) + " ...");
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if(!selection.isEmpty()) {
			for(IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
				String[] selectionSplit = selection.toString().split("/");
				String fileName = selectionSplit[selectionSplit.length-1];
				fileName = fileName.substring(0, fileName.length()-1);
				IFile file = project.getFile(fileName);
				
				selectedFile = new File(file.getLocation().toString());
			}
		}
	}

}
