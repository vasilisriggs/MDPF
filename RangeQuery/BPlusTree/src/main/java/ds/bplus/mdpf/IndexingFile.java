package ds.bplus.mdpf;

import java.io.*;
import java.text.DecimalFormat;
public class IndexingFile{
	
	private String directory = "DataDirectory/"; 
	private String category; //cluser,gaussian,real,uniform
	private String dataFolder = "raw/";
	private String indexingFolder = "indexed/";
	private String dataPathName;
	private String indexingPathName;
	private String filename;
	private DataFile df;
	private int pages;
	private DecimalFormat decf  = new DecimalFormat("#.######");
	
	public IndexingFile(DataFile df,int pages) throws IOException {
		this.df = df;
		this.pages = pages;
		int n = 2;
		
		category = df.getDataCategory();
		dataPathName = directory+category+dataFolder;
		indexingPathName = directory+category+indexingFolder;
	
		// files which I am reading and writing on.
		String readFile = df.getDataFileName(); //e.g.  10K.txt or 10K_1.txt
		String[] splitFile = readFile.split("\\.");
		filename = splitFile[0]+"_"+String.valueOf(pages)+"_indexed.txt";
		String writeFile = indexingPathName+filename;
		readFile = dataPathName+readFile;
		
		File f = new File(writeFile);
		if(f.exists()) {
			System.out.println("IndexingFile file: "+filename+" already exists. Closing.");
			return;
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));

		int rbits = getBits(pages);

		double[] min = new double[2];
		double[] max = new double[2];
		min = df.getMinXY(min);
		max = df.getMaxXY(max);
		
		double tempd;
		int[] indexing;
		double[] steps;

		String[][] binswap;
		String[] bins;
		String[] binholder;

		String b;
		String z;
		String c;

		String line;
		String[] datas;
		
		// arrays initialization
		steps = new double[n];
		indexing = new int[n];
		binswap = new String[rbits][n];
		binholder = new String[rbits];
		bins = new String[n];

		// steps for each dimension
		for(int i=0;i<steps.length;i++){
			steps[i] = (double)((max[i]-min[i])/pages);
		}
		
		// write the file header into the new file.
		writer.append(String.valueOf(df.getElements()));
		writer.append(' ');
		writer.append(String.valueOf(n));
		writer.append(' ');
		writer.append("C-Index");
		writer.append(' ');
		writer.append("Z-Index");
		writer.newLine();
		writer.append(decf.format(min[0]));
		writer.append(' ');
		writer.append(decf.format(max[0]));
		writer.append(' ');
		writer.append("X");
		writer.newLine();
		writer.append(decf.format(min[1]));
		writer.append(' ');
		writer.append(decf.format(max[1]));
		writer.append(' ');
		writer.append("Y");
		
		// read the file 
		BufferedReader br = new BufferedReader(new FileReader(readFile));
		line = br.readLine();

		while((line = br.readLine()) != null){
			writer.newLine();
			datas = line.split(" ");
			// find on which pages this set of values is.
			for(int i=0;i<datas.length;i++){
				writer.append(datas[i]);
				writer.append(' ');
				tempd = Double.parseDouble(decf.format((Double.parseDouble(datas[i])-min[i])/steps[i]));
				if(tempd>=(double)pages){
					indexing[i] = pages-1;
				}else if(tempd==0.0){ //has to do with negative indexing which I'm trying to defeat with this line.
					indexing[i] = 0;
				}else{
					indexing[i] = (int)(Math.ceil(tempd)-1);
				}
			} 

			// binary represenation. rbits = 3 (for 8 pages, so i'll zero pad numbers that have less than 3 bits; 1->001, 11->011, etc.)
			for(int i=0;i<indexing.length;i++){
				b = Integer.toBinaryString(indexing[i]);
				while(b.length()<rbits){
					b = "0"+b;
				}
				bins[i] = b.toString().trim();
			}

			// interleaving.
			for(int i=0;i<bins.length;i++){
				binholder = bins[i].split("(?!^)");
				for(int j=0;j<rbits;j++){
					binswap[j][i] = binholder[j];
				}
			}
			b = "";
			for(int i=0;i<rbits;i++){
				for(int j=0;j<n;j++){
					b = b + binswap[i][j];
				}
			}
			z = b;

			//concatenate.
			b="";
			for(int i=0;i<bins.length;i++){
				b = b+bins[i];
			}
			c = b;
			writer.append(c);
			writer.append(' ');
			writer.append(z);
		}
		System.out.println("ResultFile file: "+filename+" was created successfully.");
		writer.close();
		br.close();
	}
	
	private int getBits(int a)
	{return (int)Math.ceil(Math.log10(a)/Math.log10(2));}
	
	public int getPages() 
	{return this.pages;}
	
	public String getIndexingFileName() 
	{return filename;}
	
	public DataFile getDataObject() 
	{return df;}
	
	public String getIndexingCategory()
	{return category;}
}