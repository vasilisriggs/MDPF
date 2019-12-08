package ds.bplus.multiple;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import ds.bplus.mdpf.*;
import ds.bplus.object.TimeQuarters;
import ds.bplus.util.InvalidBTreeStateException;

public class TimeQuarterManager {
	
	private TimeQuarters[] tqs;
	private String dStatistics = "multi/statistics";
	private String filenameC;
	private String filenameZ;
	
	public TimeQuarterManager(DataFile[] df, ResultFile[] rf, TreeFile[] tf, TreeMods[] tm, int iterations) throws IOException, InvalidBTreeStateException {
		int numberOfFiles = df.length;
		tqs = new TimeQuarters[numberOfFiles];
		for(int i=0;i<numberOfFiles;i++) {
			tqs[i] = tm[i].resultQuarters(iterations);
		}
		String line = rf[0].getResultFileName(); // 100K_01_16_res.txt;
		String[] split = line.split("_");
		filenameC = split[0]+"_"+split[2]+"_time-results-quarters_C.txt";
		filenameZ = split[0]+"_"+split[2]+"_time-resutls-quarters_Z.txt";
		String writeFileC = dStatistics+"/"+filenameC;
		String writeFileZ = dStatistics+"/"+filenameZ;
		
		BufferedWriter writerC = new BufferedWriter(new FileWriter(writeFileC, true));
		BufferedWriter writerZ = new BufferedWriter(new FileWriter(writeFileZ, true));
		writerC.append("1st Quarter		2nd Quarter		3rd Quarter		4rd Quarter");
		writerC.newLine();
		writerZ.append("1st Quarter		2nd Quarter		3rd Quarter		4rd Quarter");
		writerZ.newLine();
		for(int i=0;i<numberOfFiles;i++) {
			writerC.append(String.valueOf(tqs[i].getTimeQAC())+"	  "+String.valueOf(tqs[i].getTimeQBC())+"		"+String.valueOf(tqs[i].getTimeQCC())+"		"+String.valueOf(tqs[i].getTimeQDC()));
			writerC.newLine();
			writerZ.append(String.valueOf(tqs[i].getTimeQAZ())+"	  "+String.valueOf(tqs[i].getTimeQBZ())+"		"+String.valueOf(tqs[i].getTimeQCZ())+"		"+String.valueOf(tqs[i].getTimeQDZ()));
			writerZ.newLine();
		}	
		writerC.close();
		writerZ.close();
	}
	
	public String getFileNameC()
	{return filenameC;}
	
	public String getFileNameZ()
	{return filenameZ;}
	
}
