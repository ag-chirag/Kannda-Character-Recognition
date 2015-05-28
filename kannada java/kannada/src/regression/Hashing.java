package regression;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import Utils.Utils;
import javax.imageio.ImageIO;
import main.Binarization;

public class Hashing {
	
	public int[][] imageData1 = null;
	
	public BufferedImage findAreaofInterest(int[][] imageData, BufferedImage image)
	{
		BufferedImage new_image = null;
		int tx = 0, ty = 0, bx = 0, by = 0, cn = 0;
		for (int y = 0; y < imageData.length; y++) {

			for (int x = 0; x < imageData[y].length; x++) {
				if (image.getRGB(x, y) == Color.BLACK.getRGB()) {
					if (cn == 0) {
						cn++;
						tx = x;
						ty = y;
						bx = x;
						by = y;
					} else {
						if (tx > x)
							tx = x;
						if (ty > y)
							ty = y;
						if (bx < x)
							bx = x;
						if (by < y)
							by = y;
					}
				}
			}
		}

		// ======================================

		int width = bx - tx;
		int height = by - ty;
		int[][] new_imageData = new int[height][width + 1];
		if (height > 0) {
			 new_image = new BufferedImage(width + 1, height,
					BufferedImage.TYPE_INT_RGB);

			for (int y = ty; y < by; y++) {
				for (int x = tx; x < bx; x++) {
					new_imageData[y - ty][x - tx] = imageData[y][x];
					if (imageData[y][x] == 1) {
						new_image.setRGB(x - tx, y - ty, Color.BLACK.getRGB());
					} else {
						new_image.setRGB(x - tx, y - ty, Color.WHITE.getRGB());
					}
				}
			}

			for (int y = 0; y < height; y++) {
				new_imageData[y][width] = 0;
				new_image.setRGB(width, y, Color.WHITE.getRGB());
			}
		}
		return new_image;
	}
	
	
	public void loadImage(BufferedImage image, int x, File f1, File f)
	{
		Binarization b = new Binarization();
		Hashing data = new Hashing();
		
		try{
			image = b.getBinarized(image);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		imageData1 = Utils.to2DArray(image);
		//image = data.findAreaofInterest(imageData1, image);
		//image = data.resizeImg(image, 25, 25);
		
		try {
			ImageIO.write(image, "png", f1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//int[][] imageData1 = Utils.to2DArray(image);
	
		StringBuilder toFile = new StringBuilder();
		
		for(int i = 0; i<imageData1.length; i++)
		{
			for(int j = 0; j<imageData1[i].length; j++)
			{
				System.out.print(imageData1[i][j]);
				toFile.append(imageData1[i][j]);
			}
			System.out.println();
			
			//toFile.append("hello");
			
		}
		toFile.append(System.lineSeparator());
		toFile.append(System.lineSeparator());
		  try
		    {
			  FileWriter out = new FileWriter(f, true);
	       out.write(toFile.toString());
	       System.out.println("Done ..........");
	       out.close();
		    }
		  catch(IOException ioe)
	        {
	            ioe.printStackTrace();
	        }
	}
	
	public static BufferedImage resizeImg(BufferedImage img, int newW, int newH)
    {
    int w = img.getWidth();
    int h = img.getHeight();
    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
    Graphics2D g = dimg.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
    g.dispose();
    return dimg;      
   }
	
	public static void main(String[] args)
	{
		Hashing data = new Hashing();
		BufferedImage image1 = null;
		
		//for all letters
		
		for(int j = 1; j<=594; j++)
		{
		File folder = new File("E:/Training Data/" +j);
		File[] list = folder.listFiles();
		
		File f = new File("E:/Binary Dataset/testing(" + j+ ").txt");
		
		//System.out.println("Hello" + list.length);
		
		for(int i = 1; i <= list.length; i++)
		{
			try
			{
				File f1= new File("E:/Training Data/"+j+ "/" + i + ".png");
				image1 = ImageIO.read(f1);
				data.loadImage(image1 , i, f1,f);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}	
		}
		}
		
		//for the matras
		
		for(int j = 595; j<=599; j++)
		{
			int x = j-594;
			File folder = new File("E:/Training Data/Matra/" +x);
			File[] list = folder.listFiles();
		
//			if(folder.exists())
//				System.out.println("YES");
//			else
//				System.out.println("NO");
		
			File f = new File("E:/Binary Dataset/testing(" + j+ ").txt");
		
			//System.out.println(list.length);
		
			for(int i = 1; i <= list.length; i++)
			{
				try
				{
					File f1= new File("E:/Training Data/Matra/"+x+ "/" + i + ".png");
					image1 = ImageIO.read(f1);
					//image = Scalr.resize(image1, 80);
					data.loadImage(image1 , i, f1,f);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}	
			}
		}
		
	}
}
