package ds.bplus.mdpf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StatFile {
	private int numberOfBlocks;
	private String rfDirectory = "multi/results";
	private String sfDirectory = "multi/stat-results";
	private String filename;
	private IndexingFile rf;
	public StatFile(IndexingFile rf) throws IOException{
		this.rf = rf;
		numberOfBlocks = (rf.getPages())*(rf.getPages());
		int chars = String.valueOf(numberOfBlocks).length();
		String readFile = rf.getIndexingFileName();
		String[] splitFile = readFile.split("\\.");
		filename = splitFile[0]+"_stats.txt";
		String writeFile = sfDirectory+"/"+filename; 
		readFile = rfDirectory+"/"+readFile;
		File f = new File(writeFile);
		if(f.exists()) {
			System.out.println("StatFile file: "+filename+" already exists. Closing.");
			return;
		}
		
		int[] arrC = new int[numberOfBlocks];
		int[] arrZ = new int[numberOfBlocks];
		String line;
		
		BufferedReader br = new BufferedReader(new FileReader(readFile));
		
		String[] splitter;
		line = br.readLine();
		line = br.readLine();
		line = br.readLine();
		
		while((line = br.readLine())!=null){
			splitter = line.split(" ");
			arrC[Integer.parseInt(splitter[2],2)]++;
			arrZ[Integer.parseInt(splitter[3],2)]++;
		}
		
		br.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
		String iChars;
		String append;
		
		writer.append("INDEX	Population for C-CURVE	Population for Z-CURVE");
		
		for(int i=0;i<arrC.length;i++) {
			writer.newLine();
			iChars = String.valueOf(i);
			while(iChars.length()<chars) {
				iChars = "0"+iChars;
			}
			append = iChars+"		"+String.valueOf(arrC[i])+"		"+String.valueOf(arrZ[i]);
			writer.append(append);		
		}
		System.out.println("StatFile file: "+filename+" was created successfully.");
		writer.close();
	}
		
	public IndexingFile getResultFile() 
	{return this.rf;}
}
