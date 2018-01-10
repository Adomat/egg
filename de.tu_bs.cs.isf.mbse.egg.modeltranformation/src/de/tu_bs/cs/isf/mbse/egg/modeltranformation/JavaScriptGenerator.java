package de.tu_bs.cs.isf.mbse.egg.modeltranformation;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import org.osgi.framework.Bundle;

public class JavaScriptGenerator {
	public static void generateCode(File selectedFile) throws Exception {
		if (selectedFile == null)
			return;

		Bundle bundle = Platform.getBundle("de.tu_bs.cs.isf.mbse.egg.modeltranformation");
		URL fileURL = bundle.getEntry("JavaScriptTransformation.egl");
		File egxFile = new File(FileLocator.resolve(fileURL).toURI());
		
		System.out.println(egxFile);

		// Parse main.egx
		EgxModule module = new EgxModule(new EglFileGeneratingTemplateFactory());
		module.parse(egxFile.getAbsoluteFile());
		// module.parse(new
		// File("JavaScriptTransformation.egx").getAbsoluteFile());

		if (!module.getParseProblems().isEmpty()) {
			throw new Exception("Syntax errors found.");
		}

		// Load the XML document
		PlainXmlModel model = new PlainXmlModel();
		model.setFile(selectedFile);
		// model.setFile(new File("library.xml"));
		model.setName("stupiddarnshit");
		model.load();

		// Make the document visible to the EGX program
		module.getContext().getModelRepository().addModel(model);
		// ... and execute
		module.execute();
	}
}
