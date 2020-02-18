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

import ds.bplus.bptree.*;
import ds.bplus.object.*;

import ds.bplus.util.InvalidBTreeStateException;
/**
 * This class provides functions for a B+ Tree
 * @author Vasilis
 *
 */
public class TreeMods{
	//private String tfDirectory = "data/bins";
	private String tfDirectory = "multi/bins";
	private String stDirectory = "multi/statistics";
	private String filenameC;
	private String filenameZ;
	private TreeFile tf;
	private double[] mins;
	private double[] maxs;
	private int pages;
	private BPlusTree bpc;
	private BPlusTree bpz;
	private double[] steps;
	private int rbits;
	private DecimalFormat df  = new DecimalFormat("#.######");
	private TimeQuarters tqs;
	/**
	 * TreeMods constructor
	 * @param tf TreeFile reference object
	 */
	public TreeMods(TreeFile tf) {
		this.tf = tf;
		this.bpc = tf.getBTreeC();
		this.bpz = tf.getBTreeZ();
		
		this.filenameC = tf.getFilenameC(); // Tree_2K_4_res_1024_8_24.
		this.filenameZ = tf.getFilenameZ();
		
		this.pages = tf.getPages();
		this.rbits = (int)Math.ceil(Math.log10(pages)/Math.log10(2));
		
		this.steps = new double[2];
		this.mins = new double[2];
		this.maxs = new double[2];
		
		mins[0] = tf.getMinX();
		mins[1] = tf.getMinY();
		maxs[0] = tf.getMaxX();
		maxs[1] = tf.getMaxY();
		
		for(int i=0;i<steps.length;i++){
			steps[i] = Double.parseDouble(df.format((maxs[i]-mins[i])/pages));
		}	
	}
	/**
	 * Calculates the time that rangeSearch function iterates through the four quarters of the B+ Tree indexes.
	 * 
	 * @param iterations iterations indicates how many times the program will iterate over the time calculations.
	 * @return an TimeQuarter object that holds reference to the performance times of the rangeSearch function for both C and Z bin files.
	 * @throws IOException
	 * @throws InvalidBTreeStateException
	 */
	public TimeQuarters resultQuarters(int iterations) throws IOException, InvalidBTreeStateException{ // e.g. pages == 16.
		
		long[] startAC = new long[iterations];
		long[] startBC = new long[iterations];
		long[] startCC = new long[iterations];
		long[] startDC = new long[iterations];
		
		long[] endAC = new long[iterations];
		long[] endBC = new long[iterations];
		long[] endCC = new long[iterations];
		long[] endDC = new long[iterations];
		
		long[] startAZ = new long[iterations];
		long[] startBZ = new long[iterations];
		long[] startCZ = new long[iterations];
		long[] startDZ = new long[iterations];
		
		long[] endAZ = new long[iterations];
		long[] endBZ = new long[iterations];
		long[] endCZ = new long[iterations];
		long[] endDZ = new long[iterations];
		
		long[] timeAC = new long[iterations];
		long[] timeBC = new long[iterations];
		long[] timeCC = new long[iterations];
		long[] timeDC = new long[iterations];
		long[] timeAZ = new long[iterations];
		long[] timeBZ = new long[iterations];
		long[] timeCZ = new long[iterations];
		long[] timeDZ = new long[iterations];
		
		long sumAC = 0;
		long sumBC = 0;
		long sumCC = 0;
		long sumDC = 0;
		long sumAZ = 0;
		long sumBZ = 0;
		long sumCZ = 0;
		long sumDZ = 0;
		
		long totalQuotas = (pages*pages); // 256
		long quotaA = totalQuotas/4 - 1; // 64-1 = 63
		long quotaB = quotaA + totalQuotas/4; // 63+64 = 127 
		long quotaC = quotaB + totalQuotas/4; // 127+64 = 191
		long quotaD = quotaC + totalQuotas/4; // 191+64 = 255
		
		for(int i=0;i<iterations;i++) {
			startAC[i] = System.currentTimeMillis();
			bpc.rangeSearch(0, quotaA, false); // 0-63
			endAC[i] = System.currentTimeMillis();
			
			startBC[i] = System.currentTimeMillis();
			bpc.rangeSearch(quotaA+1, quotaB, false); // 64 - 127
			endBC[i] = System.currentTimeMillis();
			
			startCC[i] = System.currentTimeMillis();
			bpc.rangeSearch(quotaB+1, quotaC, false); // 128 - 191
			endCC[i] = System.currentTimeMillis();
			
			startDC[i] = System.currentTimeMillis();
			bpc.rangeSearch(quotaC+1, quotaD, false); // 192 - 255
			endDC[i] = System.currentTimeMillis();
			
			startAZ[i] = System.currentTimeMillis();
			bpz.rangeSearch(0, quotaA, false); // 0-63
			endAZ[i] = System.currentTimeMillis();
			
			startBZ[i] = System.currentTimeMillis();
			bpz.rangeSearch(quotaA + 1, quotaB, false); // 64 - 127
			endBZ[i] = System.currentTimeMillis();
			
			startCZ[i] = System.currentTimeMillis();
			bpz.rangeSearch(quotaB+1, quotaC, false); // 128 - 191
			endCZ[i] = System.currentTimeMillis();
			
			startDZ[i] = System.currentTimeMillis();
			bpz.rangeSearch(quotaC+1, quotaD, false); // 192 - 255
			endDZ[i] = System.currentTimeMillis();
			
			
			timeAC[i] = endAC[i] - startAC[i];
			timeBC[i] = endBC[i] - startBC[i];
			timeCC[i] = endCC[i] - startCC[i];
			timeDC[i] = endDC[i] - startDC[i];
			timeAZ[i] = endAZ[i] - startAZ[i];
			timeBZ[i] = endBZ[i] - startBZ[i];
			timeCZ[i] = endCZ[i] - startCZ[i];
			timeDZ[i] = endDZ[i] - startDZ[i];
			
			sumAC = timeAC[i] + sumAC;
			sumBC = timeBC[i] + sumBC;
			sumCC = timeCC[i] + sumCC;
			sumDC = timeDC[i] + sumDC;
			sumAZ = timeAZ[i] + sumAZ;
			sumBZ = timeBZ[i] + sumBZ;
			sumCZ = timeCZ[i] + sumCZ;
			sumDZ = timeDZ[i] + sumDZ;
		}
		
		this.tqs = new TimeQuarters((sumAC/iterations),(sumBC/iterations),(sumCC/iterations),(sumDC/iterations),(sumAZ/iterations),(sumBZ/iterations),(sumCZ/iterations),(sumDZ/iterations));
		
		return tqs;
	}
	
	public void getLeafElements() throws IOException, InvalidBTreeStateException{
		// Tree_2K_1_16_res_1024-8-24_C.bin
		// Tree_3K_16_res_1024-8-24_C.bin
		
		String[] line = filenameC.split("_");
		String prefix = "LeafElements";
		// multi file
		String filefix = line[1]+"_"+line[2]+"_"+line[3]+"_"+line[4]+"_"+line[5];
		// single file
		//String filefix = line[1]+"_"+line[2]+"_"+line[4];
		String filenames = prefix+"-"+filefix;
		
		String writeFileC = stDirectory+"/"+filenames+"_C.txt";
		String writeFileZ = stDirectory+"/"+filenames+"_Z.txt";
		
		File fc = new File(writeFileC);
		File fz = new File(writeFileZ);
		
		if(fc.exists()||fz.exists()){
			System.out.println(filenames+"_C.txt or "+filenames+"_Z.txt already exists");
			return;
		}
		
		BufferedWriter writerC = new BufferedWriter(new FileWriter(writeFileC, true));
		
		long lower=0;
		long upper=0;
		long maxIndex = (pages*pages)-1;
		SearchResult sr;
		int cnt = 1;
		String chars = "";
		int maxChars = 3;
		int capacity = 0;
		TreeLeaf leaf;
		
		writerC.append("Leaf-Index	NumberOfIndexes	Range");
		
		while(upper<maxIndex) {
			writerC.newLine();
			sr = bpc.searchKey(lower, true);
			System.out.println("is it found?: "+sr.isFound());
			while(!sr.isFound()) {
				lower++; // go next index
				sr = bpc.searchKey(lower, true);
			}
			System.out.println(lower+" is found on the tree.");
			leaf = sr.getLeaf();
			sr.getLeaf().printNode();
			capacity = leaf.getCurrentCapacity();
			System.out.println("capacity is: "+capacity);
			upper = lower+capacity-1; 
			System.out.println("upper is: "+upper);
			if(upper>=maxIndex) {
				upper = maxIndex;
				capacity = (int)(upper-lower+1);
			}
			while((leaf.equals(bpc.searchKey(upper+1, true).getLeaf()))&&(upper<maxIndex)){
				System.out.println("next upper: "+upper+1+" is on the same leaf");
				System.out.println();
				upper++;
			}// if next index is on the same leaf, then the range increases by one.
			
			chars = String.valueOf(cnt);
			while(chars.length()<maxChars) {
				chars = "0"+chars;
			}
			
			writerC.append(chars+" "+String.valueOf(capacity)+" "+String.valueOf(lower)+"-"+String.valueOf(upper));
			
			lower = upper+1; // go next range
			cnt++; 
		}			
		writerC.close();
		System.out.println(filenames+"_C.txt was created successfully.");
		BufferedWriter writerZ = new BufferedWriter(new FileWriter(writeFileZ, true));
		writerZ.append("Leaf-Index	NumberOfIndexes	Range");
		lower = 0;
		upper = 0;
		cnt = 1;
		while(upper<maxIndex) {
			writerZ.newLine();
			sr = bpc.searchKey(lower, true);
			while(!sr.isFound()) {
				lower++; // go next index
				sr = bpz.searchKey(lower, true);
			}
			leaf = sr.getLeaf();
			capacity = leaf.getCurrentCapacity();
			upper = lower+capacity-1; 
			if(upper>=maxIndex) {
				upper = maxIndex;
				capacity = (int)(upper-lower+1);
			}
			while((leaf.equals(bpz.searchKey(upper+1, true).getLeaf()))&&(upper<maxIndex)) {
				upper++;
			}// if next index is on the same leaf, then the range increases by one.
			
			chars = String.valueOf(cnt);
			while(chars.length()<maxChars) {
				chars = "0"+chars;
			}
			
			writerZ.append(chars+" "+String.valueOf(capacity)+" "+String.valueOf(lower)+"-"+String.valueOf(upper));
			lower = upper+1; // go next range
			cnt++; 
		}
		writerZ.close();
		System.out.println(filenames+"_Z.txt was created successfully.");
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
	public QueryComponentsObject rangeQuery(double[] lb, double[] ub) throws IOException, InvalidBTreeStateException{
		
		ArrayList<Long> longListC = resultQuery(lb,ub,"C");
		ArrayList<Long> longListZ = resultQuery(lb,ub,"Z");
		
		int leafReadsC = 0;
		int leafReadsZ = 0;
		int falsePosC = 0;
		int falsePosZ = 0;
		
		int[] rangeIOres;
		
		boolean nonDupes = false;	
		
		long minIndex = getMinIndexFromList(longListC);
		long maxIndex = getMaxIndexFromList(longListC);
		
		long startC = System.currentTimeMillis();
		RangeResult rrC = bpc.rangeSearch(minIndex, maxIndex, nonDupes); // using getMinIndexFromList() and --Max--() inside rangeSearch() will make it take more time.
		falsePosC = rrC.getQueryResult().size();
		refineQuery(lb,ub,rrC);
		falsePosC = falsePosC - rrC.getQueryResult().size();
		long endC = System.currentTimeMillis();
		long totalC = endC - startC;
		
		bpc.getPerformanceClass().resetAllMetrics();
		rangeIOres = bpc.getPerformanceClass().rangeIO(minIndex, maxIndex, nonDupes, false);
		leafReadsC = rangeIOres[4]; // getInterminentLeafPageReads();
		
		minIndex = getMinIndexFromList(longListZ);
		maxIndex = getMaxIndexFromList(longListZ);
		
		long startZ = System.currentTimeMillis();
		RangeResult rrZ = bpz.rangeSearch(minIndex, maxIndex, nonDupes);
		falsePosZ = rrZ.getQueryResult().size();
		refineQuery(lb,ub,rrZ);
		falsePosZ = falsePosZ - rrZ.getQueryResult().size();
		long endZ = System.currentTimeMillis();
		long totalZ = endZ - startZ;
		
		bpz.getPerformanceClass().resetAllMetrics();
		rangeIOres = bpz.getPerformanceClass().rangeIO(minIndex, maxIndex, nonDupes, false);
		leafReadsZ = rangeIOres[4]; // getInterminentLeafPageReads();
		
		QueryComponentsObject qco  = new QueryComponentsObject(totalC,totalZ,rrC.getQueryResult().size(),rrZ.getQueryResult().size(), falsePosC, falsePosZ, leafReadsC, leafReadsZ);
		bpc.getPerformanceClass().resetAllMetrics();
		bpz.getPerformanceClass().resetAllMetrics();
		
		System.out.println(falsePosC);
		System.out.println(falsePosZ);
		
		return qco;	
 	}	
	public QueryComponentsObject fractionsQuery(double[] lb, double[] ub) throws IOException, InvalidBTreeStateException {
		ArrayList<Long> longListC = resultQuery(lb,ub,"C");
		ArrayList<Long> longListZ = resultQuery(lb,ub,"Z");
		
		boolean nonDupes = false;
		
		int leafReadsC = 0;
		int leafReadsZ = 0;
		int falsePosC = 0;
		int falsePosZ = 0;
		
		int[] rangeIOres;
		
		int totalSizeC = 0;
		int totalSizeZ = 0;
		
		long startC = 0;
		long startZ = 0;
		long endC = 0;
		long endZ = 0;
		long totalC = 0;
		long totalZ = 0;
		
		RangeResult rrC;
		RangeResult rrZ;
		
		for(int i=0;i<longListC.size();i++) {
			startC = System.currentTimeMillis();
			rrC = bpc.rangeSearch(longListC.get(i), longListC.get(i), nonDupes);
			falsePosC = falsePosC + rrC.getQueryResult().size();
			refineQuery(lb,ub,rrC);
			falsePosC = falsePosC - rrC.getQueryResult().size();
			endC = System.currentTimeMillis();
			totalC = totalC + (endC-startC);
			totalSizeC = totalSizeC + rrC.getQueryResult().size();
			
			rangeIOres = bpc.getPerformanceClass().rangeIO(longListC.get(i), longListC.get(i), nonDupes, false);
			leafReadsC = leafReadsC + rangeIOres[4];
			
			bpc.getPerformanceClass().resetAllMetrics();
		}
		
		
		for(int i=0;i<longListZ.size();i++) {
			startZ = System.currentTimeMillis();
			rrZ = bpz.rangeSearch(longListZ.get(i), longListZ.get(i), nonDupes);
			falsePosZ = falsePosZ + rrZ.getQueryResult().size();
			refineQuery(lb,ub,rrZ);
			falsePosZ = falsePosZ - rrZ.getQueryResult().size();
			endZ = System.currentTimeMillis();
			totalZ = totalZ + (endZ-startZ);
			totalSizeZ = totalSizeZ + rrZ.getQueryResult().size();
			
			rangeIOres = bpz.getPerformanceClass().rangeIO(longListZ.get(i), longListZ.get(i), nonDupes, false);
			leafReadsZ = leafReadsZ + rangeIOres[4];
			
			bpz.getPerformanceClass().resetAllMetrics();
		}
		
		QueryComponentsObject qco = new QueryComponentsObject(totalC, totalZ, totalSizeC, totalSizeZ, falsePosC, falsePosZ, leafReadsC, leafReadsZ);
		bpc.getPerformanceClass().resetAllMetrics();
		bpz.getPerformanceClass().resetAllMetrics();
		return qco;
	}
	
	public QueryComponentsObject rangeFractionsQuery(double[] lb, double[] ub) throws IOException, InvalidBTreeStateException {
		ArrayList<Long> longListC = resultQuery(lb,ub,"C");
		ArrayList<Long> longListZ = resultQuery(lb,ub,"Z");
		
		
		int leafReadsC = 0;
		int leafReadsZ = 0;
		int falsePosC = 0;
		int falsePosZ = 0;
		
		boolean nonDupes = false;
		
		int[] rangeIOres;
		
		long start;
		long end;
		long totalC = 0;
		long totalZ = 0;
		
		RangeResult rrC;
		RangeResult rrZ;
		
		int totalSizeC = 0;
		int totalSizeZ = 0;
		
		long min = 0;
		long max = 0;	
		
		min = longListC.get(0);
		max = longListC.get(0);
		
		for(int i=1;i<longListC.size();i++) {
			// as long as the next "value" on the list is bigger and it is actually a next number, we add it to the range.
			// else, we break the loop and calculate rangeSearch() where the range ended up being.
			// List example (indexes) : 2, 4, 5, 7, 10, 9, 11, 12
			// The ranges are: [2], [4,5], [7], [10], [9], [11,12]
			if((longListC.get(i)<min)||(longListC.get(i)-max>1)) {
				start = System.currentTimeMillis();		
				rrC = bpc.rangeSearch(min, max, nonDupes);		
				falsePosC = falsePosC + rrC.getQueryResult().size();
				refineQuery(lb,ub,rrC);
				falsePosC = falsePosC - rrC.getQueryResult().size();
				end = System.currentTimeMillis();
				totalC = totalC + (end-start);
				totalSizeC = totalSizeC + rrC.getQueryResult().size();
				rangeIOres = bpc.getPerformanceClass().rangeIO(min, max, nonDupes, false);
				leafReadsC = leafReadsC + rangeIOres[4];
				min = longListC.get(i);
				max = longListC.get(i);
			}
			max = longListC.get(i);
		}
		start = System.currentTimeMillis();
		rrC = bpc.rangeSearch(min, max, nonDupes);
		falsePosC = falsePosC + rrC.getQueryResult().size();
		refineQuery(lb,ub,rrC);
		falsePosC = falsePosC  - rrC.getQueryResult().size();
		end = System.currentTimeMillis();
		totalC = totalC + (end-start);
		totalSizeC = totalSizeC + rrC.getQueryResult().size();
		rangeIOres = bpc.getPerformanceClass().rangeIO(min, max, nonDupes, false);
		leafReadsC = leafReadsC + rangeIOres[4];
		min = longListZ.get(0);
		max = longListZ.get(0);
		for(int i=1;i<longListZ.size();i++) {
			if((longListZ.get(i)<min)||(longListZ.get(i)-max>1)) {	
				start = System.currentTimeMillis();
				rrZ = bpz.rangeSearch(min, max, nonDupes);
				falsePosZ = falsePosZ + rrZ.getQueryResult().size();
				refineQuery(lb,ub,rrZ);
				falsePosZ = falsePosZ  - rrZ.getQueryResult().size();
				end = System.currentTimeMillis();
				totalZ = totalZ + (end-start);
				totalSizeZ = totalSizeZ + rrZ.getQueryResult().size();
				rangeIOres = bpz.getPerformanceClass().rangeIO(min, max, nonDupes, false);
				leafReadsZ = leafReadsZ + rangeIOres[4];
				min = longListZ.get(i);
				max = longListZ.get(i);		
			}
			max = longListZ.get(i);
		}
		start = System.currentTimeMillis();
		rrZ = bpz.rangeSearch(min, max, nonDupes);
		falsePosZ = falsePosZ + rrZ.getQueryResult().size();
		refineQuery(lb,ub,rrZ);
		falsePosZ = falsePosZ  - rrZ.getQueryResult().size();
		end = System.currentTimeMillis();
		totalZ = totalZ + (end-start);
		totalSizeZ = totalSizeZ + rrZ.getQueryResult().size();
		rangeIOres = bpz.getPerformanceClass().rangeIO(min, max, nonDupes, false);
		leafReadsZ = leafReadsZ + rangeIOres[4];
		QueryComponentsObject qco = new QueryComponentsObject(totalC, totalZ, totalSizeC, totalSizeZ, falsePosC, falsePosZ, leafReadsC, leafReadsZ);
		return qco;	
	}
	
	private void refineQuery(double[] lb, double[] ub, RangeResult rr) {
		
		BigDecimal xMin = BigDecimal.valueOf(lb[0]);
		BigDecimal yMin = BigDecimal.valueOf(lb[1]);
		
		BigDecimal xMax = BigDecimal.valueOf(ub[0]);
		BigDecimal yMax = BigDecimal.valueOf(ub[1]);
		
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
	}

	private long getMinIndexFromList(ArrayList<Long> longlist){
		long min = MAX_VALUE;
		for(int i=0;i<longlist.size();i++) {
			if(longlist.get(i)<min) {
				min = longlist.get(i);
			}
		}
		return min;
	}
	
	private long getMaxIndexFromList(ArrayList<Long> longlist) {
		long max = 0;
		for(int i=0;i<longlist.size();i++) {
			if(longlist.get(i)>max) {
				max = longlist.get(i);
			}
		}
		return max;
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
			System.out.println("Wrong Curve. Returning");
			return null;
		}
		if(lb.length!=mins.length || lb.length!=maxs.length || ub.length!=mins.length || ub.length!=maxs.length) {
			System.out.println("Wrong dimensions. Returning");
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
				System.out.println("Out of bounds");
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
			minlong = revConcat(minIndex);
			cnt = revConcat(minIndex);
			maxlong = revConcat(maxIndex);
			System.out.println(minlong[0]+" "+minlong[1]+" "+maxlong[0]+" "+maxlong[1]);
			for(int i=(int)minlong[0];i<=maxlong[0];i++) { // we add every index that is between ( 2-dimensional ) the min and max.
				for(int j=(int)minlong[1];j<=maxlong[1];j++) { // meaning that we parse the box to get every index(interleaved or concatenated) 
					longlist.add(findIndexLong(cnt,curve)); // that is inside the lowerBound and upperBound.
					// System.out.println("cnt: "+cnt[0]+" "+cnt[1]);
					cnt[1]++;
				}
				cnt[1] = minlong[1];
				cnt[0]++;
			}		
		}else if(curve=="z" || curve=="Z") {
			minlong = revLeave(minIndex);
			cnt = revLeave(minIndex);
			maxlong = revLeave(maxIndex);
			System.out.println(minlong[0]+" "+minlong[1]+" "+maxlong[0]+" "+maxlong[1]);
			for(int i=(int)minlong[0];i<=maxlong[0];i++) {
				for(int j=(int)minlong[1];j<=maxlong[1];j++) {
					longlist.add(findIndexLong(cnt,curve));
					// System.out.println("cnt: "+cnt[0]+" "+cnt[1]);
					cnt[1]++;
				}
				cnt[1] = minlong[1];
				cnt[0]++;
			}
		}	
		return longlist;
	}
	/**
	 * Calculates on which index, the given point is for the specified curve 
	 * @param dat represents the point ( data-set )
	 * @param curve is the curve we are using at the momment
	 * @return the index that corresponds for the point and the curve
	 */
	public long findIndex(double[] dat, String curve) { // takes a double[] as an argument which contains a data set and returns the index 										
		long ind = 0; //which this data set is in.
		double temp;
		int[] index = new int[dat.length];
		for(int i=0;i<dat.length;i++) {
			temp = (double)((dat[i]-mins[i])/steps[i]);
			if(temp>=(double)pages){
				index[i] = pages-1;
			}else if(temp==0.0){ //has to do with negative indexing which I'm trying to defeat with this line.
				index[i] = 0;
			}else{
				index[i] = (int)(Math.ceil(temp)-1);
			}
		}
		String[] bins = new String[index.length];
		for(int i=0;i<index.length;i++) {
			bins[i] = Integer.toBinaryString(index[i]);
			while(bins[i].length()<rbits) {
				bins[i] = "0"+bins[i];
			}
		}
		if(curve=="C" || curve=="c") {
			ind = concat(bins);
		}else if(curve=="Z" || curve=="z") {
			ind = interleave(bins);
		}else {
			System.out.println("Wrong curve");
			return 0L;
		}
		
		return ind;
	}
	
	private long concat(String[] bins) { // concatenate
		String b = "";
		for(int i=0;i<bins.length;i++) {
			b = b+bins[i];
		}
		return Long.parseLong(b,2);
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
		if(curve=="c" || curve=="C") {
			for(int i=0;i<index.length;i++) {
				bins[i] = Integer.toBinaryString((int)index[i]);
				while(bins[i].length()<rbits) {
					bins[i] = "0"+bins[i];
				}
			}
			indexer = concat(bins);
		}else if(curve=="z" || curve=="Z") {
			for(int i=0;i<index.length;i++) {
				bins[i] = Integer.toBinaryString((int)index[i]);
				while(bins[i].length()<rbits) {
					bins[i] = "0"+bins[i];
				}
			}
			indexer = interleave(bins);
		}
		return indexer;
	}
	/**
	 * Helpful function for rangeQuery - Reverse Concatenation
	 * @param index index to be dissolved into the initial 2-point index parts
	 * @return
	 */
	private long[] revConcat(long index) { // reverse concatenate
		String[] bins = new String[2];
		long[] parts = new long[2];
		String b;
		b = Integer.toBinaryString((int)index);
		while(b.length()<rbits*2) {
			b = "0"+b;
		}
		bins[0] = b.substring(0, rbits);
		bins[1] = b.substring(rbits,b.length());
		for(int i=0;i<parts.length;i++) {
			parts[i] = Long.parseLong(bins[i],2);
		}
		return parts;
	}
	/**
	 * Helpful function for rangeQuery - Reverse Interleaving
	 * @param index index to be dissolved into the initial 2-point index parts
	 * @return
	 */
	private long[] revLeave(long index) { // reverse interleaving
		String b;
		int x = 0;
		int y = 0;
		String[] bits = new String[2];
		long[] parts = new long[2];
		String[] bins = new String[rbits*2];
		String[] binsX = new String[rbits];
		String[] binsY = new String[rbits];
		
		for(int i=0;i<binsY.length;i++) {
			binsX[i] = "";
			binsY[i] = "";
		}
		for(int i=0;i<bits.length;i++) {
			bits[i] = "";
		}
		b = Integer.toBinaryString((int)index);
		while(b.length()<rbits*2) {
			b = "0"+b;
		}
		
		bins = b.split("(?!^)"); // split every char of b into bins[].
		for(int i=0;i<bins.length;i++) {
			if((i%2)==0) {
				binsX[x] = binsX[x] + bins[i];
				x++;
			}else if((i%2)==1) {
				binsY[y] = binsY[y] + bins[i];
				y++;
			}
		}
		for(int i=0;i<rbits;i++) {
			
			bits[0] = bits[0] + binsX[i];
			bits[1] = bits[1] + binsY[i];
		}
		for(int i=0;i<bits.length;i++) {
			
			parts[i] = Long.parseLong(bits[i],2);
		}
		return parts;
	}
	private long interleave(String[] bins) { // interleaving
		String b = "";
		String[][] binswap = new String[rbits][2];
		String[] binholder = new String[rbits];
		for(int i=0;i<bins.length;i++){
			binholder = bins[i].split("(?!^)");
			for(int j=0;j<rbits;j++){
				binswap[j][i] = binholder[j];
			}
		}
		for(int i=0;i<rbits;i++){
			for(int j=0;j<2;j++){
				b = b + binswap[i][j];
			}
		}
		return Long.parseLong(b,2);
	}
	public TreeFile getTreeFile() 
	{return tf;}
	public TimeQuarters getTimeQuarters()
	{return this.tqs;}
}
