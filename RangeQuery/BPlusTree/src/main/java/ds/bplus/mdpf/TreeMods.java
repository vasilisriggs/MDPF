package ds.bplus.mdpf;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ds.bplus.bptree.BPlusTree;

public class TreeMods{
	
	private TreeFile tf;
	private double[] mins;
	private double[] maxs;
	private int pages;
	private BPlusTree bpc;
	private BPlusTree bpz;
	private double[] steps;
	private int rbits;
	private DecimalFormat df  = new DecimalFormat("#.######");
	
	public TreeMods(TreeFile tf) {
		this.tf = tf;
		this.bpc = tf.getBTreeC();
		this.bpz = tf.getBTreeZ();
		
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
		
		if(curve=="c" || curve=="C") {
			minlong = revConcat(minIndex);
			cnt = revConcat(minIndex);
			maxlong = revConcat(maxIndex);
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
	
	public long findIndex(double[] dat, String curve) { // takes a double[] as an argument which contains a data set and returns the index which this data set is in.
		long ind = 0;
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
		
		bins = b.split("(?!^)"); // split every char of b[] into bins[].
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
	
	public TreeFile getTreeFile() {
		return tf;
	}
}
