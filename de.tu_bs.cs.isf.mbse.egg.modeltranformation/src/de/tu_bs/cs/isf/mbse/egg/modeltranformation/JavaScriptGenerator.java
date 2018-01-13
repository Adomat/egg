package de.tu_bs.cs.isf.mbse.egg.modeltranformation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.osgi.framework.Bundle;

public class JavaScriptGenerator {
	public static void generateCode(File selectedFile) throws IOException, URISyntaxException {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		if(!selectedFile.getParentFile().isDirectory()) {
			System.out.println("Generator messed up...");
			return;
		}
		
		for(File sibling : selectedFile.getParentFile().listFiles()) {
			// Load all descriptions into the resource set
			if(!sibling.toURI().toString().endsWith(".egg"))
				continue;
			
			String content = new String(Files.readAllBytes(Paths.get(sibling.toURI())));
			InputStream in = new ByteArrayInputStream(content.getBytes());
			
			Resource resource = resourceSet.createResource(URI.createURI(sibling.toURI().toString()));
			resource.load(in, resourceSet.getLoadOptions());
			
			// TODO Load all levels into resource set
		}
		
		File templateFile = getFileFromBundle("de.tu_bs.cs.isf.mbse.egg.modeltranformation", "template.html");
	}
	
	private static File getFileFromBundle(String bundleString, String filePath) throws URISyntaxException, IOException {
		Bundle bundle = Platform.getBundle(bundleString);
		URL fileURL = bundle.getEntry(filePath);
		return new File(FileLocator.resolve(fileURL).toURI());
	}
}
