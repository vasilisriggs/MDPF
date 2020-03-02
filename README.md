# MDPF
**Multidimensional Partitioning Framework Based on Query-Aware and Skew-Tolerant Space Filling Curves**

Έχουμε ένα αρχείο **raw** με n-διάστατα στοιχεία **(x,y,z,...,n)**. Το αρχείο περιλαμβάνει και μια κεφαλίδα για τον αριθμό των διαστάσεων του κάθε στοιχείου καθώς και το συνολικό αριθμό των στοιχείων αυτών.
Το πρόγραμμα διαβάζει ένα τέτοιο αρχείο ( μαζί με την κεφαλίδα αυτού ) και στο πρώτο scanning ορίζει τα **min** και **max** της κάθε διάστασης.

Κάθε διάσταση χωρίζεται σε κομμάτια ( **pages** ) με μια παράμετρο που ονομάζεται **pages**. Η παράμετρος αυτή είναι ίδια για κάθε διάσταση. Αποτέλεσμα αυτής της διαμέρισης της κάθε διάστασης είναι να δημιουργηθούν χωρία (τετράγωνα για τις 2 διαστάσεις, κύβοι για τις 3 διαστάσεις, υπερκύβοι για τις n διαστάσεις). Ο αριθμός των χωρίων ορίζεται από πριν και άρα το εύρος αυτών εξαρτάται από τις μέγιστες και ελάχιστες τιμές των στοιχείων του αρχείου **raw** για κάθε διάσταση. Το χωρίο με ελάχιστα άκρα τα ελάχιστα **(x,y)** εμπίπτει στην αρχή των αξόνων **(0,0)**. Εκτός των ελαχίστων, κάθε χωρίο έχει και μέγιστα. **Η διαφορά των μεγίστων και ελαχίστων σε κάθε διάσταση για ένα χωρίο αποτελεί το εύρος του χωρίου στον άξονα και το χώρο**. 

Ο συνολικός αριθμός των χωρίων στον n-διάστατο χώρο είναι pages^n --> [0, (pages^n)-1]

Κάθε διάσταση έχει ένα **min** και ένα **max**. Το συνολικό εύρος κάθε διάστασης βρίσκεται από τη διαφορά **max-min**. Το εύρος που πρέπει να έχει κάθε χωρίο στην **i**-οστή διάσταση ισούται με το συνολικό εύρος της **i**-οστής διάστασης δια τον αριθμό των **pages**.  

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

Διαβάζουμε ένα αρχείο **raw** και δημιουργούμε ένα αρχείο **indexed** όπως περιέγραψα παραπανω.

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


Το αρχείο **indexed** περιέχει στην κεφαλίδα του:
	
	Αριθμό Στοιχείων Αριθμός Διαστάσεων C-Index Z-Index
	Min	Max	X
	Min 	Max 	Y
	.
	. στοιχεία..
	.
	x	y	c-index(binary)		z-index(binary)
	.
	.

Στη συνέχεια, διαβάζω ένα αρχείο **indexed** για να αποθηκεύσω τα στοιχεία αυτά σε ένα αρχείο **tree** που υλοποιεί ενα B+ Tree.

	while((line = br.readLine()) != null) {
		
		datas = line.split(" ");
		entry = datas[0]+","+datas[1];
			
		keyC = Long.parseLong(datas[2],2);
		keyZ = Long.parseLong(datas[3],2);
		
		bpc.insertKey(keyC, entry, duplicates);
		bpz.insertKey(keyZ, entry, duplicates)
	}

Δημιουργώ έτσι δύο αρχεία δέντρων **tree** για κάθε αρχείο **results**, καθώς οι μέθοδοι ένωσης των bits πρέπει να μελετηθούν ανεξάρτητα.
Τα αρχεία αυτά είναι:

	TreeC --> Αποθηκεύει κάθε στοιχείο στη δομή του δέντρου με Index το αποτέλεσμα του Concatenation
	TreeZ --> Αποθηκεύει κάθε στοιχείο στη δομή του δέντρου με Index το αποτέλεσμα του Interleaving

-----------------------------------------------------------------------

# B+ Tree

Ο φάκελος **RangeQuery** αποτελεί την υλοποίηση του B+ Tree. ( Credits στον andylamp: https://github.com/andylamp/BPlusTree ) 

Πρόκεται για υλοποίηση B+ Tree πάνω στο δίσκο που χρησιμοποιείται για αποθήκευση κλειδιού-τιμής ( περισσότερες λεπτομέρειες αναφέρονται στο link ).

Wikipedia link για B+ Trees https://en.wikipedia.org/wiki/B%2B_tree 


------------------------------------------------------------------------

RangeQuery/BPlusTree/src/main/java/ds/bplus/**bptree/**

Ο φάκελος αυτός περιέχει όλα τα αρχεία που με τη βοήθεια αυτών υλοποιείται το B+ Tree ( είναι ήδη υλοποιημένα από τον andylamp ).

RangeQuery/BPlusTree/src/main/java/ds/bplus/**mdpf/**

Τα αρχεία του φάκελου αυτού επρόκειτω για δική μου υλοποίηση. Πιο συγκεκριμένα:

# **DataFile.java**: 

Η κλάση **DataFile** παίρνει ως όρισμα τον αριθμό των στοιχείων και δημιουργεί ένα αρχείο **raw.txt**.
Ένα αρχείο **raw.txt** περιέχει μια κεφαλίδα:
		
	NumberOfElements	Dimensions	//Header
	.element1(x1, y1, ... n1)		//Elements..
	.element2(x2, y2, ... n2)
	.
	.
	.
	
	
Έχω δημιουργήσει τέσσερα (4) είδη κλάσεων **DataFile** στο **ds.bplus.data** κατάλογο. Είναι τα ακόλουθα:

**UniformDataFile.java**: Τα στοιχεία είναι **Random**, μοντελοποιούν συντεταγμένες (στα όρια των ΗΠΑ) με δεκαδική ακρίβεια **6** ψηφίων.

	DecimalFormat df  = new DecimalFormat("#.######");
	// *United States of America* 
	private final double lat_max = 48.682856;	//maximum latitude
	private final double lat_min = 25.712085;	//minimum latitude
	private final double long_max = -68.275846;	//maximum longitude
	private final double long_min = -124.874623;	//minimum longitude
	
και ακολουθούν ομοιόμορφη κατανομή. Η κατηγορία της κλάσης είναι 
	
	private String category = "uniform";
		
		
**GaussianDataFile.java**: Είναι ακριβώς όπως η προηγούμενη, με τη διαφορά ότι τα στοιχεία ακολουθούν την κανονική κατανομή.		
		
	private String category = "gaussian";
	
**ClusterDataFile.java**: Τα στοιχεία αποθηκεύονται ομοιόμορφα γύρω από **N** στοιχεία αναφοράς **(x,y)**.
Τα στοιχεία αναφοράς παράγονται ψευδοτυχαία ακολουθώντας ομοιόμορφη κατανομή και τα στοιχεία προς αποθήκευση παράγονται σε μέγιστη απόσταση **σ** από κάθε στοιχείο αναφοράς.
Το καινούργιο **lowerBound** και **upperBound** για κάθε διάσταση που αποτελούν τα όρια για την παραγωγή των στοιχείων υπολογίζονται:
	
	//Βρίσκω πρώτα το εύρος σε κάθε διάσταση.
	rangeX = Xmax - Xmin  
	rangeY = Yman - Ymin
	
	//Υπολογίζω τις αποστάσεις για το σημείο αναφοράς.
	
	//για το Χ
	lowerBound[0] = x_ref - (rangeX*s)/100;
	upperbound[0] = x_ref + (rangeX*s)/100;
	
	//για το y
	lowerBound[1] = y_ref - (rangeY*s)/100;
	upperBound[1] = y_ref + (rangeY*s)/100;
	
όπου **(x_ref,y_ref)** το σημείο αναφοράς και **s** η απόσταση σε ποσοστό % εκατέρωθεν του σημείου αυτού.
	
	private String category = "cluster";
	
**RealDataFile.java**: Η κλάση αυτή διαβάζει ένα αρχείο .csv με πραγματικά δεδομένα, απομονώνει τα **longitude** και **latitude** και τα αποθηκεύει σε αρχείο **raw**. Ο αριθμός των γραμμών που διαβάζει από το αρχείο .csv, και εν τέλει αποθηκεύει σε ένα αρχείο **raw** είναι ίσος με τον αριθμό που παίρνει σαν όρισμα στον constructor της, η κλάση **RealDataFile**.
	
	private String category = "real";

Προκειμένου να χρησιμοποιήσω κάποια από τις παραπάνω κλάσεις, την φορτώνω στην κλάση **DataFile** ( διορθώνοντας στα σημεία του **package**, και ονομασίες κλάσεων και constructor ) προκειμένου η **DataFile.java** να μπορεί να τρέξει.  
	
Παράδειγμα εκτέλεσης 100 χιλιάδων στοιχείων:	

	DataFile df = new DataFile(100000,"");
		
δίνει αποτέλεσμα

	RangeQuery/DataDirectory/category/raw/100K.txt

Η κλάση χειρίζεται και την περίπτωση δημιουργίας πολλαπλών αρχείων **raw** ίδιου αριθμού στοιχείων.
		
	RangeQuery/DataDirectory/category/raw/100K_1.txt //suffix = "1";
	RangeQuery/DataDirectory/category/raw/100K_2.txt //..
	RangeQuery/DataDirectory/category/raw/100K_3.txt //..
	RangeQuery/DataDirectory/category/raw/100K_4.txt //..
	RangeQuery/DataDirectory/category/raw/100K_5.txt //..
	

# **ResultFile.java**: 

Η κλάση **ResultFile** παίρνει ως όρισμα ένα **DataFile instance object** καθώς και έναν ακέραιο αριθμό **pages** που ορίζει σε πόσα "κομμάτια" θα χωριστεί η κάθε διάσταση προκειμένου να δημιουργηθούν τα χωρία στον (δισ)διάστατο χώρο.

Διαβάζει το αρχείο **DataFile** (που βρίσκεται στη μνήμη, σε κάποιο κατάλογο, είτε δίνοντας το όνομα του αρχείου raw, είτε δίνοντας το όρισμα του **DataFile** object.)

	ResultFile rf = new ResultFile(new DataFile(100000), 8)
		
					ή
					
	ResultFile rf = new ResultFile("100K.txt", 8)
		
Ο πρώτος τρόπος είναι πιο χρηστικός και προτιμότερος για πολλαπλά αρχεία.

Το output της κλάσης **IndexingFile** είναι ένα αρχείο **indexed** το οποίο συζητείται στην αρχή μαζί με τις μεθόδους ένωσης bits.
Το αρχείο **indexed** έχει την παρακάτω μορφή

	numberOfElements Dimensions C-Index Z-Index
	Min	Max	X
	Min 	Max 	Y
	.
	. στοιχεία..
	.
	x	y	c-index(binary)		z-index(binary)
	.
	.
	
Το αρχείο **indexed**:

	RangeQuery/catego/results/100K_8_res.txt

και για πολλαπλά αρχεία

	RangeQuery/DataDirectory/category/indexed/100K_1_8_res.txt
	RangeQuery/DataDirectory/category/indexed/100K_2_8_res.txt
	RangeQuery/DataDirectory/category/indexed/100K_3_8_res.txt
	RangeQuery/DataDirectory/category/indexed/100K_4_8_res.txt
	RangeQuery/DataDirectory/category/indexed/100K_5_8_res.txt
		
		
# **TreeFile.java**:

Η κλάση **TreeFile** παίρνει ως όρισμα ένα **IndexingFile instance object** καθώς και ένα σετ μεταβλητών (ακέραιοι) που αρχικοποιούν ένα δέντρο B+ Tree.

Ο **constructor** της κλάσης είναι (απλοποιημένη μορφή):

	private BPlusConfiguration bconf;
	private BPlusTreePerformanceCounter bPerf;

	public TreeFile(int pageSize, int keySize, int entrySize,IndexingFile rf) throws IOException, InvalidBTreeStateException{
		String readMode = "rw+";
		this.bconf = new BPlusConfiguration(pageSize,keySize,entrySize);
		this.bPerf = new BPlusTreePerformanceCounter(true);
		this.inf = inf;
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


Στον ίδιο constructor της **TreeFile**, εισάγω τα στοιχεία του **IndexFile object instance**:

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
	
	RangeQuery/DataDirectory/category/bins/Tree_100K_8_res_1024_8_24_C.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_8_res_1024_8_24_Z.bin
	
και για πολλαπλά αρχεία
	
	RangeQuery/DataDirectory/category/bins/Tree_100K_1_8_res_1024_8_24_C.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_1_8_res_1024_8_24_Z.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_2_8_res_1024_8_24_C.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_2_8_res_1024_8_24_Z.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_3_8_res_1024_8_24_C.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_3_8_res_1024_8_24_Z.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_4_8_res_1024_8_24_C.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_4_8_res_1024_8_24_Z.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_5_8_res_1024_8_24_C.bin
	RangeQuery/DataDirectory/category/bins/Tree_100K_5_8_res_1024_8_24_Z.bin

με 
1) pageSize	= 1024 Bytes
2) keySize	= 8 Bytes
3) entrySize	= 24 Bytes



# **TreeMods.java**: 

Η κλάση **TreeMods** διαχειρίζεται αντικείμενο **TreeFile**. Ο constructor της είναι:

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

Η κλάση αυτή περιέχει τις ακόλουθες μεθόδους που εφαρμόζονται πάνω στα δέντρα:

* **public void getLeafElements()**

Η μέθοδος αυτή επιστρέφει δύο αρχεία

	LeafElements-100K_1_256_res_1024-8-24_C.txt
	και
	LeafElements-100K_1_256_res_1024-8-24_Z.txt
	
που περιέχουν τον αριθμό και το εύρος των **indices** που βρίσκοντα σε κάθε φύλλο.
Η μέθοδος ξεκινά από το πρώτο κλειδί (0) και τελειώνει στο τελευταίο ( (page^2)-1 ).

Aρχικά, βρίσκω το πρώτο index που υπάρχει στο δέντρο, πηγαίνοντας στο επόμενο κάθε φορά που δεν το βρίσκω.
	
	long maxIndex = (pages*pages)-1;
	SearchResult sr; // Αρχικοποίηση αντικειμένου εκτός κύριου κορμού αλγορίθμου.
	
	sr = bp.searchKey(lower, true); //lower --> κλειδί και ταυτόχρονα κάτω φράγμα εύρους.
	while(!sr.isFound()) {
		lower++; // go next index
		if(lower>maxIndex) {
			break;
		}
		sr = bp.searchKey(lower, true);
	}
Βρίσκω το φύλλο στο οποίο βρίσκεται το **index**, 
	
	leaf = sr.getLeaf();
το τελευταίο κλειδί που υπάρχει στο φύλλο **leaf**,

	upper = leaf.getLastKey();
και τη χωριτικότητα του φύλλου, δηλαδή τον συνολικό αριθμό των **indices** που υπάρχει στο φύλλο,
	
	capacity = leaf.getCurrentCapacity();
και αποθηκεύω τις μεταβλητές **upper**, **lower** και **capacity** σε ένα αρχείο **LeafElements**.
	
	writer.append(chars+" "+String.valueOf(capacity)+" "+String.valueOf(lower)+"-"+String.valueOf(upper));
Τέλος

	lower = upper+1; // go next range
πηγαίνω στο επόμενο φύλλο **leaf** μετακινόντας τη μεταβλητή κάτω φράγματος **lower** ένα **index** από το τελευταίο κλειδί του προηγούμενου φύλλου.

* **private ArrayList<**Long**>** **resultQuery(double[] lb, double[] ub, String curve)**

Η μέθοδος **resultQuery()** παίρνει ως όρισμα δύο πίνακες και ένα αλφαριθμητικό που ορίζει τη μέθοδο indexing (C και Z) και επιστρέφει μία λίστα **ArrayList<**Long**>** η οποία περιέχει όλα τα indices των χωρίων που καλύπτονται από το "τετράγωνο" που δημιουργείται από τους πίνακες **lb** και **ub**.

	minIndex = findIndex(lb,curve);
	maxIndex = findIndex(ub,curve);
	
Η findIndex βρίσκει και επιστρέφει το **index** το οποίο αντιστοιχεί για τα σημεία 
	
	lb[0] // minX
	lb[1] // minY
	ub[0] // maxX
	ub[1] // maxY
	
για μία συγκεκριμένη μέθοδο indexing. (C ή Z).

Ο κώδικας της **resultQuery** συνεχίζεται ως εξής:

	minlong = revConcat(minIndex);	//minlong = revLeave(minIndex);
	cnt = revConcat(minIndex);	//cnt = revLeave(minIndex);
	maxlong = revConcat(maxIndex);	//maxlong = revLeave(maxIndex); 
	for(int i=(int)minlong[0];i<=maxlong[0];i++) { // we add every index that is between 
							( 2-dimensional ) the min and max.
		for(int j=(int)minlong[1];j<=maxlong[1];j++) { // meaning that we parse the box to get every 
								index(interleaved or concatenated)
			longlist.add(findIndexLong(cnt,curve)); // that is inside the lowerBound and upperBound.
			cnt[1]++;
		}
		cnt[1] = minlong[1];
		cnt[0]++;
	}

Η συνάρτηση 

	revConcat(index) // reverse concatenation 
και 
	
	revLeave(index) // reverse interleaving
	
εφαρμόζουν αντίθετη μέθοδο indexing προκειμένου να σπάσουν το **index** στα αρχικά χωρία με σύνολο τιμών [0, pages-1] για κάθε διάσταση.
Για παράδειγμα:

**Reverse Concatenation**:
	
	index = 10 --> Binary --> 001010 (6 bits representation)
	001010 ---> x1x2x3y1y2y3 
	
	x1x2x3 = 001 --> 1
	y1y2y3 = 010 --> 2
	
	long[0] = 1
	long[1] = 2
	
**Reverse Interleaving**:
	
	index = 10 --> Binary --> 001010 (6 bits representation)
	001010 ---> x1y1x2y2x3y3 
	
	x1 = 0, x2 = 1, x3 = 1.
	y1 = 0, y2 = 0, y3 = 0
	
	x1x2x3 = 011 --> 3
	y1y2y3 = 000 --> 0 
	
	long[0] = 3
	long[1] = 0
				
Έπειτα, η μέθοδος διασχίζει το δισδιάστατο χώρο και εισάγει τα **indices** που συναντεί στη λίστα που επιστρέφει.
	
	for i <- MINx1x2x3 to MAXx1x2x3  
		for j <- MINy1y2y3 to MAXy1y2y3
			indexOfTwoDimCoordinates <- IndexingMethod(i,j);
			list.add(indexOfTwoDimCoordinates);
		end for
	end for
Τέλος επιστρέφει την λίστα.

	return longlist;
				
* **public QueryComponentsObject rangeQuery(double[] lb, double[] ub)**

Η μέθοδος αυτή παίρνει ως όρισμα δύο δισδιάστατους πίνακες ( αφού είμαστε στις δύο διαστάσεις )
	
	double[] lb 	// lowerBound array containing minimum real values (not indices)
	double[] ub	// upperBound array containing maximum real values (not indices)
και επιστρέφει ένα αντικείμενο αποθήκευσης **QueryComponentsObject** στο οποίο αποθηκεύω:
	
	QueryComponentsObject qco  = new QueryComponentsObject(totalC,totalZ,rrC.getQueryResult().size(),
				rrZ.getQueryResult().size(),falsePosC, falsePosZ, leafReadsC, leafReadsZ);
**συνολικούς χρόνους εκτέλεσης**, **αριθμός (έγκυρων) αποτελεσμάτων**, **false positives**, και τον **αριθμό των φύλλων που διαβάστηκαν** (και για δέντρα C και Z).

Όπως και σε κάθε μέθοδο **Query()**, αρχικά χρησιμοποιώ την **resultQuery()** για να μου επιστρέψει σε μια λίστα όλα εκείνα τα **indices** στα οποία "ακουμπάει" το σετ δεδομένων των **lb** και **ub**  

	ArrayList<Long> longListC = resultQuery(lb,ub,"C");
	ArrayList<Long> longListZ = resultQuery(lb,ub,"Z");

Έπειτα, βρίσκω το ελάχιστο(**min**) και το μέγιστο(**max**) index της κάθε λίστας και χρησιμοποιώ την **rangeSearch()** στο εύρος **[min, max]**.

	long minIndex = getMinIndexFromList(longListC);
	long maxIndex = getMaxIndexFromList(longListC);
	RangeResult rrC = bpc.rangeSearch(minIndex, maxIndex, nonDupes);	
	
Αφαιρώ όλα τα στοιχεία τα οποία **ενώ** έχουν κλειδιά εντός του εύρους **[minIndex, maxIndex]**, έχουν τιμές **εκτός των οριών** **lb[]** και **ub[]**. Αυτό πραγματοποείται με τη μέθοδο **refineQuery()** η οποία παίρνει σαν όρισμα τους πίνακες **lb[]** και **ub[]** καθώς και το αντικείμενο **RangeResult** που περιέχει τα αποτελέσματα του **rangeSearch()**. Διαγράφει τα ανεπιθύμητα αποτελέσματα.

Τέλος,

	rangeIOres = bpc.getPerformanceClass().rangeIO(minIndex, maxIndex, nonDupes, false);
	leafReadsC = rangeIOres[4]; // getInterminentLeafPageReads();
	
βρίσκω πόσα φύλλα διαβάστηκαν για το query στο εύρος [minIndex, maxIndex].

Παράδειγμα:
	
	list: [5,1,3,7,9,11,15] //indices
	min = 1
	max = 15
	
	rangeSearch(1,15) --> (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
	false positives = 2,4,6,8,10,,12,13,14	//τουλάχιστον διότι για τα σωστά indices 
						// κάποια στοιχεία μπορεί να είναι εκτός εύρους
	

* **public QueryComponentsObject fractionsQuery(double[] lb, double[] ub)**

Η μέθοδος αυτή δουλεύει με παρόμοιο τρόπο με την **rangeQuery()**. Η μόνη διαφορά είναι ψάχνει ένα ένα τα indices στην λίστα και για κάθε από αυτά επαναλαμβάνει τις 
	
	RangeResult rrC = bpc.rangeSearch(list.get(i));
	refineQuery(rrC);
για το συνολικό πλήθος indices στη λίστα **list**. Επιστρέφει και αυτή ένα αντικείμενο **QueryComponentObject**.
Παράδειγμα:

	list: [5,1,3,7,9,11,15] //indices
	for i<-0 to list.size()
		rangeSearch(list.get(i));
	end for

* **public QueryComponentsObject rangeFractionsQuery(double[] lb, double[] ub)**

Η μέθοδος δουλεύει όπως οι προηγούμενες δύο (και επιστρέφει το ίδιο αντικείμενο). Όμως, αντίθετα με τις προηγούμενες, προσπαθεί να βρει εύρη συνεχόμενων indices για την **rangeSearch()**. 

	
	RangeResult rr;
	int min <- list.get(0) //first index
	int max <- list.get(0) //first index
	for i<-1 to list.size()
		if (list.get(i)<min) OR (list.get(i)-max > 1)
			rr = bp.rangeSearch(min,max);
			refineQuery(rr);
			......
			min <- list.get(i);
			max <- list.get(i); 
			......
		end if
		max <- list.get(i);
	end for
	rr  = bp.rangeSearch(min,max);
	refineQuery(rr);
	
	return;
	
Για μία λίστα όπως η παρακάτω, αυτός ο αλγόριθμος θα πράξει τα εξής:
	
	list: [5,4,1,2,3,6,7,9,10,11,15] // example list.
	
	rangeSearch(5,5);
	rangeSearch(4,4);
	rangeSearch(1,3);
	rangeSearch(6,7);
	rangeSearch(9,11);
	rangeSearch(15,15);

# Αρχεία

Για δεδομένα:
* pageSize 
* keySize
* entrySize
* pages
* numberOfElements

Παράγονται τα ακόλουθα αρχεία:

1)

	Key-Value-InsertionTime-numberOfElements_x_pages_res_pageSize-keySize-entrySize_C.txt
	Key-Value-InsertionTime-numberOfElements_x_pages_res_pageSize-keySize-entrySize_Z.txt
που αποθηκεύει το χρόνο κάθε εισαγωγής στο αντίστοιχο αρχείο δέντρου.

2) 

	TreeBlockNumber-numberOfElements-pages-pageSize-System.currentTimeMillis().txt
	
που αποθηκεύει το σύνολο των Blocks για όλα τα αρχεία της ίδιας εκτέλεσης (που δημιουργούνται από ένα **ManageFile** αντικείμενο). 
Για το σκοπό αυτό χρησιμοποιώ την μέθοδο 

	getTotalTreePages();
πάνω στο αντικείμενο δέντρου που αντιστοιχεί στο συγκεκριμένο αρχείο.

3)

	LeafElements-numberOfElements_x_pages_res_pageSize-keySize-entrySize_C.txt
	LeafElements-numberOfElements_x_pages_res_pageSize-keySize-entrySize_Z.txt
	
που αποθηκεύει τον αριθμό και το εύρος των indices που βρίσκονται σε κάθε φύλλο.

4) 

	TreeConstructionTime-numberOfElements-pages-pageSize-System.currentTimeInMillis().txt
που αποθηκεύει για το σύνολο των αρχείων της ίδιας εκτέλεσης, τον συνολικό χρόνο για την παραγωγή του αρχείου δέντρου.

*x = η αρίθμηση των αρχείων όταν δημιουργούνται κατά ομάδες των πολλών με χρήση της **ManageFile**.*
		

----------------------------------------------------------------------------------------------------------------------------
#Directory - RangeQuery
	
AIS-Data
	nari_dynamic.csv
BPlusTree

	src/main/java/ds/bplus
		bptree
			BPlusConfiguration.java
			BPlusTree.java
			BPlusTreePerformanceCounter.java
			DeleteResult.java
			KeyValueWrapper.java
			RangeResult.java
			SearchResult.java
			TreeInternalNode.java
			TreeLeaf.java
			TreeLookupOverflowNode.java
			TreeNode.java
			TreeNodeType.java
			TreeOverflow.java
		data
			ClusterDataFile.java
			GaussianDataFile.java
			RealDataFile.java
			UniformDataFile.java
		fudger
			-
		mdpf
			DataFile.java
			FileMods.java
			IndexingFile.java
			StatFile.java
			TreeFile.java
			TreeMods.java
		multiple
			ManageFile.java
		object
			KeyValuesObject.java
			QueryComponentsObject.java
			TimeQuarters.java
		util
			-
		
DataDirectory

	cluster
		bins
		indexed
		raw
		statistics
		stat-results
	gaussian
		bins
		indexed
		raw
		statistics
		stat-results
	real
		bins
		indexed
		raw
		statistics
		stat-results
	uniform
		bins
		indexed
		raw
		statistics
		stat-results
src/com/compamy

	Main.java
	Main2.java
	..
	Mains....
	
	
