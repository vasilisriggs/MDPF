package com.company;

import java.io.IOException;
import java.util.ArrayList;

import ds.bplus.bptree.BPlusTree;
import ds.bplus.mdpf.DataFile;
import ds.bplus.mdpf.ResultFile;
import ds.bplus.mdpf.TreeFile;
import ds.bplus.mdpf.TreeMods;
import ds.bplus.util.InvalidBTreeStateException;

public class Main2{
	public static void main(String[] args) throws IOException, InvalidBTreeStateException {
		TreeMods tm = new TreeMods(new TreeFile(1024,8,24,new ResultFile(new DataFile(20),4)));
		double[] lb = new double[2];
		double[] ub = new double[2];
		lb[0] = -123.493134;
		ub[0] = -68.367424;
		lb[1] = 26.158335;
		ub[1] = 48.62334;
		ArrayList<Long> longlist = tm.rangeQuery(lb, ub, "Z");
		longlist.trimToSize();
		System.out.println(longlist.size());
		for(int i=0;i<longlist.size();i++) {
			System.out.print(longlist.get(i)+" ");
		}
	}
}
