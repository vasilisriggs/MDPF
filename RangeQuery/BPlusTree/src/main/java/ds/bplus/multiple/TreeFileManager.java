package ds.bplus.multiple;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import ds.bplus.bptree.*;
import ds.bplus.mdpf.*;

public class TreeFileManager {
	
	private BPlusTree bpc;
	private BPlusTree bpz;
	public TreeFileManager() {
		
	}
	
	public void storeTreeElements(TreeFile[] tf) throws IOException {
		int numberOfFiles = tf.length;
		long numberOfBlocks;
		int numberOfLeavesC;
		int numberOfLeavesZ;
		int numberOfEntries;
		for(int i=0;i<numberOfFiles;i++) {
			this.bpc = tf[i].getBTreeC();
			this.bpz = tf[i].getBTreeZ();
			numberOfBlocks = bpc.getMaxPageNumber();
			System.out.println(numberOfBlocks);
			for(int j=0;j<numberOfBlocks;j++) {
				//
			}
		}
		return;
	}
}
