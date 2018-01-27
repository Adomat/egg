package de.tu_bs.cs.isf.mbse.egg.modeltranformation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import de.tu_bs.cs.isf.mbse.egg.descriptions.Description;
import de.tu_bs.cs.isf.mbse.egg.descriptions.DescriptionRoot;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.BlockAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.NoCollision;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.CollisionBox;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.EnemyAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.HeroAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.IdleAnimation;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.JumpAnimation;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.JumpPower;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.MaxLife;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.RunAnimation;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.ShowCollisionBox;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.Speed;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.Strength;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.BackgroundColor;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.BackgroundImage;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.Button;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.FontColor;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.FontSize;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.Logo;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.MenuPageAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.NextPage;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.StartPage;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.Text;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.TextPageAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.gui.Title;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Duration;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Pictures;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.BlockDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.EnemyDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.HeroDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gui.MenuPageDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gui.TextPageDescription;
import de.tu_bs.cs.isf.mbse.egg.level.Level;
import de.tu_bs.cs.isf.mbse.egg.level.LevelPackage;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.EndPoint;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.PlacedBlock;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.PlacedEnemy;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.WarpPoint;

public class JavaScriptGenerator {
	private static String _GAME_TITLE;
	private static String _GENERATED_CODE;
	
	public static void generateCode(File selectedFile, String projectName, Shell shell) throws IOException, URISyntaxException, CoreException {
		if(!selectedFile.getParentFile().isDirectory()) {
			System.out.println("Generator messed up...");
			return;
		}
		_GAME_TITLE = projectName;
		
		ResourceSet resourceSet = new ResourceSetImpl();
		
		// Load Egg Resources
		for(File sibling : selectedFile.getParentFile().listFiles()) {
			if(!sibling.toURI().toString().endsWith(".egg"))
				continue;
			
			String content = new String(Files.readAllBytes(Paths.get(sibling.toURI())));
			InputStream in = new ByteArrayInputStream(content.getBytes());
			
			Resource resource = resourceSet.createResource(URI.createURI(sibling.toURI().toString()));
			resource.load(in, resourceSet.getLoadOptions());
		}
		
		// Load Level Resources
		for(File sibling : selectedFile.getParentFile().listFiles()) {
			if(!sibling.toURI().toString().endsWith(".level"))
				continue;
			
			// Initialize the model
	        LevelPackage.eINSTANCE.eClass();

	        // Register the XMI resource factory for the .website extension

	        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
	        Map<String, Object> m = reg.getExtensionToFactoryMap();
	        m.put("website", new XMIResourceFactoryImpl());

	        // Get the resource
	        resourceSet.getResource(URI.createURI(sibling.toURI().toString()), true);
		}
		
		/*Bundle bundle = Platform.getBundle("de.tu_bs.cs.isf.mbse.egg.modeltranformation");
		URL fileURL = bundle.getEntry("PlaceHolder.eggtransformation");
		File templateFile = new File(FileLocator.resolve(fileURL).toURI());
		String templateContent = new String(Files.readAllBytes(Paths.get(templateFile.toURI())));*/
		
		URL url = new URL("platform:/plugin/de.tu_bs.cs.isf.mbse.egg.modeltranformation/PlaceHolder.eggtransformation");
	    InputStream inputStream = url.openConnection().getInputStream();
	    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
	    String templateContent = "";
	    while (true) {
	    	String newLine = in.readLine();
	    	if(newLine == null)
	    		break;
	    	
	    	templateContent += newLine + "\n";
	    }
	    in.close();
		
		generateCodeFromModels(resourceSet);
		String modifiedContent = String.format(templateContent, _GAME_TITLE, _GENERATED_CODE);
		
		// Create / Override the target file
		File targetFile = new File(selectedFile.getParentFile() + "/index.html");
		
		if (targetFile.exists()) {
			if(!MessageDialog.openQuestion(shell, "EGG - Override old JavaScript Code?", "Do you want EGG to generate your game now?\n\nThis will override an older version of the generated html-file."))
				return;
			targetFile.delete();
		}
		targetFile.createNewFile();

		// Write the target file
		PrintWriter out = new PrintWriter(targetFile);
//		out.println(_GENERATED_CODE);
		out.println(modifiedContent);
		out.close();
		
		for(IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()){
		    project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		}
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
						else if(description instanceof BlockDescription) {
							generateCodeForBlock((BlockDescription) description);
						}
						else if(description instanceof HeroDescription) {
							generateCodeForHero((HeroDescription) description);
						}
						else if(description instanceof EnemyDescription) {
							generateCodeForEnemy((EnemyDescription) description);
						}
						else {
							System.out.println("ATTENTION: The JavaScript Generator did not generate Code for the following Description:\n  > " + description.getClass().getSimpleName().replace("Impl", ""));
						}
					}
				}
				else if(root instanceof Level) {
					generateCodeForLevel((Level) root);
				}
			}
		}
	}

	private static void generateCodeForLevel(Level level) {
		String variableName = level.getName();
		addCodeLine("// Level \"%s\"", level.getName());
		addCodeLine("var %s = new LevelPage();", variableName);
		addCodeLine("%s.setPageKey(\"%s\");", variableName, variableName);
		addCodeLine("%s.blockSize = %d;", variableName, level.getElementSize());
		addCodeLine("%s.gravity = %f;", variableName, level.getGravity());
		if(level.getBackgroundColor() != null)
			addCodeLine("%s.setBackgroundColor(\"%s\");", variableName, level.getBackgroundColor());
		if(level.getBackgroundImage() != null)
			addCodeLine("%s.setBackgroundImage(\"%s\");", variableName, derivePictureURL(level.getBackgroundImage()));
		
		for(Entry<Integer, EMap<Integer, PlacedElement>> column : level.getElements()) {
			for(Entry<Integer, PlacedElement> row : column.getValue()) {
				if(row.getValue() != null) {
					PlacedElement element = row.getValue();

					int x = column.getKey();
					int y = level.getHeight()-1 - row.getKey();
					
					if(element instanceof PlacedBlock) {
						BlockDescription desc = ((PlacedBlock) element).getProperties();
						String blockVariableName = desc.getName();
						
						addCodeLine("%s.addBlock(%s, %d, %d);", variableName, blockVariableName, x, y);
					}
					else if(element instanceof WarpPoint) {
						if(!(((WarpPoint) element).getHeroOnEntry() == null)) {
							// This is the entry to our level
							// The last entry point will implicitely be applied to the generated game
							String heroName = ((WarpPoint) element).getHeroOnEntry().getName();
							
							addCodeLine("%s.addHero(%s, %d, %d);", variableName, heroName, x, y);
						}
						else {
							// This is an exit block to another page
							String newPage = ((WarpPoint) element).getWarpTo();

							addCodeLine("%s.addExitGate(\"%s\", %d, %d);", variableName, newPage, x, y);
						}
					}
					else if(element instanceof EndPoint) {
						addCodeLine("%s.addFinishBlock(%s, %d, %d);", variableName, x, y);
					}
					else if(element instanceof PlacedEnemy) {
						String enemyName = ((PlacedEnemy) element).getProperties().getName();
						addCodeLine("%s.addEnemy(%s, %d, %d);", variableName, enemyName, x, y);
					}
					else {
						System.out.println("ATTENTION: The JavaScript Generator did not generate Code for the following Level Element:\n  > " + element.getClass().getSimpleName().replace("Impl", ""));
					}
				}
			}
		}

		addCodeLine("pages.push(%s);\n", variableName);
		addCodeLine("");
	}

	private static void generateCodeForBlock(BlockDescription description) {
		String variableName = description.getName();
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
							addCodeLine("%s.addImage(\"%s\");", variableName, derivePictureURL(pictureURL));
						}
					}
					else if(animationProperty instanceof Duration) {
						addCodeLine("%s.animationSpeed = %d;", variableName, ((Duration) animationProperty).getValue());
					}
				}
			}
			else {
				System.out.println("\tATTENTION: The JavaScript Generator did not generate Code for the following Block Attribute:\n\t  > " + property.getClass().getSimpleName().replace("Impl", ""));
			}
		}
		
		addCodeLine("");
	}

	private static void generateCodeForEnemy(EnemyDescription description) {
		String variableName = description.getName();
		addCodeLine("// Enemy to the description with name \"%s\"", description.getName());

		addCodeLine("var %s = new EnemyCharacter();", variableName);
		
		int refreshTime = 200;
		
		for(EnemyAttribute property : description.getProperties()) {
			if(property instanceof Speed) {
				addCodeLine("%s.speed = %d;", variableName, ((Speed) property).getValue());
			}
			else if(property instanceof MaxLife) {
				addCodeLine("%s.life = %d;", variableName, ((MaxLife) property).getValue());
			}
			else if(property instanceof Strength) {
				addCodeLine("%s.strength = %d;", variableName, ((Strength) property).getValue());
			}
			else if(property instanceof JumpPower) {
				addCodeLine("%s.jumpPower = %d;", variableName, ((JumpPower) property).getValue());
			}
			else if(property instanceof CollisionBox) {
				addCodeLine("%s.setCollisionBox(%d, %d);", variableName, ((CollisionBox) property).getWidth(), ((CollisionBox) property).getHeight());
			}
			else if(property instanceof ShowCollisionBox) {
				addCodeLine("%s.showCollisionBox = true;", variableName);
			}
			else if(property instanceof RunAnimation) {
				for(AnimationAttribute animationProperty : ((RunAnimation) property).getValue().getProperties()) {
					if(animationProperty instanceof Pictures) {
						for(String pictureURL : ((Pictures) animationProperty).getValue()) {
							addCodeLine("%s.addRunImage(\"%s\");", variableName, derivePictureURL(pictureURL));
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
							addCodeLine("%s.addJumpImage(\"%s\");", variableName, derivePictureURL(pictureURL));
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
							addCodeLine("%s.addIdleImage(\"%s\");", variableName, derivePictureURL(pictureURL));
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
				System.out.println("\tATTENTION: The JavaScript Generator did not generate Code for the following Enemy Attribute:\n\t  > " + property.getClass().getSimpleName().replace("Impl", ""));
			}
		}

		addCodeLine("// The Generator will simply take the highest refresh rate of all animation types.");
		addCodeLine("%s.animationSpeed = %d;", variableName, refreshTime);
		addCodeLine("");
	}
	
	private static void generateCodeForHero(HeroDescription description) {
		String variableName = description.getName();
		addCodeLine("// Hero to the description with name \"%s\"", description.getName());

		addCodeLine("var %s = new HeroCharacter();", variableName);
		
		int refreshTime = 200;
		
		for(HeroAttribute property : description.getProperties()) {
			if(property instanceof Speed) {
				addCodeLine("%s.speed = %d;", variableName, ((Speed) property).getValue());
			}
			else if(property instanceof MaxLife) {
				addCodeLine("%s.life = %d;", variableName, ((MaxLife) property).getValue());
			}
			else if(property instanceof Strength) {
				addCodeLine("%s.strength = %d;", variableName, ((Strength) property).getValue());
			}
			else if(property instanceof JumpPower) {
				addCodeLine("%s.jumpPower = %d;", variableName, ((JumpPower) property).getValue());
			}
			else if(property instanceof CollisionBox) {
				addCodeLine("%s.setCollisionBox(%d, %d);", variableName, ((CollisionBox) property).getWidth(), ((CollisionBox) property).getHeight());
			}
			else if(property instanceof ShowCollisionBox) {
				addCodeLine("%s.showCollisionBox = true;", variableName);
			}
			else if(property instanceof RunAnimation) {
				for(AnimationAttribute animationProperty : ((RunAnimation) property).getValue().getProperties()) {
					if(animationProperty instanceof Pictures) {
						for(String pictureURL : ((Pictures) animationProperty).getValue()) {
							addCodeLine("%s.addRunImage(\"%s\");", variableName, derivePictureURL(pictureURL));
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
							addCodeLine("%s.addJumpImage(\"%s\");", variableName, derivePictureURL(pictureURL));
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
							addCodeLine("%s.addIdleImage(\"%s\");", variableName, derivePictureURL(pictureURL));
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
				System.out.println("\tATTENTION: The JavaScript Generator did not generate Code for the following Hero Attribute:\n\t  > " + property.getClass().getSimpleName().replace("Impl", ""));
			}
		}

		addCodeLine("// The Generator will simply take the highest refresh rate of all animation types.");
		addCodeLine("%s.animationSpeed = %d;", variableName, refreshTime);
		addCodeLine("");
	}

	private static void generateCodeForTextPage(TextPageDescription description) {
		String variableName = description.getName();

		addCodeLine("// Text Page to the description with name \"%s\"", description.getName());
		addCodeLine("var %s = new TextPage();", variableName);
		addCodeLine("%s.setPageKey(\"%s\");", variableName, variableName);
		
		for(TextPageAttribute property : description.getProperties()) {
			if(property instanceof Title) {
				addCodeLine(".title = \"%s\";", variableName, ((Title) property).getValue());
			}
			else if(property instanceof NextPage) {
				String nextPageKey = ((NextPage) property).getValue();
				addCodeLine("%s.newPageKey = \"%s\";", variableName, nextPageKey);
			}
			else if(property instanceof Text) {
				EList<String> allParagraphs = ((Text) property).getValue();
				for(String paragraph : allParagraphs) {
					addCodeLine("%s.addParagraph(\"%s\");", variableName, paragraph);
				}
			}
			else if(property instanceof FontColor) {
				addCodeLine("%s.textStyle = \"%s\";", variableName, ((FontColor) property).getValue());
			}
			else if(property instanceof FontSize) {
				addCodeLine("%s.textSize = %d;", variableName, ((FontSize) property).getValue());
			}
			else if(property instanceof BackgroundImage) {
				addCodeLine("%s.setBackgroundImage(\"%s\");", variableName, derivePictureURL(((BackgroundImage) property).getValue()));
			}
			else if(property instanceof BackgroundColor) {
				addCodeLine("%s.setBackgroundColor(\"%s\");", variableName, ((BackgroundColor) property).getValue());
			}
			else if(property instanceof StartPage) {
				addCodeLine("startPageKey = \"%s\";", variableName);
			}
			else {
				System.out.println("\tATTENTION: The JavaScript Generator did not generate Code for the following TextPage Attribute:\n\t  > " + property.getClass().getSimpleName().replace("Impl", ""));
			}
		}
		
		addCodeLine("pages.push(%s);\n", variableName);
	}

	private static void generateCodeForMenuPage(MenuPageDescription description) {
		String variableName = description.getName();

		addCodeLine("// Menu Page to the description with name \"%s\"", description.getName());
		addCodeLine("var %s = new MenuPage();", variableName);
		addCodeLine("%s.setPageKey(\"%s\");", variableName, variableName);
		
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
			else if(property instanceof FontColor) {
				addCodeLine("%s.textStyle = \"%s\";", variableName, ((FontColor) property).getValue());
			}
			else if(property instanceof FontSize) {
				addCodeLine("%s.textSize = %d;", variableName, ((FontSize) property).getValue());
			}
			else if(property instanceof Button) {
				String nextPageKey = ((Button) property).getPage();
				addCodeLine("%s.addButton(\"%s\", \"%s\");", variableName, ((Button) property).getLabel(), nextPageKey);
			}
			else if(property instanceof BackgroundImage) {
				addCodeLine("%s.setBackgroundImage(\"%s\");", variableName, derivePictureURL(((BackgroundImage) property).getValue()));
			}
			else if(property instanceof BackgroundColor) {
				addCodeLine("%s.setBackgroundColor(\"%s\");", variableName, ((BackgroundColor) property).getValue());
			}
			else if(property instanceof StartPage) {
				addCodeLine("startPageKey = \"%s\";", variableName);
			}
			else {
				System.out.println("\tATTENTION: The JavaScript Generator did not generate Code for the following MenuPage Attribute:\n\t  > " + property.getClass().getSimpleName().replace("Impl", ""));
			}
		}
		
		addCodeLine("pages.push(%s);\n", variableName);
	}

	private static void generateSetupCode() {
		addCodeLine("pages = [];");
		addCodeLine("");
	}
	
	private static void addCodeLine(String line, Object ... args) {
		_GENERATED_CODE += "\t" + String.format(line, args) + "\n";
	}

	private static Object derivePictureURL(String pictureURL) {
		return "images/" + pictureURL;
	}
}
