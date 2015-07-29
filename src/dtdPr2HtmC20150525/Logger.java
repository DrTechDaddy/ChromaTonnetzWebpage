package dtdPr2HtmC20150525;


import java.io.*;
import java.util.*;

public  class Logger {
	//DATA
		private String 		lfileName;
		public static PrintStream log;
		
	//METHODS
	Logger (String fileName) { 	//Constructor
		/** opens the static logfile  */
		lfileName = fileName +".log.txt";
		System.out.printf ("\nLogger lfileName= %s",lfileName);
		
		// open log output file
		try {
		    log = new PrintStream(lfileName);
		    }
				catch (SecurityException securityException){
				  System.err.println("PrT2Sk.Logger: ERROR you do not have access to file: " + lfileName);		  
				}
				catch (FileNotFoundException fileNotFoundException){
				  System.err.println("Pr2Sk.Logger: ERROR creating file: " + lfileName);
				}
		} // Constructor
	
	/*
	public static void log(String from, String what) { // output a string to the log file 	                          
			 outLog.println("\n" + from +"<:" + what + ":>" );                     		 		
	} // Log(String)	
	public static void log(String from, StringBuffer what) { // output a buffer to the log                         
			 outLog.println("\n" + from +"<:" + what.toString()  + ":>" );                    		
	} // Log(StringBuffer)	

	public void logEnd() {
		outLog.close();		//close file.
	} // htmlEnd
	*/	
	public void logDispose (){
		log.close();
	}

}
