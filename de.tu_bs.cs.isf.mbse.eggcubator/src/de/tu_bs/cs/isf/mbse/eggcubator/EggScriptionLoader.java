package de.tu_bs.cs.isf.mbse.eggcubator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.resource.XtextResourceSet;

import de.tu_bs.cs.isf.mbse.egg.descriptions.Description;
import de.tu_bs.cs.isf.mbse.egg.descriptions.DescriptionRoot;

public class EggScriptionLoader {

	private static XtextResourceSet set = new XtextResourceSet();
	private static Collection<Description> descriptions;
	
	private EggScriptionLoader() {
	}
	
	private static List<File> getFilesWithExtension(File dir, String extension) {
		return getFilesWithExtension(dir, extension, true);
	}

	private static List<File> getFilesWithExtension(File dir, String extension, boolean recursive) {
		List<File> result = new ArrayList<File>();
		for (File file : dir.listFiles()) {
			if (file.isFile() && file.getName().endsWith('.' + extension))
				result.add(file);
			else if (recursive && file.isDirectory())
				result.addAll(getFilesWithExtension(file, extension, recursive));
		}
		return result;
	}
	
	public static void init() {
		descriptions = null;
		for (Resource resource : set.getResources())
			resource.unload();
		set.getResources().clear();
	}
	
	public static void init(String project) {
		init();
		File projDir = new File(ResourcesPlugin.getWorkspace().getRoot().getProject(project).getLocation().toString());
		List<File> eggs = getFilesWithExtension(projDir, "egg");
		List<Description> desc = new ArrayList<Description>();
		for (File egg : eggs) {
			String projRelatedPath = egg.getAbsolutePath().substring(projDir.getAbsolutePath().length()).substring(1).replace(File.pathSeparatorChar, '/');
			Resource resource = set.getResource(URI.createPlatformResourceURI("/" + project + "/" + projRelatedPath, true), true);
			if (!resource.isLoaded()) {
				System.out.println("[WARN] Could not load egg resource file " + egg.getAbsolutePath());
				continue;
			}
			resource.setTrackingModification(false);
			EList<Diagnostic> errors = resource.getErrors();
			EList<EObject> contents;
			if (errors.size() > 0) {
				System.err.println("There are " + errors.size() + " errors in egg file " + projRelatedPath + "\nLoading aborted.");
				resource.unload();
				set.getResources().remove(resource);
				continue;
			} else if ((contents = resource.getContents()).size() != 1 || !(contents.get(0) instanceof DescriptionRoot)) {
				System.err.println("Invalid format in egg file " + projRelatedPath + "\nLoading aborted.");
				resource.unload();
				set.getResources().remove(resource);
				continue;
			}
			DescriptionRoot dr = (DescriptionRoot) contents.get(0);
			for (Description d : dr.getDescriptions())
				desc.add(d);
		}
		descriptions = Collections.unmodifiableCollection(desc);
	}
	
	public static Collection<Description> getDescriptions() {
		return descriptions;
	}

}
