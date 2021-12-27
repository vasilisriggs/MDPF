package ds.bplus.mdpf;
import static java.lang.Integer.MAX_VALUE;
import java.util.*;

import ds.bplus.util.BTMath;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
public class DataFile{
	// *United States of America* 
	private final double lat_max = 48.682856;
	private final double lat_min = 25.712085;
	private final double long_max = -68.275846;
	private final double long_min = -124.874623;
	// *End of Bounds*
	private double max_lat = -300;
	private double min_lat = MAX_VALUE;
	private double max_long = -300;
	private double min_long = MAX_VALUE;
	private double[] mins = new double[2];
	private double[] maxs = new double[2];
	private String nariDynamicAton = "AIS-Data/nari_dynamic_aton.csv";
	private String nariDynamicSar = "AIS-Data/nari_dynamic_sar.csv";
	private String nariDynamic = "AIS-Data/nari_dynamic.csv";
	private int nariDynamicAtonLines = 505717;
	private int nariDynamicSarLines = 4566;
	private int nariDynamicLines = 19035630;
	private final int clusters = 5;
	private final int distPer = 10; // Distance Percentage 10%.
	private String filefix;
	private String filename;
	private String directory = "DataDirectory/";
	private String category = "ais"; // cluster, uniform, ais
	private String folder = "raw/";
	private String pathName = directory+category+"/"+folder;
	private DecimalFormat df  = new DecimalFormat("#.######"); // 6-digit decimal formatting.
	private boolean isMultiple;
	private int elements;
	/**
	 * DATA FILE CONSTRUCTOR
	 * @param elements
	 * @param suffix
	 * @throws IOException
	 */
	public DataFile(int elements, String suffix) throws IOException{
		this.elements = elements;	
		if(category=="uniform") {
			uniformProcedure(elements, suffix);
		}else if(category=="cluster") {
			clusterProcedure(elements, suffix);			
		}else if(category=="ais") {
			//proccessNariFiles(nariDynamicAton,nariDynamicAtonLines,4,5);
			//proccessNariFiles(nariDynamicSar,nariDynamicSarLines,4,5);
			proccessNariFiles(nariDynamic,nariDynamicLines,6,7);
		}else {
			System.out.println("Wrong category.");
			return;
		}
   	}		
	private String returnFilefix(String suffix) {
		if(!suffix.isBlank()) {
			suffix = "_"+suffix;
			isMultiple = true;
		}
		return suffix;
	}
	private String returnElementFix(int elements) {
		String fix;
		int timesFits = 0;
		int elementsTemp = elements;
		String fileletter = "";
		while(elementsTemp>=1000){
			elementsTemp = elementsTemp/1000;
			timesFits++;
		}
		if(timesFits==0){
			fileletter = "";
		}else if(timesFits==1){
			fileletter = "K";
		}else if(timesFits==2){
			fileletter = "M";
		}else if(timesFits==3){
			fileletter = "B";
		}
		fix = String.valueOf(elementsTemp)+fileletter;	
		return fix;
	}
	private void readOnlyFileValues(String readFile) throws IOException{
		String line;
		String[] datas;
		File f = new File(readFile);
		if(f.exists()) {
			System.out.println("DataFile file: "+filename+" already exists and will read it for minimum"
					+ " and maximum values");
			BufferedReader br = new BufferedReader(new FileReader(readFile));
			line = br.readLine();
			while((line = br.readLine())!=null){
				datas = line.split(" ");
				if(Double.parseDouble(datas[0])>max_long) {
					max_long = Double.parseDouble(datas[0]);
				}
				if(Double.parseDouble(datas[0])<min_long) {
					min_long = Double.parseDouble(datas[0]);
				}
				if(Double.parseDouble(datas[1])>max_lat) {
					max_lat = Double.parseDouble(datas[1]);
				}
				if(Double.parseDouble(datas[1])<min_lat) {
					min_lat = Double.parseDouble(datas[1]);
				}
			}
			mins[0] = min_long;
			mins[1] = min_lat;
			maxs[0] = max_long;
			maxs[1] = max_lat;
			br.close();
		}
		return;
	}
	private void writeOnlyFileValuesUniform(int elements, String writeFile) throws IOException {
		File f = new File(writeFile);
		if(!f.exists()) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
	    	Random r1 = new Random();
	    	Random r2 = new Random();
	    	double r_lat = 0.0;
	    	double r_long = 0.0;
		    	
	    	writer.append(String.valueOf(elements));
	    	writer.append(' ');
	    	writer.append(String.valueOf(2));
		    	
	    	for(int i=0;i<elements;i++){
	    		writer.newLine();
	    		r_lat = BTMath.formatDouble(this.lat_min + r1.nextDouble()*(this.lat_max - this.lat_min));
	    		r_long = BTMath.formatDouble(this.long_min + r2.nextDouble()*(this.long_max - this.long_min));
	    		writer.append(df.format(r_long));
	    		writer.append(' ');
	    		writer.append(df.format(r_lat));
	    		if(r_lat>max_lat) {
	    			max_lat = r_lat;
	    		}
	    		if(r_lat<min_lat) {
	    			min_lat = r_lat;
	    		}
	    		if(r_long>max_long) {
	    			max_long = r_long;
	    		}
	    		if(r_long<min_long){
	    			min_long = r_long;
	    		}
	    	}
	    	writer.close();	
	    	mins[0] = min_long;
	    	mins[1] = min_lat;
	    	maxs[0] = max_long;
	    	maxs[1] = max_lat;
	    	System.out.println("DataFile file: "+filename+" was created successfully.");
		}
		return;
	}	
	private void writeOnlyFileValuesCluster(int elements, String writeFile) throws IOException {
		
		File f = new File(writeFile);
		
		if(!f.exists()) {
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
			
			writer.append(String.valueOf(elements));
	    	writer.append(' ');
	    	writer.append(String.valueOf(2));
			
			Random r1 = new Random();
			Random r2 = new Random();
			
			int elementCounter = 0;
			int elementsUniform = (elements*2)/10;
			
			double[] xClusterPoints = new double[5];
			double[] yClusterPoints = new double[5];
			
			double clusterRange = 3.1;
			
			double generatedLong = 0.0;
			double generatedLat = 0.0;
			
			xClusterPoints[0] = BTMath.findDimensionDistance(long_min, long_max, 5);
			yClusterPoints[0] = BTMath.findDimensionDistance(lat_min, lat_max, 5);
			
			xClusterPoints[1] = BTMath.findDimensionDistance(long_min, long_max, 5);
			yClusterPoints[1] = BTMath.findDimensionDistance(lat_min, lat_max, 95);
			
			xClusterPoints[2] = BTMath.findDimensionDistance(long_min, long_max, 50);
			yClusterPoints[2] = BTMath.findDimensionDistance(lat_min, lat_max, 50);
			
			xClusterPoints[3] = BTMath.findDimensionDistance(long_min, long_max, 95);
			yClusterPoints[3] = BTMath.findDimensionDistance(lat_min, lat_max, 95);
			
			xClusterPoints[4] = BTMath.findDimensionDistance(long_min, long_max, 95);
			yClusterPoints[4] = BTMath.findDimensionDistance(lat_min, lat_max, 5);
			
			//generate 20% of values outside the range of the cluster centers.
			
			while(elementCounter<elementsUniform) {
				
				generatedLong = BTMath.formatDouble(this.long_min + r1.nextDouble()*(this.long_max - this.long_min));
				generatedLat = BTMath.formatDouble(this.lat_min + r2.nextDouble()*(this.lat_max - this.lat_min));
				// if not in the range of any cluster so to be considered to belong to that cluster , insert it to file.
				if(!(BTMath.inClusterRange(xClusterPoints[0],generatedLong,yClusterPoints[0],generatedLat,clusterRange)||BTMath.inClusterRange(xClusterPoints[1],generatedLong,yClusterPoints[1],generatedLat,clusterRange)
						||BTMath.inClusterRange(xClusterPoints[2],generatedLong,yClusterPoints[2],generatedLat,clusterRange)||BTMath.inClusterRange(xClusterPoints[3],generatedLong,yClusterPoints[3],generatedLat,clusterRange)
						||BTMath.inClusterRange(xClusterPoints[4],generatedLong,yClusterPoints[4],generatedLat,clusterRange))) {
					writer.newLine();
					writer.append(df.format(generatedLong));
		    		writer.append(' ');
		    		writer.append(df.format(generatedLat));
		    		
		    		if(generatedLat>max_lat) {
		    			max_lat = generatedLat;
		    		}
		    		if(generatedLat<min_lat) {
		    			min_lat = generatedLat;
		    		}
		    		if(generatedLong>max_long) {
		    			max_long = generatedLong;
		    		}
		    		if(generatedLong<min_long){
		    			min_long = generatedLong;
		    		}
		    		
		    		elementCounter++;
				}
			}
			while(elementCounter<elements) {
				generatedLong = BTMath.formatDouble(this.long_min + r1.nextDouble()*(this.long_max - this.long_min));
				generatedLat = BTMath.formatDouble(this.lat_min + r2.nextDouble()*(this.lat_max - this.lat_min));
				// if in the range of any cluster so to be considered to belong to that cluster , insert it to file.
				if((BTMath.inClusterRange(xClusterPoints[0],generatedLong,yClusterPoints[0],generatedLat,clusterRange)||BTMath.inClusterRange(xClusterPoints[1],generatedLong,yClusterPoints[1],generatedLat,clusterRange)
						||BTMath.inClusterRange(xClusterPoints[2],generatedLong,yClusterPoints[2],generatedLat,clusterRange)||BTMath.inClusterRange(xClusterPoints[3],generatedLong,yClusterPoints[3],generatedLat,clusterRange)
						||BTMath.inClusterRange(xClusterPoints[4],generatedLong,yClusterPoints[4],generatedLat,clusterRange))) {
					
					writer.newLine();
					writer.append(df.format(generatedLong));
		    		writer.append(' ');
		    		writer.append(df.format(generatedLat));
		    		
		    		if(generatedLat>max_lat) {
		    			max_lat = generatedLat;
		    		}
		    		if(generatedLat<min_lat) {
		    			min_lat = generatedLat;
		    		}
		    		if(generatedLong>max_long) {
		    			max_long = generatedLong;
		    		}
		    		if(generatedLong<min_long){
		    			min_long = generatedLong;
		    		}
		    		
		    		elementCounter++;
				}
			}
			writer.close();
			mins[0] = min_long;
	    	mins[1] = min_lat;
	    	maxs[0] = max_long;
	    	maxs[1] = max_lat;
			System.out.println("DataFile file: "+filename+" was created successfully.");
		}
		return;
	}
	private void uniformProcedure(int elements, String suffix) throws IOException {
		filefix = returnElementFix(elements);
		filename = filefix+returnFilefix(suffix)+".txt";
		String writeFile = pathName+filename;
		readOnlyFileValues(writeFile);
		writeOnlyFileValuesUniform(elements, writeFile);
	}
	private void clusterProcedure(int elements, String suffix) throws IOException {
		filefix = returnElementFix(elements);
		filename = filefix+returnFilefix(suffix)+".txt";
		String writeFile = pathName+filename;
		readOnlyFileValues(writeFile);
		writeOnlyFileValuesCluster(elements, writeFile);
	}
	private void proccessNariFiles(String nariFile, int nariLines, int lonPos, int latPos) throws IOException {
		filefix = returnElementFix(nariLines);
		filename = filefix+returnFilefix("");
		String writeFile = pathName+filename;
		readOnlyFileValues(writeFile);
		writeOnlyFileValuesAIS(nariLines,nariFile,writeFile,lonPos,latPos);	
	}
	private void writeOnlyFileValuesAIS(int elements, String aisFile ,String writeFile, int lonPos, int latPos) throws IOException{
		File f = new File(writeFile);
		if(!f.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(aisFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
		    
	    	writer.append(String.valueOf(elements));
	    	writer.append(' ');
	    	writer.append(String.valueOf(2));
	    		    	
	    	String line;
	    	String[] datas;
	    	
	    	double lonTemp;
	    	double latTemp;
	    	
	    	line = br.readLine();
	    	
	    	while((line = br.readLine())!=null) {
	    		
	    		writer.newLine();
	    		datas = line.split(",");
	    		lonTemp = Double.parseDouble(datas[lonPos]);
	    		latTemp = Double.parseDouble(datas[latPos]);
	    		
	    		if(lonTemp>max_long) {
	    			max_long = lonTemp;
	    		}else if(lonTemp<min_long) {
	    			min_long = lonTemp;
	    		}
	    		if(latTemp>max_lat) {
	    			max_lat = latTemp;
	    		}else if(latTemp<min_lat) {
	    			min_lat = latTemp;
	    		}
	    		writer.append(df.format(lonTemp));
	    		writer.append(' ');
	    		writer.append(df.format(latTemp));
	    		
	    	}
	    	writer.close();	
	    	br.close();
	    	mins[0] = min_long;
	    	mins[1] = min_lat;
	    	maxs[0] = max_long;
	    	maxs[1] = max_lat;
	    	System.out.println("DataFile file: "+filename+" was created successfully.");	
		}
		return;
	}
	public void getMaxXY(double[] max) {
    	if(maxs.length!=max.length) {
    		return;
    	}else{
    		for(int i=0;i<maxs.length;i++) {
    			max[i] = maxs[i];
    		}
    	}
    }
    public void getMinXY(double[] min) {
    	if(mins.length!=min.length){
    		return;
    	}else{
    		for(int i=0;i<mins.length;i++) {
    			min[i] = mins[i];
    		}
    	}
    }
	public double getMaxLatitude()
   	{return max_lat;}
   	public double getMaxLongitude()
   	{return max_long;}  	
   	public double getMinLatitude()
   	{return min_lat;}
   	public double getMinLongitude()
   	{return min_long;}
   	public int getElements()
   	{return elements;}
    public String getFileFix()
    {return filefix;}
    public String getDataDirectory() 
    {return directory;}
    public String getDataCategory() 
    {return category+"/";}
    public String getDataFolder() 
    {return folder;}
    public String getDataPath() 
    {return pathName;}
    public String getDataFileName() 
    {return filename;}
    public String getDataFullPathName() 
    {return pathName+filename;}
    public boolean getDataMultiple()
    {return isMultiple;}
}