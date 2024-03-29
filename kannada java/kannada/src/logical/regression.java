package logical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import main.Document;
import Utils.Utils;
import regression.unicode;
import temp.data1;

public class regression {
	
	public void calculateMax(int k)
	{
		//Data d = new Data(); 
		data1 d = new data1();
		
		d.createList();
		
		//System.out.println(d.list.elementAt(0).elementAt(0));
		
		String charInst = new String();
		String content = new String();
		
		String s = new String("E:/temp/testing(" +(k)+ ").txt");
		File file = new File(s);
		int[] maxArr = new int[2500];
		double[] sumArr = new double[2500];
		try{
			if(file.exists())
			{
				content = Utils.readFile(s);
			}
			//else 
				//d.putStringContent("0", new File("E:/oneCount/oneCount.txt"));
			//System.out.println("CONTENT: " + content);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
			int inst1 = 0;
		//System.out.println(d.list.size());
		
		for(int i=0; i<d.list.size(); i++)
		{
			Vector<String> t1 = new Vector<String>();			
			t1 = d.list.elementAt(i);

			int max = 0, inst = 0, sum=0;
			for(int j=0; j<t1.size(); j++)
			{
				charInst = t1.elementAt(j);
				//System.out.println("HELLO");
				//System.out.println("CHARINST: " + charInst);
				int count = 0;
				for(int x = 0; x <625; x++)
					if(content.charAt(x) == charInst.charAt(x) && content.charAt(x) == '1')
						count++;
				sum = sum + count;
				if(count>max)
				{
					max = count;
					//System.out.println(max);
					inst = j;
				}
			}
			maxArr[i] = max;
			//System.out.println("INST and MAX and I: " + inst + " " + max + " " +i);
			d.getOne(i, inst, k);
		}
		//System.out.println("Maximum Values:");
		//System.out.print(maxArr[a] + " ");
			
			y = maxArr;
		
	}
	
	public int meanX = 0, meanY = 0, t1 = 0, t2 = 0;
	double b0 = 0.0;
	float b1 = 0;
	public int[] x = new int[2500], y = new int[2500];
	double[] p = new double[2500];
	public int[] diffx = new int[2500], diffy = new int[2500], sqx = new int[2500], pro = new int[2500];
	static int x1 = 0, x2 = 0;
	public static StringBuilder result = new StringBuilder();
	long startTime = 0, endTime = 0, time[] = new long[15];;
	public void getmeanX(int k)
	{
		File f = new File("E:/oneCount/oneCount(" + k + ").txt");
		int i = 0;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while((line = br.readLine())!= null)
			{
				x[i] = Integer.parseInt(line);
				meanX += (x[i]);
				i++;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		meanX = meanX/562;
		
		//System.out.println("MeanX = " + meanX);
		//System.out.print("X array values: ");
		//for(int j = 0; j<594; j++)
			//System.out.print(x[j] + " ");
		//System.out.println();
	}
	
	public void getmeanY()
	{
		for(int i=0; i<y.length; i++)
		{
			if(x[i] != 0)
			y[i] = y[i]*100/x[i];
			meanY += (y[i]);
		}
		meanY = meanY/562;
		/*System.out.println("MeanY = " + meanY);
		System.out.print("Y array values: ");
		for(int j = 0; j<594; j++)
			System.out.print(y[j] + " ");
		System.out.println();
	*/}

	public void getCoeff()
	{
		for(int i= 0; i<x.length; i++)
		{
			diffx[i] = x[i] - meanX;
			diffy[i] = y[i] - meanY;
			
			sqx[i] = diffx[i]*diffx[i];
			t1 += sqx[i];
			
			pro[i] = diffx[i]*diffy[i];
			t2 += pro[i];
		}
		//System.out.println("Sqaures sum= " +t1);
		//System.out.println("product sum= " +t2);
		
		b1 = (float)((float)(t2)/(float)(t1));
		b0 = meanY - (b1 * meanX);
		
		//System.out.println("Coefficients: " + b0 + " " + b1);
	}
	
	public void getProb()
	{
		regression r = new regression();
		double w = 0;
		//System.out.println("Probability values: ");
		for(int i = 0; i<y.length; i++)
		{
			if(x[i]!= 0)
			{
				w = (double)(((double)b0 + ((double)b1 * (double)y[i])));
				double t = Math.exp(w);
				p[i] = t/(t+1);
			}
			else
				p[i] = 0;
			//System.out.println(i + "th element" + ":" + p[i] + " " );
		}
	}
	
	int currentLine = 1, currentWord = 1;
	Document doc = Document.getInstance();
	public void findGreatest(int a)
	{
		String path = a + "";
		Document.Letter letter = doc.getLetter(path);
		unicode u = new unicode();
		double max = p[0];
		int index = 0;
		data1 d = new data1();
		u.method();
		
		for(int i = 1; i < p.length; i++)
		{
			if(p[i] > max)
			{
				max = p[i];
				index = i;
			}
		}
		
		//System.out.println("FINAL MAX AND INDEX: " + max + " " +index);
		
		System.out.println(index);
		System.out.println(u.unicodes.get((index+1)));
		
//		File f = new File("E:/result.txt");
//		d.putStringContent((String)u.unicodes.get((index+1)), f);
		if(letter.getLine().getPosition() != currentLine)
		{
			currentLine = letter.getLine().getPosition();
			currentWord = 1;
			result.append("\\n");
		}
		else if(letter.getWord().getPostion() != currentWord)
		{
			currentWord = letter.getWord().getPostion();
			result.append(" ");
		}
		result.append((String)u.unicodes.get((index+1)));
	}
	
	public void method()
	{
		regression r = new regression();
		
		File f1 = new File("E:/temp");
		File[] list1 = f1.listFiles();
		
		for(int a = 0; a < list1.length; a++)
		{
			r.calculateMax(a+1);
			r.getmeanX(a+1);
			r.getmeanY();
			r.getCoeff();
			r.getProb();
			r.findGreatest(a + 1);	
			endTime = System.nanoTime();
			//System.out.println((endTime - startTime)/1000000);
			//time[a] = (endTime - startTime)/1000000;
		}
		//	r.findTime();
		System.out.println();
	}
	
	public void findTime()
	{
		long min = 0, max = 0, sum = time[0];
		min = time[0]; max = time[0];
		
		for(int j = 1; j<15; j++)
		{
			if(time[j]>max)
				max = time[j];
			if(time[j]<min)
				min = time[j];
			sum = sum + time[j];
		}
		System.out.println("TIME: " + min + " " + max + " " + (sum/15));
	}
}
