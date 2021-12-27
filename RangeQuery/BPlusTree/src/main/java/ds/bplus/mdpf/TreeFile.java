package ds.bplus.mdpf;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import ds.bplus.bptree.BPlusConfiguration;
import ds.bplus.bptree.BPlusTree;
import ds.bplus.bptree.BPlusTreePerformanceCounter;
import ds.bplus.object.*;
import ds.bplus.util.InvalidBTreeStateException;
public class TreeFile {
	private String fileNameBody;
	private String directory = "DataDirectory/";
	private String category; //cluser,uniform,ais
	private String treeFolder = "bins/";
	private String treePathName;
		
	private String readFile;
	private String writeFilePathC;
	private String writeFilePathZ;
	
	private String fileNameC;
	private String fileNameZ;
	
	private String cCurve = "C";
	private String zCurve = "Z";
	
	private CreateComponentsObject ccoC;
	private CreateComponentsObject ccoZ;
	
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	
	private BPlusTree bpc;
	private BPlusTree bpz;
	private BPlusConfiguration bConf;
	private BPlusTreePerformanceCounter bPerfC;
	private BPlusTreePerformanceCounter bPerfZ;
	private IndexingFile inf;
	
	private int pages;
	private boolean binariesExisted = false;
	
	public TreeFile(int pageSize, int keySize, int entrySize, IndexingFile inf) throws IOException, InvalidBTreeStateException{
		
		this.bConf = new BPlusConfiguration(pageSize,keySize,entrySize);
		this.bPerfC = new BPlusTreePerformanceCounter(true);
		this.bPerfZ = new BPlusTreePerformanceCounter(true);
		this.inf = inf;
		
		setParams();
		setFileParams(pageSize,keySize,entrySize);		
		loadTreeFiles();
		
		if(!binariesExisted()) {
			writeTreeFiles(pageSize,keySize,entrySize);
			getCreationStatistics();
		}
	}
	private void writeTreeFiles(int pageSize, int keySize, int entrySize) throws IOException, InvalidBTreeStateException {
		
		String readMode = "rw+";
		System.out.println("Initializing TreeFile file. Inserting...");
		BufferedReader br = new BufferedReader(new FileReader(readFile));
		
		bpc = new BPlusTree(bConf,readMode,writeFilePathC,bPerfC);
		bpz = new BPlusTree(bConf,readMode,writeFilePathZ,bPerfZ);
		
		bpc.setCurveMethod(cCurve);
		bpz.setCurveMethod(zCurve);
		
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
		
		while((line = br.readLine()) != null) {
			datas = line.split(" ");
			entry = datas[0]+","+datas[1];
			keyC = Long.parseLong(datas[2],2);
			keyZ = Long.parseLong(datas[3],2);
			
			bpc.insertKey(keyC, entry, duplicates);		
			bpz.insertKey(keyZ, entry, duplicates);			
		}
		bpc.commitTree();
		bpz.commitTree();
		System.out.println("Tree Files: "+fileNameC+", "+fileNameZ+" were created successfully.");
		br.close();
	}
	private void setFileParams(int pageSize, int keySize, int entrySize) {
		readFile = inf.getIndexingFileName();
		String[] splitFile = readFile.split("\\."); 
		readFile = inf.getIndexingPathName()+readFile;
		fileNameBody = splitFile[0];
		fileNameC = "Tree_"+fileNameBody+"_"+String.valueOf(pageSize)+"-"+String.valueOf(keySize+"-"+String.valueOf(entrySize))+"_C.bin";
		fileNameZ = "Tree_"+fileNameBody+"_"+String.valueOf(pageSize)+"-"+String.valueOf(keySize+"-"+String.valueOf(entrySize))+"_Z.bin";
		writeFilePathC = treePathName+fileNameC; 													
		writeFilePathZ = treePathName+fileNameZ;
	}
	private void loadTreeFiles() throws IOException, InvalidBTreeStateException {
		File fc = new File(writeFilePathC);
		File fz = new File(writeFilePathZ);
		
		if(fz.exists() && fc.exists()) {
			System.out.println("Tree Files already exist. Loading Files...");
			String readMode = "rw";
			bpc = new BPlusTree(bConf,readMode,writeFilePathC,bPerfC);
			bpz = new BPlusTree(bConf,readMode,writeFilePathZ,bPerfZ);
			minX = inf.getDataObject().getMinLongitude();
			maxX = inf.getDataObject().getMaxLongitude();
			minY = inf.getDataObject().getMinLatitude();
			maxY = inf.getDataObject().getMaxLatitude();
			binariesExisted = true;
		}
	}
	private void setParams() {
		category = inf.getIndexingCategory()+"/";
		treePathName = directory+category+treeFolder;
		pages = inf.getPages();
	}
	public int getElements() 
	{return getIndexingObject().getDataObject().getElements();}
	public BPlusConfiguration getBConf() 
	{return bConf;}
	public BPlusTreePerformanceCounter getBPerfC()
	{return bPerfC;}
	public BPlusTreePerformanceCounter getBPerfZ() 
	{return bPerfZ;}
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
	public String getFileNameC() 
	{return fileNameC;}
	public String getFileNameZ() 
	{return fileNameZ;}
	public IndexingFile getIndexingObject() 
	{return inf;}
	private void getCreationStatistics() {
		ccoC = new CreateComponentsObject(fileNameC, this.getElements(), pages, this.bConf.getPageSize(), this.bConf.getKeySize(), this.bConf.getEntrySize(), 
				bPerfC.getTotalNodeReads(), bPerfC.getTotalInternalNodeReads(), bPerfC.getTotalLeafNodeReads(), bPerfC.getTotalOverflowReads(), 
				bPerfC.getTotalNodeWrites(), bPerfC.getTotalInternalNodeWrites(), bPerfC.getTotalLeafNodeWrites(), bPerfC.getTotalOverflowWrites(), 
				bPerfC.getTotalSplits(), bPerfC.getTotalRootSplits(), bPerfC.getTotalInternalNodeSplits(), bPerfC.getTotalLeafSplits(), bPerfC.getTotalPages(), 
				bPerfC.getTotalOverflowPages(), bPerfC.getTotalInternalNodes(), bPerfC.getTotalLeaves());
		
		ccoZ = new CreateComponentsObject(fileNameZ, this.getElements(), pages, this.bConf.getPageSize(), this.bConf.getKeySize(), this.bConf.getEntrySize(), 
				bPerfZ.getTotalNodeReads(), bPerfZ.getTotalInternalNodeReads(), bPerfZ.getTotalLeafNodeReads(), bPerfZ.getTotalOverflowReads(), 
				bPerfZ.getTotalNodeWrites(), bPerfZ.getTotalInternalNodeWrites(), bPerfZ.getTotalLeafNodeWrites(), bPerfZ.getTotalOverflowWrites(), 
				bPerfZ.getTotalSplits(), bPerfZ.getTotalRootSplits(), bPerfZ.getTotalInternalNodeSplits(), bPerfZ.getTotalLeafSplits(), bPerfZ.getTotalPages(), 
				bPerfZ.getTotalOverflowPages(), bPerfZ.getTotalInternalNodes(), bPerfZ.getTotalLeaves());
	}
	public CreateComponentsObject getStatObjectC() 
	{return ccoC;}
	public CreateComponentsObject getStatObjectZ() 
	{return ccoZ;}
}