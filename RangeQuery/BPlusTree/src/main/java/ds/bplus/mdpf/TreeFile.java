package ds.bplus.mdpf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ds.bplus.bptree.BPlusConfiguration;
import ds.bplus.bptree.BPlusTree;
import ds.bplus.bptree.BPlusTreePerformanceCounter;
import ds.bplus.util.InvalidBTreeStateException;

public class TreeFile {
	private String rfDirectory = "results";
	private String treeDirectory = "bins";
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
	
	
	public TreeFile(int pageSize, int keySize, int entrySize, ResultFile rf) throws IOException, InvalidBTreeStateException{
		String readMode = "rw+";
		this.bconf = new BPlusConfiguration(pageSize,keySize,entrySize);
		this.bPerf = new BPlusTreePerformanceCounter(true);
		this.rf = rf;
		String readFile = rf.getResultFileName();
		
		String[] splitFile = readFile.split("_");
		filenameC = "MyTree_"+splitFile[0]+"_"+splitFile[1]+"_C.bin";
		filenameZ = "MyTree_"+splitFile[0]+"_"+splitFile[1]+"_Z.bin";
		pages = Integer.parseInt(splitFile[1]);
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
			return;
		}
		System.out.println("Initializing TreeFile file. Inserting...");
		BufferedReader br = new BufferedReader(new FileReader(rfDirectory+"/"+readFile));
		
		bpc = new BPlusTree(bconf,readMode,writeFileC,bPerf);
		bpz = new BPlusTree(bconf,readMode,writeFileZ,bPerf);
		
		String entry;
		long keyC;
		long keyZ;
		boolean duplicates = false;
		String[] minmax;
		String[] datas;
		String line;
		
		//
		line = br.readLine(); // reads dims
		line = br.readLine(); // reads min and max of X
		minmax = line.split(" ");
		minX = Double.parseDouble(minmax[0]);
		maxX = Double.parseDouble(minmax[1]);
		line = br.readLine(); // reads min and max of Y
		minmax = line.split(" ");
		minY = Double.parseDouble(minmax[0]);
		maxY = Double.parseDouble(minmax[1]);
		
		while((line = br.readLine()) != null) {
			datas = line.split(" ");
			entry = datas[0]+"-"+datas[1];
			keyC = Long.parseLong(datas[2],2);
			keyZ = Long.parseLong(datas[3],2);
			bpc.insertKey(keyC, entry, duplicates);
			bpz.insertKey(keyZ, entry, duplicates);
		}
		System.out.println("Insertion successful.");
		bpc.commitTree();
		bpz.commitTree();
		bpc.printCurrentConfiguration();
		br.close();
		
	}
	
	public BPlusTree getBTreeC(){
		return bpc;
	}
	public BPlusTree getBTreeZ() {
		return bpz;
	}
	
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
}
