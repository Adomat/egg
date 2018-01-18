package de.tu_bs.cs.isf.mbse.eggcubator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.graphiti.platform.AbstractExtension;
import org.eclipse.graphiti.ui.platform.IImageProvider;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.ui.services.IImageService;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.block.BlockAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.character.EnemyAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.attributes.item.ItemAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationAttribute;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.AnimationDescription;
import de.tu_bs.cs.isf.mbse.egg.descriptions.auxiliary.Pictures;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedBlock;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedElement;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedEnemy;
import de.tu_bs.cs.isf.mbse.egg.level.PlacedItem;

// including {@Link AbstractImageProvider} instead of extending it, so the Hashtable can be cleared on reload

public class EggImageProvider extends AbstractExtension implements IImageProvider {

	public static final String IMG_PREFIX = "de.tu_bs.cs.isf.mbse.egg.";
	
	private String pluginId;

	private Hashtable<String, String> htKeyImage = new Hashtable<String, String>();

	public EggImageProvider() {
		addAvailableImages();
	}

	/**
	 * @see org.eclipse.graphiti.ui.platform.AbstractImageProvider#getPluginId
	 */
	final public String getPluginId() {
		return this.pluginId;
	}

	/**
	 * @see org.eclipse.graphiti.ui.platform.AbstractImageProvider#setPluginId
	 */
	final public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	/**
	 * @see org.eclipse.graphiti.ui.platform.AbstractImageProvider#getImageFilePath
	 */
	final public String getImageFilePath(String imageId) {
		Object htObject = this.htKeyImage.get(imageId);
		if (htObject instanceof String) {
			return (String) htObject;
		}
		return null;
	}

	/**
	 * @see org.eclipse.graphiti.ui.platform.AbstractImageProvider#addImageFilePath
	 */
	final protected void addImageFilePath(String imageId, String imageFilePath) {
		if (this.htKeyImage.get(imageId) != null) {
			System.err.println("Image with ID '" + imageId + "' is already registered");
			return;
		}

		this.htKeyImage.put(imageId, imageFilePath);
	}

	/**
	 * List of supported file extensions.
	 */
	private static final List<String> supportedImageTypes = Arrays.asList("png", "jpg", "jpeg", "gif");
	
	/**
	 * Creates a list of Images in given dir and it's subdirs.
	 * @param dir The directory to search in
	 * @return List of all image files found
	 */
	public List<File> getImagesFromDir(File dir) {
		ArrayList<File> result = new ArrayList<>();
	    File[] files = dir.listFiles();
	    for (File file : files) {
	    	// add file if supported and look into subdirectories for more files
	    	String name = file.getName().toLowerCase();
	        if (file.isFile() && supportedImageTypes.contains(name.substring(name.lastIndexOf('.') + 1))) {
	        	result.add(file);
	        } else if (file.isDirectory()) {
	            result.addAll(getImagesFromDir(file));
	        }
	    }
	    return result;
	}
	
	/**
	 * Convert imagePath in project to corresponding id.
	 * @return image id
	 */
	public static String getImageId(PlacedElement element) {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (!(editor instanceof LevelEditor))
			throw new IllegalStateException("Wrong editor used. Cannot use this one for this operation");
		LevelEditor levelEditor = (LevelEditor) editor;
		
		String imagePath = null;
		if (element instanceof PlacedBlock) {
			for (BlockAttribute attr : ((PlacedBlock) element).getProperties().getProperties()) {
				if (attr instanceof AnimationDescription) {
					for (AnimationAttribute animation : ((AnimationDescription) attr).getProperties()) {
						if (animation instanceof Pictures) {
							if (((Pictures) animation).getValue().size() > 0)
								imagePath = ((Pictures) animation).getValue().get(0);
							else
								throw new IllegalStateException("Block doesn't contain any image");
							break;
						}
					}
					break;
				}
			}
		} else if (element instanceof PlacedEnemy) {
			for (EnemyAttribute attr : ((PlacedEnemy) element).getProperties().getProperties()) {
				if (attr instanceof AnimationDescription) {
					for (AnimationAttribute animation : ((AnimationDescription) attr).getProperties()) {
						if (animation instanceof Pictures) {
							if (((Pictures) animation).getValue().size() > 0)
								imagePath = ((Pictures) animation).getValue().get(0);
							else
								throw new IllegalStateException("Enemy doesn't contain any image");
							break;
						}
					}
					break;
				}
			}
		} else if (element instanceof PlacedItem) {
			for (ItemAttribute attr : ((PlacedItem) element).getProperties().getProperties()) {
				if (attr instanceof AnimationDescription) {
					for (AnimationAttribute animation : ((AnimationDescription) attr).getProperties()) {
						if (animation instanceof Pictures) {
							if (((Pictures) animation).getValue().size() > 0)
								imagePath = ((Pictures) animation).getValue().get(0);
							else
								throw new IllegalStateException("Item doesn't contain any image");
							break;
						}
					}
					break;
				}
			}
		} else
			throw new IllegalStateException("Unknown element type");
		if (imagePath == null)
			throw new IllegalStateException("Image path for element must be set in order to use it");
				
		return getImageId(levelEditor.getCurrentProject(), imagePath);
	}
	
	/**
	 * Convert imagePath in project to corresponding id.
	 * @return image id
	 */
	public static String getImageId(String project, String imagePath) {
		return IMG_PREFIX + project + '.' + imagePath.substring(0, imagePath.lastIndexOf('.'));
	}

	/**
	 * Adds all egg related images to this provider.
	 * @see org.eclipse.graphiti.ui.platform.AbstractImageProvider#addAvailableImages
	 */
	protected void addAvailableImages() {
		for (IProject proj : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			File dir = new File(proj.getLocation().toString() + File.separatorChar + "images"); // TODO find correct image dir
			if (dir.exists()) {
				List<File> files = getImagesFromDir(dir);
				if (files != null) {
					for (File f : files) {
						String imageName = f.getName().substring(0, f.getName().lastIndexOf('.'));
						int dirEnd = dir.getAbsolutePath().length();
						int pathEnd = f.getAbsolutePath().lastIndexOf(File.separatorChar);
						pathEnd = pathEnd != -1 ? pathEnd : 0; // in case of no dir
						String folderPrefix = f.getAbsolutePath().substring(dirEnd, pathEnd).replace(File.separatorChar, '.');
						String imageId = IMG_PREFIX + proj.getName() + '.' + (!folderPrefix.isEmpty() ? folderPrefix + '.' : "") + imageName;
						String imagePath = "file:/" + f.getAbsolutePath();
						addImageFilePath(imageId, imagePath);
					}
				}
			}
		}
	}

	/**
	 * Clears the provided images and performs a new {@Link EggImageProvider#addAvailableImages}.
	 * IMPORTANT: be sure the images of this image provider aren't in use anymore.
	 */
	public void refreshImages() {
		IImageService imageService = GraphitiUi.getImageService();
		for (Map.Entry<String, String> id : this.htKeyImage.entrySet())
			imageService.removeImageFromRegistry(id.getKey()); // this removes and disposes the image
		this.htKeyImage.clear();
		addAvailableImages();
	}
}
