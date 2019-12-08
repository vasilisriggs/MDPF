package ds.bplus.mdpf;

import static java.lang.Long.MAX_VALUE;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
	private TimeNumberObject tno;
	private TimeQuarters tqs;
	/**
	 * TreeMods constructor
	 * @param tf TreeFile reference object
	 */
	public TreeMods(TreeFile tf) {
		this.tf = tf;
		this.bpc = tf.getBTreeC();
		this.bpz = tf.getBTreeZ();
		
		this.filenameC = tf.getFilenameC();
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
	
	public TimeNumberObject resultQuery(double[] lb, double[] ub) throws IOException, InvalidBTreeStateException {
		ArrayList<Long> longlistC = rangeQuery(lb,ub,"C");
		ArrayList<Long> longlistZ = rangeQuery(lb,ub,"Z");
		
		if((longlistC.size()==0) || (longlistZ.size()==0)){
			return null;
		}
		
		long startC = System.currentTimeMillis();
		RangeResult rrC = bpc.rangeSearch(getMinIndexFromList(longlistC),getMaxIndexFromList(longlistC), false);
		long endC = System.currentTimeMillis();
		int listSizeC = rrC.getQueryResult().size();
		
		long startZ = System.currentTimeMillis();
		RangeResult rrZ = bpz.rangeSearch(getMinIndexFromList(longlistZ), getMaxIndexFromList(longlistZ), false); // false = allows duplicates.
		long endZ = System.currentTimeMillis();
		
		int listSizeZ = rrZ.getQueryResult().size();
		this.tno = new TimeNumberObject(endC-startC,endZ-startZ,listSizeC,listSizeZ);	
		return tno;
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
	public ArrayList<Long> rangeQuery(double[] lb, double[] ub, String curve){
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
					System.out.println("cnt: "+cnt[0]+" "+cnt[1]);
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
					System.out.println("cnt: "+cnt[0]+" "+cnt[1]);
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
	
	public TimeNumberObject getTimeNumberObject() 
	{return this.tno;}
	
	public TimeQuarters getTimeQuarters()
	{return this.tqs;}
	
}
