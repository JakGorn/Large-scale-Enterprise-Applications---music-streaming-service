package pl.edu.pg.student.lsea.lab.analysis;


/**
 * Class representing a single thread used for the analysis of data.
 * @author Jakub Górniak
 */
public class AnalysisThread implements Runnable{

	/** AnalysisYear object used for performing analysis tasks */
	private AnalysisYear a; 
	
	/** PerformanceAnalysis object used for measuring performance of analysis */
	private PerformanceAnalysis p;
	
	/**
	 * Constructor of the analysis thread
	 * @param a AnalysisYear object used for performing analysis tasks
	 */
	public AnalysisThread(AnalysisYear a, PerformanceAnalysis p) {
		this.a = a;
		this.p = p;
	}

	@Override
	public void run() {
		a.performAnalysis();
		p.endNotification();
	}
}
