package ds.bplus.util;
import java.lang.Math;
import java.text.DecimalFormat;
import java.util.ArrayList;
public final class BTMath {
	private BTMath(){}	
	public static DecimalFormat dec = new DecimalFormat("#.######");
	public static String concatenateXY(String[] bins) {
		String c = "";
		for(int i=0;i<bins.length;i++){
			c = c + bins[i];
		}
		return c;
	}
	public static String concatenateYX(String[] bins) {
		String c = "";
		for(int i=0;i<bins.length;i++){
			c =  bins[i] + c;
		}
		return c;
	}
	public static String interLeavingXY(String[] bins, int rbits, int n) {
		String[] binholder = new String[rbits];
		String[][] binswap = new String[rbits][n];
		String z="";
		for(int i=0;i<bins.length;i++){
			binholder = bins[i].split("(?!^)");
			for(int j=0;j<rbits;j++){
				binswap[j][i] = binholder[j];
			}
		}
		for(int i=0;i<rbits;i++){
			for(int j=0;j<n;j++){
				z = z + binswap[i][j];
			}
		}
		return z;
	}
	public static String interLeavingYX(String[] bins, int rbits, int n) {
		String[] binholder = new String[rbits];
		String[][] binswap = new String[rbits][n];
		String z="";
		for(int i=0;i<bins.length;i++){
			binholder = bins[i].split("(?!^)");
			for(int j=0;j<rbits;j++){
				binswap[j][i] = binholder[j];
			}
		}
		for(int i=0;i<rbits;i++){
			for(int j=n-1;j>-1;j--){
				z = z + binswap[i][j];
			}
		}
		return z;
	}
	public static long[] reverseConcatenateXY(long index, int rbits) { // reverse concatenate
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
	public static long[] reverseInterleaveXY(long index, int rbits) { // reverse interleaving
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
	public static void binaryPadding(int[] indexing, String[] bins, int rbits) {
		String binaryString;
		for(int i=0;i<indexing.length;i++){
			binaryString = Integer.toBinaryString(indexing[i]);
			while(binaryString.length()<rbits){
				binaryString = "0"+binaryString;
			}
			bins[i] = binaryString.toString().trim();
		}
	}
	public static int setIndexing(String datas, double min, double steps, int indexing, int pages) {
		double tempd;
		datas = dec.format(Double.parseDouble(datas));
		tempd = BTMath.formatDouble((Double.parseDouble(datas)-min)/steps);		
		if(tempd>=(double)pages){
			indexing = pages-1;
		}else if(tempd==0.0){ //has to do with negative indexing which I'm trying to defeat with this line.
			indexing = 0;
		}else{
			indexing = (int)(Math.ceil(tempd)-1);
		}
		return indexing;
	}
	public static double[] setSteps(double[] steps, double[] min, double[] max, int pages) {
		for(int i=0;i<steps.length;i++){
			steps[i] = formatDouble((max[i]-min[i])/pages);
		}
		return steps;
	}
	public static double formatDouble(double aDouble) {
		aDouble = Double.parseDouble(dec.format(aDouble));
		return aDouble;
	}
	public static int getBits(int a)
	{return (int)Math.ceil(Math.log10(a)/Math.log10(2));}
	public static long getMinIndexFromList(ArrayList<Long> longlist){
		long min = longlist.get(0);
		for(int i=1;i<longlist.size();i++) {
			if(longlist.get(i)<min) {
				min = longlist.get(i);
			}
		}
		return min;
	}
	public static long getMaxIndexFromList(ArrayList<Long> longlist) {
		long max = longlist.get(0);
		for(int i=1;i<longlist.size();i++) {
			if(longlist.get(i)>max) {
				max = longlist.get(i);
			}
		}
		return max;
	}
	public static ArrayList<Long> listSort(ArrayList<Long> al, int pages){
		int[] array = new int[pages*pages];
		for(int i=0;i<array.length;i++) {
			array[i] = 0;
		}
		for(int j=0;j<al.size();j++) {
			array[Integer.parseInt(String.valueOf(al.get(j)))] = 1; //cant cast Long to int so I do it this way.
		}
		al.clear();
		for(int k=0;k<array.length;k++) {
			if(array[k]==1) {
				al.add((long) k);
			}
		}
		al.trimToSize();
		return al;
	}
	
	public void print(String string) {
		System.out.println(String.valueOf(string));
	}
}
