# MDPF
**Multidimensional Partitioning Framework Based on Query-Aware and Skew-Tolerant Space Filling Curves**

Έχουμε ένα αρχείο **raw** με n-διάστατα στοιχεία **(x,y,z,...,n)**. Το αρχείο περιλαμβάνει και μια κεφαλίδα για τον αριθμό των διαστάσεων του κάθε στοιχείου καθώς και το συνολικό αριθμό των στοιχείων αυτών.
Το πρόγραμμα διαβάζει ένα τέτοιο αρχείο ( μαζί με την κεφαλίδα αυτού ) και στο πρώτο scanning ορίζει τα **min** και **max** της κάθε διάστασης.

Κάθε διάσταση χωρίζεται σε κομμάτια ( **pages** ) με μια παράμετρο που ονομάζεται **pages**. Η παράμετρος αυτή είναι ίδια για κάθε διάσταση. Αποτέλεσμα αυτής της διαμέρισης της κάθε διάστασης είναι να δημιουργηθούν χωρία (τετράγωνα για τις 2 διαστάσεις, κύβοι για τις 3 διαστάσεις, υπερκύβοι για τις n διαστάσεις). Ο αριθμός των χωρίων ορίζεται από πριν και άρα το εύρος αυτών εξαρτάται από τις μέγιστες και ελάχιστες τιμές των στοιχείων του αρχείου raw για κάθε διάσταση. Το χωρίο με ελάχιστα άκρα τα ελάχιστα **(x,y)** εμπίπτει στην αρχή των αξόνων **(0,0)**. Εκτός των ελαχίστων, κάθε χωρίο έχει και μέγιστα. **Η διαφορά των μεγίστων και ελαχίστων σε κάθε διάσταση για ένα χωρίο αποτελεί το εύρος του χωρίου στον άξονα και το χώρο**. 

Ο συνολικός αριθμός των χωρίων στον n-διάστατο χώρο είναι pages^n --> [0, (pages^n)-1]

Κάθε διάσταση έχει ένα **min** και ένα **max**. Το συνολικό εύρος κάθε διάστασης βρίσκεται από τη διαφορά **max-min**. Το εύρος που πρέπει να έχει κάθε χωρίο στην **i**-οστή διάσταση ισούται με το συνολικό εύρος της **i**-οστής διάστασης δια τον αριθμό των pages.  

	for(int i=0;i<steps.length;i++){
		steps[i] = (double)((max[i]-min[i])/pages);
	}
	/* i --> dimension index */  
	
Η αρίθμηση σε κάθε διάσταση για τα όρια κάθε χωρίου αρχίζει από το 0 και τελειώνει στον αριθμό pages-1  --> **[0, pages-1]**. Ο αριθμός των ελάχιστων **bits** που χρησιμοποιούνται για την αναπαράσταση του αριθμού (pages-1), χρησιμοποιούνται για την αρίθμηση των ορίων κάθε χωρίου. Εάν, για παράδειγμα, χωρίσουμε τη κάθε διάσταση σε 8 χωρία, τότε: **[0,7]** -> **3 bits** αναπαράστασης => **[000, 111]**. **Τα συνολικά χωρία που δημιουργούνται στην περίπτωση αυτή είναι 8^n --> [0, (8^n)-1] για τις n διαστάσεις**. 

Από δω και στο εξής θα αναφέρομαι σε ένα δισδιάστατο set στοιχείων. Δηλαδή για ένα αρχείο **raw** με δισδιάστατα στοιχεία **(x,y)**.

Να αναφέρω σε αυτό το σημείο για λόγους πληρότητας ότι έχω φέρει όλα τα στοιχεία σε ένα ορθοκανονικό σύστημα ημιαξόνων, όπου η ελάχιστη τιμή για την κάθε διάσταση

	min[i]
είναι το σημείο 0 στο σύστημα ημιαξόνων στη διάσταση αυτή. **(0,0) ==> (min[0], min[1])**.

Κάθε στοιχείο του αρχείου **raw** "πέφτει" πάνω σε ενα συγκεκριμένο χωρίο. Κάθε χωρίο αποτελείται από διαστάσεις και κάθε διάσταση είναι αριθμημένη ==> **[0, pages-1]** σε δυαδική αναπαράσταση. Έχοντας την δυαδική αναπαράσταση ( **bits** ) της κάθε διάστασης του χωρίου, μπορώ να ενώσω με 2 ( στην παρούσα εργασία ) τρόπους τα **bits** προκειμένου να προσδώσω ένα συγκεκριμένο αναγνωριστικό στα χωρία ( και συνεπώς στα στοιχεία που "πέφτουν" πάνω στα χωρία αυτά ). Μετά την ένωση των **bits** των δυαδικών αναπαραστάσεων της κάθε διάστασης, θα προκύψει ένας αριθμός μεταξύ του **0** και του **(pages^n)-1**, όπου n ο αριθμός των διαστάσεων και κάθε αριθμός που ανήκει σε αυτά τα όρια θα δωθεί **αμφιμονοσήμαντα** σε κάθε χωρίο. **Να διευκρινίσω ότι χειρίζομαι μία μόνο μέθοδο ένωσης bits κάθε φορά και καθεμία από αυτές τις δύο μεθόδους αποτελεί ξεχωριστό αντικείμενο μελέτης**.

Έχουμε 2 τρόπους ένωσης των bits.
1) Concatenation (C-Curve)
2) Interleaving	 (Z-Curve) 	

Έστω pages = 8. Συνεπώς έχουμε 3 bits για κάθε δυαδική αναπαράσταση. Επιπλέον έχουμε 2 διαστάσεις. Οπότε έχουμε συνολικά 64 χωρία που δημιουργούνται στο χώρο **[0,63]**.

Έστω ένα δισδιάστατο στοιχείο. Το στοιχείο αυτό "πέφτει" πάνω σε ένα χωρίο με συντεταγμένες **(X,Y)**. **Bits αναπαράστασης = 3**.
		
	X = x1x2x3
	Y = y1y2y3
	Concatenation Method ==> x1x2x3y1y2y3 ή y1y2y3x1x2x3
	Interleaving  Method ==> x1y1x2y2x3y3 ή y1x1y2x2y3x3
	
	Ο δεκαδικός αριθμός που προκύπτει αποτελεί το μοναδικό αναγνωριστικό για κάθε χωρίο και 
	σε κάθε στοιχείο που "πέφτει" πάνω σε εκείνο το χωρίο προσδίδεται αυτό το αναγνωριστικό.

Διαβάζουμε ένα αρχείο **raw** και δημιουργούμε ένα αρχείο **results** όπως περιέγραψα παραπανω.

		while((line = br.readLine()) != null){
			writer.newLine();
			datas = line.split(" ");
			// find on which pages this set of values is.
			for(int i=0;i<datas.length;i++){
				writer.append(datas[i]);
				writer.append(' ');
				tempd = Double.parseDouble(decf.format((Double.parseDouble(datas[i])-min[i])/steps[i]));
				if(tempd>=(double)pages){
					indexing[i] = pages-1;
				}else if(tempd==0.0){ 
				//has to do with negative indexing which I'm trying to defeat with this line.
					indexing[i] = 0;
				}else{
					indexing[i] = (int)(Math.ceil(tempd)-1);
				}
			} 
			// binary represenation. rbits = 3 
			//(for 8 pages, so i'll zero pad numbers that have less than 3 bits;
			//1->001,11->011, etc.)
			for(int i=0;i<indexing.length;i++){
				b = Integer.toBinaryString(indexing[i]);
				while(b.length()<rbits){
					b = "0"+b;
				}
				bins[i] = b.toString().trim();
			}
			// interleaving.
			for(int i=0;i<bins.length;i++){
				binholder = bins[i].split("(?!^)");
				for(int j=0;j<rbits;j++){
					binswap[j][i] = binholder[j];
				}
			}
			b = "";
			for(int i=0;i<rbits;i++){
				for(int j=0;j<n;j++){
					b = b + binswap[i][j];
				}
			}
			z = b;
			//concatenate.
			b="";
			for(int i=0;i<bins.length;i++){
				b = b+bins[i];
			}
			c = b;
			writer.append(c);
			writer.append(' ');
			writer.append(z);
		}


Το αρχείο results περιέχει στην κεφαλίδα του:
	
	Αριθμό Στοιχείων Αριθμός Διαστάσεων C-Index Z-Index
	Min	Max	X
	Min 	Max 	Y
	.
	. στοιχεία..
	.
	x	y	c-index(binary)		z-index(binary)
	.
	.

Στη συνέχεια, διαβάζω ένα αρχείο results για αποθηκεύσω τα στοιχεία αυτά σε ένα αρχείο tree που υλοποιεί ενα B+ Tree.

	while((line = br.readLine()) != null) {
		
		datas = line.split(" ");
		entry = datas[0]+","+datas[1];
			
		keyC = Long.parseLong(datas[2],2);
		keyZ = Long.parseLong(datas[3],2);
		
		bpc.insertKey(keyC, entry, duplicates);
		bpz.insertKey(keyZ, entry, duplicates)
	}

Δημιουργώ έτσι δύο αρχεία δέντρων tree για κάθε αρχείο results, καθώς οι μέθοδοι ένωσης των bits πρέπει να μελετηθούν ανεξάρτητα.
Τα αρχεία αυτά είναι:

		TreeC --> Αποθηκεύει κάθε στοιχείο στη δομή του δέντρου με Index το αποτέλεσμα του Concatenation
		TreeZ --> Αποθηκεύει κάθε στοιχείο στη δομή του δέντρου με Index το αποτέλεσμα του Interleaving

-----------------------------------------------------------------------

# B+ Tree

Ο φάκελος RangeQuery αποτελεί την υλοποίηση του B+ Tree. ( Credits στον andylamp: https://github.com/andylamp/BPlusTree ) 

Πρόκεται για υλοποίηση B+ Tree πάνω στο δίσκο που χρησιμοποιείται για αποθήκευση κλειδιού-τιμής ( περισσότερες λεπτομέρειες αναφέρονται στο link ).

Wikipedia link για B+ Trees https://en.wikipedia.org/wiki/B%2B_tree 


------------------------------------------------------------------------

RangeQuery/BPlusTree/src/main/java/ds/bplus/**bptree/**

Ο φάκελος αυτός περιέχει όλα τα αρχεία που με τη βοήθεια αυτών υλοποιείται το B+ Tree ( είναι ήδη υλοποιημένα από τον andylamp ).

RangeQuery/BPlusTree/src/main/java/ds/bplus/**mdpf/**

Τα αρχεία του φάκελου αυτού επρόκειτω για δική μου υλοποίηση. Πιο συγκεκριμένα:

**DataFile.java**: 

Η κλάση **DataFile** παίρνει ως όρισμα τον αριθμό των στοιχείων και δημιουργεί ένα αρχείο raw.txt.
Ένα αρχείο **raw.txt** περιέχει μια κεφαλίδα:
		
		NumberOfElements	Dimensions	//Header
		.element1(x1, y1, ... n1)		//Elements..
		.element2(x2, y2, ... n2)
		.
		.
		.
Τα στοιχεία είναι **Random()**, μοντελοποιούν συντεταγμένες (στα όρια των ΗΠΑ) με δεκαδική ακρίβεια **6** ψηφίων
	
	DecimalFormat df  = new DecimalFormat("#.######");
	
	// *United States of America* 
	private final double lat_max = 48.682856;	//maximum latitude
	private final double lat_min = 25.712085;	//minimum latitude
	private final double long_max = -68.275846;	//maximum longitude
	private final double long_min = -124.874623;	//minimum longitude
	
και ακολουθούν μια κατανομή με συγκεκριμένη τυπική απόκλιση **σ**.
		
		DataFile df = new DataFile(100000)
		
δίνει αποτέλεσμα

		RangeQuery/multi/raw/100K.txt

Η κλάση χειρίζεται και την περίπτωση δημιουργίας πολλαπλών αρχείων **raw** ίδιου αριθμού στοιχείων.
		
		RangeQuery/multi/raw/100K_1.txt
		RangeQuery/multi/raw/100K_2.txt
		RangeQuery/multi/raw/100K_3.txt
		RangeQuery/multi/raw/100K_4.txt
		RangeQuery/multi/raw/100K_5.txt
	

**ResultFile.java**: 

Η κλάση **ResultFile** παίρνει ως όρισμα ένα **DataFile instance object** καθώς και έναν ακέραιο αριθμό **pages** που ορίζει σε πόσα "κομμάτια" θα χωριστεί η κάθε διάσταση προκειμένου να δημιουργηθούν τα χωρία στον (δισ)διάστατο χώρο.

Διαβάζει το αρχείο DataFile (που βρίσκεται στη μνήμη, σε κάποιο κατάλογο, είτε δίνοντας το όνομα του αρχείου raw, είτε δίνοντας το όρισμα του DataFile object.)

		ResultFile rf = new ResultFile(new DataFile(100000), 8)
		
						ή
						
		ResultFile rf = new ResultFile("100K.txt", 8)
		
Ο πρώτος τρόπος είναι πιο χρηστικός και προτιμότερος για πολλαπλά αρχεία.

Το output της κλάσης **ResultFile** είναι ένα αρχείο **result** το οποίο συζητείται στην αρχή μαζί με τις μεθόδους ένωσης bits.
Το αρχείο **result** έχει την παρακάτω μορφή

	numberOfElements Dimensions C-Index Z-Index
	Min	Max	X
	Min 	Max 	Y
	.
	. στοιχεία..
	.
	x	y	c-index(binary)		z-index(binary)
	.
	.
	
Το αρχείο **result**:

		RangeQuery/multi/results/100K_8.txt

και για πολλαπλά αρχεία

		RangeQuery/multi/results/100K_1_8.txt
		RangeQuery/multi/results/100K_2_8.txt
		RangeQuery/multi/results/100K_3_8.txt
		RangeQuery/multi/results/100K_4_8.txt
		RangeQuery/multi/results/100K_5_8.txt
			
		
**TreeFile.java**:

Η κλάση **TreeFile** παίρνει ως όρισμα ένα **ResultFile instance object** καθώς και ένα σετ μεταβλητών ( ακέραιοι ) που αρχικοποιούν ένα δέντρο B+ Tree.

Ο **constructor** της κλάσης είναι (απλοποιημένη μορφή):

	private BPlusConfiguration bconf;
	private BPlusTreePerformanceCounter bPerf;

	public TreeFile(int pageSize, int keySize, int entrySize, ResultFile rf) throws IOException, InvalidBTreeStateException{
		String readMode = "rw+";
		this.bconf = new BPlusConfiguration(pageSize,keySize,entrySize);
		this.bPerf = new BPlusTreePerformanceCounter(true);
		this.rf = rf;
	}

Η κλάση **BPlusConfiguration** ουσιαστικά αρχικοποιεί το δέντρο με τις παραμέτρους **pageSize**, **keySize**, **entrySize**.
		
		
	/**
	*
	*@param pageSize page size (default is 1024 bytes)
	*@param keySize key size (default is long [8 bytes])
	*@param entrySize satellite data (default is 20 bytes)
	*/
	public BPlusConfiguration(int pageSize, int keySize, int entrySize){
		basicParams(pageSize, keySize, entrySize);
		initializeCommons(pageSize, keySize, entrySize, 1000);
	}

με 

	private void basicParams(int pageSize, int keySize, int entrySize) {
        	this.pageSize = pageSize;   // page size (in bytes)
        	this.entrySize = entrySize; // entry size (in bytes)
        	this.keySize = keySize;     // key size (in bytes)
	}

και 

	private void initializeCommon(int pageSize, int keySize, int entrySize, int conditionThreshold) {
        	this.headerSize =                                   // header size in bytes
                	(Integer.SIZE * 4 + 4 * Long.SIZE) / 8;
        	this.internalNodeHeaderSize = (Short.SIZE + Integer.SIZE) / 8; // 6 bytes
        	this.leafHeaderSize = (Short.SIZE + 2 * Long.SIZE + Integer.SIZE) / 8; // 22 bytes
        	this.lookupOverflowHeaderSize = 14;
        	this.lookupPageSize = pageSize - headerSize;        // lookup page size
        	this.conditionThreshold = conditionThreshold;       // iterations for conditioning
        	// now calculate the tree degree
        	this.treeDegree = calculateDegree(2*keySize, internalNodeHeaderSize);
        	// leaf & overflow have the same header size.
        	this.leafNodeDegree = calculateDegree((2*keySize)+entrySize, leafHeaderSize);
        	this.overflowPageDegree = calculateDegree(entrySize, leafHeaderSize);
        	this.lookupOverflowPageDegree = calculateDegree(keySize,
		lookupOverflowHeaderSize);
		checkDegreeValidity();
    }

1) **pageSize** ορίζει το μέγιστο μέγεθος που μπορεί να έχει ένα Block σε Bytes.

2) **keySize** ορίζει το μέγεθος σε Bytes του κλειδιού (ή αναγνωριστικού) κάθε στοιχείου.

3) **entrySize** ορίζει το μέγεθος σε Bytes των χρήσιμων δεδομένων (payload, satellite data).


Η κλάση **BPlusTreePerformanceCounter** χρησιμοποιείται για να κρατάει record για τα metrics του δέντρου. Ο costructor είναι:
		
	public BPlusTreePerformanceCounter(boolean trackIO) {
		this.trackIO = trackIO;
		resetAllMetrics();
	}
	
Τα metrics της είναι:

	private int totalNodeReads;
	private int totalInternalNodeReads;
	private int totalLeafNodeReads;
	private int totalOverflowReads;
	
	private int totalNodeWrites;
	private int totalInternalNodeWrites;
	private int totalLeafNodeWrites;
	private int totalOverflowWrites;
			
	private int totalInsertionReads;
	private int totalDeletionReads;
	private int totalSearchReads;
	private int totalRangeQueryReads;
	private int totalInsertionWrites;
	private int totalDeletionWrites;
	private int totalSearchWrites;
	private int totalRangeQueryWrites;
		
	private int pageReads;
	private int pageWrites;
		
	private int pageInternalReads;
	private int pageLeafReads;
	private int pageOverflowReads;
	
	private int pageInternalWrites;
	private int pageLeafWrites;
	private int pageOverflowWrites;
		
	private int totalInsertions;
	private int totalDeletions;
	private int totalSearches;
	private int totalRangeQueries;
		
	private int totalSplits;
	private int totalRootSplits;
	private int totalInternalNodeSplits;
	private int totalLeafSplits;
		
		
	private int totalPages;
	private int totalOverflowPages;
	private int totalInternalNodes;
	private int totalLeaves;
	
	private int totalInternalNodeDeletions;
	private int totalLeafNodeDeletions;
	private int totalOverflowPagesDeletions;


Στον ίδιο constructor της **TreeFile**, εισάγω τα στοιχεία του **Result Object instance**:

	while((line = br.readLine()) != null) {
		datas = line.split(" ");
		entry = datas[0]+","+datas[1];
	
		keyC = Long.parseLong(datas[2],2);
		keyZ = Long.parseLong(datas[3],2);
	
		bpc.insertKey(keyC, entry, duplicates);
		bpz.insertKey(keyZ, entry, duplicates);	
	}

Τα **bpc** και **bpz** είναι αντικείμενα κλάσης **BPlusTree**:
	
	bpc = new BPlusTree(bconf,readMode,writeFileC,bPerf);
	bpz = new BPlusTree(bconf,readMode,writeFileZ,bPerf);
	
με **bconf**, **readMode** και **bPerf** όπως ορίστηκαν παραπάνω. Το **writeFileC** και **writeFileZ** είναι τα ονόματα των αρχείων δέντρου. Για κάθε αρχείο **result** παράγονται δέντρα για κάθε τεχνική ένωσης bits.
	
	RangeQuery/multi/bins/Tree_100K_8_res_1024_8_24_C.bin
	RangeQuery/multi/bins/Tree_100K_8_res_1024_8_24_Z.bin
	
και για πολλαπλά αρχεία
	
	RangeQuery/multi/bins/Tree_100K_1_8_res_1024_8_24_C.bin
	RangeQuery/multi/bins/Tree_100K_1_8_res_1024_8_24_Z.bin
	RangeQuery/multi/bins/Tree_100K_2_8_res_1024_8_24_C.bin
	RangeQuery/multi/bins/Tree_100K_2_8_res_1024_8_24_Z.bin
	RangeQuery/multi/bins/Tree_100K_3_8_res_1024_8_24_C.bin
	RangeQuery/multi/bins/Tree_100K_3_8_res_1024_8_24_Z.bin
	RangeQuery/multi/bins/Tree_100K_4_8_res_1024_8_24_C.bin
	RangeQuery/multi/bins/Tree_100K_4_8_res_1024_8_24_Z.bin
	RangeQuery/multi/bins/Tree_100K_5_8_res_1024_8_24_C.bin
	RangeQuery/multi/bins/Tree_100K_5_8_res_1024_8_24_Z.bin

με 
1) pageSize	= 1024
2) keySize	= 8
3) entrySize	= 24 










