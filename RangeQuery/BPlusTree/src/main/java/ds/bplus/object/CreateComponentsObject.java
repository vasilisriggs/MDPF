package ds.bplus.object;

public class CreateComponentsObject {
	
	private String fileName;
	
	private int totalNodeReads;
    private int totalInternalNodeReads;
    private int totalLeafNodeReads;
    private int totalOverflowReads;
    
    private int totalNodeWrites;
    private int totalInternalNodeWrites;
    private int totalLeafNodeWrites;
    private int totalOverflowWrites;
    
    private int totalSplits;
    private int totalRootSplits;
    private int totalInternalNodeSplits;
    private int totalLeafSplits;
    
    private int totalPages;
    private int totalOverflowPages;
    private int totalInternalNodes;
    private int totalLeaves;
	
	public CreateComponentsObject(String fileName,int numberOfElements, int page, int pageSize, int keySize, int entrySize, int totalNodeReads, int totalInternalNodeReads, int totalLeafNodeReads, int totalOverflowReads,
			int totalNodeWrites, int totalInternalNodeWrites, int totalLeafNodeWrites, int totalOverflowWrites, int totalSplits, int totalRootSplits,
			int totalInternalNodeSplits, int totalLeafSplits, int totalPages, int totalOverflowPages, int totalInternalNodes, int totalLeaves) {
		
		this.fileName = fileName;
		
		this.totalNodeReads = totalNodeReads;
		this.totalInternalNodeReads = totalInternalNodeReads;
		this.totalLeafNodeReads = totalLeafNodeReads;
		this.totalOverflowReads = totalOverflowReads;
		
		this.totalNodeWrites = totalNodeWrites;
		this.totalInternalNodeWrites = totalInternalNodeWrites;
		this.totalLeafNodeWrites = totalLeafNodeWrites;
		this.totalOverflowWrites = totalOverflowWrites;
		
		this.totalSplits = totalSplits;
		this.totalRootSplits = totalRootSplits;
		this.totalInternalNodeSplits = totalInternalNodeSplits;
		this.totalLeafSplits = totalLeafSplits;
		
		this.totalPages = totalPages;
		this.totalOverflowPages = totalOverflowPages;
		this.totalInternalNodes = totalInternalNodes;
		this.totalLeaves = totalLeaves;
	}
	
	public String getFileName() 
	{return fileName;}
	
	public int getTotalNodeReads() 
    {return totalNodeReads;}
    public int getTotalInternalNodeReads() 
    {return totalInternalNodeReads;}
    public int getTotalLeafNodeReads() 
    {return totalLeafNodeReads;}
    public int getTotalOverflowReads() 
    {return totalOverflowReads;}   
    
    public int getTotalNodeWrites() 
    {return totalNodeWrites;}
    public int getTotalInternalNodeWrites() 
    {return totalInternalNodeWrites;}
    public int getTotalLeafNodeWrites() 
    {return totalLeafNodeWrites;}
    public int getTotalOverflowWrites() 
    {return totalOverflowWrites;}
    
    public int getTotalSplits() 
    {return totalSplits;}
    public int getTotalRootSplits() 
    {return totalRootSplits;}
    public int getTotalInternalNodeSplits() 
    {return totalInternalNodeSplits;}
    public int getTotalLeafSplits() 
    {return totalLeafSplits;}
    
    public int getTotalPages() 
    {return totalPages;}
    public int getTotalOverflowPages() 
    {return totalOverflowPages;}
    public int getTotalInternalNodes() 
    {return totalInternalNodes;}
    public int getTotalLeaves() 
    {return totalLeaves;} 
}
