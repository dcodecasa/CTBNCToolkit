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
package CTBNCToolkit;

/**
 * @author Daniele Codecasa <codecasa.job@gmail.com>
 *
 * Class that implement the results of the
 * learning process.
 * It contains the sufficient statistics.
 */
public class GenericLearningResults implements ILearningResults {

	private SufficientStatistics[] sufficientStats;
	
	
	/**
	 * Constructor.
	 * 
	 * @param sufficientStats sufficient statistics
	 */
	public GenericLearningResults(SufficientStatistics[] sufficientStats) {
		
		if( sufficientStats == null)
			throw new IllegalArgumentException("Error: null sufficient statistics argument");
		if( sufficientStats.length == 0)
			throw new IllegalArgumentException("Error: empty sufficient statistics argument");
		
		this.sufficientStats = sufficientStats;
	}
	

	/* (non-Javadoc)
	 * @see CTBNToolkit.ILearningResults#getSufficientStatistics()
	 */
	@Override
	public SufficientStatistics[] getSufficientStatistics() {

		return this.sufficientStats;
	}

}
