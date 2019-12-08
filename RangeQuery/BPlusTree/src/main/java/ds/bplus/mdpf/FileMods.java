package ds.bplus.mdpf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.*;


public class FileMods {
	private String multipleDir = "multi";
	private String singleDir = "data";
	private String binaryDir = "bins";
	private String rawDir = "raw";
	private String resultsDir = "results";
	private String statResultsDir = "stat-results";
	private String statisticsDir = "statistics";
	private String treeTimesFilename = "TreeConstructionRunningTime";
	private String treeBlocksFilename = "TreeBlockNumber"; 
	public FileMods() {}
	
	/**
	 * Stores the running time of the insertion of the elements in a B+ Tree in a text file. 
	 * 
	 * @param filefix is used as a name prefix for the text file,
	 * @param pages is the number of pages per dimension,
	 * @param pageSize is the size of a block of a B+ Tree,
	 * @param names is the names of the  B+ Tree files that are being created from the insertion,
	 * @param times is the tree construction time of a tree.
	 * @throws IOException
	 */
	public void storeTreeTimes(String filefix, int pages, int pageSize, String[][] names, long[][] times) throws IOException {
		String filename = treeTimesFilename+"-"+filefix+"-"+String.valueOf(pages)+"-"+String.valueOf(pageSize)+"-"+String.valueOf(System.currentTimeMillis())+".txt";
		String writeFile = multipleDir+"/"+statisticsDir+"/"+filename;
		System.out.println(filename);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
		writer.append("Tree Construction Times for "+names.length+" Files");
		writer.newLine();
		writer.newLine();
		for(int i=0;i<names.length;i++) {
			for(int j=0;j<names[i].length;j++) {
				writer.append(names[i][j]+": "+times[i][j]);
				writer.newLine();
			}
		}
		writer.close();
		return;
	}
	/**
	 * 
	 * @param filefix is used as a name prefix for the text file
	 * @param pages is the number of pages per dimension
	 * @param pageSize is the size of a block of a B+ Tree
	 * @param names is the names of the B+ Tree files that are being created from the insertion
	 * @param blockNums is the number of blocks per tree file
	 * @throws IOException
	 */
	public void storeTreeBlocks(String filefix, int pages, int pageSize, String[][] names, long[][] blockNums) throws IOException {
		String filename = treeBlocksFilename+"-"+filefix+"-"+String.valueOf(pages)+"-"+String.valueOf(pageSize)+"-"+String.valueOf(System.currentTimeMillis())+".txt";
		String writeFile = multipleDir+"/"+statisticsDir+"/"+filename;
		System.out.println(filename);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
		writer.append("Tree Construction Times for "+names.length+" Files");
		writer.newLine();
		writer.newLine();
		for(int i=0;i<names.length;i++) {
			for(int j=0;j<names[i].length;j++) {
				writer.append(names[i][j]+": "+blockNums[i][j]);
				writer.newLine();
			}
		}
		writer.close();
		return;
	}
}
