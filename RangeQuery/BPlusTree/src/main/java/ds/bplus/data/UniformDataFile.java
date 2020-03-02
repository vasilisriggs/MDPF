package ds.bplus.data;

import static java.lang.Integer.MAX_VALUE;
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
public class UniformDataFile{
	// *United States of America* 
	private final double lat_max = 48.682856;
	private final double lat_min = 25.712085;
	private final double long_max = -68.275846;
	private final double long_min = -124.874623;
	
	private double max_lat = -300;
	private double min_lat = MAX_VALUE;
	private double max_long = -300;
	private double min_long = MAX_VALUE;
	
	private double[] mins = new double[2];
	private double[] maxs = new double[2];
		
	private String filefix;
	private String filename;
	private String directory = "DataDirectory/";
	private String category = "uniform/"; //cluser,gaussian,real,uniform
	private String folder = "raw/";
	private String pathName = directory+category+folder;
	
	private boolean isMultiple;
	
	private int rows;
	
	public UniformDataFile(int elements, String suffix) throws IOException{
		if(!suffix.isBlank()) {
			suffix = "_"+suffix;
		}
		String line;
		String[] datas;
		this.rows = elements;
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
		filefix = String.valueOf(elementsTemp)+fileletter;
		filename = filefix+suffix+".txt";
		String writeFile = pathName+filename;
		File f = new File(writeFile);
		if(f.exists()) {
			System.out.println("DataFile file: "+filename+" already exists and will read it for minimum and maximum values");
			BufferedReader br = new BufferedReader(new FileReader(writeFile));
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
			return;
		}
		
		DecimalFormat df  = new DecimalFormat("#.######");
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
    		r_lat = this.lat_min + r1.nextDouble()*(this.lat_max - this.lat_min);
    		r_long = this.long_min + r2.nextDouble()*(this.long_max - this.long_min);
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
	
	public double getMaxLatitude()
   	{return max_lat;}
	
   	public double getMaxLongitude()
   	{return max_long;}
   	
   	public double getMinLatitude()
   	{return min_lat;}
   	
   	public double getMinLongitude()
   	{return min_long;}
   	
   	public int getElements()
   	{return rows;}
    	    	
    public double[] getMaxXY(double[] max) {
    	if(maxs.length!=max.length) {
    		return null;
    	}else{
    		for(int i=0;i<maxs.length;i++) {
    			max[i] = maxs[i];
    		}
    	}
    	return max;
    }
    public double[] getMinXY(double[] min) {
    	if(mins.length!=min.length){
    		return null;
    	}else{
    		for(int i=0;i<mins.length;i++) {
    			min[i] = mins[i];
    		}
    	}
    	return min;
    }
    
    public String getFileFix()
    {return filefix;}
    
    public String getDataDirectory() 
    {return directory;}
    
    public String getDataCategory() 
    {return category;}
    
    public String getDataFolder() 
    {return folder;}
    
    public String getDataPath() 
    {return pathName;}
    
    public String getDataFullPathName() 
    {return pathName+filename;}
    
    public String getDataFileName() 
    {return filename;}
    
    public boolean getDataMultiple()
    {return isMultiple;}
}