package dtdPr2HtmC20150525
;



// MUSIC DATA CLASSES
//	commented fields reserved for further development

//GLOBAL CONSTANTS
class INI {
		//GLOBAL constants are used to set array sizes and limit loops
		public static final int MAX_HEADS	= 15;	// max header lines: 1 0* T F V K M H S 
		public static final int MAX_LINES	= 800;	//max lines in score; 
		public static final int	MAX_NOTES	= 90;	//max notes on line: piano keyboard + fudge [MIDI 21-108]
		public static final int MAX_HTM		= 127;	// max cols in htm file: counts, notes, exprs
		public static final int MAX_VOICES	= 16;	// maximum voice colors
		public static final int MAX_CSV     = 255;	// maximum chars in csv file line, including commas and doubled quotes, etc.!		
		public static final String DEF_FONT_MUSIC	= "Courier New";	// default music font
		public static final String DEF_FONT_COUNTS	= "Courier New";	// default counts font
		public static final String DEF_FONT_EXPR	= "Arial";	// default expr font
		
		public static final String DEF_COLOR = "black";		// default font color
		
		public static final String DEF_FILE = 	// default common file name for testing
				// 	"//SHEETS//timetest";
				// 	"//SHEETS//CBXLinusAndLucy";
				// 	"//SHEETS//CBX-WhatChildIsThis";
				// 	"//SHEETS//AlleyCat53-76";
				// 	"//SHEETS//Head";
				// 	"//SHEETS//st-12scaleXmp";
			
				//	"//SHEETS//st-ExampleCSS";	
				//	"//SHEETS//st-CbxLinusLucySolo";
				//		"//SHEETS//st-SkipToMyLou-c48";
				//	"//SHEETS//st-OTannenbaum-F68";
				
				//	"//SHEETS//st-JesuJoyOfMansDesiring-G";
				//	"//SHEETS//st-WishUMerryXmas-C";
				//	"//SHEETS//st-GreatPumpkinWaltz-F6";
				//	"//SHEETS//st-CbxChristmasSong-d8";
				// "//SHEETS//st-CbxChristmastime-F6";
				//"//SHEETS//ct-scale-triad-jazz-spiral-nostaff.v0610";
				//	 "//SHEETS//st-AAi1-32ClassyRagb-F8";
				//	 "//SHEETS//st-AA3-32ClassyRag-F8";
				//	"//SHEETS//st-AA3-32ClassyRag-keyline";
		 		//	"//SHEETS//st-AA3-32ClassyChords";
				  	"//SHEETS//st-QuietRevolution";
		public static final String DEF_FONT_SIZE	= "12pt";	// default fontsize
		public static final String DEF_CSS = //default css stylesheet
				//	"stafftonnetz1b";
					"stafftonnetz";
		public static final String DEF_FONT_CSS = //default css font stylesheet
					"stafftonnetz2font";
		
}

class Layout {	//Singleton object
	// file info
	public 	String  commonFileName;	// filename root
	//score info
	public  String	docTitle;		// title of piece				// from LT
	public  String	docComposer;	// composer						//from LT
	public  String	docLyrics;		// lyricist						//from LT
	// column style info
	public  String	fontMusic;		//font filename.css for music chars 		// from LF
	public  String	fontsizeMusic;	//font-size of music font in css syntax //1em = 12pt = 16px = 100%. 
	public  String	fontCounts;		//font file name for counts chars		// from LF
	public  String	fontsizeCounts;
	public  String	fontExpression;	//font name for chord chars		// from LF
	public  String	fontsizeExpression;	
	// channel info
	public	int			numVoices;				//number of voice colors not counting  0	// from LV
	public  String[] 	colorsVoice = new String[INI.MAX_VOICES];	//array of HTML voice colors; 0=default thru numVoices;
	// column layout info
	public  int			countsLo;		//midi number of first/loweest count position
	public  int 		countsHi;		//midi number of last/highest count possition	
	public  int			midiLo;		//midi number of first/loweest staff position
	public  int 		midiHi;		//midi number of last/highest staff possition
	public  int			exprLo;		//midi number of first/loweest expression position
	public  int 		exprHi;		//midi number of last/highest expression possition
	public	int			sTokenLo;	// Lo token on score line per K 
	public	int			sTokenHi;	// Hi token on score line per K 
	// music column detail: subscripted by token subscript [0..lineLen-1]
	public  int [] 		midiNums = 	new int [INI.MAX_HTM];	    //midi numbers for pianoroll positons
	public  String [] 	staffChars = new String [INI.MAX_HTM]; 	// model line with counts, staff chars, chords for pianoroll positions
	public  String [] 	noteheadChars  = new String [INI.MAX_HTM]; //shapenote chars for pianoroll positions
	public  int []		currentColor	= new int [INI.MAX_HTM];

	//public int		colsCounts;		//number of counts colums:  m-r t.s
	//public int		colsNotes;		//number of staff columns:
	//public int		colsChords;		//number of chord columns
	
	 @Override public String toString() {
		    StringBuilder result = new StringBuilder();
		    String NEW_LINE = System.getProperty("line.separator");

		    result.append(this.getClass().getName() + " Object: {" + NEW_LINE);
		    result.append(" docTitle: " + docTitle + NEW_LINE);
		    result.append(" docComposer: " + docComposer + NEW_LINE);
		    result.append(" docLyrics: " + docLyrics + NEW_LINE );
		    result.append(" fontMusic: " + fontMusic + NEW_LINE);
		    result.append(" fontsizeMusic: " + fontsizeMusic + NEW_LINE);
		    result.append(" fontCounts: " + fontCounts + NEW_LINE);
		    result.append(" fontsizeCounts: " + fontsizeCounts + NEW_LINE);
		    result.append(" fontExpression: " + fontExpression + NEW_LINE);
		    result.append(" fontsizeExpression: " + fontsizeExpression + NEW_LINE);
		    result.append(" numVoices: " + Integer.toString(numVoices) + NEW_LINE);
		    result.append(" colorsVoice: ");
		    for (int iv=0; iv < INI.MAX_VOICES; iv++ ){
		    	result.append(" [" + Integer.toString(iv) +"]=" + colorsVoice[iv]);
		    }
		    result.append(NEW_LINE);
		    result.append(" countsLo: " + Integer.toString(countsLo) + NEW_LINE);
		    result.append(" countsHi: "   + Integer.toString(countsHi) + NEW_LINE);
		    result.append(" midiLo: " + Integer.toString(midiLo) + NEW_LINE);
		    result.append(" midiHi: "   + Integer.toString(midiHi) + NEW_LINE);
		    result.append(" exprLo: " + Integer.toString(exprLo) + NEW_LINE);
		    result.append(" exprHi: " + Integer.toString(exprHi) + NEW_LINE);
		    result.append(" sTokenLo: "   + Integer.toString(sTokenLo) + NEW_LINE);
		    result.append(" sTokenHi: "   + Integer.toString(sTokenHi) + NEW_LINE);
		    
		    result.append(" midiNums: ");
		    for (int iv=0; iv < INI.MAX_HTM; iv++ ){
		    	result.append(" [" + Integer.toString(iv) +"]=" + Integer.toString(midiNums[iv]) );
		    }
		    result.append(NEW_LINE);
		    
		    result.append(" staffChars: ");
		    for (int iv=0; iv < INI.MAX_HTM; iv++ ){
		    	result.append(" [" + Integer.toString(iv) +"]=" + staffChars[iv]);
		    }
		    result.append(NEW_LINE);
    		
		    result.append(" noteheadChars: ");
		    for (int iv=0; iv < INI.MAX_HTM; iv++ ){
		    	result.append(" [" + Integer.toString(iv) +"]=" + noteheadChars[iv]);
		    }
		    result.append(NEW_LINE);
		    
		    result.append(" currentColor: ");
		    for (int iv=0; iv < INI.MAX_HTM; iv++ ){
		    	result.append(" [" + Integer.toString(iv) +"]=" + Integer.toString(currentColor[iv]) );
		    }
		    result.append(NEW_LINE);
		   
		    result.append("}");

		    return result.toString();
		  }
	 
	 // methods for Singleton object
	 //see: http://www.javacoffeebreak.com/articles/designpatterns/
	 
  private Layout ()	{	// private constructor not callable outside Layout
    // no code req'd
  }

  public static Layout theLayout() { // creates a single instance of Layout if needed, then returns a ref to it
    if (ref == null) {
        // first time called; create an instance by calling private constructor
       ref = new Layout();	
    }
    return ref;
  }

  public Object clone()	// to prevent cloning the object
	throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException();   // that'll teach 'em
  }

  private static Layout ref;

} //Layout =====================================


class Event {	//event timing label
	public String	counts;		// string of measure-repeat tick,sub of timing info

	//String	fingersL	//left-hand fingers
			// 0-values for unused fields 
	//int	measure;		// measure number (could be multiple on the same line; initial anacrusis = 0
	//int 	repeat			// 0 for noraml; 1,2, etc for alternative repetitions * 
	//int	count;			// sub-division of measure, typically 1-origin
	//int	sub;			// sub-division of count; 1-origin; 0=dot for subdivisions or swing extension
	// mathematical time position
	//int 	tick;			// tick of this event	0-origin* 
	//int	of numticks;		// number of ticks in this measure, extended to equalize swing.		 
	}	
	
class Note {
	public int		voice;		// number (midi channel):  b  t  a  s  = 8 7  6 5  4 3  2 1;  10=drums
	public int 		MIDI;		// absolute ET pitch  middle-C = 60
	public String	head;		// notehead char; shape-notes already transposed for scale
    //String	degree;		// solfedge value  d r m f s l t  OPT FOLLOW u o a e i for bb b n # x
    //String	accidental; // w b n # x
    //String	finger;	// 6 7 8 9 0  1 2 3 4 5
	}

class Expression {	// expression and analysis info:
	//String	expression;	//notations for attack, phrasing, volume, etc.
	public String	chord;
	//String	fingersL;
	//String	fingersR;
}

// GLOBALS



public class Main {
	/**
	 * @param args
	*/
	public static void main(String[] args) {
		
		Layout lM = Layout.theLayout();
		
		lM.commonFileName = INI.DEF_FILE;
						
		System.out.printf ("\nMain: commonFileName=%s ",lM.commonFileName);
		
		String[] sourceP;	//input tokens from piano roll
		String parsedP;	//
		String targetH;	//output line from htm
		int		i;	//loop counter;

		/*  
		 // model objct invocation--BUT dispose doesn't work this way! 
		 Resource r = new Resource();
		 try {
		     //work
		 } finally {
		     r.dispose();	// but this requires static methods !!!
		 }
		 */
	

// BEGIN LOG FILE
			System.out.println ("\nMain: new Logger");	
			
			Logger l = new Logger (lM.commonFileName);   //** try passing l to the other objects as parm **//
				
// BEGIN INPUT CSV FILE
			  System.out.println ("\nMain: new PrDoc");
			  Logger.log.println ("\nMain: new PrDoc");	
			  
			  PrDoc p = new PrDoc(lM.commonFileName);
								
// BEGIN OUTPUT HTM FILE
		  System.out.println ("\nMain: new HtmrDoc");
		  Logger.log.println ("\nMain: new HtmDoc");	
		  
		  HtmDoc h = new HtmDoc(lM.commonFileName);

// INPUT LAYOUT	
		
		  p.prGetLayout();		//get layout lines from csv file	
		  
		  System.out.printf ("\nMain: Layout=%s",lM.toString());
		  Logger.log.printf ("\nMain: Layout=%s",lM.toString());

// START HTM DOCUMENT
		  
		  //Main uses a PUSH method
		  //invoke PrDoc to get a "text staff line" from the Pr file
		  //pass to line to the various converters to write lines to the score files
		  //PrDoc and converterDocs keep track of their own state 
		  //   and insert intermediate lines (tags, bars, etc.) as needed
		  
		  System.out.println ("\nMain: CALL  htmBegin");
		  Logger.log.println ("\nMain: CALL  htmBegin");		  
		  h.htmBegin();	// start htm page using header info
		  
		  
// PROCESS MUSIC LINES
		/* MODEL
		String line = null;  
		while( ( line = reader.readLine() ) != null )  
		{  
		   process the line  
		}  
		*/
		  
		  
		  System.out.println ("\nMain: CALL prGetLine");
		  Logger.log.println ("\nMain: CALL prGetLine");	
		  
		  
		  //READ SOURCE FILE
		  
		  for (i=0;i<INI.MAX_LINES; i++) {		// safety catch for inf loop
			  sourceP = null;
		  if( (sourceP = p.prGetLine() ) == null) break;
		  
			  System.out.println ("\nMain: prGetLine, htmPutStaffLine");
			  Logger.log.println ("\nMain: prGetLine, htmPutStaffLine"); 
			  
			  // parsedP =  p.parseLine (sourceP); 
			 // h.renderLine;
			  
			
			  h.htmPutStaffLine(sourceP);			 			
		  }
		  
		  
		  // h.hthmLine();
		  System.out.println ("\nMain: htmEnd");
		  Logger.log.println ("\nMain: htmEnd");		  
		  h.htmEnd();  

	
//QUIT
		  System.out.println ("\nMain: QUIT");
		  Logger.log.println ("\nMain: QUIT");		  		  
			h.htmDispose();
			p.prDispose();
			l.logDispose();


		}
	
}
