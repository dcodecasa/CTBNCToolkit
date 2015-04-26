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
 * Interface for learning results.
 */
public interface ILearningResults {

	/**
	 * Return the sufficient statistics used
	 * in the learning.
	 * 
	 * @return sufficient statistics
	 */
	public SufficientStatistics[]  getSufficientStatistics();
}
