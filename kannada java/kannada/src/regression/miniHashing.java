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

public class miniHashing {
	
	public int[][] imageData1 = null;
	
	
	public void loadImage(BufferedImage image, int x, File f1, File f)
	{
		Binarization b = new Binarization();
		
		try{
			image = b.getBinarized(image);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		imageData1 = Utils.to2DArray(image);
		//image = data.findAreaofInterest(imageData1, image);
		//image = Hashing.resizeImg(image, 25, 25);
		
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
	
	public void methodforDocument()
	{
		miniHashing data = new miniHashing();
		BufferedImage image1 = null;
		
		File folder = new File("E:/letters/");
		File[] list = folder.listFiles();
		
		//System.out.println("List Length = " + list.length);
		
		for(int i = 1; i <= (list.length); i++)
		{
			File f = new File("E:/temp/testing(" + i+ ").txt");
		
			try
			{
				File f1= new File("E:/letters/" + i + ".png");
				image1 = ImageIO.read(f1);
				data.loadImage(image1, i, f1, f);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}	
		}
	}
}