package ds.bplus.object;

public class TimeNumberObject {
	private long timeC;
	private long timeZ;
	private int recordsC;
	private int recordsZ;
	
	public TimeNumberObject(long timeC, long timeZ, int recordsC, int recordsZ) {
		this.timeC = timeC;
		this.timeZ = timeZ;
		this.recordsC = recordsC;
		this.recordsZ = recordsZ;
	}
		
	public long getTimeC() 
	{return timeC;}
	
	public long getTimeZ() 
	{return timeZ;}
	
	public int getRecordsC() 
	{return recordsC;}
	
	public int getRecordsZ() 
	{return recordsZ;}
}
