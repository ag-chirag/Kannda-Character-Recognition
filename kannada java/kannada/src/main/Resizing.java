package main;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Utils.Utils;

public class Resizing {

	private final static int DESIRED_WIDTH = 25;
	private final static int DESIRED_HEIGHT = 25;

	public static void main(String[] args) {
		String path = "E:/backup/Matra/";
		File folder = new File(path);
		new Resizing().resize(folder, null);

	}

	public void resize(File folder, String writeToPath) {
		int i = 1;
		String writeToBase = "E:/Matra Dataset/";
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				i = 1;
				writeToPath = writeToBase + fileEntry.getName() + "/";
				new File(writeToPath).mkdir();
				resize(fileEntry, writeToPath);
			} else {
				BufferedImage optimizedImage = null;
				try {
					optimizedImage = ImageIO.read(fileEntry);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				BufferedImage optimizedImage = Utils
//						.findAreaOfInterest(character);
				if (optimizedImage != null) {
					BufferedImage resizedImage;
					int newWidth = optimizedImage.getWidth();
					int newHeight = optimizedImage.getHeight();
					if (((newWidth > DESIRED_WIDTH) && (newHeight > DESIRED_HEIGHT))) {
						resizedImage = Utils.getResizedImage(optimizedImage,
								DESIRED_WIDTH, DESIRED_HEIGHT,
								RenderingHints.VALUE_INTERPOLATION_BILINEAR,
								true);

						resizedImage = Utils.brutBinarize(resizedImage);
//						resizedImage = Utils.generateImage(new Thinning()
//								.doZhangSuenThinning(Utils
//										.to2DArray(resizedImage)));
						
						try {
							ImageIO.write(resizedImage, "png", new File(writeToPath + i + ".png"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						i++;
					}

				}
			}
		}

	}
}
