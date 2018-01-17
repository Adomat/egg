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
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.BlockAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.NoCollision;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.HeroAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.IdleAnimation;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.JumpAnimation;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.JumpPower;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.MaxLife;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.RunAnimation;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.Speed;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.Logo;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.MenuPageAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.NextPage;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.TextPageAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.Title;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Duration;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Pictures;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.BlockDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.HeroDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gui.MenuPageDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gui.PageDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gui.TextPageDescription;

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
						else if(description instanceof TextPageDescription) {
							generateCodeForTextPage((TextPageDescription) description);
						}
						else if(description instanceof HeroDescription) {
							generateCodeForHero((HeroDescription) description);
						}
						else if(description instanceof BlockDescription) {
							generateCodeForBlock((BlockDescription) description);
						}
						else {
							System.out.println("ATTENTION: The JavaScript Generator did not generate Code for the following Description:\n  > " + description.getClass().getSimpleName().replace("Impl", ""));
						}
					}
				}
			}
		}
	}

	private static void generateCodeForBlock(BlockDescription description) {
		String variableName = generateVariableNameFromDescription(description);
		addCodeLine("// Block to the description with name \"%s\"", description.getName());

		addCodeLine("var %s = new Block();", variableName);

		for(BlockAttribute property : description.getProperties()) {
			if(property instanceof NoCollision) {
				addCodeLine("%s.isSolid = false;", variableName);
			}
			else if(property instanceof AnimationDescription) {
				for(AnimationAttribute animationProperty : ((AnimationDescription) property).getProperties()) {
					if(animationProperty instanceof Pictures) {
						for(String pictureURL : ((Pictures) animationProperty).getValue()) {
							addCodeLine("%s.addImage(\"%s\");", variableName, pictureURL);
						}
					}
					else if(animationProperty instanceof Duration) {
						addCodeLine("%s.animationSpeed = %d;", variableName, ((Duration) animationProperty).getValue());
					}
				}
			}
			else {
				System.out.println("\tATTENTION: The JavaScript Generator did not generate Code for the following Attribute:\n\t  > " + property.getClass().getSimpleName().replace("Impl", ""));
			}
		}
		
		addCodeLine("");
	}

	private static void generateCodeForHero(HeroDescription description) {
		String variableName = generateVariableNameFromDescription(description);
		addCodeLine("// Hero to the description with name \"%s\"", description.getName());

		addCodeLine("var %s = new HeroCharacter();", variableName);
		addCodeLine("%s.setCollisionBox(%d, %d);", variableName, 123456789, 123456789);
		
		int refreshTime = 200;
		
		for(HeroAttribute property : description.getProperties()) {
			if(property instanceof Speed) {
				addCodeLine("%s.speed = %d;", variableName, ((Speed) property).getValue());
			}
			else if(property instanceof MaxLife) {
				addCodeLine("%s.life = %d;", variableName, ((MaxLife) property).getValue());
			}
			else if(property instanceof JumpPower) {
				addCodeLine("%s.jumpPower = %d;", variableName, ((JumpPower) property).getValue());
			}
			else if(property instanceof RunAnimation) {
				for(AnimationAttribute animationProperty : ((RunAnimation) property).getValue().getProperties()) {
					if(animationProperty instanceof Pictures) {
						for(String pictureURL : ((Pictures) animationProperty).getValue()) {
							addCodeLine("%s.addRunImage(\"%s\");", variableName, pictureURL);
						}
					}
					else if(animationProperty instanceof Duration) {
						if(((Duration) animationProperty).getValue() > refreshTime) {
							refreshTime = ((Duration) animationProperty).getValue();
						}
					}
				}
			}
			else if(property instanceof JumpAnimation) {
				for(AnimationAttribute animationProperty : ((JumpAnimation) property).getValue().getProperties()) {
					if(animationProperty instanceof Pictures) {
						for(String pictureURL : ((Pictures) animationProperty).getValue()) {
							addCodeLine("%s.addJumpImage(\"%s\");", variableName, pictureURL);
						}
					}
					else if(animationProperty instanceof Duration) {
						if(((Duration) animationProperty).getValue() > refreshTime) {
							refreshTime = ((Duration) animationProperty).getValue();
						}
					}
				}
			}
			else if(property instanceof IdleAnimation) {
				for(AnimationAttribute animationProperty : ((IdleAnimation) property).getValue().getProperties()) {
					if(animationProperty instanceof Pictures) {
						for(String pictureURL : ((Pictures) animationProperty).getValue()) {
							addCodeLine("%s.addIdleImage(\"%s\");", variableName, pictureURL);
						}
					}
					else if(animationProperty instanceof Duration) {
						if(((Duration) animationProperty).getValue() > refreshTime) {
							refreshTime = ((Duration) animationProperty).getValue();
						}
					}
				}
			}
			else {
				System.out.println("\tATTENTION: The JavaScript Generator did not generate Code for the following Attribute:\n\t  > " + property.getClass().getSimpleName().replace("Impl", ""));
			}
		}

		addCodeLine("%s.animationSpeed = %d;", variableName, refreshTime);
		addCodeLine("// Hiiiiii, please add me to the level \\o thaaaaaanks\n");
	}

	private static void generateCodeForTextPage(TextPageDescription description) {
		String variableName = generateVariableNameFromDescription(description);

		addCodeLine("// Text Page to the description with name \"%s\"", description.getName());
		for(TextPageAttribute property : description.getProperties()) {
			if(property instanceof Title) {
				addCodeLine("var %s = new TextPage(\"%s\");", variableName, ((Title) property).getValue());
				break;
			}
		}
		
		for(TextPageAttribute property : description.getProperties()) {
			if(property instanceof NextPage) {
				PageDescription nextPage = ((NextPage) property).getValue();
				String nextPageKey = generateVariableNameFromDescription(nextPage);
				addCodeLine("tutorialPage.newPageKey = \"menu\";", variableName, nextPageKey);
				break;
			}
		}
		
		addCodeLine("%s.setPageKey(\"%s\");", variableName, variableName);
		addCodeLine("%s.setBackgroundImage(\"%s\");", variableName, "TODO");
		addCodeLine("%s.setBackgroundColor(\"%s\");", variableName, "TODO");

		addCodeLine("%s.addParagraph(\"%s\");", variableName, "TODO");
		addCodeLine("%s.addParagraph(\"%s\");", variableName, "TODO");
		addCodeLine("%s.addParagraph(\"%s\");", variableName, "TODO");
		addCodeLine("%s.addParagraph(\"%s\");", variableName, "TODO");
		
		addCodeLine("pages.push(%s);\n", variableName);
	}

	private static void generateCodeForMenuPage(MenuPageDescription description) {
		String variableName = generateVariableNameFromDescription(description);

		addCodeLine("// Menu Page to the description with name \"%s\"", description.getName());
		addCodeLine("var %s = new MenuPage();", variableName);
		addCodeLine("%s.setPageKey(\"%s\");", variableName, variableName);
		
		addCodeLine("%s.setBackgroundImage(\"%s\");", variableName, "TODO");
		addCodeLine("%s.setBackgroundColor(\"%s\");", variableName, "TODO");

		addCodeLine("%s.addButton(\"%s\", \"%s\");", variableName, "TODO", "TODO");
		addCodeLine("%s.addButton(\"%s\", \"%s\");", variableName, "TODO", "TODO");
		addCodeLine("%s.addButton(\"%s\", \"%s\");", variableName, "TODO", "TODO");
		addCodeLine("%s.addButton(\"%s\", \"%s\");", variableName, "TODO", "TODO");
		
		for(MenuPageAttribute property : description.getProperties()) {
			if(property instanceof Logo) {
				for(AnimationAttribute animationProperty : ((Logo) property).getAnimation().getProperties()) {
					if(animationProperty instanceof Pictures) {
						for(String pictureURL : ((Pictures) animationProperty).getValue()) {
							addCodeLine("%s.addLogoImage(\"%s\");", variableName, pictureURL);
						}
					}
					else if(animationProperty instanceof Duration) {
						addCodeLine("%s.logoAnimationSpeed = %d;", variableName, ((Duration) animationProperty).getValue());
					}
				}
			}
//			else if(property instanceof Title) {
//				// TODO what are we gonna do with this?
//				addCodeLine("// %s.doSomeThingWithTheDamnTitle(\"%s\");", variableName, ((Title) property).getValue());
//			}
			else {
				System.out.println("\tATTENTION: The JavaScript Generator did not generate Code for the following Attribute:\n\t  > " + property.getClass().getSimpleName().replace("Impl", ""));
			}
		}
		
		addCodeLine("pages.push(%s);\n", variableName);
	}

	private static void generateSetupCode() {
		addCodeLine("blockSize = %d;", 123456789);
		addCodeLine("startPageKey = \"%s\";", "TODO");
		
		addCodeLine("");
		addCodeLine("pages = [];");
		addCodeLine("");
	}
	
	private static void addCodeLine(String line, Object ... args) {
		_GENERATED_CODE += "\t" + String.format(line, args) + "\n";
	}
	
	private static String generateVariableNameFromDescription(Description desc) {
		String metaClassName = desc.getClass().getSimpleName();
		metaClassName = metaClassName.toLowerCase().replaceAll("impl", "").replaceAll("description", "");
		return metaClassName + "_" + desc.getName();
	}

	private static File getFileFromBundle(String bundleString, String filePath) throws URISyntaxException, IOException {
		Bundle bundle = Platform.getBundle(bundleString);
		URL fileURL = bundle.getEntry(filePath);
		return new File(FileLocator.resolve(fileURL).toURI());
	}
}
