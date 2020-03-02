package ds.bplus.data;

import static java.lang.Integer.MAX_VALUE;
import java.io.*;
import java.text.DecimalFormat;
public class RealDataFile{
	
	private double max_lat = -300;
	private double min_lat = MAX_VALUE;
	private double max_long = -300;
	private double min_long = MAX_VALUE;
	
	private double[] mins = new double[2];
	private double[] maxs = new double[2];
		
	private String filefix;
	private String filename;
	private String directory = "DataDirectory/";
	private String category = "real/"; //cluser,gaussian,real,uniform
	private String folder = "raw/";
	private String pathName = directory+category+folder;
	private String AIS_DataPath = "AIS-Data/nari_dynamic.csv";
	
	private boolean isMultiple;
	
	private int rows;
	
	public RealDataFile(int elements, String suffix) throws IOException{
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
		DecimalFormat df  = new DecimalFormat("#.######");
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
				if(Double.parseDouble(df.format(datas[6]))>max_long) {
					max_long = Double.parseDouble(datas[6]);
				}
				if(Double.parseDouble(df.format(datas[6]))<min_long) {
					min_long = Double.parseDouble(datas[6]);
				}
				if(Double.parseDouble(df.format(datas[7]))>max_lat) {
					max_lat = Double.parseDouble(datas[7]);
				}
				if(Double.parseDouble(df.format(datas[7]))<min_lat) {
					min_lat = Double.parseDouble(datas[7]);
				}
			}
			mins[0] = min_long;
			mins[1] = min_lat;
			maxs[0] = max_long;
			maxs[1] = max_lat;
			
			br.close();
			return;
		}
		
    	BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile, true));
    	BufferedReader br = new BufferedReader(new FileReader(AIS_DataPath));
    	
    	writer.append(String.valueOf(elements));
    	writer.append(' ');
    	writer.append(String.valueOf(2));
	    int cnt = 0;
	    while(((line = br.readLine())!=null)&&(cnt<elements)){
	    	writer.newLine();
	    	datas = line.split(",");
	    	
	    	if(Double.parseDouble(datas[6])>max_long) {
				max_long = Double.parseDouble(datas[6]);
			}
			if(Double.parseDouble(datas[6])<min_long) {
				min_long = Double.parseDouble(datas[6]);
			}
			if(Double.parseDouble(datas[7])>max_lat) {
				max_lat = Double.parseDouble(datas[7]);
			}
			if(Double.parseDouble(datas[7])<min_lat) {
				min_lat = Double.parseDouble(datas[7]);
			}
			
	    	writer.append(df.format(datas[6]));
	    	writer.append(' ');
	    	writer.append(df.format(datas[7]));
	    	cnt++;
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