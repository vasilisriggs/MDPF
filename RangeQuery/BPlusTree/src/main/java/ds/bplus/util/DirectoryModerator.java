package ds.bplus.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class DirectoryModerator {
	private String queryDirectory = "DataDirectory/query/";
	private String indexedFileUniform = "DataDirectory/uniform/indexed";
	private String indexedFileClusters = "DataDirectory/cluster/indexed";
	private String indexedFileAIS = "DataDirectory/ais/indexed";
	public DirectoryModerator() {}
	
	private boolean patternFlag;
	public void findIndexDensity() throws IOException{
		File uniformIndexed = new File(indexedFileAIS);
		File[] indexedList = uniformIndexed.listFiles();
		for(int i=0;i<indexedList.length;i++) {
			System.out.println(indexedList[i]);
			indexDensityCalculation(String.valueOf(indexedList[i]));		
		}
	}
	private void indexDensityCalculation(String fileName) throws IOException {	
		String[] line = fileName.split("_");
		//int numberOfIndices = Integer.parseInt(line[2]);
		int numberOfIndices = Integer.parseInt(line[1]);
		int[] arrOfIndices = new int[4096];
		//String densityFile = line[0]+"_" +line[1]+"_"+line[2]+"_density_"+line[3];
		String densityFile = line[0]+"_"+line[1]+"_density_"+line[2];
		String lines;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String[] lineBreak = new String[4];
		lines = br.readLine();
		System.out.println(lines);
		lines = br.readLine();
		lines = br.readLine();
		while((lines=br.readLine())!=null) {
			lineBreak = lines.split(" ");
			arrOfIndices[Integer.parseInt(lineBreak[2],2)]++;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(densityFile, true));
		for(int i=0;i<arrOfIndices.length;i++) {
			writer.append(i+" "+arrOfIndices[i]);
			writer.newLine();
		}
		writer.close();
		return;
	}
	
	public void findAvgOnQueryResults() throws IOException {

		int fractionsQueryCounter = 0;
		int rangeFractionsQueryCounter = 0;
		int rangeQueryCounter = 0;
		
		String[] averageResults;
		
		String[] fractionsQueryArray;
		String[] rangeFractionsQueryArray;
		String[] rangeQueryArray;
		
		String fractionsQueryWriteFile = "DataDirectory/query/QueryResults-FractionsQuery-Average-Statistics.csv";
		String rangeFractionsQueryWriteFile = "DataDirectory/query/QueryResults-RangeFractionsQuery-Average-Statistics.csv"; 
		String rangeQueryWriteFile = "DataDirectory/query/QueryResults-RangeQuery-Average-Statistics.csv";
		
		String[] line;
		
		//String[][] lines = new String[15][5];
		
		File queryDir = new File(queryDirectory);
		File[] queryDirFile = queryDir.listFiles();
		
		for(int i=1;i<queryDirFile.length;i++) {
			line = queryDirFile[i].getName().split("-");
			//System.out.println(line[1]);
			if(line[1].equals("FractionsQuery")) {
				fractionsQueryCounter++;
			}else if(line[1].equals("RangeFractionsQuery")) {
				rangeFractionsQueryCounter++;
			}else if(line[1].equals("RangeQuery")) {
				rangeQueryCounter++;
			}
		}
		
		fractionsQueryArray = new String[fractionsQueryCounter];
		rangeFractionsQueryArray = new String[rangeFractionsQueryCounter];
		rangeQueryArray = new String[rangeQueryCounter];
		
		fractionsQueryCounter = 0;
		rangeFractionsQueryCounter = 0;
		rangeQueryCounter = 0;
		
		for(int i=1;i<queryDirFile.length;i++) {
			line = queryDirFile[i].getName().split("-");
			if(line[1].equals("FractionsQuery")) {
				fractionsQueryArray[fractionsQueryCounter] = queryDirFile[i].getName();
				fractionsQueryCounter++;
			}else if(line[1].equals("RangeFractionsQuery")) {
				rangeFractionsQueryArray[rangeFractionsQueryCounter] = queryDirFile[i].getName();
				rangeFractionsQueryCounter++;
			}else if(line[1].equals("RangeQuery")) {
				rangeQueryArray[rangeQueryCounter] = queryDirFile[i].getName();
				rangeQueryCounter++;
			}
		}
		
		//System.out.println(fractionsQueryCounter+" "+rangeFractionsQueryCounter+" "+rangeQueryCounter);
		
		//Opening 3 writers.
		BufferedWriter writerFQ = new BufferedWriter(new FileWriter(fractionsQueryWriteFile, true));
		BufferedWriter writerRFQ = new BufferedWriter(new FileWriter(rangeFractionsQueryWriteFile, true));
		BufferedWriter writerRQ = new BufferedWriter(new FileWriter(rangeQueryWriteFile, true));
		
		queryHeader(writerFQ);
		queryHeader(writerRFQ);
		queryHeader(writerRQ);
		
		BufferedReader[] fqr = new BufferedReader[fractionsQueryCounter];
		BufferedReader[] rfqr = new BufferedReader[rangeFractionsQueryCounter];
		BufferedReader[] rqr = new BufferedReader[rangeQueryCounter];
		
		managePatternFiles(fractionsQueryCounter,writerFQ,fqr,fractionsQueryArray);
		managePatternFiles(rangeFractionsQueryCounter, writerRFQ, rfqr, rangeFractionsQueryArray);
		managePatternFiles(rangeQueryCounter, writerRQ, rqr, rangeQueryArray);
					
		writerFQ.close();
		writerRFQ.close();
		writerRQ.close();
	}
	
	private void queryHeader(BufferedWriter writer) throws IOException {
		writer.append("curvingMethod,numberOfElements,page,pageSize,keySize,entrySize,totalRecords,falsePositives,totalNodeReads,totalInternalNodeReads,totalLeafNodeReads,"
				+ "totalOverflowReads,totalSearches,totalRangeQueries");
		writer.newLine();
	}
	
	private String[] managePattern(BufferedWriter writer, BufferedReader reader) throws IOException {
		String patternHeader = reader.readLine();
		if(patternFlag == true) {
			writer.append(patternHeader);
			writer.newLine();
			patternFlag = false;
		}
		
		String[] line = new String[2];
		
		String[] arrayOneC = new String[14];
		String[] arrayTwoC = new String[14];
		String[] arrayThreeC = new String[14];
		String[] arrayFourC = new String[14];
		String[] arrayFiveC = new String[14];
		
		String[] arrayOneZ = new String[14];
		String[] arrayTwoZ = new String[14];
		String[] arrayThreeZ = new String[14];
		String[] arrayFourZ = new String[14];
		String[] arrayFiveZ = new String[14];
		
		int[] valueArrayC = new int[5];
		int[] valueArrayZ = new int[5];
		
		double avgC;
		double avgZ;
		
				
		arrayOneC = reader.readLine().split(",");
		arrayOneZ = reader.readLine().split(",");
		
		arrayTwoC = reader.readLine().split(",");
		arrayTwoZ = reader.readLine().split(",");
		
		arrayThreeC = reader.readLine().split(",");
		arrayThreeZ = reader.readLine().split(",");
		
		arrayFourC = reader.readLine().split(",");
		arrayFourZ = reader.readLine().split(",");
		
		arrayFiveC = reader.readLine().split(",");
		arrayFiveZ = reader.readLine().split(",");
		
		line[0] = "C"+","+arrayOneC[1]+","+arrayOneC[2]+","+arrayOneC[3]+","+arrayOneC[4]+","+arrayOneC[5];
		line[1] = "Z"+","+arrayOneZ[1]+","+arrayOneZ[2]+","+arrayOneZ[3]+","+arrayOneZ[4]+","+arrayOneZ[5];
		
		System.out.println(arrayOneC[6]);
		
		for(int i=6;i<14;i++) {
			
			valueArrayC[0] = Integer.parseInt(arrayOneC[i]);
			valueArrayC[1] = Integer.parseInt(arrayTwoC[i]);
			valueArrayC[2] = Integer.parseInt(arrayThreeC[i]);
			valueArrayC[3] = Integer.parseInt(arrayFourC[i]);
			valueArrayC[4] = Integer.parseInt(arrayFiveC[i]);
			
			avgC = BTMath.avgArr(valueArrayC);
			
			valueArrayZ[0] = Integer.parseInt(arrayOneZ[i]);
			valueArrayZ[1] = Integer.parseInt(arrayTwoZ[i]);
			valueArrayZ[2] = Integer.parseInt(arrayThreeZ[i]);
			valueArrayZ[3] = Integer.parseInt(arrayFourZ[i]);
			valueArrayZ[4] = Integer.parseInt(arrayFiveZ[i]);
			
			avgZ = BTMath.avgArr(valueArrayZ);
			
			line[0] = line[0]+","+String.valueOf(BTMath.formatDouble(avgC));
			line[1] = line[1]+","+String.valueOf(BTMath.formatDouble(avgZ));
		
		}
		
		System.out.println(line[0]);
		System.out.println(line[1]);
			
		return line;
	}
	
	private void managePatternFiles(int counter, BufferedWriter writer, BufferedReader[] reader, String[] stringFiles) throws IOException {
		String[] averageResults;
		String lines;
		for(int i=0;i<counter;i++) {
			reader[i] = new BufferedReader(new FileReader(queryDirectory+stringFiles[i]));			
			//read header to get it out of the way
			lines = reader[i].readLine();
		}
		
		for(int j=0;j<819;j++) {
			patternFlag = true;
			for(int i=0;i<counter;i++) {
				averageResults = managePattern(writer,reader[i]);
				writer.append(averageResults[0]);
				writer.newLine();
				writer.append(averageResults[1]);
				writer.newLine();
			}
		}
	}
	
	
	
	
}