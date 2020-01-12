package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import ds.bplus.bptree.BPlusTree;
import ds.bplus.mdpf.*;
import ds.bplus.multiple.*;
import ds.bplus.object.*;
import ds.bplus.util.InvalidBTreeStateException;

public class Main2{
	public static void main(String[] args) throws IOException, InvalidBTreeStateException {
		double[] lb = new double[2];
		double[] ub = new double[2];
		
		lb[0] = -125.731711; // x min 
		lb[1] = 20.788937; // y min
		
		ub[0] = -68.384345;  // x max
		ub[1] = 48.641731;  // y max
		
		
		TreeMods tm = new TreeMods(new TreeFile(1024,8,24, new ResultFile(new DataFile(3000), 16)));
		
		tm.getLeafElements();
		
		QueryComponentsObject qco1 = tm.rangeQuery(lb, ub);
		QueryComponentsObject qco2 = tm.fractionsQuery(lb, ub);
		QueryComponentsObject qco3 = tm.rangeFractionsQuery(lb, ub);
		
		System.out.println(qco1.getRecordsC()+", "+qco1.getRecordsZ());
		System.out.println(qco2.getRecordsC()+", "+qco2.getRecordsZ());
		System.out.println(qco3.getRecordsC()+", "+qco3.getRecordsZ());
		
		/*
		System.out.println("Reading 3k.txt file for original result");
		String readFile = "multi/raw/3K.txt";
		BufferedReader br = new BufferedReader(new FileReader(readFile));
		
		BigDecimal xMin = BigDecimal.valueOf(lb[0]);
		BigDecimal yMin = BigDecimal.valueOf(lb[1]);
		
		BigDecimal xMax = BigDecimal.valueOf(ub[0]);
		BigDecimal yMax = BigDecimal.valueOf(ub[1]);
		
		BigDecimal[] bg = new BigDecimal[2];
		
		int cnt = 0;
		
		String line;
		String[] splitter;
		
		line = br.readLine();
		while((line = br.readLine())!=null) {
			splitter = line.split(" ");
			bg[0] = BigDecimal.valueOf(Double.parseDouble(splitter[0])); // x value
			bg[1] = BigDecimal.valueOf(Double.parseDouble(splitter[1])); // y value
			if(((bg[0].compareTo(xMin)>=0)&&(bg[0].compareTo(xMax)<=0))&&((bg[1].compareTo(yMin)>=0)&&(bg[1].compareTo(yMax)<=0))){
				cnt++;
				
			}else {
				// System.out.println(bg[0]+" "+bg[1]);
			}
		}
		
		br.close();
		System.out.println(cnt); */
		
		//ManageFiles mf = new ManageFiles(1, 20000, 16, 1024, 8, 24, false, false, true, true); // ManageFiles(numberOfFiles, elements, pages, pageSize, keySize, entrySize, statFiles?, TQM?, filemods?, leafElements?)	
	}
	
}
