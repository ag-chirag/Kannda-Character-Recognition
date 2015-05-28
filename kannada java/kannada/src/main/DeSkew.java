package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Utils.Utils;

public class DeSkew {

    /**
     * Representation of a line in the image.
     */
    public class HoughLine {

        // count of points in the line
        public int count = 0;
        // index in matrix.
        public int index = 0;
        // the line is represented as all x, y that solve y * cos(alpha) - x *
        // sin(alpha) = d
        public double alpha;
        public double d;
    }
    // the source image
    private BufferedImage cImage;
    // the range of angles to search for lines
    private double cAlphaStart = -20;
    private double cAlphaStep = 0.2;
    private int cSteps = 40 * 5;
    // pre-calculation of sin and cos
    private double[] cSinA;
    private double[] cCosA;
    // range of d
    private double cDMin;
    private double cDStep = 1.0;
    private int cDCount;
    // count of points that fit in a line
    private int[] cHMatrix;

    // constructor
    public DeSkew(BufferedImage image) {
        this.cImage = image;
    }

    public BufferedImage rotateMyImage( double angle) {
    	BufferedImage img = cImage;
    	int w = img.getWidth();
    	int h = img.getHeight();
    	BufferedImage dimg =new BufferedImage(w, h, img.getType());
    	Graphics2D g = dimg.createGraphics();
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
    	RenderingHints.VALUE_ANTIALIAS_ON);
    	 
    	g.rotate(Math.toRadians(angle), w/2, h/2);
    	 
    	g.drawImage(img, null, 0, 0);
    	return dimg;
    	}
    
    public BufferedImage rotate(double angle)
    {
    	BufferedImage img = cImage;
        double sin = Math.abs(Math.sin(Math.toRadians(angle))),
               cos = Math.abs(Math.cos(Math.toRadians(angle)));

        int w = img.getWidth(null), h = img.getHeight(null);

        int neww = (int) Math.floor(w*cos + h*sin),
            newh = (int) Math.floor(h*cos + w*sin);

        BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_RGB);
        
        for(int x=0;x<bimg.getWidth();x++)
        	for(int y=0;y<bimg.getHeight();y++)
        		bimg.setRGB(x, y, Color.WHITE.getRGB());
        Graphics2D g = bimg.createGraphics();
        g.translate((neww-w)/2, (newh-h)/2);
        g.rotate(Math.toRadians(angle), w/2, h/2);
        g.drawRenderedImage(img, null);
        g.dispose();
        return bimg;
    }
    
    // calculate the skew angle of the image cImage
    public double getSkewAngle() {
        DeSkew.HoughLine[] hl;
        double sum = 0.0;
        int count = 0;
        
        // perform Hough Transformation
        calc();
        // top 20 of the detected lines in the image
        hl = getTop(20);

        if (hl.length >= 20) {
            // average angle of the lines
            for (int i = 0; i < 19; i++) {
                sum += hl[i].alpha;
                count++;
            }
            return (sum / count);
        } else {
            return 0.0d;
        }
    }

    // calculate the count lines in the image with most points
    private DeSkew.HoughLine[] getTop(int count) {

        DeSkew.HoughLine[] hl = new DeSkew.HoughLine[count];
        for (int i = 0; i < count; i++) {
            hl[i] = new DeSkew.HoughLine();
        }

        DeSkew.HoughLine tmp;

        for (int i = 0; i < (this.cHMatrix.length - 1); i++) {
            if (this.cHMatrix[i] > hl[count - 1].count) {
                hl[count - 1].count = this.cHMatrix[i];
                hl[count - 1].index = i;
                int j = count - 1;
                while ((j > 0) && (hl[j].count > hl[j - 1].count)) {
                    tmp = hl[j];
                    hl[j] = hl[j - 1];
                    hl[j - 1] = tmp;
                    j--;
                }
            }
        }

        int alphaIndex;
        int dIndex;
        
        for (int i = 0; i < count; i++) {
            dIndex = hl[i].index / cSteps; // integer division, no
            // remainder
            alphaIndex = hl[i].index - dIndex * cSteps;
            hl[i].alpha = getAlpha(alphaIndex);
            hl[i].d = dIndex + cDMin;		//not being used
        }

        return hl;
    }

    // Hough Transformation
    private void calc() {
        int hMin = (int) ((this.cImage.getHeight()) / 4.0);
        int hMax = (int) ((this.cImage.getHeight()) * 3.0 / 4.0);
        init();

        for (int y = hMin; y < hMax; y++) {
            for (int x = 1; x < (this.cImage.getWidth() - 2); x++) {
                // only lower edges are considered
                if (cImage.getRGB(x, y) == Color.BLACK.getRGB()) {
                    if (!(cImage.getRGB(x, y+1) == Color.BLACK.getRGB())) {
                        calc(x, y);
                    }
                }
            }
        }

    }

    // calculate all lines through the point (x,y)
    private void calc(int x, int y) {
        double d;
        int dIndex;
        int index;

        for (int alpha = 0; alpha < (this.cSteps - 1); alpha++) {
            d = y * this.cCosA[alpha] - x * this.cSinA[alpha];
            dIndex = (int) (d - this.cDMin);
            index = dIndex * this.cSteps + alpha;
            try {
                this.cHMatrix[index] += 1;
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }

    private void init() {

        double angle;

        // pre-calculation of sin and cos
        this.cSinA = new double[this.cSteps - 1];
        this.cCosA = new double[this.cSteps - 1];

        for (int i = 0; i < (this.cSteps - 1); i++) {
            angle = getAlpha(i) * Math.PI / 180.0;
            this.cSinA[i] = Math.sin(angle);
            this.cCosA[i] = Math.cos(angle);
        }

        // range of d
        this.cDMin = -this.cImage.getWidth();
        this.cDCount = (int) (2.0 * ((this.cImage.getWidth() + this.cImage.getHeight())) / this.cDStep);
        this.cHMatrix = new int[this.cDCount * this.cSteps];

    }

    public double getAlpha(int index) {
        return this.cAlphaStart + (index * this.cAlphaStep);
    }
    
    public static void deSkewLines(int picNum)
    {   	
    	File folder = new File("E:/buffer/");
		File[] listOfFiles = folder.listFiles();
		for (int i =1;i <= listOfFiles.length ;i++ ) { // this is because program couldnt read the files in seq
			
			BufferedImage image = null;
			try {
				image = ImageIO.read(new File("E:/buffer/"+(i) + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			DeSkew d = new DeSkew(image);
			BufferedImage b = d.rotate(-d.getSkewAngle());			
			picNum = new HorizontalProjectionProfile(Utils.to2DArray(b)).extractLines("E:/lines/", picNum);
			
		}
    }
}