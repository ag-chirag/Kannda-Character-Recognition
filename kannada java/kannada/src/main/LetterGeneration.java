package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Utils.Utils;


// THIS CLASS IS NO LONGER BEING USED
public class LetterGeneration {

	String toPath = null;
	public void readLinesAndCrop(String fromPath, String toPath)
	{
		this.toPath = toPath;
		File folder = new File(fromPath);
		File[] listOfFiles = folder.listFiles();
		int picNum = 0;
		for (int i =1;i <= listOfFiles.length ;i++ ) { // this is because program couldnt read the files in seq
			BufferedImage line = null;				
			try {
				line = ImageIO.read(new File(fromPath+(i) + ".png"));
				//				checkWidth(Utils.getAvgHieght(Utils.to2DArray(line)), line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//			System.out.println("E:/lines/"+i+".png"+" picNum = " + String.valueOf(picNum+1));
			picNum = findAreaOfInterestAndCropLines(Utils.to2DArray(line),
					line, ++picNum); // Saves to E:/letters


		}
	}

	private int findAreaOfInterestAndCropLines(int[][] imageData,
			BufferedImage image, int picNum) {
//		BufferedImage new_image = Utils.findAreaOfInterest(imageData, image);
		if(image !=null)
			return cropLines(image, imageData, image.getHeight(), image.getWidth(), picNum); 
		else
			return picNum;
	}

	private int cropLines(BufferedImage image, int[][] imageData, int hieght,
			int width, int picNum) {
		boolean found_first = false, entirely_white = false, reached_end = false;
		int topLeftX = -1, topRightX = -1;
		int x = 0, row = 0;
		int check_white;

		while (row < imageData.length) {
			for (x = 0; x < imageData[row].length; x++) {
				check_white = 0;
				for (int y = 0; y < imageData.length; y++) {
					if ((image.getRGB(x, y) == Color.BLACK.getRGB())) {
						if (!found_first) {
							topLeftX = x;
							found_first = true;
							break;
						}
					} else {
						check_white++;
					}
				}
				if ((check_white == imageData.length) && found_first) {
					topRightX = x;
					entirely_white = true;
					break;
				}
			}
			row++;
			if (entirely_white)
				break;
		}

		if (topLeftX == -1 && topRightX == -1) // only whitespace found
			return picNum;

		//		System.out.println("topLeftX = " + topLeftX + " topRightX-topLeftX = "
		//				+ (topRightX - topLeftX) + " hieght = " + hieght + " width = "
		//				+ width + " width - (topRightX+1) = "
		//				+ (width - (topRightX + 1)));
		BufferedImage new_image = null, remaining_image = null;
		try {
			new_image = image.getSubimage(topLeftX, 0, topRightX - topLeftX,
					hieght);
			if ((width - (topRightX + 1)) != 0) { // if the width of the
				// remaining
				// image is zero, we have
				// reached the end.
				remaining_image = image.getSubimage(topRightX + 1, 0, width
						- (topRightX + 1), hieght);
			} else
				reached_end = true;
		} catch (RasterFormatException e) {
			e.printStackTrace();
			return picNum;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ImageIO.write(new_image, "png", new File(toPath + picNum
					+ ".png"));
			if (!reached_end)
				return cropLines(remaining_image, Utils.to2DArray(remaining_image), hieght,
						remaining_image.getWidth(), picNum + 1);
			else
				return picNum;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return picNum;
	}

}
