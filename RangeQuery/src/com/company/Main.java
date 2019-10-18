package com.company;

import ds.bplus.bptree.BPlusConfiguration;
import ds.bplus.bptree.BPlusTree;
import ds.bplus.bptree.BPlusTreePerformanceCounter;
import ds.bplus.mdpf.DataFile;
import ds.bplus.util.InvalidBTreeStateException;

import java.io.IOException;

import static java.lang.Integer.MAX_VALUE;

public class Main {

    public static void main(String[] args) throws IOException, InvalidBTreeStateException {
       final String filePath = "bins/MyTreeZ.bin";
       final String readMode = "rw";

        /* Initialize B-tree
            treeFilePath: associated stored file path
            mode rw: Read from the associated file
        */
        BPlusTree bp = new BPlusTree(new BPlusConfiguration(1024,8,24), readMode, filePath, new BPlusTreePerformanceCounter(true));

        /* Information regarding the created B-tree */
        bp.getTreeConfiguration().printConfiguration();

        /*
          Range query and I/O tracking
          Examples:
         */
        bp.getPerformanceClass().rangeIO(1, 32132131,false,true);
        bp.getPerformanceClass().rangeIO(1, 2,false,true);
        bp.getPerformanceClass().rangeIO(2, 5,false,true);
        bp.getPerformanceClass().rangeIO(20, 338,false,true);
        //..
        
        

    }
}
