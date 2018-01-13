package de.tu_bs.cs.isf.mbse.egg.modeltranformation;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.osgi.framework.Bundle;

// https://www.eclipse.org/epsilon/examples/index.php?example=org.eclipse.epsilon.examples.standalone

public class JavaScriptGeneratorEGL {
	public static void generateCode(File selectedFile) throws Exception {
		if (selectedFile == null)
			return;
		
		File egxFile = getFileFromBundle("de.tu_bs.cs.isf.mbse.egg.modeltranformation", "JavaScriptTransformation.egx");

		// Parse egx file
		EgxModule module = new EgxModule(new EglFileGeneratingTemplateFactory());
		module.parse(egxFile.getAbsoluteFile());

		if (!module.getParseProblems().isEmpty()) {
			throw new Exception("Syntax errors found in EGX File.");
		}

		// Make the documents visible to the EGX program
		for (IModel model : getModels(selectedFile)) {
			module.getContext().getModelRepository().addModel(model);
		}
		
		// ... and execute
		module.execute();
	}

	public static List<IModel> getModels(File selectedFile) throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		
		File metaModelFile = getFileFromBundle("de.tu_bs.cs.isf.mbse.egg", "models/descriptions.ecore");
		
		models.add(createEmfModel("Model", selectedFile.toURI().toString(), metaModelFile.toURI().toString(), true, true));
		
		return models;
	}

	private static EmfModel createEmfModel(String name, String modelURI, String metamodelURI, boolean readOnLoad, boolean storeOnDisposal) 
			throws Exception {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, metamodelURI);
		properties.put(EmfModel.PROPERTY_MODEL_URI, modelURI);
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}
	
	
	
	

	private static IFile getIFileFromBundle(String bundleString, String filePath) throws URISyntaxException, IOException {
		File file = getFileFromBundle(bundleString, filePath);
		IPath pathToSelectedFile = Path.fromOSString(file.getAbsolutePath());
		IFile selectedFile = ResourcesPlugin.getWorkspace().getRoot().getFile(pathToSelectedFile);
		
		return null;
	}
	
	private static File getFileFromBundle(String bundleString, String filePath) throws URISyntaxException, IOException {
		Bundle bundle = Platform.getBundle(bundleString);
		URL fileURL = bundle.getEntry(filePath);
		return new File(FileLocator.resolve(fileURL).toURI());
	}
}
