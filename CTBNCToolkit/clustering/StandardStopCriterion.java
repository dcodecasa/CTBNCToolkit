/**
 * Copyright (c) 2012-2013, Daniele Codecasa <codecasa.job@gmail.com>,
 * Models and Algorithms for Data & Text Mining (MAD) laboratory of
 * Milano-Bicocca University, and all the CTBNCToolkit contributors
 * that will follow.
 * All rights reserved.
 *
 * @author Daniele Codecasa and all the CTBNCToolkit contributors that will follow.
 * @copyright 2012-2013 Daniele Codecasa, MAD laboratory, and all the CTBNCToolkit contributors that will follow
 */
package CTBNCToolkit.clustering;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that define a standard stopping critarion.
 */
public class StandardStopCriterion implements IStopCriterion {

	private int maxIteration;
	private double changedBound;
	
	/**
	 * Constructor
	 * 
	 * @param maxIteration max number of iteration (valid range {2,..,+oo})
	 * @param changedBound bound of percentage of changed clusters under with stop the clustering (valid range [0,1])
	 * @throws IllegalArgumentException in case of illegal argument
	 */
	public StandardStopCriterion(int maxIteration, double changedBound) throws IllegalArgumentException {
		
		if( maxIteration <= 1)
			throw new IllegalArgumentException("Error: max iteration must be bigger then 0");
		if( changedBound < 0.0 || changedBound > 1.0)
			throw new IllegalArgumentException("Error: changed percentage bound must be in [0,1]");
		
		this.maxIteration = maxIteration;
		this.changedBound = changedBound;
	}
	
	
	/**
	 * Return the max iteration set.
	 * 
	 * @return maximum number of iteration
	 */
	public int getMaxIteration(){
		
		return this.maxIteration;
	}
	
	
	/**
	 * Return the percentage bound under with
	 * cut the clustering algorithm.
	 * 
	 * @return percentage bound
	 */
	public double getChangedBound() {
		
		return this.changedBound;
	}
	
		
	/* (non-Javadoc)
	 * @see CTBNToolkit.clustering.IStopCriterion#paramsStop(int, double)
	 */
	@Override
	public boolean paramsStop(int iIteration, double changedCluster)
			throws RuntimeException {

		return (iIteration > this.maxIteration || changedCluster <= this.changedBound);
	}

}
