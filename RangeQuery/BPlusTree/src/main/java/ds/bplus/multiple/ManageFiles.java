package ds.bplus.multiple;

import java.io.IOException;
import ds.bplus.mdpf.*;
import ds.bplus.util.InvalidBTreeStateException;

public class ManageFiles{
	private DataFile[] df;
	private ResultFile[] rf;
	private StatFile[] sf;
	private TreeFile[] tf;
	private TreeMods[] tm;
	
	private final int numberOfIterations = 1;
	
	public ManageFiles(int numberOfFiles, int elements , int pages, int pageSize, int keySize, int entrySize, boolean statFiles, boolean timeQuartersManager, boolean fileMods) throws IOException, InvalidBTreeStateException {
		String fileSuffix = String.valueOf(numberOfFiles);
		long[][] blockNums = new long[numberOfFiles][2];
		long[][] times = new long[numberOfFiles][2];
		String[][] names = new String[numberOfFiles][2];
		int charBits = fileSuffix.length();
		df = new DataFile[numberOfFiles];
		rf = new ResultFile[numberOfFiles];
		sf = new StatFile[numberOfFiles];
		tf = new TreeFile[numberOfFiles];
		tm = new TreeMods[numberOfFiles];
		System.out.println("StatFiles creation: "+statFiles);
		System.out.println("TimeQuartersManager creation: "+timeQuartersManager);
		System.out.println("FileMods creation: "+fileMods);
		for(int i=0;i<numberOfFiles;i++){
			fileSuffix = String.valueOf(i+1);
			while(fileSuffix.length()<charBits) {
				fileSuffix = "0"+fileSuffix;
			}
			df[i] = new DataFile(elements,fileSuffix);
			rf[i] = new ResultFile(df[i],pages);
			tf[i] = new TreeFile(pageSize,keySize,entrySize, rf[i]);
			tm[i] = new TreeMods(tf[i]);
			
			times[i][0] = tf[i].returnTimeC();
			times[i][1] = tf[i].returnTimeZ();
			names[i][0] = tf[i].getFilenameC();
			names[i][1] = tf[i].getFilenameZ();
			blockNums[i][0] = tf[i].getBTreeC().getTotalTreePages();
			blockNums[i][1] = tf[i].getBTreeZ().getTotalTreePages();
			if(statFiles) {
				sf[i] = new StatFile(rf[i]);  
			}
		}
		
		if(timeQuartersManager) {
			TimeQuarterManager tqm = new TimeQuarterManager(df, rf, tf, tm, numberOfIterations);
		}
		
		if(tf[0].binariesExisted()&&fileMods) {
			FileMods fm = new FileMods();
			fm.storeTreeTimes(df[0].returnFileFix(), rf[0].getPages(), tf[0].getBTreeC().getTreeConfiguration().getPageSize(), names, times);
			fm.storeTreeBlocks(df[0].returnFileFix(), rf[0].getPages(), tf[0].getBTreeC().getTreeConfiguration().getPageSize(), names, blockNums);
		}
	}
}