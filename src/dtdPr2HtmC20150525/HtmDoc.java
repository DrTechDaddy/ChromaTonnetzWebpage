package dtdPr2HtmC20150525;


import java.io.*;
import java.util.*;


/** HtmDoc class generates the score as an HTM document */
public class HtmDoc {
	
	/** 
	 * DATA
	 * 	hFile:	output file for htm score
	 *  // state variables
	 * 
	 * METHODS 
	 * HtmDoc:	constructor
	 * html:	page
	 * head:	header section
	 * body:
	 * 	table:	score table
	 * 	column methods to format individual columns
	 * 		voice:
	 * 		note:
	 * 	row:	create cells for note row
	 * 		barlines:
	 * 			measureBar:
	 * 			countBar
	 * 			phraseBar:
	 * 			lineBreak
	 * 		counts:	count cell
	 *		notes:	note cell
	 *			staffline:
	 *			voice:
	 *			notehead:		
	 *		chords: chord cell			
	 * */
	
	//DATA
	
	private Layout lH;
	
	private		String 		hfileName;
	private		String		hTitle;
	private 	Formatter 	outHtm; 		// html file output object

	private		String[]	hsTokens;	    // input tokens for music content
	
	private		StringBuffer hbTagLine;		// buffer for tag content
	private		StringBuffer hbStaffLine;	// buffer for music content
	private		StringBuffer hbBarLine;		// buffer for barline content
	
	private		String		 hs;			// work string
	private		StringBuffer hb;			// work buffer
	
	private		int			hLineNum;		// output score line number

	
	

	StringBuffer countData = new StringBuffer();
	StringBuffer notesData = new StringBuffer();
	StringBuffer chordData = new StringBuffer();
	
	
	String hsBarLineSolid = "<div class=\"st-bar\"><div class=\"st-bar-solid\"></div><div class=\"st-bar-solid\"></div><div class=\"st-bar-solid\"></div></div>";
	String hsBarLineDashed = "<div class=\"st-bar\"><div class=\"st-bar-dashed\"></div><div class=\"st-bar-dashed\"></div><div class=\"st-bar-dashed\"></div></div>";
	String hsBarLineDotted = "<div class=\"st-bar\"><div class=\"st-bar-dotted\"></div><div class=\"st-bar-dotted\"></div><div class=\"st-bar-dotted\"></div></div>";
	String hsBarLineThin = "<div class=\"st-bar\"><div class=\"st-bar-thin\"></div><div class=\"st-bar-thin\"></div><div class=\"st-bar-thin\"></div></div>";
	String hsBarLineThick = "<div class=\"st-bar\"><div class=\"st-bar-thick\"></div><div class=\"st-bar-thick\"></div><div class=\"st-bar-thick\"></div></div>";
	String hsBarLineDouble = "<div class=\"st-bar\"><div class=\"st-bar-double\"></div><div class=\"st-bar-double\"></div><div class=\"st-bar-double\"></div></div>";

	// String hsBarPageBreak 	= "<div class=\"st-bar\"><div class=\"st-bar-break\"></div><div class=\"st-bar-break\"></div><div class=\"st-bar-break\"></div></div>"
	//						+ "<div class=\"page-break\"></div>";
	 String hsBarPageBreak 	= "</div><div class=\"st-page\">";

	//METHODS
	HtmDoc(String fileName){ 	//Constructor
		hfileName = fileName +".html";
		System.out.printf ("\nHtmDoc hfileName= %s",hfileName);
		Logger.log.printf ("\nHtmDoc hfileName= %s",hfileName);
	
		// open htm output file
		System.out.println("\nHtmDoc:" + hfileName +"," + hTitle);
		try{
		outHtm = new Formatter(hfileName);//instantiate output file writer for file clients.txt 
		}
				catch (SecurityException securityException){
				  System.err.println("PrToSk1.openFile: ERROR you do not have access to file: " + hfileName);		  
				}
				catch (FileNotFoundException fileNotFoundException){
				  System.err.println("PrToSk1.openFile: ERROR creating file: " + hfileName);
				}
		hbStaffLine = new StringBuffer();	//place to build a staff line
		hbBarLine 	= new StringBuffer();	//place to build a bar line
		hs = new String("");
		hb = new StringBuffer();

	} // Constructor
	
	/*  having problems ref destructor from main
	public void htmFinal () {	// "destructor"
		// write buffers to log
		//LOG// hbTagLine
		//LOG// hbStaffLine
		if (outhtm != null) {
		outhtm.close();		//close file
		}
	} // dispose
	*/
	

	void htmOutLine(StringBuffer hsb) { // output a buffer to the html file	                          
			 htmOutLine(hsb.toString() );                      
	}	// htmOutLine(StringBuffer)
	
	
	void htmOutLine(String hs) { // output a string to the html file
	 	 try
		   {                            
			 outHtm.format("\n%s",hs );                      
		     //outlog.printf("\nPrToSk1.outhtmLineLine: OUTPUT LINE [ %d %s ]",linenum,);
		   } // try
			 	   catch ( FormatterClosedException formatterClosedException )
			 	   {
			 	     System.err.println("PrToSk1.outhtmLine: OUTPUT ERROR: error writing to file ");
			 	     return;
			 	   } // catch
			 	   catch(NoSuchElementException noSuchElementException)
			 	   {
			 	     System.err.println("PrToSk1.outhtmLine: INPUT ERROR: invalid input line ignored; reenter ");
			 	     //input.nextLine();//discard input and read next line
			 		     return;
			 	   }	//catch
		
	} // htmOutLine(String)
	
	// create html document  --boilerplate
	public void htmBegin() {
		lH=Layout.theLayout();
		System.out.printf ("\nHtmBegin Layout= %s",lH.toString());
		Logger.log.printf ("\nHtmBegin Layout= %s",lH.toString());		
		
		//generate beginning of html doc
		//hbTagLine.setLength(0);	//reset htm tag buffer
		//hbStaffLine.setLength(0);	//reset htm buffer	

		htmOutLine("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
		htmOutLine("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		htmOutLine("<!-- StaffTonnetz Score Webpage -->");
		htmOutLine("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
		
		
		htmOutLine("  <head>");
		htmOutLine("    <meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\" />");	
		htmOutLine("    <title>" + hfileName + "</title>");	
		htmOutLine("    <link rel=\"stylesheet\" type=\"text/css\" href=\""+INI.DEF_CSS+".css\" />");
		htmOutLine("    <link rel=\"stylesheet\" type=\"text/css\" href=\""+lH.fontMusic+".css\" />");
		htmOutLine("    <style> * { font-size: "+lH.fontsizeMusic+";" 
							    +" line-height: "+lH.fontsizeMusic+";} </style>");
		htmOutLine("  </head>");
		
		System.out.printf ("\nHtmBegin </head>");
		Logger.log.printf ("\nHtmBegin </head>");	
		
		htmOutLine(	"<body>");
		//Title Line
		htmOutLine(		"<table style=\"width: 100%; border-collapse: collapse; border: none;\">");	//table to format title line: no borders
		htmOutLine(			"<tr>");
		htmOutLine(				"<td style=\"text-align: left;\">");
		htmOutLine("Words: " + lH.docLyrics);
		htmOutLine(				"</td>");
		htmOutLine(				"<td style=\"text-align: center; font-size: 125%\">");
		htmOutLine(lH.docTitle);
		htmOutLine(				"</td>");
		htmOutLine(				"<td style=\"text-align:right;\">");		
		htmOutLine("Music: " + lH.docComposer);
		htmOutLine(				"</td>");	
		htmOutLine(			"</tr>");
		htmOutLine(		"</table>");
			
		//htmOutLine("<p> This doc has " + lH.numVoices +" voices: ");
		//for (int iv = 0; iv <= lH.numVoices; iv++){
		//	htmOutLine("<span style=\"color:" + lH.colorsVoice[iv] + " \">" + iv + "</span>");
		//}
		//htmOutLine("</p>");		
		
		// START SCORE TABLE
		//htmOutLine("&nbsp;<br/>");
		htmOutLine("<div class=\"st-score\">");		// table tag closed by htmEnd
		htmOutLine("<div class=\"st-page\">");	// table row
		
		
		System.out.printf ("\nHtmBegin <table>");
		Logger.log.printf ("\nHtmBegin <table>");	
				
		//Create first line
		//for (int it=lH.sTokenLo; it <= lH.sTokenHi; it++){
		//	hsTokens[it]="";
		//}
		System.out.printf ("\nHtmBegin hsTokens");
		Logger.log.printf ("\nHtmBegin hsTokens");
		int ic;	//column counter
		
		countData.setLength(0); 
		notesData.setLength(0); 
		chordData.setLength(0);		
		for(ic=lH.countsLo; ic<=lH.countsHi; ic++) {
			countData.append(lH.staffChars[ic]); 
		}

		System.out.printf ("\nHtmBegin counts");
		Logger.log.printf ("\nHtmBegin counts");	
		
		for(ic=lH.midiLo; ic<=lH.midiHi; ic++) {
			notesData.append(",");
			notesData.append(lH.staffChars[ic]); 
			
		}
		for(ic=lH.exprLo; ic<=lH.exprHi; ic++) {
			chordData.append("&nbsp;");
			chordData.append(lH.staffChars[ic]); 
		}
		
		
			
		// FIRST LINE IS MODEL STAFF LINE NO NOTES
		htmOutLine("<div class=\"st-start\">");
		htmOutLine(" <div class=\"st-count\">"+countData+"</div>");
		htmOutLine(" <div class=\"st-notes\">"+notesData+"</div>");
		htmOutLine(" <div class=\"st-chord\">"+chordData+"</div>");
		htmOutLine("</div>"); // /st-start
		
		// reset line count
		hLineNum = 0;
		
		
		
		//  MODEL
		//sb.append( String.format("%3d",measure));
		//sb.append(".x  ");
		//s="x" + "y";
		/*
		hb.setLength(0);
		hb.append("    STUB BODY BUFFER");
		StringBuffer hb2 = new StringBuffer("some body text");
		hs	=	   "	STUB BODY TEXT ";
		
		
		htmOutLine(hb);
		htmOutLine(hb2);
		htmOutLine(hs);
		System.out.printf ("\nHtmDoc.HtmBegin: hb=%s, hb2=%s, hs=%s", hb,hb2,hs);
		*/
		System.out.printf ("\nHtmBegin END");
		Logger.log.printf ("\nHtmBegin END");		
	} // htmlBegin

		
	public void htmPutStaffLine(String[] hsTokens){
		Layout lH=Layout.theLayout();
		int nTokens =Math.min(hsTokens.length,lH.sTokenHi);
		String t;	//token
		String nt;	//note type: sToken[0] n v f
		String bt;	//bar  type: sToken[1] | - ! _ . #
		 
		int		vn;	//voice number
		String 	vc;	//voice color 
		String	vh;	//voice notehead
		
		
		
		hLineNum++;		// increment line number
	    nt=hsTokens[0];	//note type
	    bt=hsTokens[1];	//bar type		
		
		System.out.printf ("\nhtmPutStaffLine: #<%d> hsTokens[%d]: nt=<%s> bt=<%s>",hLineNum,nTokens, nt, bt);		//DEBUG//
		Logger.log.printf ("\nhtmPutStaffLine: #<%d> hsTokens[%d]: nt=<%s> bt=<%s>",hLineNum,nTokens, nt, bt);		//DEBUG//
		
		for(int it=0; it<= nTokens;  it++){																			//DEBUG//
			System.out.printf ("[%d]=<%s>",it,hsTokens[it]);
			Logger.log.printf ("[%d]=<%s>",it,hsTokens[it]);
		}
		
		// OUTPUT BAR LINE
		hbBarLine.setLength(0);
	    if(bt.matches("\\|")){	// bar line
			hbBarLine.append(hsBarLineSolid);     		    	
	    }
	    else if(bt.matches("\\-")){	// bar line
			hbBarLine.append(hsBarLineThin);     		    	
	    }else if(bt.matches("\\!")){	// bar line
			hbBarLine.append(hsBarLineThick);     		    	
	    }    
	    else if(bt.matches("\\_")){	// bar line
			hbBarLine.append(hsBarLineDashed);     		    	
	    }
	    else if(bt.matches("\\.")){	// bar line
			hbBarLine.append(hsBarLineDotted);
	    }
		else if(bt.matches("\\#")){	// bar line
			hbBarLine.append(hsBarLineDouble);    
	    }		
	    else{}	//no barline
	    if(nt.matches("p")){	// PAGE BREAK (after bar; no notes) 
		   	hbBarLine.append(hsBarPageBreak);  
		   	System.out.printf("\nhtmPutStaffLine  bsBarPageBreak= \n%s",hbBarLine);			//DEBUG//
		    Logger.log.printf("\nhtmPutStaffLine  bsBarPageBreak= \n%s",hbBarLine);			//DEBUG//
	  }	   			
	    
    if(hbBarLine.length() > 0) {
		htmOutLine(hbBarLine);
		System.out.printf("\nhtmPutStaffLine  bsBarLine= \n%s",hbBarLine);			//DEBUG//
	    Logger.log.printf("\nhtmPutStaffLine  hbBarLine= \n%s",hbBarLine);			//DEBUG//
    }
	    
	  if(nt.matches("p")){	// PAGE BREAK; skip staff line processing
	  }	    				   
	  else{
		// STAFF LINE
	    
		htmOutLine("<div class=\"st-staff\">");	// table row
		System.out.printf("\nhtmPutStaffLine  hbStaffLine= \n%s",hbStaffLine);
	    Logger.log.printf("\nhtmPutStaffLine  hbStaffLine= \n%s",hbStaffLine);	
	  
	    	

	 // OUTPUT COUNT: column 1 cell
		
		countData.setLength(0);
		for (int ic=lH.countsLo; ic <= lH.countsHi; ic++) {
			t=hsTokens[ic];
			if (t.matches("")){t="&nbsp;";}
			countData.append(t);
		}
		hbStaffLine.setLength(0);	//reset buffer		
		hbStaffLine.append("  <div class=\"st-count\">");
		hbStaffLine.append(countData);
		hbStaffLine.append("</div>");
		
		htmOutLine(hbStaffLine);
		System.out.printf("\nhtmPutStaffLine"+"\nhbStaffLine= %s",hbStaffLine);
	    Logger.log.printf("\nhtmPutStaffLine"+"\nhbStaffLine= %s",hbStaffLine);		
		
	 // OUTPUT NOTES: COLUMN 2 CELL
	
		notesData.setLength(0);
			
			for (int in=lH.midiLo; in<= lH.midiHi; in++){
				vn=0;
				t=hsTokens[in];
				if (t.matches("")){
					notesData.append(",");
				}
				else if (nt.matches("n")) { // note line
					notesData.append(t);				
				} // if n 
				
				else if (nt.matches("v")) { // voice line -- set voice number
					if      (t.matches("s")) {vn=1;}
					else if (t.matches("a")) {vn=2;}
					else if (t.matches("t")) {vn=3;}
					else if (t.matches("b")) {vn=4;}
					else if( t.matches("^[0-9]$"))	{vn = Integer.parseInt(t);}//convert voice number to int
					else 					{vn=0;}
					
					if (vn == 0)	{vh=t;}	//not voiced note; set head to input symbol
					else 		{vh=lH.noteheadChars[in];}	//set head to shape-note for staff position
				} // if v
				
				else if (nt.matches("f")) { // finger line -- set voice number to bass or soprano
					if		( t.matches("^[12345]$")) {vn=1;}	//Right Hand - soprano
					else if	( t.matches("^[67890]$")) {vn=4;}	//Left Hand - bass
					else 							{vn=0;}
					//stub: treat as voice until finger-font available
					if (vn == 0)	{vh=t;}	//not voiced note; set head to input symbol
					else 		{vh=lH.noteheadChars[in];}	//set head to shape-note for staff position
				}// if f
					
				//color and output note
				if(0 < vn && vn <= lH.numVoices){  // valid voice number
					lH.currentColor[in]=vn;		//remember color of this staff position
					vh = lH.noteheadChars[in];
				
					//color the note
					//?? span  SOMETIMES adds extra space around colored noteheads
					vn=lH.currentColor[in];		// get current color for this position	
					vc=lH.colorsVoice[vn];
					//notesData.append("<span style=\"color:"+vc+";\">");	// color tag
					notesData.append(vh);		//note symbol
					//notesData.append("</span>");							// color end tag
					if(vh.matches("%")){	// rest cancels position color AFTER printing
						lH.currentColor[in]=0;
					}// if %
					
				} // if vn
				else { //  non-voice note -- no color
					notesData.append(t);
					
				} // if vn 
				notesData.append(lH.staffChars[in]);
			} // for in
			hbStaffLine.setLength(0);	//reset buffer		
			hbStaffLine.append("  <div class=\"st-notes\">");
			hbStaffLine.append(notesData);
			hbStaffLine.append("</div>");
			
			htmOutLine(hbStaffLine);
			System.out.printf("\nhtmPutStaffLine"+"\nhbStaffLine= %s",hbStaffLine);
		    Logger.log.printf("\nhtmPutStaffLine"+"\nhbStaffLine= %s",hbStaffLine);		

	 // OUTPUT CHORD: COLUMN 3 CELL
		chordData.setLength(0);
		for (int ix=lH.exprLo; ix <= lH.exprHi; ix++) {
			chordData.append("&nbsp;");		// space between columns
			t=hsTokens[ix];
			if (t.matches("")){t="&nbsp;";}	//placeholder			
			chordData.append(t);
		} // for ix
		
		hbStaffLine.setLength(0);	//reset buffer		
		hbStaffLine.append("  <div class=\"st-chord\">");
		hbStaffLine.append(chordData);
		hbStaffLine.append("</div>");
		htmOutLine(hbStaffLine);
		System.out.printf("\nhtmPutStaffLine"+"\nhbStaffLine= %s",hbStaffLine);
	    Logger.log.printf("\nhtmPutStaffLine"+"\nhbStaffLine= %s",hbStaffLine);

	// END STAFF LINE TABLE ROW
		htmOutLine("</div>"); // /st-staff
		System.out.printf("\nhtmPutStaffLine"+"\nhbStaffLine= %s",hbStaffLine);
	    Logger.log.printf("\nhtmPutStaffLine"+"\nhbStaffLine= %s",hbStaffLine);
	  }// else nt != p
		
		return;
 	}

		
	public void htmEnd() {
		htmOutLine("</div>");	//st-page
		htmOutLine("</div>");	//st-score		
		htmOutLine("   </body>");
		htmOutLine(" </html>");
		
		outHtm.close();		//close file.
	} // htmlEnd	
	public void htmDispose (){
		outHtm.close();		//close file.
	}
}	//class
