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

// including {@Link AbstractImageProvider} instead of extending it, so the Hashtable can be cleared on reload

public class EggImageProvider extends AbstractExtension implements IImageProvider {
protected static final String PREFIX = "de.tu_bs.cs.isf.mbse.egg.";
	
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
	 * Adds all egg related images to this provider.
	 * @see org.eclipse.graphiti.ui.platform.AbstractImageProvider#addAvailableImages
	 */
	protected void addAvailableImages() {
		// ResourcesPlugin.getWorkspace().getRoot().getProject() is null at start
		for (IProject proj : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			File dir = new File(proj.getLocation().toString() + File.separatorChar + "images"); // TODO correct image dir
			if (dir.exists()) {
				List<File> files = getImagesFromDir(dir);
				if (files != null) {
					for (File f : files) {
						String imageName = f.getName().substring(0, f.getName().lastIndexOf('.'));
						int dirEnd = dir.getAbsolutePath().length() + 1;
						int pathEnd = f.getAbsolutePath().lastIndexOf(File.separatorChar);
						String folderPrefix = f.getAbsolutePath().substring(dirEnd, pathEnd).replace(File.separatorChar, '.');
						String imageId = PREFIX + folderPrefix + '.' + imageName;
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
