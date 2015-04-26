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
 * Interface for binary decider. Allow to classify
 * in a binary problem using a not 0.5 threshold.
 */
public class BinaryDecider implements IClassifyDecider {

	private double bound;
	
	/**
	 * Constructor.
	 * 
	 * @param bound bound under which the second class is chosen instead of the first
	 */
	public BinaryDecider(double bound) {
		
		if( bound < 0 || bound > 1)
			throw new IllegalArgumentException("Error: the probability bound must be in [0,1]");
		
		this.bound = bound;
	}
	
	/* (non-Javadoc)
	 * @see CTBNToolkit.IClassifyDecider#decide(CTBNToolkit.IClassificationResult)
	 */
	@Override
	public int decide(IClassificationResult<Double> results)
			throws RuntimeException {

		double[] p = results.getPDistribution();
		if( p.length > 2)
			throw new IllegalArgumentException("Only binary decision are managed");
		
		return (p[0] >= this.bound)?0:1;
	}

}
