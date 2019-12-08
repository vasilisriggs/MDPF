package ds.bplus.object;

public class TimeQuarters {
	private long timeQAC;
	private long timeQBC;
	private long timeQCC;
	private long timeQDC;
	private long timeQAZ;
	private long timeQBZ;
	private long timeQCZ;
	private long timeQDZ;
	
	public TimeQuarters(long timeQAC, long timeQBC, long timeQCC, long timeQDC, long timeQAZ, long timeQBZ, long timeQCZ, long timeQDZ) {
		this.timeQAC = timeQAC;
		this.timeQBC = timeQBC;
		this.timeQCC = timeQCC;
		this.timeQDC = timeQDC;
		this.timeQAZ = timeQAZ;
		this.timeQBZ = timeQBZ;
		this.timeQCZ = timeQCZ;
		this.timeQDZ = timeQDZ;
	}
	
	public long getTimeQAC()
	{return this.timeQAC;}
	
	public long getTimeQBC()
	{return this.timeQBC;}
	
	public long getTimeQCC()
	{return this.timeQCC;}
	
	public long getTimeQDC()
	{return this.timeQDC;}
	
	public long getTimeQAZ()
	{return this.timeQAZ;}
	
	public long getTimeQBZ()
	{return this.timeQBZ;}
	
	public long getTimeQCZ()
	{return this.timeQCZ;}
	
	public long getTimeQDZ()
	{return this.timeQDZ;}
}
