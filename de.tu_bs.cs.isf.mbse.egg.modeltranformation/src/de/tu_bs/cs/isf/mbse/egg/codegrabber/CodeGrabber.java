package de.tu_bs.cs.isf.mbse.egg.codegrabber;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeGrabber {
	private static String _JAVASCRIPT_CODE;

	public static void main(String[] args) throws IOException {
		File classFile = new File(CodeGrabber.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		
		for(File project : classFile.getParentFile().getParentFile().listFiles()) {
			if(project.toString().endsWith("de.tu_bs.cs.isf.mbse.egg.templategame")) {
				for(File rootFile : project.listFiles()) {
					if(rootFile.toString().endsWith("index.html")) {
						String htmlContent = readFile(rootFile);

						int firstScript = htmlContent.indexOf("script");
						int lastScript = htmlContent.lastIndexOf("script");
						
						htmlContent = htmlContent.substring(0, firstScript) + "script>%s</" + htmlContent.substring(lastScript, htmlContent.length());
						
						for(File anotherRootFile : project.listFiles()) {
							if(anotherRootFile.toString().endsWith("js")) {
								getJavaScriptCodeFromFolders(anotherRootFile);
								break;
							}
						}
						
						System.out.println(_JAVASCRIPT_CODE);
						
						break;
					}
				}
				
				break;
			}
		}
	}
	
	private static void getJavaScriptCodeFromFolders(File anotherRootFile) throws IOException {
		if(anotherRootFile.isDirectory()) {
			for(File child : anotherRootFile.listFiles()) {
				getJavaScriptCodeFromFolders(child);
			}
		} else if(anotherRootFile.toString().endsWith("js")) {
			_JAVASCRIPT_CODE += "\n===============================================\n"
								+ anotherRootFile.toString()
								+ "\n===============================================\n\n\n"
								+ readFile(anotherRootFile)
								+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
		}
	}

	private static String readFile(File file) throws IOException {
		return new String(Files.readAllBytes(Paths.get(file.toURI())));
	}

}
