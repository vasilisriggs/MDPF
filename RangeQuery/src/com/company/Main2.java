package com.company;

import java.io.IOException;
import ds.bplus.mdpf.DataFile;
import ds.bplus.mdpf.IndexingFile;
import ds.bplus.mdpf.TreeFile;
import ds.bplus.multiple.ManageFiles;
import ds.bplus.util.BTMath;
import ds.bplus.util.DirectoryModerator;
import ds.bplus.util.InvalidBTreeStateException;

public class Main2 {
	public static void main(String[] args) throws IOException, InvalidBTreeStateException {
		
		/*int numberOfFiles = 5;
		int entrySize = 24;
		int keySize = 8;
		
		int[] pageSize = {1024,2048,4096};
		int[] totalRecords = {100000,200000,500000};
		int[] pages = {16,32,64};
		
		ManageFiles mf;
		
		for(int i=0;i<totalRecords.length;i++) {
			for(int j=0;j<pages.length;j++) {
				for(int k=0;k<pageSize.length;k++) {
					mf = new ManageFiles(numberOfFiles,totalRecords[i],pages[j],pageSize[k],keySize,entrySize);
				}
			}
		}*/
		
		//IndexingFile inx = new IndexingFile(new DataFile(500000,""),10);
		
		DirectoryModerator dm = new DirectoryModerator();
		dm.findIndexDensity();
		
		
		
	}
}
