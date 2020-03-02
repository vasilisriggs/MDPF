package ds.bplus.mdpf;

import java.io.*;
import java.text.DecimalFormat;


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
	private DecimalFormat decf  = new DecimalFormat("#.#");
	public FileMods(){}
	
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
	
	public void cleanDir() {
		String[] folders = new String[5];
		folders[0] = "bins/";
		folders[1] = "indexed/";
		folders[2] = "raw/";
		folders[3] = "stat-results/";
		folders[4] = "statistics/";
		
		String directory = "DataDirectory/";
		
		String[] category = new String[4];
		category[0] = "cluster/";
		category[1] = "gaussian/";
		category[2] = "real/";
		category[3] = "uniform/";
		
		File[] dataDir = null;
		
		for(int i=0;i<folders.length;i++) {
			for(int j=0;j<category.length;j++) {		
				dataDir = new File(directory+category[j]+folders[i]).listFiles();
				for(int k=0;k<dataDir.length;k++) {
					dataDir[k].delete();
					System.out.println(dataDir[k]+" was deleted successfully");
				}				
			}
		}
	}
	
	public void sampling(int sampleFrequency) throws IOException {
		String dir = "multi/statistics";
		String fileFix = "Key-Value-InsertionTime";
		
		String readFile = "";
		String[] line;
		String tempS = "";
		
		double bd;
		
		long total = 0;
		int cnt = 0;
		
		BufferedReader br;
		BufferedWriter writer;
		String writeFile;
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles(); // array of relative pathnames.
		
		// I want to proccess only Key-Value-InsertionTime files.
		for(int i=0;i<listOfFiles.length;i++) {
			readFile = listOfFiles[i].getName();
			line = readFile.split("-");
			tempS = line[0]+"-"+line[1]+"-"+line[2];
			if(tempS.equals(fileFix)) {
				System.out.println(listOfFiles[i]);
				writeFile = dir+"/"+"Sampled-By-"+sampleFrequency+"-"+readFile;
				br = new BufferedReader(new FileReader(listOfFiles[i]));
				writer = new BufferedWriter(new FileWriter(writeFile, true));
				
				while((tempS = br.readLine()) != null) {
					cnt++; // 1st line
					total = total + Long.parseLong(tempS);
					if(cnt==sampleFrequency) {
						bd = Double.valueOf((double)((double)total/sampleFrequency));
						writer.append(String.valueOf(bd));
						writer.newLine();
						total = 0;
						cnt = 0;
					}
				}
				writer.close();
				br.close();
			}
		}	
	}
	
}
