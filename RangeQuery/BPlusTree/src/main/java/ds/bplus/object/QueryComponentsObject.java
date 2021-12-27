package ds.bplus.object;
/**
 * This class creates a good store wrap for the statistics produced by a Query.
 * 
 * 
 * @author Vasilis
 *
 */
public class QueryComponentsObject{
	private String fileName;
	
	private int totalRecords;
	private int falsePositives;

	private int totalNodeReads;
    private int totalInternalNodeReads;
    private int totalLeafNodeReads;
    private int totalOverflowReads;
    
    private int totalSearches;
    private int totalRangeQueries;
	
	public QueryComponentsObject(String fileName, int totalRecords, int falsePositives, int totalNodeReads, int totalInternalNodeReads, int totalLeafNodeReads, int totalOverflowReads, int totalSearches, 
			int totalRangeQueries) {
		this.fileName = fileName;
		
		this.totalRecords = totalRecords;
		this.falsePositives = falsePositives;
		
		this.totalNodeReads = totalNodeReads;
		this.totalInternalNodeReads = totalInternalNodeReads;
		this.totalLeafNodeReads = totalLeafNodeReads;
		this.totalOverflowReads = totalOverflowReads;
		
		this.totalSearches = totalSearches;
		this.totalRangeQueries = totalRangeQueries;
	}
	
	public String getFileName()
	{return fileName;}
	public int getTotalRecords()
	{return totalRecords;}
	public int getFalsePositives() 
	{return falsePositives;}
	public int getTotalNodeReads()
	{return totalNodeReads;}
	public int getTotalInternalNodeReads()
	{return totalInternalNodeReads;}
	public int getTotalLeafNodeReads()
	{return totalLeafNodeReads;}
	public int getTotalOverflowReads()
	{return totalOverflowReads;}
	public int getTotalSearches() 
	{return totalSearches;}
	public int getTotalRangeQueries()
	{return totalRangeQueries;}
}
