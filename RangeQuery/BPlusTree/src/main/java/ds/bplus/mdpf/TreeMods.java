package ds.bplus.mdpf;
import static java.lang.Long.MAX_VALUE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import ds.bplus.bptree.*;
import ds.bplus.object.*;
import ds.bplus.util.BTMath;
import ds.bplus.util.InvalidBTreeStateException;
/**
 * This class provides functions for a B+ Tree
 * @author Vasilis
 *
 */
public class TreeMods{
	private String directory = "DataDirectory/";
	private String category; //cluser,gaussian,real,uniform
	private String treeFolder = "bins/";
	private String treePathName;
	private String fileNameC;
	private String fileNameZ;
	private TreeFile tf;
	private IndexingFile inf;
	private double[] mins;
	private double[] maxs;
	private int pages;
	private QueryComponentsObject qcoC;
	private QueryComponentsObject qcoZ;
	private BPlusTreePerformanceCounter BPerfC;
	private BPlusTreePerformanceCounter BPerfZ;
	private BPlusTree bpc;
	private BPlusTree bpz;
	private double[] steps;
	private int rbits;
	private int n = 2;
	private DecimalFormat df  = new DecimalFormat("#.######");
	private boolean isMultiple;
	public TreeMods(TreeFile tf) {
		this.tf = tf;
		this.inf = this.tf.getIndexingObject();
		setParams();
	}	
	/**	This function realizes a range query. The rangeSearch on the .bin file consists of a range between the
	 * minimum and the maximum index of each Curve-List. rangeSearch([minIndex,maxIndex]). Returns all values.
	 * Refinement happens on the RangeResult object.
	 * @param lb is the lowerBound array for the 2-dim
	 * @param  ub is the upperBound array for the 2-dim
	 * @return a QueryComponentsObject object
	 * @throws InvalidBTreeStateException 
	 * @throws IOException 
	 */
	public void rangeQuery(double[] lb, double[] ub) throws IOException, InvalidBTreeStateException{
		// B+ TREE C CURVE METHOD
		bpc.getPerformanceClass().resetAllMetrics();
		ArrayList<Long> listC = resultQuery(lb,ub,"C");
		if(listC!=null) {
			RangeResult rrC = bpc.rangeSearch(BTMath.getMinIndexFromList(listC), BTMath.getMaxIndexFromList(listC), false);
			if(rrC.getQueryResult()!=null) {
				bpc.getPerformanceClass().incrementTotalRecords(rrC.getQueryResult().size());
				bpc.getPerformanceClass().incrementFalsePositives(refineQuery(lb,ub,rrC));
			}
		}
		// B+ TREE Z CURVE METHOD
		bpz.getPerformanceClass().resetAllMetrics();
		ArrayList<Long> listZ = resultQuery(lb,ub,"Z");
		if(listZ!=null) {
			RangeResult rrZ = bpz.rangeSearch(BTMath.getMinIndexFromList(listZ), BTMath.getMaxIndexFromList(listZ), false);
			if(rrZ.getQueryResult()!=null) {
				bpz.getPerformanceClass().incrementTotalRecords(rrZ.getQueryResult().size());
				bpz.getPerformanceClass().incrementFalsePositives(refineQuery(lb,ub,rrZ));
			}
		}	
		
		getQueryStatistics();
 	}		
	public void fractionsQuery(double[] lb, double[] ub) throws IOException, InvalidBTreeStateException {			
		// B+ TREE C CURVE METHOD
		bpc.getPerformanceClass().resetAllMetrics();
		ArrayList<Long> listC = resultQuery(lb,ub,"C");
		if(listC!=null) {
			SearchResult srC;
			for(int i=0;i<listC.size();i++) {
				srC = bpc.searchKey(listC.get(i), false);
				if(srC.getValues()!=null) {
					bpc.getPerformanceClass().incrementTotalRecords(srC.getValues().size());
					bpc.getPerformanceClass().incrementFalsePositives(refineQuerySearch(lb,ub,srC));
				}else {
					
				}
			}
		}
		
		
		// B+ TREE Z CURVE METHOD
		bpz.getPerformanceClass().resetAllMetrics();
		ArrayList<Long> listZ = resultQuery(lb,ub,"Z");
		if(listZ!=null) {
			SearchResult srZ;
			for(int i=0;i<listZ.size();i++) {
				srZ = bpz.searchKey(listZ.get(i), false);
				if(srZ.getValues()!=null) {
					bpz.getPerformanceClass().incrementTotalRecords(srZ.getValues().size());
					bpz.getPerformanceClass().incrementFalsePositives(refineQuerySearch(lb,ub,srZ));
				}
				
			}
		}
		
		getQueryStatistics();
		
	}
	
	public void rangeFractionsQuery(double[] lb, double[] ub) throws IOException, InvalidBTreeStateException {
		// B+ TREE C CURVING METHOD
		bpc.getPerformanceClass().resetAllMetrics();
		RangeResult rrC;	
		SearchResult srC;
		ArrayList<Long> listC = resultQuery(lb,ub,"C");
		if(listC!=null) {
			BTMath.listSort(listC,pages);
			long min = listC.get(0);
			long max = listC.get(0);
			for(int i=1;i<listC.size();i++) { // parsing all indices that where returned
				if(listC.get(i)-max>1) { //  if current index not consecutive with the last:
					if(max==min) { // if the min index is the same with the max index that means that we have an index that is alone so we:
						srC = bpc.searchKey(max, false); // we searchKey() instead of rangeSearch()
						if(srC.getValues()!=null) {
							bpc.getPerformanceClass().incrementTotalRecords(srC.getValues().size());
							bpc.getPerformanceClass().incrementFalsePositives(refineQuerySearch(lb,ub,srC));
						}		
					}else {
						rrC = bpc.rangeSearch(min, max, false);	
						if(rrC.getQueryResult()!=null) {
							bpc.getPerformanceClass().incrementTotalRecords(rrC.getQueryResult().size());
							bpc.getPerformanceClass().incrementFalsePositives(refineQuery(lb,ub,rrC));
						}
					}
					min = listC.get(i); // we move the minimum pivot to the current index
				}
				max = listC.get(i); // we always move the maximum pivot to the currect index
			}
			if(max==min) {
				srC = bpc.searchKey(max, false);
				if(srC.getValues()!=null) {
					bpc.getPerformanceClass().incrementTotalRecords(srC.getValues().size());
					bpc.getPerformanceClass().incrementFalsePositives(refineQuerySearch(lb,ub,srC));
				}
				
			}else {
				rrC = bpc.rangeSearch(min, max, false);
				if(rrC.getQueryResult()!=null) {
					bpc.getPerformanceClass().incrementTotalRecords(rrC.getQueryResult().size());
					bpc.getPerformanceClass().incrementFalsePositives(refineQuery(lb,ub,rrC));
				}
			}
		}
		
		//B+ TREE Z CURVING METHOD
		bpz.getPerformanceClass().resetAllMetrics();
		RangeResult rrZ;	
		SearchResult srZ;
		ArrayList<Long> listZ = resultQuery(lb,ub,"Z");
		if(listZ!=null) {
			BTMath.listSort(listZ,pages);
			long min = listZ.get(0);
			long max = listZ.get(0);
			for(int i=1;i<listZ.size();i++) { // parsing all indices that where returned
				if(listZ.get(i)-max>1) { //  if current index not consecutive with the last:
					if(max==min) { // if the min index is the same with the max index that means that we have an index that is alone so we:
						srZ = bpz.searchKey(max, false); // we searchKey() instead of rangeSearch()
						if(srZ.getValues()!=null) {
							bpz.getPerformanceClass().incrementTotalRecords(srZ.getValues().size());
							bpz.getPerformanceClass().incrementFalsePositives(refineQuerySearch(lb,ub,srZ));
						}		
					}else {
						rrZ = bpz.rangeSearch(min, max, false);	
						if(rrZ.getQueryResult()!=null) {
							bpz.getPerformanceClass().incrementTotalRecords(rrZ.getQueryResult().size());
							bpz.getPerformanceClass().incrementFalsePositives(refineQuery(lb,ub,rrZ));
						}
					}
					min = listZ.get(i); // we move the minimum pivot to the current index
				}
				max = listZ.get(i); // we always move the maximum pivot to the currect index
			}
			if(max==min) {
				srZ = bpz.searchKey(max, false);
				if(srZ.getValues()!=null) {
					bpz.getPerformanceClass().incrementTotalRecords(srZ.getValues().size());
					bpz.getPerformanceClass().incrementFalsePositives(refineQuerySearch(lb,ub,srZ));
				}
				
			}else {
				rrZ = bpz.rangeSearch(min, max, false);
				if(rrZ.getQueryResult()!=null) {
					bpz.getPerformanceClass().incrementTotalRecords(rrZ.getQueryResult().size());
					bpz.getPerformanceClass().incrementFalsePositives(refineQuery(lb,ub,rrZ));
				}
			}
		}		
		
		getQueryStatistics();
	}
	private int refineQuery(double[] lb, double[] ub, RangeResult rr) {
		
		BigDecimal xMin = BigDecimal.valueOf(lb[0]);
		BigDecimal yMin = BigDecimal.valueOf(lb[1]);
		
		BigDecimal xMax = BigDecimal.valueOf(ub[0]);
		BigDecimal yMax = BigDecimal.valueOf(ub[1]);
		
		int size = rr.getQueryResult().size();
		String line;
		String[] arr = new String[2];
		
		BigDecimal[] bg = new BigDecimal[2];
		
		for(int i=0;i<rr.getQueryResult().size();i++) {
			
			line = rr.getQueryResult().get(i).getValue();
			arr = line.split(",");
			bg[0] = BigDecimal.valueOf(Double.parseDouble(arr[0])); // x value
			bg[1] = BigDecimal.valueOf(Double.parseDouble(arr[1])); // y value
			
			if(!(((bg[0].compareTo(xMin)>=0)&&(bg[0].compareTo(xMax)<=0))&&((bg[1].compareTo(yMin)>=0)&&(bg[1].compareTo(yMax)<=0)))){
				rr.getQueryResult().remove(i);
				i--;
			}
		}
		return(size - rr.getQueryResult().size());
	}
	private int refineQuerySearch(double[] lb, double[] ub, SearchResult sr) {
		
		BigDecimal xMin = BigDecimal.valueOf(lb[0]);
		BigDecimal yMin = BigDecimal.valueOf(lb[1]);
		
		BigDecimal xMax = BigDecimal.valueOf(ub[0]);
		BigDecimal yMax = BigDecimal.valueOf(ub[1]);
		
		int size = sr.getValues().size();
		String line;
		String[] arr = new String[2];
		
		BigDecimal[] bg = new BigDecimal[2];
		
		for(int i=0;i<sr.getValues().size();i++) {
			
			line = sr.getValues().get(i);
			arr = line.split(",");
			bg[0] = BigDecimal.valueOf(Double.parseDouble(arr[0])); // x value
			bg[1] = BigDecimal.valueOf(Double.parseDouble(arr[1])); // y value
			
			if(!(((bg[0].compareTo(xMin)>=0)&&(bg[0].compareTo(xMax)<=0))&&((bg[1].compareTo(yMin)>=0)&&(bg[1].compareTo(yMax)<=0)))){
				sr.getValues().remove(i);
				i--;
			}
		}
		return (size - sr.getValues().size());
	}
	/**
	 * Given the bounds and the type of the space filling curve, it calculates and returns which indexes are being filled or "touched" by the bounds in the arguments.
	 * @param lb lowerBound array
	 * @param ub upperBound array
	 * @param curve Curve: C or Z
	 * @return Returns a list which holds the resulted indexes for a specific curve
	 */
	private ArrayList<Long> resultQuery(double[] lb, double[] ub, String curve){
		
		if(curve!="c" && curve!="C" && curve!="z" && curve!="Z") {
			System.out.println("Wrong Curve. Returning.");
			return null;
		}
		if(lb.length!=mins.length || lb.length!=maxs.length || ub.length!=mins.length || ub.length!=maxs.length) {
			System.out.println("Wrong dimensions. Returning.");
			return null;
		}
		for(int i=0;i<lb.length;i++) {
			if(mins[i]>lb[i]) {
				lb[i] = mins[i];
			}
			if(maxs[i]<ub[i]) {
				ub[i] = maxs[i];
			}
			if(lb[i]>maxs[i] || ub[i]<mins[i]) {
				System.out.println("Out of bounds.");
				return null;
			}
		}
		
		ArrayList<Long> longlist = new ArrayList<Long>();
		
		long[] minlong = new long[2];
		long[] maxlong = new long[2];
		long[] cnt = new long[2];
		long minIndex;
		long maxIndex;
		
		minIndex = findIndex(lb,curve);
		maxIndex = findIndex(ub,curve);		
		if(curve=="c" || curve=="C") { // breaking the indexes into the x-y decimal (of the before binary) representation.
			minlong = BTMath.reverseConcatenateXY(minIndex,rbits);
			cnt = BTMath.reverseConcatenateXY(minIndex,rbits);
			maxlong = BTMath.reverseConcatenateXY(maxIndex,rbits);
			for(cnt[0]=minlong[0];cnt[0]<=maxlong[0];cnt[0]++) { // we add every index that is between ( 2-dimensional ) the min and max.
				for(cnt[1]=minlong[1];cnt[1]<=maxlong[1];cnt[1]++) { // meaning that we parse the box to get every index(interleaved or concatenated)
					longlist.add(findIndexLong(cnt,curve)); // that is inside the lowerBound and upperBound.
				}
			}		
		}else if(curve=="z" || curve=="Z") {
			minlong = BTMath.reverseInterleaveXY(minIndex,rbits);
			cnt = BTMath.reverseInterleaveXY(minIndex,rbits);
			maxlong = BTMath.reverseInterleaveXY(maxIndex,rbits);
			for(cnt[0]=minlong[0];cnt[0]<=maxlong[0];cnt[0]++) {
				for(cnt[1]=minlong[1];cnt[1]<=maxlong[1];cnt[1]++) {
					longlist.add(findIndexLong(cnt,curve));
				}
			}
		}	
		return longlist;
	}
	/**
	 * Calculates on which index, the given point is for the specified curve 
	 * @param dataSet represents the point ( data-set )
	 * @param curve is the curve we are using at the momment
	 * @return the index that corresponds for the point and the curve
	 */
	public long findIndex(double[] dataSet, String curve) { // takes a double[] as an argument which contains a data set and returns the index 		
		String[] dataString = new String[dataSet.length];
		long index = 0; //which this data set is in.
		int[] indexing = new int[dataSet.length];
		String[] bins = new String[dataSet.length];
		for(int i=0;i<dataSet.length;i++) {
			dataString[i] = String.valueOf(BTMath.formatDouble(dataSet[i]));
			indexing[i] = BTMath.setIndexing(dataString[i], mins[i], steps[i], indexing[i], pages);
		}
		BTMath.binaryPadding(indexing, bins, rbits);
		if(curve=="C" || curve=="c") {
			index = Long.parseLong(BTMath.concatenateXY(bins),2);
		}else if(curve=="Z" || curve=="z") {
			index = Long.parseLong(BTMath.interLeavingXY(bins, rbits, n),2);
		}else {
			System.out.println("Wrong curve.");
			return 0L;
		}		
		return index;
	}
	/**
	 * 
	 * @param index is the 2-point indexes of the 2 dimensions
	 * @param curve is the curve we are on
	 * @return the space filling curve index
	 */
	private long findIndexLong(long[] index, String curve) { // same as findIndex, but here we take a long[] as an argument
		long indexer = 0L;
		String[] bins = new String[index.length];
		int[] indexing = new int[index.length];
		for(int i=0;i<index.length;i++) {
			indexing[i] = (int)index[i];
		}
		BTMath.binaryPadding(indexing, bins, rbits);
		if(curve=="c" || curve=="C") {
			indexer = Long.parseLong(BTMath.concatenateXY(bins),2);
		}else if(curve=="z" || curve=="Z") {
			indexer = Long.parseLong(BTMath.interLeavingXY(bins, rbits, n),2);
		}
		return indexer;
	}	
	private void  setParams(){
		category = tf.getIndexingObject().getIndexingCategory();
		isMultiple = tf.getIndexingObject().getDataObject().getDataMultiple();
		treePathName = directory+category+treeFolder;
		
		bpc = tf.getBTreeC();
		bpz = tf.getBTreeZ();
		
		steps = new double[2];
		mins = new double[2];
		maxs = new double[2];
		
		mins[0] = tf.getMinX();
		mins[1] = tf.getMinY();
		maxs[0] = tf.getMaxX();
		maxs[1] = tf.getMaxY();
		
		pages = tf.getPages();
		
		steps = BTMath.setSteps(steps, mins, maxs, pages);
		
		fileNameC = tf.getFileNameC(); 
		fileNameZ = tf.getFileNameZ();
		
		rbits = BTMath.getBits(pages);
	}
	private void getQueryStatistics() {
		qcoC = new QueryComponentsObject(fileNameC, tf.getBPerfC().getTotalRecords(), tf.getBPerfC().getFalsePositives(), tf.getBPerfC().getTotalNodeReads(), 
				tf.getBPerfC().getTotalInternalNodeReads(), tf.getBPerfC().getTotalLeafNodeReads(), tf.getBPerfC().getTotalOverflowReads(), 
				tf.getBPerfC().getTotalSearches(), tf.getBPerfC().getTotalRangeQueries());
		qcoZ = new QueryComponentsObject(fileNameZ, tf.getBPerfZ().getTotalRecords(), tf.getBPerfZ().getFalsePositives(), tf.getBPerfZ().getTotalNodeReads(), 
				tf.getBPerfZ().getTotalInternalNodeReads(), tf.getBPerfZ().getTotalLeafNodeReads(), tf.getBPerfZ().getTotalOverflowReads(), 
				tf.getBPerfZ().getTotalSearches(), tf.getBPerfZ().getTotalRangeQueries());
	}
	public QueryComponentsObject getQueryObjectC() 
	{return qcoC;}
	public QueryComponentsObject getQueryObjectZ() 
	{return qcoZ;}
	public TreeFile getTreeObject() 
	{return tf;}
}
