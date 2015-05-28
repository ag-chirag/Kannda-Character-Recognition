package temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import Utils.Utils;

public class data1 {
	public Vector<Vector<String>> list = new Vector<Vector<String>>(650);
	Vector<Integer> t2 = new Vector<Integer>();
	int[] sizes = new int[600];
	
	public void createList()
	{
		Hashtable h = new Hashtable();
		String content = null;
		
		for(int j=1; j<=599; j++)
		{
			String f = new String("E:/Binary Dataset/testing(" +j+ ").txt");
			File file = new File(f);
			try{
				if(file.exists())
				{
					content = Utils.readFile(f);
					 h.put(j, content);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			//System.out.println(h.get(j) + " " + j);
		}
		//System.out.println("Checking content" + h.get(324) + " " + 324);

		
		for(int j=1; j<=599; j++)
		{
			String dataFile = (String)h.get(j);

			String[] st = null;
			
			File f = new File("E:/Binary Dataset/testing(" +j+ ").txt");
			Vector<String> t1 = new Vector<String>();
			if(f.exists())
			{
				st = dataFile.split("\n"+"\n");
				//System.out.println("File " +j+ ": " + st.length);
				
				for(int i=0; i<st.length; i++)
				{
					//System.out.println("file: " +st[i]);
					t1.add(i, st[i]);
					//System.out.println("file_temp: " + t1.elementAt(i));
				}
				//System.out.println("file_temp size: "+t1.size());
			}
			list.add((j-1), t1);
		}		
		
//		System.out.println(list.elementAt(0).elementAt(15));	
	}
	
	public void putStringContent(String data, File f)
	{
		StringBuilder s = new StringBuilder();
		s.append(data);
		s.append(System.lineSeparator());
		try
	    {
		  FileWriter out = new FileWriter(f, true);
		  out.write(s.toString());
		 // System.out.println("Done ..........");
		  out.close();
	    }
	  catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
	}

	public void getOne(int a, int b,int k)
	{
		data1 d = new data1();
		
		String fileName = new String("E:/Binary Dataset/testing(" + (a+1) + ").txt");
		
		//System.out.println("A AND B: " +a  + " " + b);
		
		File f2 = new File("E:/oneCount/oneCount(" + k +").txt");
		
		File file = new File(fileName);
		String content = new String();
		
		int count1 = 0, count0 = 0;
		String[] st = null;
		
		try{
			if(file.exists())
			{
				content = Utils.readFile(fileName);
				st = content.split("\n"+"\n");
				//System.out.println("one COunt:" + st.length);
				for(int i = 0; i<625; i++)
					if(st[(b)].charAt(i)=='1')
						count1++;
				//System.out.println("NO of ones:" +count1);
					String x = new String(""+count1);
					d.putStringContent(x, f2);
					//for(int ab = 0; ab<10000; ab++) ;
			}
			//System.out.println("CONTENT: " + content);
			else
			{
				d.putStringContent("0", f2);
			//for(int ab = 0; ab<10000; ab++) ;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
			}
	
	public void calculateMax()
	{
		data1 d = new data1();
		
		String charInst = new String();
		String content = new String();
		File f = new File("E:/temp");
		File f1[] = f.listFiles();
		
		for(int k = 0; k< f1.length; k++)
		{
			String s = new String("E:/temp/testing(" +(k+1)+ ").txt");
			File file = new File(s);
			int[] maxArr = new int[600];
			try{
				if(file.exists())
				{
					content = Utils.readFile(s);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			for(int i=0; i<list.size(); i++)
			{
				Vector<String> t1 = new Vector<String>();
			
				t1 = list.elementAt(i);
				sizes[i] = t1.size();
			
				int max = 0;
				for(int j=0; j<t1.size(); j++)
				{
					charInst = t1.elementAt(j);
					int count = 0;
					for(int x = 0; x <2500; x++)
						if(content.charAt(x) == charInst.charAt(x))
							count++;
					if(count>max)
						max = count;
				}
				maxArr[i] = max;
			}
			//call here
			
		}
	}
	
	
}
