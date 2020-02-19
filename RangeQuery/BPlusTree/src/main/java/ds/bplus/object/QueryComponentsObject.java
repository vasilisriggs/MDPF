package ds.bplus.object;
/**
 * This class creates a good store wrap for the values produced by a Query.
 * 
 * 
 * @author Vasilis
 *
 */
public class QueryComponentsObject{
	private long timeC;
	private long timeZ;
	private int recordsC;
	private int recordsZ;
	private int falseRegsC;
	private int falseRegsZ;
	private int leafReadsC;
	private int leafReadsZ;
	/**		
	 * 
	 * @param timeC is the time of the query for Tree-C
	 * @param timeZ is the time of the query for Tree-Z
	 * @param recordsC is the number of the records that have been recovered successfully by the query on the Tree-C
	 * @param recordsZ is the number of the records that have been recovered successfully by the query on the Tree-Z
	 * @param falseRegsC is the number of the false positive registries found on Tree-C
	 * @param falseRegsZ is the number of the false positive registries found on Tree-Z
	 */
	public QueryComponentsObject(long timeC, long timeZ, int recordsC, int recordsZ, int falseRegsC, int falseRegsZ, int leafReadsC, int leafReadsZ) {
		this.timeC = timeC;
		this.timeZ = timeZ;
		this.recordsC = recordsC;
		this.recordsZ = recordsZ;
		this.falseRegsC = falseRegsC;
		this.falseRegsZ = falseRegsZ;
		this.leafReadsC = leafReadsC;
		this.leafReadsZ = leafReadsZ;
	}
		
	public long getTimeC() 
	{return timeC;}
	
	public long getTimeZ() 
	{return timeZ;}
	
	public int getRecordsC() 
	{return recordsC;}
	
	public int getRecordsZ() 
	{return recordsZ;}
	
	public int getFalseRegsC()
	{return falseRegsC;}
	
	public int getFalseRegsZ()
	{return falseRegsZ;}
	
	public int getLeafReadsC()
	{return leafReadsC;}
	
	public int getLeadReadsZ()
	{return leafReadsZ;}
	
	
}
