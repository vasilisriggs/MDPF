package com.company;

import java.io.IOException;
import java.util.ArrayList;

import ds.bplus.bptree.BPlusTree;
import ds.bplus.mdpf.DataFile;
import ds.bplus.mdpf.FileMods;
import ds.bplus.mdpf.ResultFile;
import ds.bplus.mdpf.TreeFile;
import ds.bplus.mdpf.TreeMods;
import ds.bplus.multiple.ManageFiles;
import ds.bplus.util.InvalidBTreeStateException;

public class Main2{
	public static void main(String[] args) throws IOException, InvalidBTreeStateException {
		ManageFiles mf = new ManageFiles(5, 2000, 16, 1024, 8, 24, true, true, true); // ManageFiles(numberOfFiles, elements, pages, pageSize, keySize, entrySize, statFiles?, TQM?, filemods?)	
	}
}
