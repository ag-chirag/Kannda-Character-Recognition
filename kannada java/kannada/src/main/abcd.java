package main;
import java.io.File;
import java.io.IOException;

public class abcd {

	public static void main(String[] argv) throws IOException {
		File folder = new File("E:/Training Data/");
		new abcd().rename(folder);
	}

	public void rename(final File folder) {
		int i = 1; 
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				i=1;
				rename(fileEntry);
			} else {
			    try {
			    	String absolutePath = fileEntry.getAbsolutePath();
			    	String filePath = absolutePath.
		    	    	     substring(0,absolutePath.lastIndexOf(File.separator));
			    	
			        File destFile = new File(filePath + "/" + i + ".png");
			        if (destFile.exists()) {
			                throw new IOException(fileEntry.getCanonicalPath() + " was not successfully renamed"); 
			        }
			        if (!fileEntry.renameTo(destFile)){
			            throw new IOException(fileEntry.getCanonicalPath() + " was not successfully renamed");
			        } 

			    } catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				} finally {
			          i++;
			    }
			}
		}
	}

}