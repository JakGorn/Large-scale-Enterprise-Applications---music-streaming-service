/**
 * 
 */
package pl.edu.pg.student.lsea.lab.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for holding and printing data about performance of the analysis
 * @author Jakub Górniak
 */
public class PerformanceAnalysis {

	/** start time of the analysis */
	private long start;
	
	/** end times of the analysis of single element (year) */
	private List<Long> end = new ArrayList<Long>();

	/**
	 * Constructor of the PerformanceAnalysis class
	 * Initializes start time
	 */
	public PerformanceAnalysis() {
		start = System.currentTimeMillis();
	}
	
	/**
	 * Adds end time of finishing single year analysis
	 */
	public synchronized void endNotification() {
		long e = System.currentTimeMillis();
		end.add(e);
	}
	
	/**
	 * Prints results of performance to the file
	 * @param fileName name of the file to which data will be saved
	 */
	public void printResults (String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    for(Long time : end)
	    {
	    	double duration = ((double)(time-start))/1000;
	    	printWriter.println(String.format("%.2f", duration));
	    }
	    printWriter.close();
	}
	
}
