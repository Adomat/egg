package de.tu_bs.cs.isf.mbse.egg.modeltranformation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

import de.tu_bs.cs.isf.mbse.egg.descriptions.Description;
import de.tu_bs.cs.isf.mbse.egg.descriptions.DescriptionRoot;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.Logo;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.MenuPageAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Duration;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Pictures;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gui.MenuPageDescription;

public class JavaScriptGenerator {
	private static String _GENERATED_CODE;
	
	public static void generateCode(File selectedFile, Shell shell) throws IOException, URISyntaxException {
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
		
		File templateFile = getFileFromBundle("de.tu_bs.cs.isf.mbse.egg.modeltranformation", "PlaceHolder.eggtransformation");
		String templateContent = new String(Files.readAllBytes(Paths.get(templateFile.toURI())));
		
		generateCodeFromModels(resourceSet);
		String modifiedContent = String.format(templateContent, _GENERATED_CODE);
		
		// Create / Override the target file
		File targetFile = new File(selectedFile.getParentFile() + "/index.html");
		
		if (targetFile.exists()) {
			if (MessageDialog.openQuestion(shell, "EGG - Override old version?",
					"You are about to override your latest game version.\n\nContinue anyway?"))
				targetFile.delete();
			else return;
		}
		targetFile.createNewFile();

		// Write the target file
		PrintWriter out = new PrintWriter(targetFile);
//		out.println(_GENERATED_CODE);
		out.println(modifiedContent);
		out.close();
	}
	
	private static void generateCodeFromModels(ResourceSet resourceSet) {
		_GENERATED_CODE = "\n\t// =========================================== //\n"
				+ "\t// This Method consists of EGG Generated Code. //\n"
				+ "\t// =========================================== //\n\n";
		
		generateSetupCode();
		
		for(Resource resource : resourceSet.getResources()) {
			for(EObject root : resource.getContents()) {
				if(root instanceof DescriptionRoot) {
					for(Description description : ((DescriptionRoot) root).getDescriptions()) {
						if(description instanceof MenuPageDescription) {
							generateCodeForMenuPage((MenuPageDescription) description);
						}
					}
				}
			}
		}
	}

	private static void generateSetupCode() {
		addCodeLine("blockSize = %d;", 123456789);
		addCodeLine("startPageKey = \"%s\";", "TODO");
		
		addCodeLine("");
		addCodeLine("pages = [];");
		addCodeLine("");
	}

	private static void generateCodeForMenuPage(MenuPageDescription description) {
		String variableName = "MenuPage_" + description.getName();

		addCodeLine("// Menu Page to the description with name \"%s\"", description.getName());
		addCodeLine("var %s = new MenuPage();", variableName);
		addCodeLine("menuPage.setPageKey(\"%s\");", variableName);
		
		addCodeLine("menuPage.setBackgroundImage(\"%s\");", "TODO");
		addCodeLine("menuPage.setBackgroundColor(\"%s\");", "TODO");

		addCodeLine("menuPage.addButton(\"%s\", \"%s\");", "TODO", "TODO");
		addCodeLine("menuPage.addButton(\"%s\", \"%s\");", "TODO", "TODO");
		addCodeLine("menuPage.addButton(\"%s\", \"%s\");", "TODO", "TODO");
		addCodeLine("menuPage.addButton(\"%s\", \"%s\");", "TODO", "TODO");
		
		for(MenuPageAttribute property : description.getProperties()) {
			if(property instanceof Logo) {
				for(AnimationAttribute animationProperty : ((Logo) property).getAnimation().getProperties()) {
					if(animationProperty instanceof Pictures) {
						for(String pictureURL : ((Pictures) animationProperty).getValue()) {
							addCodeLine("menuPage.addLogoImage(\"%s\");", pictureURL);
						}
					}
					else if(animationProperty instanceof Duration) {
						addCodeLine("menuPage.logoAnimationSpeed = %d;", ((Duration) animationProperty).getValue());
					}
				}
			}
		}
	}
	
	private static void addCodeLine(String line, Object ... args) {
		_GENERATED_CODE += "\t" + String.format(line, args) + "\n";
	}

	private static File getFileFromBundle(String bundleString, String filePath) throws URISyntaxException, IOException {
		Bundle bundle = Platform.getBundle(bundleString);
		URL fileURL = bundle.getEntry(filePath);
		return new File(FileLocator.resolve(fileURL).toURI());
	}
}
