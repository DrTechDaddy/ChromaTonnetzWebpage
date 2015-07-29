package dtdPr2HtmC20150525;


import java.io.*;
import java.util.*;

/** PrDoc parses the stafftonnetz 1b csv score file */

public class  PrDoc {
	/** piano roll document format
	
	// ROW  LAYOUTS 
	// L layout info rows: col [0],[1]
	 * L,null: comment line, ignored
	 * L,T	Titles: work, [composer], [lyrics]; missing titles blank
	 * L,F	Fonts, Size: music, size, [timing, size], [expression, size]; missing values default to system default font
	 * L,V	n, d, c, c, ...	n= number of voices; d = default note color; c c ... HTML colors for voice 1, 2, etc.
	 * 		if d is null, no colors are used 
	 * 
	// L,K column codes for csv file
	//		t = timing
	//		s = staff positions
	//		c = chord and expression info
	 * 
	// 	//	T = timing constants; specify measure(s) 1-origin, counts per measure 1-orgn, and ticks per count 0-orgn
	 * 
	 * 
	// 	L,M = midi note numbers for each staff position; 0 for not staff
	 * 	
	 * 
	// 	L,H = note head characters (shape notes) for each staff position (may be transposed by user); "" for none	 
	 * 
	// 	L,S:  THIS MUST BE THE LAST HEADER LINE. 
	 * 		= model staff line used to determine column layout of staff, includes:
	 * 			0 place-holders for counts columns
	 * 			* staff characters for each staff position; 
	 * 				these can be aligned as "shape notes" to assign a specific symbol to a scale degree vs. a pitch	
	 * 			string place-holders for chord/expression columns
	 * 
	//	NOTE DATA ROWS
	//	NOTE CODE col[0]
		n	note letter names 	A B C D E F G  natural; d e  g a b FLAT; c f SHARP; TBD: I J  H K L Black (Keller)
									// or w b n # x for accidentals (FOLLOWOING 
		s	solfedge row (scale degree names: d r m f s l t  DIATONIC;  b k n  p j  PENTATONIC  TBD: + u o a FLAT; + e i sharp
		v	voice row ( numbers )
		f	finger row (numbers)
		p	page break [after bar if any; no notes]
	//	*	TBD multiple values in one entry, e.g: v n s f separated by space

	 	BAR CODES col[1]: Bar precedes note; for end bar, add blank note line
	 	null	no bar; also used with page break
	 **				uses border -width -style 
	 *	#		section			(double)	pound
	 * 	!		group border  (thick )		exclamation
	 * 	|		measure	border (solid)		vertical bar
	 * 	-		count border	(thin )		hyphen
	 * 	.		tick (subcount) border (dotted) -- more visible that dashed		period
	 *  _		phrase break	(dashed)	underscore				
	 TBE*  G		grayed (duplicate) line follows; used before or after page break
	 TBD*  L		blank line 
	 //TBD  [| ending |]   |: repeat :|  { expression } 

	 
	 //
	 *  COUNTS
	 *  number	measure, count, or tick
	 *  .		marker for count / tick columns
	 // 	GENERAL
	 * 	
	 	, 			column separator
	 	% or z			rest
	 	" or y			continue
	 	^ or v			staccato
	 	u				fermata
	 	NOTES row
	 	A=G,a-g		note value A-G  NATURAL, de gab FLAT; cf SHARP
	 	VOICES
	 	s a t b	
	 	FINGERS
	 	6 7 8 9 0    1 2 3 4 5		LH, RH	
	 	 

	 * */
	
	// DATA
	
	// PrDoc  State into
	
	
	//private  int [] 		midiNums = new int [90];	    //midi numbers for pianoroll positons
	//private String [] 	staffChars = new String [90]; // staff chars for pianoroll positions
	//private String [] 	noteheadChars  = new String [90]; //note chars for pianoroll positions
	
	private String 		ifileName;  
	private Scanner 	input;  // text file input object

	private	String 		isStaffLine;	// input buffer from csv
	private String 		ifStaffLine;    //filtered input line; csv escapes corrected
	
	private	String		ps;				// work string
	private	StringBuffer pb;			// work buffer
	private int[]		lastVoice;		//last (highest) voice to use this pitch; for voicing % " etc.
	
	private	int			sourceLineNum;		//current source line
	private int			colnum;			// current column in line; <0 means no line in buffer
	
	//RECORD LAYOUT
	String				recType;		//col 1: record type
	String				subType;		//col 2; record subtype 
	
	
	// METHODS

	
	PrDoc(String fileName) { //Constructor
		//open input
		ifileName = fileName+".csv"; 
		System.out.printf ("\nPrDoc: ifileName= %s",ifileName);
		Logger.log.printf ("\nPrDoc: ifileName= %s",ifileName);
		

		 try{
			 input = new Scanner( new File (ifileName) ).useDelimiter(",");//instantiate input file reader object
		 }
			catch (FileNotFoundException fileNotFoundException ) {
			  System.err.println("PrDoc: ERROR opening file: " + ifileName);
			  System.exit(1);
			}
		System.out.printf ("\nPrDoc: new Scanner");
		Logger.log.printf ("\nPrDoc: new Scanner");
		
		lastVoice	=	new int[90]; 	//initialized to 0 by java	
		sourceLineNum =0;
		
	} // Constructor
	
	
	public Layout  prGetLayout() {
		Layout lP = Layout.theLayout();
		StringBuffer obStaffLine = new StringBuffer();
		
		System.out.printf ("\nprGetLayout: ");
		Logger.log.printf ("\nprGetLayout: ");		
 
	 String isStaffLine = new String();					//raw input line: user csv file
	 String ifStaffLine = new String();  //filtered input line; csv escapes corrected
//	 StringBuffer obStaffLine = new StringBuffer(250);  // output line for printing
	 
	 
	// initialize staff globals
/*
	 	public String	docTitle;		// title of piece				// from LT
		public String	docComposer;	// composer						//from LT
		public String	docLyrics;		// lyricist						//from LT
		public String	fontMusic;		//font name for music chars		// from LF
		public String	fontCounts;		//font name for counts chars		// from LF
		public String	fontExpression;	//font name for chord chars		// from LF
		public int 		numVoices;		//number of voice colors starting 0	// from LV
		public String[]	colorsVoice;	//array of HTML voice colors; 0=default thru numVoices-1;
		public int		midiStart;		//midi number of first/loweest staff position
		public int 		midiEnd;			//midi number of last/highest staf possition
		public int [] 		midiNums = 	new int [INI.MAX_HTM];	    //midi numbers for pianoroll positons
		public String [] 	staffChars = new String [INI.MAX_HTM]; // staff chars for pianoroll positions
		public String [] 	noteheadChars  = new String [INI.MAX_HTM]; //note chars for pianoroll positions

		
		
		//public int		colsCounts;		//number of counts colums:  m-r t.s
		//public int		colsNotes;		//number of staff columns:
		//public int		colsChords;		//number of chord columns
	 measure=0;
	 count=0;
	 subcnt=0;
	 ticksPerCount = 0;
	 subcntPerCount=0;
	 int measureNow=-1;
	 int countNow=-1;
	 int subcntNow=-1;
	 int measurePrev=-2;
	 int countPrev=-2;
	 int subcntPrev=-2;
	 measureUsed = countUsed = subcntUsed = false;
	 
	 timeForce = false;
*/	 
	for (int ihl =1;ihl<INI.MAX_HEADS;ihl++) {					// get layout lines	
		String[] tokens=prGetLine();
		int numTokens = tokens.length;
		recType = tokens[0];
	if(! recType.matches("L") )	break;		//ERROR: not a header record
		subType = tokens[1];
	    	// L layout info rows:
	    	 //* L,T	Titles: work, [composer], [lyrics]; missing titles blank
	    	 //* L,F	Fonts, Size: music, size, [timing, size], [expression, size]; missing values default to system default font
	    	 //* L,V	n, d, c, c, ...	n= number of voices; d = default note color; c c ... HTML colors for voice 1, 2, etc.
	    	 //* 
	    	 if(subType.matches("T")) {	//titles
	    		 if (tokens[2] != ""){ lP.docTitle = tokens[2];} else {lP.docTitle=lP.commonFileName;}
	    		 lP.docComposer=tokens[3];
	    		 lP.docLyrics=tokens[4];
	    	 }
	    	 else if (subType.matches("F")) {	//fonts
	    		if(! tokens[2].matches("")) {lP.fontMusic=tokens[2];} else {lP.fontMusic=INI.DEF_FONT_MUSIC;}
	    		if(! tokens[3].matches("")) {lP.fontsizeMusic=tokens[3];} else {lP.fontsizeMusic=INI.DEF_FONT_SIZE;}

		    	if(! tokens[4].matches("")) {lP.fontCounts=tokens[4];} else{ lP.fontCounts=INI.DEF_FONT_COUNTS;}
	    	 	if(! tokens[5].matches("")) {lP.fontsizeCounts=tokens[5];} else{ lP.fontsizeCounts=lP.fontsizeMusic;}

		     	if(! tokens[6].matches("")) {lP.fontExpression=tokens[6];} else {lP.fontExpression=INI.DEF_FONT_EXPR;}
	     		if(! tokens[7].matches("")) {lP.fontsizeExpression=tokens[7];} else {lP.fontsizeExpression=lP.fontsizeMusic;}
	    	 }
	    	 else if (subType.matches("V")) {	//voice colors
	    		 
	    		 if(tokens[2].matches("\\d+")) {
	    			 lP.numVoices=Integer.parseInt(tokens[2]);
	    			 for(int icolor=0; icolor <= lP.numVoices; icolor++) {
	    		 		lP.colorsVoice[icolor]=tokens[icolor+3];
	    		 	}
	    		 }
	    		 else {lP.numVoices=0; lP.colorsVoice[0]=INI.DEF_COLOR ; }

	    	}  // if VV
		    else if (subType.matches("K")){	// column layout
			   lP.countsLo=lP.midiLo=lP.exprLo=numTokens+1;	//initialize Lo
			   lP.countsHi=lP.midiHi=lP.exprHi=-1;					//initialize Hi
			   for( int tokenNum=2; tokenNum < numTokens; tokenNum++ ){
		     	   String tokenChars = tokens[tokenNum]; 
		     	   
		     	   //Logger.log.printf("\nprGetLayout K: INPUT TOKEN[%d]=<%s>",tokenNum, tokenChars);
		          //output.format("\n<%s>",tokens[tokenNum]); 
		     	   if (tokenChars.matches("t") ){
		     		   if(tokenNum<lP.countsLo){
		     			   lP.countsLo=tokenNum;
		     		   }
		     		   if(tokenNum>lP.countsHi){
		     			   lP.countsHi=tokenNum;
		     		   }
		     		   
		     		  //Logger.log.printf("\nprGetLayout K: Counts [<%d>,:<%d> ]",lP.countsLo,lP.countsHi );
		     	  } // if t	     	  
		     	   else if (tokenChars.matches("s") ){
		     		   if(tokenNum<lP.midiLo){
		     			   lP.midiLo=tokenNum;
		     		   }
		     		   if(tokenNum>lP.midiHi){
		     			   lP.midiHi=tokenNum;
		     		   } 
		     		  
		     		  //Logger.log.printf("\nprGetLayout K: Midi   [<%d>,:<%d> ]",lP.midiLo,lP.midiHi );
		     	   }// if s
		     	  
		     	  else if (tokenChars.matches("c") ){
		     		   if(tokenNum<lP.exprLo){
		     			   lP.exprLo=tokenNum;
		     		   } 
		     		   if(tokenNum>lP.exprHi){
		     			   lP.exprHi=tokenNum;
		     		   }
		     		  
		     		  //Logger.log.printf("\nprGetLayout K: Expr   [<%d>,:<%d> ]",lP.exprLo,lP.exprHi );
		     	  } // if c
		     	  
		     	  else	   {}
		     	   
		     	   
		    	 } //for k
			   	lP.sTokenLo=lP.countsLo;
		     	lP.sTokenHi=lP.exprHi;
		     	//Logger.log.printf("\nprGetLayout.convertFIle K: sToken   [<%d>,:<%d> ]",lP.sTokenLo,lP.sTokenHi );
		    } //if k
		     
			   else if (subType.matches("H") ) { // ShapeNote Chars	
				   for( int tokenNum=lP.midiLo; tokenNum <=lP.midiHi; tokenNum++ ) {
			     	   	lP.noteheadChars[tokenNum] = "";
				   }
				   for( int tokenNum=lP.midiLo; tokenNum <= lP.midiHi; tokenNum++ ){
			     	   lP.noteheadChars[tokenNum] = tokens[tokenNum];
			     	   
			     	  //Logger.log.printf("\nprGetLayout H: INPUT TOKEN[%d]=<%s>",tokenNum,tokens[tokenNum]);
		              //output.format("\n<%s>",tokens[tokenNum]);
		              //Logger.log.printf("\nPrToSk1.convertFIle H: OUTPUT HEAD[%d]=<%s>",tokenNum,lP.noteheadChars[tokenNum]);
		              //output.format("<%s>",lP.noteheadChars[tokenNum] );
			       }  //for H
			   }//if H
			   else if (subType.matches("M") ) {// MIDI note numbers	
				   for (int m=0; m < INI.MAX_HTM; m++) {
					   lP.midiNums[m]=0;	//zero all unused values
				   }
				   for( int tokenNum=lP.midiLo; tokenNum <= lP.midiHi; tokenNum++ ){			 
			     	   lP.midiNums[tokenNum] = Integer.parseInt( tokens[tokenNum] );
			     	   
			     	  //Logger.log.printf("\nprGetLayout M: INPUT TOKEN[%d]=<%s>",tokenNum,tokens[tokenNum]);
		              //output.format("\n<%s>",tokens[tokenNum]);
		              //Logger.log.printf("\nprGetLayout M: OUTPUT MIDI[%d]=<%d>",tokenNum,lP.midiNums[tokenNum]);
		              //output.format("<%d>", lP.midiNums[tokenNum] );
			       } // for M
			   } // if M
			   else if (subType.matches("S") ) { // model line with Staff Chars 
				   
				   for( int tokenNum=2; tokenNum < numTokens; tokenNum++ ) {
			     	   	lP.staffChars[tokenNum] = tokens[tokenNum];
			     	  
			              //Logger.log.printf("\nprGetLayout S: INPUT TOKEN[%d]=<%s>",tokenNum,tokens[tokenNum]);
			              //output.format("\n<%s>",tokens[tokenNum]);
			              //Logger.log.printf("\nprGetLayout S: OUTPUT STAFF[%d]=<%s>",tokenNum,lP.staffChars[tokenNum]);
			              //output.format("<%s>",lP.staffChars[tokenNum] );
				   }//for S
	break;		// S is last header record type.
			   }//if S
			   else { ; }	// ignore subtyoe of L
	}//for L headline
	
	for(int ic =0; ic < INI.MAX_HTM; ic++ )	{//det default color
		lP.currentColor[ic]=0;
	}
	
	// at this point, the iP header should be complete
	//    and a presumably blank, unprocessed line is in tokens[].
	
	 	System.out.printf (	"\nprGetLayout: END sourceLineNum=<%d>",sourceLineNum);
		Logger.log.printf ("\nprGetLayout:  END sourceLineNum=<%d>",sourceLineNum);	
		
	   return(lP);
	   
	}//getLayout
	

	
	public String[] prGetLine() {	//returns String[] of tokens
	 // Get the next line from the source
		ifStaffLine = new String(); 
		ifStaffLine="";
		String[] osTokens;
		String[] nullTokens = {"",""};	//blank line for Error Return
	System.out.printf ("\n\nPrDoc.prGetLine");
	Logger.log.printf ("\n\nPrDoc.prGetLine");
		
		if( input.hasNext() ) { //another staffLine?
		 // input line
		   try{
		     isStaffLine= input.nextLine() ;
		     ++sourceLineNum;
		   System.out.printf("\nPrDoc.prGetLine "
		    		 +"isStaffLine #%d=\n<%s>",sourceLineNum,isStaffLine);
		   Logger.log.printf("\nPrDoc.prGetLine "
		    		 +"isStaffLine #%d=\n<%s>",sourceLineNum,isStaffLine);
		   }
				   catch ( FormatterClosedException formatterClosedException ){
				     System.err.println("PrToSk1.convertFile: OUTPUT ERROR: error writing to file ");
				     return(nullTokens);
				   }
				   catch(NoSuchElementException noSuchElementException){
				     System.err.println("PrToSk1.convertFile: INPUT ERROR: invalid input line ignored; reenter ");
				     //input.nextLine();//discard input and read next line
				     return(null);
				   }
			 //clean up csv format: convert smart quotes, etc.		
			 ifStaffLine = prFixCsv(isStaffLine);   //filter csv line: remove qualifier quotes
			 
			 osTokens = prTokenize (ifStaffLine);	// Convert to tokens
			 int nt = osTokens.length;
		System.out.printf("\nprGetLine: osTokens[%d]: ",nt);
		Logger.log.printf("\nprGetLine: osTokens[%d]: ",nt);
			 for (int it=0; it < nt; it++){
		System.out.printf(" [%d]=<%s>",it, osTokens[it]);
		Logger.log.printf(" [%d]=<%s>",it, osTokens[it]);
			 }
			return (osTokens);    	   
		} //if input.hasnext()
		else{
			return (null);
		}//if input.hasnext()	
		
		
	} // prGetLine
	
	private String prFixCsv (String origin) {
		//this routine cleans up data quotes and commas in a csv file
		//this routine replaces "," with single space 
		// and """" with a single quote and curly quotes with straight
		
		//Mac Numbers and Excel CSV files: 
		  // encloses commas in quotes ","
		  // enclose quotes in quotes, and doubles the data quote:    """"
		  // excel   double quotes straight, left, right:	,"""",ì,î,	
		  // numbers double quotes straight, left, right:	,"""",Äú‚Äù,			  
		  // excel   single quotes straight, left, right:	,',ë,í,		
		  // numbers single quotes straight, left, right:	,',Äò‚Äô,	
		System.out.println("\nprFixCsv: START ");
		Logger.log.println("\nprFixCsv: START ");
		
		//Logger.log.printf("\nprFixCsv: INPUT < %s>",origin);
			StringBuffer destiny = new StringBuffer();
			
		  	char inchar, outchar, inNext;
		  	for(int i=0;i<origin.length();i++){
			  inchar = origin.charAt(i);
			  outchar=inchar;
			  //Logger.log.printf("\nprFixCsv: IN[%d]=<%s>",i,inchar);
			  
			  if( inchar=='ì' || inchar =='î'  ) { //Excel smart quote
				outchar='\"';					  
			  }
			  else if( inchar=='ë' || inchar =='í'  ) { //Excel smart apost  
				outchar='\'';					  
			  }
			  else if(inchar=='Ä'){
				  inNext = origin.charAt(i+1);
				  
				  if(inNext=='ò' || inNext =='ô') { // Numbers smart apost 
					outchar='\'';
					i+=1;
				  }
				  else if(inNext=='ú' || inNext =='ù') { // Numbers smart quote
		  			outchar='\"';
		  			i+=1;
				  }
			  }
			  else if(inchar=='\"'){
				  inNext = origin.charAt(i+1);
				  
				  if(inNext==',' && origin.charAt(i+2)=='\"'){	//replace comma with space
					  outchar=' ';
					  i+=2;
				  }
				  else if(inNext=='\'' && origin.charAt(i+2)=='\"'){	//replace csv strait apost
					  outchar='\'';
					  i+=2;
				  }
				  else if(inNext=='\"'
						&& origin.charAt(i+2)=='\"'
						&& origin.charAt(i+3)=='\"'){	//replace csv straight quote 
							 outchar='\"';
							 i+=3;
				  }
				  else {
					  Logger.log.printf(" UNEXPECTED CHAR FOLLOWING QUOTE = <%s>",origin.charAt(i+1) );
				  }
				  
			  	}
			  	else{
			  		//// stub //// just copy it
			  		outchar=inchar;
			  		//// endstub
			  	}
			  	destiny.append(outchar);
			  	//Logger.log.printf(" OUT=<%s>",outchar);
		  	}
			String dest = destiny.toString();
			
			//Logger.log.printf("\nprFixCsv: OUTPUT< %s>",dest);
			System.out.println("\nprFixCsv: END ");
			Logger.log.println("\nprFixCsv: END ");
			return dest;
			
	} // prFixCsv 

	

	
	public String[] prTokenize(String ifStaffLine) {
		//System.out.printf ("\nprTokenize: ifStaffLine # %d =<%s>", sourceLineNum, ifStaffLine);
		//Logger.log.printf ("\nprTokenize: ifStaffLine # %d =<%s>", sourceLineNum, ifStaffLine);	
		
		String[] slTokens = ifStaffLine.split(",",-1); //split on commas, force nulls	
		int nTokens = slTokens.length;
		
		//System.out.printf ("\nprTokenize: numTokens=<%d> tokens: ",nTokens);
		//Logger.log.printf ("\nprTokenize: numTokens=<%d> tokens: ",nTokens);	
		
		//for(int it=0; it<nTokens; it++){
			//System.out.printf ("{%d]=<%s>",it,slTokens[it]);
			//Logger.log.printf ("{%d]=<%s>",it,slTokens[it]);
		//}
		
		return (slTokens);
	} //prTokenize
	
	
	public void prDispose (){
		input.close();
	} //dispose
	
} // class
