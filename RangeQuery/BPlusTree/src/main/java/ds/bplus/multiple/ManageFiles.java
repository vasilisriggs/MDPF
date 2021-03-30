package ds.bplus.multiple;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ds.bplus.mdpf.*;
import ds.bplus.object.CreateComponentsObject;
import ds.bplus.object.QueryComponentsObject;
import ds.bplus.util.BTMath;
import ds.bplus.util.InvalidBTreeStateException;
public class ManageFiles{
	private DataFile[] df;
	private boolean toApplyQueryPattern = false;
	private IndexingFile[] inf;
	private TreeFile[] tf;
	private CreateComponentsObject[] statsC;
	private CreateComponentsObject[] statsZ;
	private QueryComponentsObject[] resultsC;
	private QueryComponentsObject[] resultsZ;
	private TreeMods[] tm;
	private double[] lb = new double[2];
	private double[] ub = new double[2];
	private String stats = "DataDirectory/statistics/CreationResults";
	private String queries = "DataDirectory/query/QueryPatterns.txt";
	private String rangeQueryFile = "DataDirectory/query/QueryResults-RangeQuery.csv";
	private String rangeFractionQueryFile = "DataDirectory/query/QueryResults-RangeFractionsQuery.csv";
	private String fractionsQueryFile = "DataDirectory/query/QueryResults-FractionsQuery.csv";
	public ManageFiles(int numberOfFiles, int elements , int pages, int pageSize, int keySize, int entrySize) throws IOException, InvalidBTreeStateException {
		String fileSuffix = String.valueOf(numberOfFiles);
		int charBits = fileSuffix.length();
		df = new DataFile[numberOfFiles];
		inf = new IndexingFile[numberOfFiles];
		tf = new TreeFile[numberOfFiles];
		statsC = new CreateComponentsObject[numberOfFiles];
		statsZ = new CreateComponentsObject[numberOfFiles];
		tm = new TreeMods[numberOfFiles];
		for(int i=0;i<numberOfFiles;i++){
			fileSuffix = String.valueOf(i+1);
			while(fileSuffix.length()<charBits) {
				fileSuffix = "0"+fileSuffix;
			}
			df[i] = new DataFile(elements,fileSuffix);
			inf[i] = new IndexingFile(df[i],pages);
			tf[i] = new TreeFile(pageSize,keySize,entrySize, inf[i]);
			if(!tf[i].binariesExisted()) {
				statsC[i] = tf[i].getStatObjectC();
				statsZ[i] = tf[i].getStatObjectZ();
			}else {
				tm[i] = new TreeMods(tf[i]);
				toApplyQueryPattern = true;
			}
		}
		stats = stats+"-"+String.valueOf(elements/1000)+"K-"+pages+"-"+pageSize+".csv";
		File queryFile = new File(stats);
		if(!queryFile.exists()){
			storeCreationResults(numberOfFiles,stats);
		}
		//generateQueryPatterns();
		
		File queryResults = new File(rangeQueryFile);
		if(!queryResults.exists()&&toApplyQueryPattern) {
			// start procedure.
			BufferedWriter writerRQ = new BufferedWriter(new FileWriter(rangeQueryFile, true));
			queryHeader(writerRQ);
			
			BufferedWriter writerRFQ = new BufferedWriter(new FileWriter(rangeFractionQueryFile, true));
			queryHeader(writerRFQ);
			
			BufferedWriter writerFQ = new BufferedWriter(new FileWriter(fractionsQueryFile, true));
			queryHeader(writerFQ);
			
			
			BufferedReader br = new BufferedReader(new FileReader(queries));
			
			double[] distance = new double[2];
			String line;
			String[] pattern;
			
			line = br.readLine();
			while((line=br.readLine())!=null) {
				pattern = line.split(" ");
				
				writerRQ.append("QueryPattern "+pattern[0]+" "+pattern[1]+" "+pattern[2]+" "+pattern[3]);
				writerRQ.newLine();
				
				writerRFQ.append("QueryPattern "+pattern[0]+" "+pattern[1]+" "+pattern[2]+" "+pattern[3]);
				writerRFQ.newLine();
				
				writerFQ.append("QueryPattern "+pattern[0]+" "+pattern[1]+" "+pattern[2]+" "+pattern[3]);
				writerFQ.newLine();
				
				for(int i=0;i<numberOfFiles;i++) {
					
					distance[0] = BTMath.formatDouble(tf[i].getMaxX()-tf[i].getMinX());
					distance[1] = BTMath.formatDouble(tf[i].getMaxY()-tf[i].getMinY());
					
					lb[0] = BTMath.formatDouble((distance[0]*Integer.parseInt(pattern[0])/(double)100)+tf[i].getMinX());
					lb[1] = BTMath.formatDouble((distance[1]*Integer.parseInt(pattern[2])/(double)100)+tf[i].getMinY());
				
					ub[0] = BTMath.formatDouble((distance[0]*Integer.parseInt(pattern[1])/(double)100)+tf[i].getMinX());
					ub[1] = BTMath.formatDouble((distance[1]*Integer.parseInt(pattern[3])/(double)100)+tf[i].getMinY());
														
					tm[i].rangeQuery(lb, ub);
					writerRQ.append(getQueryLine(tm[i].getQueryObjectC(),tf[i]));		
					writerRQ.newLine();
					writerRQ.append(getQueryLine(tm[i].getQueryObjectZ(),tf[i]));		
					writerRQ.newLine();
					
					tm[i].rangeFractionsQuery(lb, ub);
					writerRFQ.append(getQueryLine(tm[i].getQueryObjectC(),tf[i]));		
					writerRFQ.newLine();
					writerRFQ.append(getQueryLine(tm[i].getQueryObjectZ(),tf[i]));		
					writerRFQ.newLine();
					
					tm[i].fractionsQuery(lb, ub);
					writerFQ.append(getQueryLine(tm[i].getQueryObjectC(),tf[i]));		
					writerFQ.newLine();
					writerFQ.append(getQueryLine(tm[i].getQueryObjectZ(),tf[i]));		
					writerFQ.newLine();	
				}
			}
			writerRQ.close();
			writerRFQ.close();
			writerFQ.close();
			br.close();
		}	
	}
	private void queryHeader(BufferedWriter writer) throws IOException {
		writer.append("filename,numberOfElements,page,pageSize,keySize,entrySize,totalRecords,falsePositives,totalNodeReads,totalInternalNodeReads,totalLeafNodeReads,"
				+ "totalOverflowReads,totalSearches,totalRangeQueries");
		writer.newLine();
	}
	private void storeCreationResults(int numberOfFiles, String writeFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
		writer.append("filename,numberOfElements,page,pageSize,keySize,entrySize,totalNodeReads,totalInternalNodeReads,totalLeafNodeReads,totalOverflowReads,"
				+ "totalNodeWrites,totalInternalNodeWrites,totalLeafNodeWrites,totalOverflowWrites,totalSplits,totalRootSplits,totalInternalNodeSplits,"
				+ "totalLeafSplits,totalPages,totalOverflowPages,totalInternalNodes,totalLeaves");
		for(int i=0;i<numberOfFiles;i++) {
			writer.newLine();
			writer.append(getCreateLine(statsC[i],tf[i]));
			writer.newLine();
			writer.append(getCreateLine(statsZ[i],tf[i]));
		}
		writer.close();
	}
	private String getQueryLine(QueryComponentsObject stat, TreeFile tf) {
		String line = String.valueOf(stat.getFileName()+","+tf.getElements()+","+tf.getPages()+","+tf.getBConf().getPageSize()+","+tf.getBConf().getKeySize()+","+tf.getBConf().getEntrySize()+
				","+stat.getTotalRecords()+","+stat.getFalsePositives()+","+stat.getTotalNodeReads()+","+stat.getTotalInternalNodeReads()+","+stat.getTotalLeafNodeReads()+
				","+stat.getTotalOverflowReads()+","+stat.getTotalSearches()+","+stat.getTotalRangeQueries());
		System.out.println(line);
		return line;
	}
	private String getCreateLine(CreateComponentsObject stat, TreeFile tf) throws IOException{
		String line = String.valueOf(stat.getFileName()+","+tf.getElements()+","+tf.getPages()+","+tf.getBConf().getPageSize()+","+tf.getBConf().getKeySize()+
				","+tf.getBConf().getEntrySize()+","+stat.getTotalNodeReads()+","+stat.getTotalInternalNodeReads()+","+stat.getTotalLeafNodeReads()+
				","+stat.getTotalOverflowReads()+","+stat.getTotalNodeWrites()+","+stat.getTotalInternalNodeWrites()+","+stat.getTotalLeafNodeWrites()+
				","+stat.getTotalOverflowWrites()+","+stat.getTotalSplits()+","+stat.getTotalRootSplits()+","+stat.getTotalInternalNodeSplits()+
				","+stat.getTotalLeafSplits()+","+stat.getTotalPages()+","+stat.getTotalOverflowPages()+","+stat.getTotalInternalNodes()+
				","+stat.getTotalLeaves());
		return line;
	}
	public void generateQueryPatterns() throws IOException {	
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.queries, true));
		writer.append("QUERY PATTERNS - PERCENTAGE VALUES ONLY.");
		for(int i=1;i<=10;i++) {
			for(int j=1;j<=10;j++) {
				if(multiplyValues(i,j)>=10 && multiplyValues(i,j)<=20) {
					generateAllPossibleValues(i,j,writer);
				}
			}
		}
		writer.close();
	}
	private void generateAllPossibleValues(int a, int b, BufferedWriter writer) throws IOException {
		int dX = a;
		int dY = b;
		int x=0;
		int y=0;
		for(int i=0;i<=10-dX;i++) {
			x = (i+dX)*10;
			for(int j=0;j<=10-dY;j++) {
				y = (j+dY)*10;	
				writer.newLine();
				writer.append(i*10+" "+x+" "+j*10+" "+y);
			}
		}	
	}
	private int multiplyValues(int a, int b) 
	{return a*b;}
}
