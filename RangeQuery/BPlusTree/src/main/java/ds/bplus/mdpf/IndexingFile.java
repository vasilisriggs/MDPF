package ds.bplus.mdpf;
import java.io.*;
import java.text.DecimalFormat;
import ds.bplus.util.BTMath;
public class IndexingFile{
	private String directory = "DataDirectory/"; 
	private String category; 
	private String dataFolder = "raw/";
	private String indexingFolder = "indexed/";
	private String dataPathName;
	private String indexingPathName;
	private String filename;
	private DataFile df;
	private int pages;
	private DecimalFormat decf  = new DecimalFormat("#.######");
	private String readFile;
	private String writeFile;
	/*INDEXING FILE CONSTRUCTOR
	 * 
	 */
	public IndexingFile(DataFile df,int pages) throws IOException {
		this.df = df;
		this.pages = pages;
		int n = 2;
		proccessIndex(this.df,pages,n);
	}
	private void writeFileValues(String writeFile, int n) throws IOException {
		File f = new File(writeFile);
		if(f.exists()) {
			System.out.println("IndexingFile file: "+filename+" already exists. Closing.");
			return;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
		System.out.println(writeFile);
		int rbits = BTMath.getBits(pages);
		double[] min = new double[n];
		double[] max = new double[n];
		df.getMinXY(min);
		df.getMaxXY(max);
		int[] indexing = new int[n];
		double[] steps = new double[n];
		String[] datas;		
		String line;
		// arrays initialization
		String[] bins = new String[n];
		// set the steps for each dimension
		BTMath.setSteps(steps,min,max,pages);
		// write the file header into the new file.
		createHeader(writer,min,max,n);
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
				indexing[i] = BTMath.setIndexing(datas[i],min[i],steps[i],indexing[i],pages);
			}
			// Binary represenation. rbits = 3 (for 8 pages, so i'll zero pad numbers that have less than 3 bits; 1->001, 11->011, etc.)
			BTMath.binaryPadding(indexing,bins,rbits);
			// Concatenate.
			writer.append(BTMath.concatenateXY(bins));
			writer.append(' ');
			// Interleaving
			writer.append(BTMath.interLeavingXY(bins,rbits,n));
		}
		System.out.println("IndexedFile File: "+filename+" was created successfully.");
		writer.close();
		br.close();		
	}
	private void proccessIndex(DataFile df, int pages, int n) throws IOException {
		setPathParams();
		setReadFile(); // Sets the file to read.
		setWriteFile(); // Sets the file to write on.
		writeFileValues(this.writeFile, n);
	}
	private void createHeader(BufferedWriter writer, double[] min, double[] max, int n) throws IOException{	
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
	}
	private void setReadFile() {
		String file = dataPathName + df.getDataFileName();
		this.readFile = file;
	}	
	private void setWriteFile() {
		String[] splitFile = df.getDataFileName().split("\\.");
		filename = splitFile[0]+"_"+String.valueOf(this.pages)+"_indexed.txt";
		this.writeFile = indexingPathName+filename;	
	}
	private void setPathParams() {
		category = this.df.getDataCategory();
		dataPathName = directory+category+dataFolder;
		indexingPathName = directory+category+indexingFolder;
	}
	public int getPages() 
	{return this.pages;}
	public String getIndexingPathName() 
	{return indexingPathName;}
	public String getIndexingFileName() 
	{return filename;}
	public DataFile getDataObject() 
	{return df;}
	public String getIndexingCategory()
	{return category;}
}