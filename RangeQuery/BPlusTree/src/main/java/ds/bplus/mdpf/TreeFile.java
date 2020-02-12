package ds.bplus.mdpf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ds.bplus.bptree.BPlusConfiguration;
import ds.bplus.bptree.BPlusTree;
import ds.bplus.bptree.BPlusTreePerformanceCounter;
import ds.bplus.util.InvalidBTreeStateException;

public class TreeFile {
	//private String rfDirectory = "data/results";
	//private String treeDirectory = "data/bins";
	private String rfDirectory = "multi/results";
	private String treeDirectory = "multi/bins";
	private String stDirectory = "multi/statistics";
	private long timeC = 0L;
	private long timeZ = 0L;
	private String filenameC;
	private String filenameZ;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private ResultFile rf;
	private BPlusConfiguration bconf;
	private BPlusTreePerformanceCounter bPerf;
	private BPlusTree bpc;
	private BPlusTree bpz;
	private int pages;
	private boolean binariesExisted;
	
	public TreeFile(int pageSize, int keySize, int entrySize, ResultFile rf) throws IOException, InvalidBTreeStateException{
		
		String readMode = "rw+";
		this.bconf = new BPlusConfiguration(pageSize,keySize,entrySize);
		this.bPerf = new BPlusTreePerformanceCounter(true);
		this.rf = rf;
		
		String readFile = rf.getResultFileName(); 
		String[] splitFile = readFile.split("\\."); 
		filenameC = "Tree_"+splitFile[0]+"_"+String.valueOf(pageSize)+"-"+String.valueOf(keySize+"-"+String.valueOf(entrySize))+"_C.bin"; 													
		filenameZ = "Tree_"+splitFile[0]+"_"+String.valueOf(pageSize)+"-"+String.valueOf(keySize+"-"+String.valueOf(entrySize))+"_Z.bin";
		pages = rf.getPages();
		String writeFileC = treeDirectory+"/"+filenameC;
		String writeFileZ = treeDirectory+"/"+filenameZ;
		
		File fc = new File(writeFileC);
		File fz = new File(writeFileZ);
		
		if(fz.exists() && fc.exists()) {
			System.out.println("Tree files already exist. Reading for values.");
			readMode = "rw";
			bpc = new BPlusTree(bconf,readMode,writeFileC,bPerf);
			bpz = new BPlusTree(bconf,readMode,writeFileZ,bPerf);
			minX = rf.getDataFile().getMinLongitude();
			maxX = rf.getDataFile().getMaxLongitude();
			minY = rf.getDataFile().getMinLatitude();
			maxY = rf.getDataFile().getMaxLatitude();
			binariesExisted = true;
			return;
		}
		
		binariesExisted = false;
		
		System.out.println("Initializing TreeFile file. Inserting...");
		BufferedReader br = new BufferedReader(new FileReader(rfDirectory+"/"+readFile));
		
		bpc = new BPlusTree(bconf,readMode,writeFileC,bPerf);
		bpz = new BPlusTree(bconf,readMode,writeFileZ,bPerf);
		
		
		long start;
		long end;
		String entry;
		long keyC;
		long keyZ;
		boolean duplicates = false;
		String[] minmax;
		String[] datas;
		String line;
		
		line = br.readLine(); 
		line = br.readLine(); 
		minmax = line.split(" ");
		minX = Double.parseDouble(minmax[0]);
		maxX = Double.parseDouble(minmax[1]);
		line = br.readLine(); 
		minmax = line.split(" ");
		minY = Double.parseDouble(minmax[0]);
		maxY = Double.parseDouble(minmax[1]);
		
		String nodePrefix = "Key-Value-InsertionTime"; // -2K_1_16_1024-8-24_C.txt . etc.
		
		String writeTimeC = stDirectory+"/"+nodePrefix+"-"+splitFile[0]+"_"+String.valueOf(pageSize)+"-"+String.valueOf(keySize+"-"+String.valueOf(entrySize))+"_C.txt";
		String writeTimeZ = stDirectory+"/"+nodePrefix+"-"+splitFile[0]+"_"+String.valueOf(pageSize)+"-"+String.valueOf(keySize+"-"+String.valueOf(entrySize))+"_Z.txt";
		
		//BufferedWriter writerC = new BufferedWriter(new FileWriter(writeTimeC, true));
		//BufferedWriter writerZ = new BufferedWriter(new FileWriter(writeTimeZ, true));
		
		int cnt = 0;
		while((line = br.readLine()) != null) {
			
			cnt++;
			System.out.println(cnt);
			datas = line.split(" ");
			entry = datas[0]+","+datas[1];
			
			keyC = Long.parseLong(datas[2],2);
			keyZ = Long.parseLong(datas[3],2);
			
			start = System.currentTimeMillis();
			bpc.insertKey(keyC, entry, duplicates);
			end = System.currentTimeMillis();
			timeC = timeC + (end-start);
			
			//writerC.append(String.valueOf(end-start));
			//writerC.newLine();
			
			start = System.currentTimeMillis();
			bpz.insertKey(keyZ, entry, duplicates);
			end = System.currentTimeMillis();
			timeZ = timeZ + (end-start);
			
			//writerZ.append(String.valueOf(end-start));
			//writerZ.newLine();
		}
		
		//writerZ.close();
		//writerC.close();
		
		bpc.commitTree();
		bpz.commitTree();
		System.out.println("Insertion and TreeFiles files:"+filenameC+" "+filenameZ+" were created with success.");
		bpc.printCurrentConfiguration();
		br.close();
	}
	
	public BPlusConfiguration getBConf() 
	{return bconf;}
	
	public BPlusTreePerformanceCounter getBPerf() 
	{return bPerf;}
	
	public boolean binariesExisted() 
	{return binariesExisted;}
	
	public BPlusTree getBTreeC()
	{return bpc;}
	
	public BPlusTree getBTreeZ()
	{return bpz;}
	
	public double getMinX() 
	{return minX;}
	
	public double getMaxX() 
	{return maxX;}
	
	public double getMinY() 
	{return minY;}
	
	public double getMaxY() 
	{return maxY;}
	
	public int getPages() 
	{return pages;}
	
	public String getFilenameC() 
	{return filenameC;}
	
	public String getFilenameZ() 
	{return filenameZ;}
	
	public ResultFile getResultFile() 
	{return rf;}
	
	public long returnTimeC() 
	{return timeC;}
	
	public long returnTimeZ() 
	{return timeZ;}
}
